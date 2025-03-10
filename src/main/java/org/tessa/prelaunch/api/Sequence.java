package org.tessa.prelaunch.api;
import java.util.Collection;
import java.util.Set;
import java.util.Map;
import java.io.File;

// implementations are thread safe
// T must be one of the follow : an enum, extend cloneable, or have a copy constructor
/**
 * A thread-safe interface representing a sequence of elements with a defined timeline (ticks).
 *
 * @param <T> The type of elements in the sequence, constrained to those that implement {@code SafeValue}.
 *            {@code T} must either be an enum, extend {@code Cloneable}, or have a copy constructor.
 */
@SuppressWarnings("unused")
public interface Sequence<T extends SafeValue> extends Cloneable {

    /**
     * Gets the highest tick in the sequence that has a value set.
     *
     * @return The last set tick or {@code -1} if no terms have been set.
     */
    int tick();

    /**
     * Retrieves the value at the given tick.
     *
     * @param tick The tick index of the value to retrieve.
     * @return The value at the specified tick.
     */
    T at(int tick);

    /**
     * Gets the name of the sequence.
     *
     * @return The name of the sequence.
     */
    String name();

    /**
     * Sets the name of the sequence.
     *
     * @param name The new name of the sequence.
     * @param <R> A type of sequence extending this sequence.
     * @return The current sequence instance, with the updated name.
     */
    <R extends Sequence<T>> R setName(String name);

    /**
     * Sets a default value for the sequence. The default value is used to
     * fill gaps to prevent {@code IndexOutOfBoundsException}.
     *
     * @param defaultValue The default value for the sequence.
     * @param <R> A type of sequence extending this sequence.
     * @return The current sequence instance, with the updated default value.
     */
    <R extends Sequence<T>> R setDefaultValue(T defaultValue);

    /**
     * Gets the default value of the sequence.
     *
     * @return The default value of the sequence.
     */
    T defaultValue();

    /**
     * Returns a deep copy of a subsequence. If bounds are out of range,
     * the method returns a subsequence of `[Math.max(0, startingTick), Math.min(until, this.tick())]`.
     *
     * @param startingTick The start tick index (inclusive).
     * @param until The end tick index (exclusive).
     * @param <R> A type of sequence extending this sequence.
     * @return A deep-copy subsequence of the current sequence.
     */
    <R extends Sequence<T>> R subsequence(int startingTick, int until);

    /**
     * Adds a value to the end of the sequence.
     *
     * @param value The value to add to the sequence.
     * @param <R> A type of sequence extending this sequence.
     * @return The current sequence instance.
     */
    <R extends Sequence<T>> R add(T value);

    /**
     * Inserts a value at the specified tick index. Pre-existing elements
     * from the index onward are shifted to the right, while gaps are filled
     * with the default value.
     *
     * @param tick The index to insert the value into.
     * @param value The value to add to the sequence.
     * @param <R> A type of sequence extending this sequence.
     * @return The current sequence instance.
     */
    <R extends Sequence<T>> R add(int tick, T value);

    /**
     * Appends all terms of another sequence to this sequence.
     *
     * @param sequence The sequence whose terms are to be added.
     * @param <R> A type of sequence extending this sequence.
     * @return The current sequence instance.
     */
    <R extends Sequence<T>> R add(R sequence);

    /**
     * Appends deep copies of all terms of another sequence to this sequence.
     *
     * @param sequence The sequence whose terms are to be copied and added.
     * @param <R> A type of sequence extending this sequence.
     * @return The current sequence instance.
     */
    <R extends Sequence<T>> R addCopy(R sequence);

    /**
     * Fills the sequence with a value from the specified starting tick up to, but not including, the {@code until} tick.
     * Existing elements in this range are shifted to the right.
     *
     * @param startingTick The starting tick index (inclusive).
     * @param until The endpoint tick index (exclusive).
     * @param value The value to insert.
     * @param <R> A type of sequence extending this sequence.
     * @return The current sequence instance.
     */
    <R extends Sequence<T>> R addFromUntil(int startingTick, int until, T value);

