package monitoringutils;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import projcyclon.MyActor;
import projcyclon.Peer;

/**
 *
 * @author luca
 */
public class ThreadMonitor extends Thread {
    
    public static final String AVERAGE = "Average neighbors : ";
    public static final String AVERAGE_CYCLE = "Average cycle : ";

    @Override
    public void run() {
        try {
            sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ThreadMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }

        MyPanel pan = new MyPanel();
        JFrame frame = new JFrame("Monitor");
        frame.setContentPane(pan);
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                Peer p = Registro.reg.get(Integer.parseInt(e.getKeyChar() + ""));
                System.err.println("Peer " + p.name() + " Event " + e.getKeyChar());
                p.run();
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        frame.setVisible(true);
        
        while (true) {
            try {
                sleep(100);
                double average = 0;
                double avCycle = 0;
                for (Peer p : Registro.reg) {
                    avCycle += p.cycle;
                    average += p.neighbors.size();
                    String tot = "\n";
                    for (MyActor a : p.neighbors) {
                        tot+=a.getActor() + "\n";
                        tot+=a.getTimestamp() + "\n";
                        tot+= "\n";
                    }
                    pan.map.get(p.name()).setText(tot);
                }
                
                
                average = average / Registro.reg.size();
                avCycle = avCycle / Registro.reg.size();
                pan.mediaPeer.setText(AVERAGE + round(average, 2));
                pan.mediaCycle.setText(AVERAGE_CYCLE + round(avCycle, 2));

            } catch (Exception ex) {
                Logger.getLogger(ThreadMonitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    public double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
}

}
