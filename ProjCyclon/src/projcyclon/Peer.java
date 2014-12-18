package projcyclon;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
                    
                    //send(2, new MyActor(0, m.sender));
                    //addNewNeighbor(m.type, m.sender, m.peer);
                    break;
                case 2:
                    addNewNeighbor(m.type, m.sender, m.peer);
                    break;
            }
        }
    }

    private void addNewNeighbor(int type, int sender, ArrayList<MyActor> peer) {
        long now = System.currentTimeMillis();
        System.err.println("sono " + getKey() + " " +peer.size());
        neighbors.addAll(peer);
        if(type==1){
            neighbors.remove(new MyActor(0, sender));
            neighbors.add(new MyActor(now, sender));
        }
        neighbors.remove(new MyActor(now, getKey()));
    }
    

    private void send(int type, MyActor to) {
        //ArrayList<MyActor> peerReply = getPeerForReply(type, to.getActor());
        //System.err.println(name() + " " + peerReply.size());
        //to.getActor().tell(new MessagePeer(type, this.getSelf(), peerReply), null);
    }

    private void getInitialPeerFromTracker() {
        tracker.tell(new Message(0, getKey()), ActorRef.noSender());
    }

    public void initializeTracker() {
        this.tracker = ProjCyclon.tracker;
    }
    
    private MyActor selectPeerToContact() {

        Collections.sort(neighbors, new MyActor(0,getKey()));

        return neighbors.get(0);

    }
    
    private String name(){
        return this.getSelf().path().name();
    }

    @Override
    public void run() {
        try {
            
            send(1, selectPeerToContact());

        } catch (Exception e) {
        }

    }
    
    public int getKey(){
        for (Entry<Integer, ActorRef> entry : ProjCyclon.peer.entrySet()) {
            if (this.getSelf().equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return -2;
    }

}
