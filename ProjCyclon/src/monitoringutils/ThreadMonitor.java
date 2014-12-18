

package monitoringutils;

import java.util.logging.Level;
import java.util.logging.Logger;
import projcyclon.MyActor;
import projcyclon.Peer;

/**
 *
 * @author luca
 */
public class ThreadMonitor extends Thread{

    @Override
    public void run() {          
        try {
            sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(ThreadMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        while(true){      

            try {
                sleep(500);
            
                String tot = "";
            for(Peer p:Registro.reg){
                String g=p.getSelf().path().name() + " - " + p.neighbors.size() + " -";
                for(MyActor a:p.neighbors)
                    g += " " + a.getActor();
                tot+=g + "\n";
            }
                System.out.println(tot + "\n");
            
            } catch (Exception ex) {
                Logger.getLogger(ThreadMonitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
