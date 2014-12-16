package projcyclon;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import java.util.ArrayList;
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
        
        initializeTracker();
        
        getInitialPeerFromTracker();
        
        Registro.reg.add(this);
        
        /*ProjCyclon.system.scheduler().schedule(
                Duration.create(0, TimeUnit.MILLISECONDS),
                Duration.create(delta, TimeUnit.MILLISECONDS),
                this,
                ProjCyclon.system.dispatcher()
        );*/
        
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof MessagePeer) {
            MessagePeer m = (MessagePeer) message;
            switch (m.type) {
                case 1:
                    neighbors.addAll(m.peer);
                    break;
                case 2:
                    /*System.out.println("Io essere " + this.getSelf().path().name());
                    System.out.println("e avere ricevuto risposta da " + m.sender.path().name());*/
            }
        }

        for(IActorRef n:neighbors){
            System.out.println(this.getSelf().path().name() + " " +n.getActor().path().name());
        }
        //System.err.println(this.getSelf().path().name() + " " + neighbors.size());
    }
    
    public void initializeTracker(){
        this.tracker = ProjCyclon.tracker;
    }

    @Override
    public void run() {
        
        
    }

    private void getInitialPeerFromTracker() {
        tracker.tell(new Message(0, this.getSelf()), null);
    }
    

}
