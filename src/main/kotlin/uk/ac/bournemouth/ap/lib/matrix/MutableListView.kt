package uk.ac.bournemouth.ap.lib.matrix

/**
 * Limited mutable view on lists that allows updating element in a specific position, but does not allow
 * adding/removing elements (the list size is fixed)
 */
public interface MutableListView<E>: ListView<E> {
    public operator fun set(index: Int, element: E):E
}