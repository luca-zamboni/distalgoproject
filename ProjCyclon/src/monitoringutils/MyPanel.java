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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
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
            
            JButton button2 = new JButton("Malicious");
            button2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    long timastamp = System.currentTimeMillis();
                    Collections.shuffle(Registro.mal);
                    for (final Peer peer2 : Registro.mal){
                        peer2.maliciusNeighbors.add(new MyActor(timastamp, peer.name()));
                    }
                    Registro.mal.add(peer);
                    peer.startAttack();
                    
                }
            });
            pan.add(button2);
            
            pan.add(area);
            
            prin.add(pan);
        }
        
        JPanel left = new JPanel();
        left.setLayout(new GridLayout(4, 1));
        
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
        
        JButton kill = new JButton("Kill half");
        kill.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Peer> half =  Registro.reg.subList(0,Registro.reg.size()/2 );
                for (final Peer peer : half) {
                    peer.deactive();
                }
            }
        });
        left.add(kill);
        
        JButton attacco = new JButton("Attack");
        attacco.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Peer> half =  Registro.reg.subList(0,5);
                for (final Peer peer : half) {
                    long timastamp = System.currentTimeMillis();
                    for (final Peer peer2 : half) {
                        peer.maliciusNeighbors.add(new MyActor(timastamp, peer2.name()));
                    }
                    peer.startAttack();
                    System.err.println(peer.name() + " son cattivo");
                }
            }
        });
        left.add(attacco);
        
        mediaPeer = new JLabel();
        stat.add(mediaPeer);
        mediaCycle = new JLabel();
        stat.add(mediaCycle);
        
        this.add(stat,BorderLayout.NORTH);
        this.add(left,BorderLayout.WEST);
        this.add(prin,BorderLayout.CENTER);
            
    }
    
    
    
}
