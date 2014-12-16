/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projcyclon;

import akka.*;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import scala.Serializable;

/**
 *
 * @author luca
 */
public class ProjCyclon {
    
    public static ActorSystem system;

    public static void main(String[] args) {
        
        system = ActorSystem.create("Cyclon-system");

        int NPeer;
        
        // DECOMMENTARE QUANDO CONSEGNAMO
        /*try {
            String sNPeer = JOptionPane.showInputDialog("Quanti peer vuoi?");
            NPeer = Integer.parseInt(sNPeer);
        } catch (Exception e) {
            NPeer = 4;
        }*/

        NPeer = 4;
        
        ArrayList<IActorRef> peer = new ArrayList<>();
        
        for (int i = 0; i < NPeer; i++) {
            peer.add(new IActorRef(1,system.actorOf(Props.create(Peer.class), "Peer_" + i)));
            System.out.println("Creazione di Peer_" + i);
        }

        

    }

}
