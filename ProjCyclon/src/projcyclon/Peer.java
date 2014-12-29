package projcyclon;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Cancellable;
import akka.actor.UntypedActor;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import monitoringutils.Registro;
import scala.concurrent.duration.Duration;

/**
 *
 * @author luca
 */
public class Peer extends UntypedActor implements Runnable {

    public static final int delta = 200;
    public static final int DEF_PEER_NUM = 1;
    public static final int DEF_CONTAINED = 4;

    public boolean active = true;

    public List<MyActor> neighbors = Collections.synchronizedList(new ArrayList());

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

        /*/**/
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
                case 0:
                    addNewNeighbor(m.type, m.sender, m.peer);
                case 1:
                    send(2, new MyActor(0, m.sender));
                    addNewNeighbor(m.type, m.sender, m.peer);
                    break;
                case 2:
                    if (m.sender.equals(toRecive.getActor())) {
                        addNewNeighbor(m.type, m.sender, m.peer);
                        startAutoUpdate();
                    }
                    break;
            }
        }
    }

    private synchronized void addNewNeighbor(int type, String sender, ArrayList<MyActor> peer) {
        //long now = System.currentTimeMillis();
        for (MyActor a : peer) {
            if (!neighbors.contains(a)) {
                neighbors.add(a);
            }
        }

        if (type == 1) {
            for (int i = 0;
                    type != 0 && neighbors.size() < DEF_CONTAINED && i < savedNeighborsBis.size();
                    i++) {
                if (!neighbors.contains(savedNeighborsBis.get(i))) {
                    neighbors.add(savedNeighborsBis.remove(i));
                    if(neighbors.size()>DEF_CONTAINED){
                        System.err.println(name()+" " +neighbors.size() );
                    }
                }
            }
        } /* else {
            for (int i = 0;
                    type != 0 && neighbors.size() < DEF_CONTAINED && i < savedNeighbors.size();
                    i++) {
                if (!neighbors.contains(savedNeighbors.get(i))) {
                    neighbors.add(savedNeighbors.remove(i));
                }
            }
        }*/

    }

    private void send(int type, MyActor to) {
        ArrayList<MyActor> peerReply;
        if (type == 1) {
            peerReply = getPeerRandom(to.getActor());
            savedNeighbors.addAll(peerReply);
        } else {
            peerReply = getPeerForReply(to.getActor());
            savedNeighborsBis.addAll(peerReply);
        }

        getPointer(to.getActor()).tell(new MessagePeer(type, name(), peerReply), null);
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
                    ret.add(neighbors.remove(i));
                }
            }
        } catch (Exception e) {
            System.out.println("Errore " + name());
        }
        return ret;
    }

    private void getInitialPeerFromTracker() {
        tracker.tell(new Message(0, name()), ActorRef.noSender());
    }

    private MyActor selectPeerToContact() {

        Collections.sort(neighbors, new MyActor(0, name()));

        return neighbors.get(0);

    }

    @Override
    public void run() {
        try {
            while (neighbors.isEmpty()) {

                //System.err.println(name() + " sono addormentato");
                sleep(delta);
            }

            cycle++;
            toRecive = selectPeerToContact();

            send(1, toRecive);
            //System.err.println(name() + " send to " + toRecive);

        } catch (Exception e) {
            //System.err.println("No peer to contact " + name());
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
        updater = ProjCyclon.system.scheduler().scheduleOnce(
                Duration.create(delta, TimeUnit.MILLISECONDS),
                this,
                ProjCyclon.system.dispatcher()
        );
    }

    public void stopAutoUpdate() {
        updater.cancel();
    }

}
