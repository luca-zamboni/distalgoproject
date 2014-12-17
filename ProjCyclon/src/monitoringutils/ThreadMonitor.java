

package monitoringutils;

import java.util.logging.Level;
import java.util.logging.Logger;
import projcyclon.IActorRef;
import projcyclon.Peer;

/**
 *
 * @author luca
 */
public class ThreadMonitor extends Thread{

    @Override
    public void run() {
        while(true){
            try {
                sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadMonitor.class.getName()).log(Level.SEVERE, null, ex);
            }
            for(Peer p:Registro.reg){
                String g=" ";
                for(IActorRef a:p.neighbors)
                    g += " " + a.getActor().path().name();
                System.out.println(p.neighbors.size() + g);
            }
            System.out.println("");
            
            
        }
    }
    
}
