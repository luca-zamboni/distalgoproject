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
import static projcyclon.ProjCyclon.system;

/**
 *
 * @author luca
 */
public class Tracker extends UntypedActor{
    
    public static final int DEF_PEER_NUM = 5;
    
    ArrayList<IActorRef> peer;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        peer = new ArrayList<>();
        peer.add(new IActorRef(System.currentTimeMillis(),this.getSelf()));
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Message) {
            
            Message m = (Message) message;
            
            if(!peer.contains(m.sender)){
                peer.add(new IActorRef(System.currentTimeMillis(), m.sender));
            }
            
            switch (m.type) {
                case 0:
                    //System.err.println(m.sender.path().name());
                    ArrayList<IActorRef> peerReply = getPeerForReply(m.sender);
                    m.sender.tell(new MessagePeer(1, this.getSelf(), peerReply), null);
                break;
                default:
                    System.err.println("Error reading message");
                break;
            }
        }
    }
    
    private ArrayList<IActorRef> getPeerForReply(ActorRef sender){
        ArrayList<IActorRef> ret = new ArrayList<>();
        for(int i = 0; i<DEF_PEER_NUM && i < peer.size(); i++) {
            if(!peer.get(i).getActor().equals(sender))
                ret.add(peer.get(i));
        }
        System.err.println(ret.size());
        return ret;
    }
    
}
