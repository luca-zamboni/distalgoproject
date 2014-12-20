package monitoringutils;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

    @Override
    public void run() {
        try {
            sleep(500);
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
                sleep(300);
                for (Peer p : Registro.reg) {
                    String tot = "\n";
                    for (MyActor a : p.neighbors) {
                        tot+=a.getActor() + "\n";
                        tot+=a.getTimestamp() + "\n";
                        tot+= "\n";
                    }
                    pan.map.get(p.name()).setText(tot);
                }

            } catch (Exception ex) {
                Logger.getLogger(ThreadMonitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

}
