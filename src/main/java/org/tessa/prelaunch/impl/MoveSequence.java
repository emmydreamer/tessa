package org.tessa.prelaunch.impl;
import org.tessa.prelaunch.api.Sequence;
import org.tessa.prelaunch.api.Move;

public class MoveSequence extends AbstractSequence<Move> implements Sequence<Move> {

    public MoveSequence(String name, Move defaultValue) {
        super(name, defaultValue);
    }

    public String sequenceType() {
        return "MoveSequence";
    }

    @Override
    public synchronized boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        MoveSequence sequence = (MoveSequence) obj;
        if(tick() != sequence.tick()) return false;
        for(int i = 0; i <= tick(); i++) {
            if(!at(i).equals(sequence.at(i))) return false;
        }
        return true;
    }

    public synchronized MoveSequence subsequence(int startingTick, int until) {
        if(startingTick < 0 || until > tick() + 1) {
            logWarning("MoveSequence:subsequence", new IndexOutOfBoundsException("indices on subsequence call out of bounds"));
            startingTick = Math.max(startingTick, 0);
            until = Math.min(until, tick() + 1);
        }
        MoveSequence subsequence = new MoveSequence(name() + "_[" + startingTick + "," + until + ")", defaultValue());
        for(int i = startingTick; i < until; i++) subsequence.add(at(i).deepCopy());
        return subsequence;
    }

    public synchronized MoveSequence copy(String copyname) {
        MoveSequence copy = new MoveSequence(copyname, defaultValue().shallowCopy());
        for(int i = 0; i <= tick(); i++) copy.add(at(i).shallowCopy());
        return copy;
    }

    public synchronized MoveSequence deepCopy(String copyname) {
        MoveSequence copy = new MoveSequence(copyname, defaultValue().deepCopy());
        for(int i = 0; i <= tick(); i++) copy.add(at(i).deepCopy());
        return copy;
    }

}