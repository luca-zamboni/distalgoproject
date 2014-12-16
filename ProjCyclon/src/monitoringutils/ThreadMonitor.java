

package monitoringutils;

import java.util.logging.Level;
import java.util.logging.Logger;
import projcyclon.Peer;

/**
 *
 * @author luca
 */
public class ThreadMonitor extends Thread{

    @Override
    public void run() {
        while(true){
            for(Peer p:Registro.reg){
                System.out.println(p.neighbors.size());
            }
            System.out.println("");
            
            try {
                sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadMonitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
