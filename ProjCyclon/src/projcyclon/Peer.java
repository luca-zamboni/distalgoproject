package projcyclon;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import monitoringutils.Registro;
import scala.concurrent.duration.Duration;

/**
 *
 * @author luca
 */
public class Peer extends UntypedActor implements Runnable {
    
    public static int delta = 500;

    public boolean active = true;

    public ArrayList<IActorRef> neighbors = new ArrayList<>();
    
    public ActorRef tracker;

    @Override
    public void preStart() throws Exception {
        
        super.preStart();
        
        Registro.reg.add(this);
        
        initializeTracker();
        
        getInitialPeerFromTracker();
        
        ProjCyclon.system.scheduler().schedule(
                Duration.create(0, TimeUnit.MILLISECONDS),
                Duration.create(delta, TimeUnit.MILLISECONDS),
                this,
                ProjCyclon.system.dispatcher()
        );
        
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof MessagePeer) {
            MessagePeer m = (MessagePeer) message;
            switch (m.type) {
                case 1:
                    addNewNeighbor(m.peer);
                    break;
            }
        }
        
        if (message instanceof Message) {
            
            Message m = (Message) message;
            
            switch (m.type) {
                case 0:
                    
                    //System.err.println("Ricevuto " + getSelf().path().name());
                    //System.err.println(m.sender.path().name());
                    ArrayList<IActorRef> peerReply = getPeerForReply(m.sender);
                    m.sender.tell(new MessagePeer(1, this.getSelf(), peerReply), null);
                    
                    sendReq(new IActorRef(0,m.sender));
                    
                break;
                default:
                    System.err.println("Error reading message");
                break;
            }
        }

        /*for(IActorRef n:neighbors){
            System.out.println(this.getSelf().path().name() + " " +n.getActor().path().name());
        }*/
    }
    

    @Override
    public void run() {
        
        try {
            
            sendReq(selectPeerToContact());
            
        } catch (Exception e) {
        }
        
    }
    
    private ArrayList<IActorRef> getPeerForReply(ActorRef sender) {
        int Npeer = neighbors.size() / 2;
        ArrayList<IActorRef> ret = new ArrayList<>();
        
        Collections.sort(neighbors, new IActorRef(0, tracker));
        
        for (int i = 0; i < Npeer; i++) {
            if(!sender.equals(neighbors.get(i)))
                ret.add(neighbors.remove(i));
        }
        
        //neighbors.remove(sender);
        
        return ret;
    }
    
    private IActorRef selectPeerToContact(){
        
        Collections.sort(neighbors, new IActorRef(0, tracker));
        //System.err.println(neighbors.size());
        return neighbors.get(0);
        
    }

    private void addNewNeighbor(ArrayList<IActorRef> peer) {
        
        for(IActorRef f: peer){
            if(!neighbors.contains(f)){
                neighbors.add(f);
            }
        }
    }
    
    private void sendReq(IActorRef reciver){
        reciver.getActor().tell(new Message(0, this.getSelf()), null);
    }

    private void getInitialPeerFromTracker() {
        sendReq(new IActorRef(0, tracker));
    }
    
    public void initializeTracker(){
        this.tracker = ProjCyclon.tracker;
    }

    

}
