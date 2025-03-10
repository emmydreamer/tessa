package org.tessa.prelaunch.api;

public interface SafeValue {

    boolean isEnum();
    <T extends SafeValue> T shallowCopy();
    <T extends SafeValue> T deepCopy();

    // the implementing class should also override equals and hashcode if it is not an enum

    @Override
    String toString();



}
