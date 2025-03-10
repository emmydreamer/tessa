package org.tessa.prelaunch.impl;
import org.tessa.prelaunch.api.Sequence;
import org.tessa.prelaunch.api.SafeValue;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.io.File;

import static org.tessa.prelaunch.TessaPreLaunch.logger;

@SuppressWarnings("unchecked")
public abstract class AbstractSequence<T extends SafeValue> implements Sequence<T> {

    public static String WARNING_MESSAGE = "Sequence Warning. Type: %s State -> Tick: %s \n Number of terms: %s \n Terms: %s \n Method: %s";

    private final LinkedList<T> term;
    private int tick;
    private String name;
    private T defaultValue;
    protected AbstractSequence(String name, T defaultValue) {
        this.term = new LinkedList<>();
        this.tick = -1;
        this.name = name;
        this.defaultValue = defaultValue;
    }


    public abstract String sequenceType();

    // return a subsequence deep copy
    public abstract <R extends Sequence<T>> R subsequence(int startingTick, int until);

    // should enforce T overriding equals
    // then simply compare the sequence terms for equality
    public abstract boolean equals(Object obj);

    public abstract <R extends Sequence<T>> R copy(String copyname);

    public abstract <R extends Sequence<T>> R deepCopy(String copyname);

    @Override
    public final synchronized String toString() {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i <= this.tick(); i++) builder.append(String.format("%d=%s\n", i, this.at(i)));
        return builder.toString();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean hasValueAt(int tick) {
        return tick >= 0 && tick < this.term.size();
    }
    // returns the last set tick

    @SuppressWarnings({"UnnecessaryToStringCall", "StringConcatenationArgumentToLogCall"})
    protected void logWarning(String methodName, Exception e) {
        logger.warn(String.format(WARNING_MESSAGE, this.sequenceType(), this.tick, this.term.size(), this.toString(), methodName), e);
    }

    // returns last set tick
    public int tick() {
        return this.tick;
    }

    // get reference to value at tick
    @Override
    public T at(int tick) {
        if(!hasValueAt(tick)) {
            logWarning("Sequence::at", new IndexOutOfBoundsException("Tick outside of sequence"));
            return this.defaultValue;
        }
        return this.term.get(tick);
    }
    // get sequence name
    public String name() {
        return this.name;
    }
    // set sequence name
    public final synchronized <R extends Sequence<T>> R setName(String name) {
        this.name = name;
        return (R) this;
    }

    public T defaultValue() {
        return this.defaultValue;
    }

    public final synchronized <R extends Sequence<T>> R setDefaultValue(T defaultValue) {
        this.defaultValue = defaultValue.deepCopy();
        return (R) this;
    }


    private void fillWithDefault(int lo, int until) {
        for(int i = lo; i < until; i++) this.term.add(i, this.defaultValue);
    }

    // add value at end of sequence
    public final synchronized <R extends Sequence<T>> R add(T value) {
        this.tick++;
        this.term.add(value);
        return (R) this;
    }
    // add at tick, preexisting elements in [tick, end] are bumped to [tick +1, end + 1]
    public final synchronized <R extends Sequence<T>> R add(int tick, T value) {
        if(!hasValueAt(tick -1)) {
            fillWithDefault(this.tick + 1, tick - 1);
            this.tick = tick - 1;
            logWarning(this.getClass().getName() + "::add", new IndexOutOfBoundsException("Tick outside of sequence"));
        }
        this.term.add(tick, value);
        this.tick++;
        return (R) this;
    }

    // add a sequence to the end of the sequence
    // references to terms are the same
    public final synchronized <R extends Sequence<T>> R add(R sequence) {
        for(int i = 0; i <= sequence.tick(); i++) this.add(sequence.at(i));
        return (R) this;
    }

    public final synchronized <R extends Sequence<T>> R addCopy(R sequence) {
        for(int i = 0; i <= sequence.tick(); i++) this.add((T) sequence.at(i).deepCopy());
        return (R) this;
    }

    // add value at indices [lo, until) without overwriting.
    // preexisting elements in [lo, until) are moved to the right
    public final synchronized <R extends Sequence<T>> R addFromUntil(int lo, int until, T value) {
        if(!hasValueAt(lo - 1)) {
            fillWithDefault(this.tick + 1, lo);
            this.tick = lo - 1;
            logWarning("Sequence::addFromUntil", new IndexOutOfBoundsException("Tick outside of sequence"));
        }
        for(int i = lo; i < until; i++) this.term.add(i, value);
        return (R) this;
    }

