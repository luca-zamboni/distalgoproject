/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package projcyclon;

import akka.actor.ActorRef;

/**
 *
 * @author luca
 */
public class IActorRef{
    private int index;
    private long timestamp;
    private ActorRef actor;

    public IActorRef(int index, long timestamp, ActorRef actor) {
        this.index = index;
        this.timestamp = timestamp;
        this.actor = actor;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ActorRef getActor() {
        return actor;
    }

    public void setActor(ActorRef actor) {
        this.actor = actor;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    
    
}
