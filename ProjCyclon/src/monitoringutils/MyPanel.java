/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package monitoringutils;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import projcyclon.Peer;

/**
 *
 * @author luca
 */
public class MyPanel extends JPanel{
    
    HashMap<String,JTextArea> map = new HashMap<>();
    
    JPanel stat = new JPanel();
    JLabel mediaPeer;
    JLabel mediaCycle;
    
    public MyPanel() {
        
        this.setLayout(new BorderLayout());
        
        JPanel prin = new JPanel();
        prin.setLayout(new GridLayout());
        
        for (final Peer peer : Registro.reg) {
            
            Panel pan = new Panel();
            JTextArea area = new JTextArea();
            map.put(peer.name(), area);
            
            JButton button = new JButton(peer.name());
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    peer.active = false;
                }
            });
            pan.add(button);
            pan.add(area);
            
            prin.add(pan);
        }
        
        JPanel left = new JPanel();
        left.setLayout(new GridLayout(2, 1));
        
        JButton start = new JButton("Start");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (final Peer peer : Registro.reg) {
                    peer.startAutoUpdate();
                }
            }
        });
        left.add(start);
        
        JButton stop = new JButton("Stop");
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (final Peer peer : Registro.reg) {
                    peer.stopAutoUpdate();
                }
            }
        });
        left.add(stop);
        
        mediaPeer = new JLabel();
        stat.add(mediaPeer);
        mediaCycle = new JLabel();
        stat.add(mediaCycle);
        
        this.add(stat,BorderLayout.NORTH);
        this.add(left,BorderLayout.WEST);
        this.add(prin,BorderLayout.CENTER);
            
    }
    
    
    
}