    /**
     * Overwrites the value at the given tick index. If the tick is beyond the current bounds,
     * the gap will be filled with the default value before adding the new value.
     *
     * @param tick The tick index to overwrite.
     * @param value The value to set.
     * @param <R> A type of sequence extending this sequence.
     * @return The current sequence instance.
     */
    <R extends Sequence<T>> R set(int tick, T value);

    /**
     * Overwrites sequence values in the range `[startingTick, until)` with the provided value.
     * Any missing elements are created using the given value.
     *
     * @param startingTick The starting tick index (inclusive).
     * @param until The end tick index (exclusive).
     * @param value The value used for overwriting or creating new entries.
     * @param <R> A type of sequence extending this sequence.
     * @return The current sequence instance.
     */
    <R extends Sequence<T>> R setFromUntil(int startingTick, int until, T value);

    /**
     * Removes the element at the specified tick index. Remaining elements are shifted to the left.
     *
     * @param tick The tick index of the value to remove.
     * @param <R> A type of sequence extending this sequence.
     * @return The current sequence instance.
     */
    <R extends Sequence<T>> R remove(int tick);

    /**
     * Removes elements in the range `[startingTick, until)` if they exist. Remaining elements
     * are shifted to close the gap.
     *
     * @param startingTick The starting tick index (inclusive).
     * @param until The end tick index (exclusive).
     * @param <R> A type of sequence extending this sequence.
     * @return The current sequence instance.
     */
    <R extends Sequence<T>> R remove(int startingTick, int until);

    /**
     * Removes all occurrences of the specified value from the sequence.
     * Remaining elements are shifted to close the gaps.
     *
     * @param value The value to remove.
     * @param <R> A type of sequence extending this sequence.
     * @return The current sequence instance.
     */
    <R extends Sequence<T>> R removeAll(T value);

    /**
     * Removes all elements from the sequence that match any value from the provided collection.
     *
     * @param values The collection of values to remove.
     * @param <R> A type of sequence extending this sequence.
     * @return The current sequence instance.
     */
    <R extends Sequence<T>> R removeAll(Collection<T> values);

    /**
     * Removes all terms from the sequence that match the terms of the specified subsequence.
     *
     * @param subsequence The subsequence whose terms should be removed.
     * @param <R> A type of sequence extending this sequence.
     * @return The current sequence instance.
     */
    <R extends Sequence<T>> R removeAll(R subsequence);

    /**
     * Clears the sequence, removing all elements.
     *
     * @param <R> A type of sequence extending this sequence.
     * @return The current sequence instance.
     */
    <R extends Sequence<T>> R clear();

    /**
     * Creates a shallow copy of the sequence with the specified name.
     *
     * @param copyname The name for the copy.
     * @param <R> A type of sequence extending this sequence.
     * @return A shallow copy of the sequence.
     */
    <R extends Sequence<T>> R copy(String copyname);

    /**
     * Creates a deep copy of the sequence with the specified name.
     *
     * @param copyname The name for the copy.
     * @param <R> A type of sequence extending this sequence.
     * @return A deep copy of the sequence.
     */
    <R extends Sequence<T>> R deepCopy(String copyname);

    /**
     * Checks for semantic equality between this sequence and another object.
     * Two sequences are considered equal if their terms at each tick are equal.
     *
     * @param obj The object to compare with.
     * @return {@code true} if the sequences are equal, {@code false} otherwise.
     */
    boolean equals(Object obj);

    /**
     * Generates a string representation of the sequence in the form `tick=value\\n`.
     *
     * @return A string describing the sequence.
     */
    String toString();

    /**
     * Converts the sequence to a {@code Set} of its elements.
     *
     * @return A set containing the sequence elements.
     */
    Set<T> asSet();

    /**
     * Converts the sequence to a {@code Map}, where the tick index is the key, and the element is the value.
     *
     * @return A map representation of the sequence.
     */
    Map<Integer, T> asMap();

    /**
     * Writes the sequence to a file in the format `tick=value\\n`.
     *
     * @param filename The name of the file to write to.
     * @return The {@code File} reference of the written file.
     */
    File write(String filename);
}