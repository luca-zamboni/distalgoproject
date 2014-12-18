/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package projcyclon;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import java.util.ArrayList;
import java.util.Collections;
import static projcyclon.ProjCyclon.system;

/**
 *
 * @author luca
 */
public class Tracker extends UntypedActor{
    
    public static final int DEF_PEER_NUM = 5;
    
    ArrayList<MyActor> peer;
    
    int myKey = -1;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        peer = new ArrayList<>();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        System.err.println("kaskdk");
        if (message instanceof Message) {
            
            Message m = (Message) message;
            
            if(!peer.contains(new MyActor(0,m.sender))){
                peer.add(new MyActor(System.currentTimeMillis(), m.sender));
            }
            
            switch (m.type) {
                case 0:
                    ArrayList<MyActor> peerReply = getPeerForReply(m.sender);
                    getPointer(m.sender).tell(new MessagePeer(2, -1 , peerReply), null);
                break;
                default:
                    System.err.println("Error reading message");
                break;
            }
        }
        
    }
    
    
    private ArrayList<MyActor> getPeerForReply(int sender){
        ArrayList<MyActor> ret = new ArrayList<>();
        Collections.sort(peer, new MyActor(0,-1));
        for(int i = 0; i<DEF_PEER_NUM && i < peer.size(); i++) {
            if(!peer.get(i).equals(new MyActor(i, sender))){
                ret.add(peer.get(i));
            }else if(peer.size()!=1){
                i-=1;
            }
        }
        return ret;
    }
    
    private ActorRef getPointer(int point){
        return ProjCyclon.peer.get(point);
    }
    
}
