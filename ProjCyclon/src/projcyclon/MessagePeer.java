/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package projcyclon;

import akka.actor.ActorRef;
import java.util.ArrayList;
import scala.Serializable;

/**
 *
 * @author luca
 */
public class MessagePeer implements Serializable{
    
    public final int type;
    public final String sender;
    public final ArrayList<MyActor> peer;

    public MessagePeer(int type, String sender, ArrayList<MyActor> peer) {
        this.type = type;
        this.sender = sender;
        this.peer = peer;
    }
    
}
