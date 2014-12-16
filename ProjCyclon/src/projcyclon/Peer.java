package projcyclon;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import static akka.persistence.journal.JournalSpec$class.system;
import static akka.persistence.snapshot.SnapshotStoreSpec$class.system;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import scala.concurrent.duration.Duration;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author luca
 */
public class Peer extends UntypedActor implements Runnable {
    
    public static int delta = 500;

    public boolean active = true;

    public ArrayList<ActorRef> neighbors = new ArrayList<>();
    
    public ActorRef tracker;
    

    @Override
    public void preStart() throws Exception {
        super.preStart();
        
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
        System.err.println("asd");
        if (message instanceof Message) {
            Message m = (Message) message;
            switch (m.type) {
                case 1:
                    System.out.println("Io essere " + this.getSelf().path().name());
                    System.out.println("e rispondere a " + m.sender.path().name());
                    m.sender.tell(new Message(2, "wuwuuwuw", this.getSelf()), null);
                    break;
                case 2:
                    System.out.println("Io essere " + this.getSelf().path().name());
                    System.out.println("e avere ricevuto risposta da " + m.sender.path().name());
            }
        }

    }
    
    public void initializeTracker(){
        this.tracker = ProjCyclon.tracker;
    }

    @Override
    public void run() {
        
    }

    private void getInitialPeerFromTracker() {
        //tracker.tell(new Message(0, "ciao tracker", this.getSelf()), null);
    }

}
