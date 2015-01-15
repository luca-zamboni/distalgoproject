/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projcyclon;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import monitoringutils.ThreadMonitor;

/**
 *
 * @author luca
 */
public class ProjCyclon {
    
    public static final String TRACKER_NAME = "Tracker";
    
    public static ActorSystem system;
    public static ActorRef tracker;
    public static int nei;
    public static int scamb;
    

    public static void main(String[] args) {
        
        system = ActorSystem.create("Cyclon-system");

        int NPeer;
        
        // DECOMMENTARE QUANDO CONSEGNAMO
        try {
            String sNPeer = JOptionPane.showInputDialog("Number of Peers?");
            String snei = JOptionPane.showInputDialog("Neighboars");
            String sscamb = JOptionPane.showInputDialog("Changed?");
            NPeer = Integer.parseInt(sNPeer);
            nei = Integer.parseInt(snei);
            scamb = Integer.parseInt(sscamb);
        } catch (Exception e) {
            NPeer = 10;
            nei = 5;
            scamb = 2;
            
        }
        
        //NPeer = 10;
        
        ProjCyclon.tracker = system.actorOf(Props.create(Tracker.class), TRACKER_NAME);
        
        for (int i = 0; i < NPeer; i++) {
            //long time = System.currentTimeMillis();
            system.actorOf(Props.create(Peer.class), "Peer_" + i);
        }
        //int j = JOptionPane.showConfirmDialog(null, "Start monitoring?");
        new ThreadMonitor().start();

    }
    
    

}
