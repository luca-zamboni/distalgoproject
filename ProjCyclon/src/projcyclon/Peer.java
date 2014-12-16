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
public class Peer extends UntypedActor  {
    
    
    
    public boolean active = true;
    
    public ArrayList<ActorRef> neighbors = new ArrayList<>();

    @Override
    public void preStart() throws Exception {
        super.preStart();
        ProjCyclon.system.scheduler().scheduleOnce(Duration.create(50, TimeUnit.MILLISECONDS),
            new Runnable() {
              @Override
              public void run() {
                  System.out.println("sono una merda");
              }
          }, ProjCyclon.system.dispatcher());
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof Message){
            Message m = (Message) message;
            switch(m.type){
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
        
        System.out.println("");
            
    }
    
}
