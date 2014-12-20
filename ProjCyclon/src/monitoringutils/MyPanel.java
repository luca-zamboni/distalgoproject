/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package monitoringutils;

import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Thread.sleep;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import projcyclon.MyActor;
import projcyclon.Peer;

/**
 *
 * @author luca
 */
public class MyPanel extends JPanel{
    
    HashMap<String,JTextArea> map = new HashMap<>();
    public MyPanel() {
        this.setLayout(new GridLayout());
        
        for (final Peer peer : Registro.reg) {
            
            Panel pan = new Panel();
            JTextArea area = new JTextArea();
            map.put(peer.name(), area);
            
            JButton button = new JButton(peer.name());
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    peer.run();
                }
            });
            pan.add(button);
            pan.add(area);
            
            this.add(pan);
        }
            
    }
    
    
    
}
