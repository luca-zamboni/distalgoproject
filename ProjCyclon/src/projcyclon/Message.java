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
public class Message implements Serializable {

    public final String sender;
    public final int type;

    public Message(int type, String sender) {
        this.sender = sender;
        this.type = type;
    }
    
}
