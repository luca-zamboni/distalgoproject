package projcyclon;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import monitoringutils.Registro;
import scala.concurrent.duration.Duration;

/**
 *
 * @author luca
 */
public class Peer extends UntypedActor implements Runnable {

    public static int delta = 2000;
    public static int DEF_PEER_NUM = 5;

    public boolean active = true;

    public ArrayList<MyActor> neighbors = new ArrayList<>();

    public ActorRef tracker;

    @Override
    public void preStart() throws Exception {

        super.preStart();
    
        Registro.reg.add(this);

        initializeTracker();

        getInitialPeerFromTracker();

        /*ProjCyclon.system.scheduler().schedule(
                Duration.create(0, TimeUnit.MILLISECONDS),
                Duration.create(delta, TimeUnit.MILLISECONDS),
                this,
                ProjCyclon.system.dispatcher()
        );/**/
        /*ProjCyclon.system.scheduler().scheduleOnce(
         Duration.create(1000, TimeUnit.MILLISECONDS),
         this,
         ProjCyclon.system.dispatcher()
         );/**/

    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof MessagePeer) {
            MessagePeer m = (MessagePeer) message;
            
            switch (m.type) {
                case 1:
                    
                    send(2, new MyActor(0, m.sender));
                    addNewNeighbor(m.type, m.sender, m.peer);
                    break;
                case 2:
                    addNewNeighbor(m.type, m.sender, m.peer);
                    break;
            }
        }
    }

    private void addNewNeighbor(int type, String sender, ArrayList<MyActor> peer) {
        long now = System.currentTimeMillis();
        for(MyActor a:peer){
            if(!neighbors.contains(a)){
                neighbors.add(a);
            }
        }
        if(type==1){
            neighbors.remove(new MyActor(0, sender));
            neighbors.add(new MyActor(now, sender));
        }
        neighbors.remove(new MyActor(now, name()));
        
    }
    

    private void send(int type, MyActor to) {
        
        ArrayList<MyActor> peerReply = getPeerForReply( to.getActor());
        getPointer(to.getActor()).tell(new MessagePeer(type, name(), peerReply), null);
    }
    
    private ArrayList<MyActor> getPeerForReply(String actor) {
        ArrayList<MyActor> ret = new ArrayList<>();
        Collections.sort(neighbors, new MyActor(0,""));
        for(int i = 0; i<DEF_PEER_NUM && i < neighbors.size(); i++) {
            if(!neighbors.get(i).equals(new MyActor(i, actor))){
                ret.add(neighbors.get(i));
            }
        }
        return ret;
    }

    private void getInitialPeerFromTracker() {
        tracker.tell(new Message(0, name()), ActorRef.noSender());
    }
    
    private MyActor selectPeerToContact() {

        Collections.sort(neighbors, new MyActor(0,name()));

        return neighbors.get(0);

    }
    

    @Override
    public void run() {
        try {
            send(1, selectPeerToContact());

        } catch (Exception e) {
            System.err.println("No peer to contact");
        }

    }
    public String name(){
        return this.getSelf().path().name();
    }
    public void initializeTracker() {
        this.tracker = ProjCyclon.tracker;
    }
    private ActorSelection getPointer(String name){
        ActorSelection point = ProjCyclon.system.actorSelection("/user/"+name);
        return point;
    }

}
