/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package projcyclon;

import akka.actor.Props;
import akka.actor.UntypedActor;
import java.util.ArrayList;
import static projcyclon.ProjCyclon.system;

/**
 *
 * @author luca
 */
public class Tracker extends UntypedActor{
    
    public static final int DEF_PEER_NUM = 4;
    
    ArrayList<IActorRef> peer;

    @Override
    public void preStart() throws Exception {
        super.preStart();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Message) {
            Message m = (Message) message;
            switch (m.type) {
                case 0:
                    ArrayList<IActorRef> peerReply = getPeerForReply();
                    m.sender.tell(new MessagePeer(1, this.getSelf(), null), null);
                default:
                    System.err.println("Error reading message");
            }
        }
    }
    
    private ArrayList<IActorRef> getPeerForReply(){
        ArrayList<IActorRef> ret = new ArrayList<>();
        int i = 0;
        for (int j = 0; j < DEF_PEER_NUM; j++) {
            ret.add(getRandomPeer());
        }
        return ret;
    }
    
    private IActorRef getRandomPeer(){
        int rand = (int) (Math.random() * peer.size());
        return peer.get(rand);
    }
    
}
