/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package projcyclon;

import akka.actor.ActorRef;
import java.util.Comparator;
import java.util.Objects;

/**
 *
 * @author luca
 */
public class MyActor implements Comparator<MyActor>{
    private long timestamp;
    private int  actor;

    public MyActor( long timestamp, int actor) {
        this.timestamp = timestamp;
        this.actor = actor;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getActor() {
        return actor;
    }

    public void setActor(int actor) {
        this.actor = actor;
    }

    @Override
    public int compare(MyActor o1, MyActor o2) {
        if(o1.timestamp > o2.timestamp)
            return 1;
        else
            return -1;
    }

    @Override
    public boolean equals(Object obj) {
        MyActor a = (MyActor) obj;
        return this.actor == a.actor;
    }
    
}
