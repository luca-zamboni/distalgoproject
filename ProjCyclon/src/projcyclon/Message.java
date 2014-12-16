/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package projcyclon;

import akka.actor.ActorRef;
import scala.Serializable;

/**
 *
 * @author luca
 */
public class Message implements Serializable {

    public final String stringa;
    public final ActorRef sender;
    public final int type;

    public Message(int type, String who, ActorRef sender) {
        this.stringa = who;
        this.sender = sender;
        this.type = type;
    }

    

    
}
