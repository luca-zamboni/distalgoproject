package projcyclon;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Cancellable;
import akka.actor.UntypedActor;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import monitoringutils.Registro;
import scala.concurrent.duration.Duration;

/**
 * @author luca & luca
 */
public class Peer extends UntypedActor implements Runnable {

    public static final int DELTA = 100;
    public static final int DEF_PEER_NUM = 2;
    public static final int DEF_CONTAINED = 5;

    public boolean active = true;
    
    public boolean attack = false;

    public List<MyActor> neighbors = Collections.synchronizedList(new ArrayList());
    
    public List<MyActor> maliciusNeighbors = Collections.synchronizedList(new ArrayList());

    public List<MyActor> savedNeighbors = Collections.synchronizedList(new ArrayList());
    public List<MyActor> savedNeighborsBis = Collections.synchronizedList(new ArrayList());

    public ActorRef tracker;

    private Cancellable updater;

    public int cycle = 0;

    public MyActor toRecive;

    @Override
    public void preStart() throws Exception {

        super.preStart();

        Registro.reg.add(this);

        initializeTracker();

        getInitialPeerFromTracker();

    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof MessagePeer){
            MessagePeer m = (MessagePeer) message;
            if(active){
                switch (m.type) {
                    case MessagePeer.DIED:
                        merge(m.type, m.sender, m.peer);
                        removeNodeFromNeighbors(m.sender);
                        AutoUpdateNow();
                        break;
                    case 0:
                        merge(m.type, m.sender, m.peer);
                        break;
                    case 1:
                        send(2, new MyActor(0, m.sender));
                        merge(m.type, m.sender, m.peer);
                        break;
                    case 2:
                        if (m.sender.equals(toRecive.getActor())) {
                            merge(m.type, m.sender, m.peer);
                            startAutoUpdate();
                        }
                        break;
                }
            }else{
                send(MessagePeer.DIED, new MyActor(0, m.sender));
            }
        }
    }

    private void merge(int type, String sender, ArrayList<MyActor> peer) {
        
        String b = neighbors.size() + " " + peer.size();
        
        for (MyActor a : peer) {
            if (!neighbors.contains(a) && neighbors.size() < DEF_CONTAINED ) {
                neighbors.add(a);
            }
        }
        if (type == 1) {
            for (int i = 0;
                    type != 0 && neighbors.size() < DEF_CONTAINED && i < savedNeighborsBis.size(); i++) {
                if (!neighbors.contains(savedNeighborsBis.get(i))) {
                    neighbors.add(savedNeighborsBis.get(i));
                }
            }
            savedNeighborsBis.clear();
        } else if(type == 2 || type == MessagePeer.DIED){
            for (int i = 0;
                    type != 0 && neighbors.size() < DEF_CONTAINED && i < savedNeighbors.size(); i++) {
                
                if (!neighbors.contains(savedNeighbors.get(i))) {
                    neighbors.add(savedNeighbors.get(i));
                }
            }
            savedNeighbors.clear();
        }
        neighbors.remove(new MyActor(type, name()));
    }

    private void send(int type, MyActor to) {
        ArrayList<MyActor> peerReply = new ArrayList<>();
        if(active && !attack){
            if (type == 1) {
                peerReply = getPeerRandom(to.getActor());
            } else {
                peerReply = getPeerForReply(to.getActor());
            }
        }
        if(attack){
            peerReply = getMaliciousPeer(to.getActor());
        }
        
        getPointer(to.getActor()).tell(new MessagePeer(type, name(), peerReply), null);
    }
    
    private ArrayList<MyActor> getMaliciousPeer(String actor) {
        ArrayList<MyActor> ret = new ArrayList<>();
        try {
            Collections.shuffle(maliciusNeighbors);
            for (int i = 0; i < DEF_PEER_NUM - 1 && i < maliciusNeighbors.size(); i++) {
                if (!maliciusNeighbors.get(i).equals(new MyActor(i, actor))) {
                    //System.err.println(maliciusNeighbors.get(i));
                    ret.add(maliciusNeighbors.get(i));
                }
            }
        } catch (Exception e) {
            System.out.println("Errore " + name());
        }
        Long time = System.currentTimeMillis();
        
        savedNeighbors.addAll(ret);
        ret.add(new MyActor(time, name()));
        maliciusNeighbors.remove(new MyActor(0, actor));
        return ret;
    }

    private final ArrayList<MyActor> getPeerRandom(String actor) {
        ArrayList<MyActor> ret = new ArrayList<>();
        try {
            Collections.shuffle(neighbors);
            for (int i = 0; i < DEF_PEER_NUM - 1 && i < neighbors.size(); i++) {
                if (!neighbors.get(i).equals(new MyActor(i, actor))) {
                    ret.add(neighbors.remove(i));
                }
            }
        } catch (Exception e) {
            System.out.println("Errore " + name());
        }
        Long time = System.currentTimeMillis();
        
        savedNeighbors.addAll(ret);
        ret.add(new MyActor(time, name()));
        neighbors.remove(new MyActor(0, actor));
        return ret;
    }

    private final ArrayList<MyActor> getPeerForReply(String actor) {
        ArrayList<MyActor> ret = new ArrayList<>();
        try {
            Collections.sort(neighbors, new MyActor(0, ""));
            for (int i = 0; i < DEF_PEER_NUM && i < neighbors.size(); i++) {
                if (!neighbors.get(i).equals(new MyActor(i, actor))) {
                    ret.add(neighbors.remove(neighbors.size() - i -1));
                }
            }
        } catch (Exception e) {
            System.out.println("Errore " + name() + " " + e.getMessage());
        }
            savedNeighborsBis.addAll(ret);
        return ret;
    }

    private void getInitialPeerFromTracker() {
        tracker.tell(new Message(0, name()), ActorRef.noSender());
    }

    private MyActor selectPeerToContact() {

        Collections.sort(neighbors, new MyActor(0, name()));

        return neighbors.get(0);

    }
    
    private void removeNodeFromNeighbors(String sender) {
        neighbors.remove(new MyActor(0,sender));
    }

    @Override
    public void run() {
        try {
            while (neighbors.isEmpty() && active) {

                System.err.println(name() + " sono addormentato");
                sleep(DELTA);
            }

                            
            if(active){
                cycle++;
                toRecive = selectPeerToContact();

                send(1, toRecive);
                //System.err.println(name() + " send to " + toRecive);
            }
        } catch (Exception e) {
            System.err.println("No peer to contact " + name());
            //System.out.println(neighbors.size());
            //e.printStackTrace();
            startAutoUpdate();
        }
    }

    public String name() {
        return this.getSelf().path().name();
    }

    public void initializeTracker() {
        this.tracker = ProjCyclon.tracker;
    }

    private ActorSelection getPointer(String name) {
        ActorSelection point = ProjCyclon.system.actorSelection("/user/" + name);
        return point;
    }

    public void startAutoUpdate() {
        if(active)
            updater = ProjCyclon.system.scheduler().scheduleOnce(Duration.create(DELTA, TimeUnit.MILLISECONDS),
                this,
                ProjCyclon.system.dispatcher()
            );
    }
    public void AutoUpdateNow() {
        updater = ProjCyclon.system.scheduler().scheduleOnce(
                Duration.create(0, TimeUnit.MILLISECONDS),
                this,
                ProjCyclon.system.dispatcher()
        );
    }
    public void stopAutoUpdate() {
        updater.cancel();
    }
    public void deactive(){
        active = false;
    }
    public void startAttack(){
        attack = true;
    }


}