    // overwrite the element at tick with value
    // if the tick is outside the sequence:
    // first the gap is filled with the default sequence value
    // then value is added at the end
    public final synchronized <R extends Sequence<T>> R set(int tick, T value) {
        if(!hasValueAt(tick)) {
            fillWithDefault(this.tick + 1, tick + 1);
            this.tick = tick;
        }
        this.term.set(tick, value);
        return (R) this;
    }

    // elements at indices [startingTick, until) are replaced with value if the exist
    // At places in [startingTick, until) without preexisting elements, value is created there
    // if necessary, terms [tick() + 1, startingTick) are fulled with defaultValue()
    public final synchronized <R extends Sequence<T>> R setFromUntil(int startingTick, int until, T value) {
        int lo = Math.max(startingTick, 0);
        for(int i = lo; i < until; i++) {
            if(!hasValueAt(i)) this.term.add(i, value);
            else this.term.set(i, value);
        }
        return (R) this;
    }
    // removes element with index tick
    // preexisting elements [tick + 1, end] are moved one to the left
    // if tick doesn't have an element, the error is logged and the same sequence returned
    public final synchronized <R extends Sequence<T>> R remove(int tick) {
        if(!hasValueAt(tick)) {
            logWarning(this.getClass().getName() + "::remove", new IndexOutOfBoundsException("Tick outside of sequence"));
            return (R) this;
        }
        this.term.remove(tick);
        this.tick--;
        return (R) this;
    }
    // elements at indices [startingTick, until) are removed if they exist
    // preexisting elements at [until, end] are moved to [startingTick, end - (until - startingTick)]
    @SuppressWarnings("ListRemoveInLoop")
    public final synchronized <R extends Sequence<T>> R remove(int startingTick, int until) {
        if(startingTick < 0 || until > this.tick() + 1) logWarning(this.getClass().getName() + "::remove", new IndexOutOfBoundsException("Tick outside of sequence"));
        startingTick = Math.max(startingTick, 0);
        until = Math.min(until, this.tick() + 1);
        for(int i = --until; i >= startingTick; i-- ) this.term.remove(i);
        return (R) this;
    }
    // removes all elements with element.equals(value). Later elements bumped to the left
    public final synchronized <R extends Sequence<T>> R removeAll(T value) {
        this.term.removeIf(v -> v.equals(value));
        return (R) this;
    }
    // remove all elements with element.equals(value) for some value in values
    // remaining elements are bumped left to fill holes in the sequence
    public final synchronized <R extends Sequence<T>> R removeAll(Collection<T> values) {
        for(T value : values) this.removeAll(value);
        return (R) this;
    }
    public final synchronized <R extends Sequence<T>> R removeAll(R subsequence) {
        if(subsequence.tick() > this.tick()) return (R) this;
        if(subsequence.tick() < 0) return (R) this;
        for(int i = 0; i < this.tick() - subsequence.tick() + 1; i++) {
            if(this.subsequence(i, i + subsequence.tick() + 1).equals(subsequence)) remove(i, i + subsequence.tick() + 1);
            if(this.tick - i + 1 < subsequence.tick()) break;
        }
        return (R) this;
    }




    // the sequence is emptied of all elements
    public final synchronized <R extends Sequence<T>> R clear() {
        this.term.clear();
        this.tick = -1;
        return (R) this;
    }

    // returns a set of the elements that form the terms
    // the set of value references for non-enum
    // the enum constants themselves otherwise
    public final synchronized Set<T> asSet() {
        return new HashSet<>(this.term);
    }
    // returns a map from tick to element
    // the map is only a reference copy
    public final synchronized Map<Integer, T> asMap() {
        HashMap<Integer, T> map = new HashMap<>();
        for(int i = 0; i <= this.tick(); i++) map.put(i, this.at(i));
        return map;
    }
    // overwrites the sequence to filename in the form tick=value\n
    // returns the File reference which is null on exception
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public final synchronized File write(String filename) {
        File file = new File(filename);
        try {
            if(file.exists()) file.delete();
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            for(int i = 0; i <= this.tick(); i++) {
                fw.write(String.format("%d=%s\n", i, this.at(i)));
            }
            fw.close();
        } catch (IOException e) {
            logger.error("{}::write", this.getClass().getName(), e);
            return null;
        }
        return file;
    }



}
