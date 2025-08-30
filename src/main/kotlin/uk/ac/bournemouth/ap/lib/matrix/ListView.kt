package uk.ac.bournemouth.ap.lib.matrix

/**
 * A view that allows you to access an indexed collection.
 */
public interface ListView<out E>: List<E> {
    public override operator fun get(index: Int): E

    override fun contains(element: @UnsafeVariance E): Boolean {
        for (i in 0 until size) {
            if (get(i) == element) return true
        }
        return false
    }

    /**
     * Note this is O(n^2)
     */
    override fun containsAll(elements: Collection<@UnsafeVariance E>): Boolean {
        for(e in elements) {
            if (!contains(e)) return false
        }
        return true
    }

    override fun isEmpty(): Boolean = size == 0

    override fun iterator(): Iterator<E> = IndexedIterator(this)

    private class IndexedIterator<E>(
        private val collection: ListView<E>,
        private var index: Int = 0
    ) : ListIterator<E> {

        override fun hasNext(): Boolean = index < collection.size
        override fun next(): E = collection[index++]

        override fun hasPrevious(): Boolean = index > 0

        override fun nextIndex(): Int = index

        override fun previous(): E = collection[index-1].also { --index } // only if no exception

        override fun previousIndex(): Int = index - 1
    }


    override fun indexOf(element: @UnsafeVariance E): Int {
        for(i in 0 until size) {
            if (get(i) == element) return i
        }
        return -1
    }

    override fun lastIndexOf(element: @UnsafeVariance E): Int {
        for(i in (size-1) downTo 0) {
            if (get(i) == element) return i
        }
        return -1
    }

    override fun listIterator(): ListIterator<E> {
        return IndexedIterator(this)
    }

    override fun listIterator(index: Int): ListIterator<E> {
        return IndexedIterator(this, index)
    }

    override fun subList(fromIndex: Int, toIndex: Int): ListView<E> {
        require(toIndex in 0..size) { "toIndex out of range: $toIndex !in 0..$size" }
        require(fromIndex in 0 .. toIndex) { "fromIndex out of range: $fromIndex !in 0..$toIndex" }
        return SubList(this, fromIndex, toIndex)
    }

    private class SubList<E>(private val base: ListView<E>, private val start: Int, end: Int): ListView<E> {
        override fun get(index: Int): E {
            if (index !in 0..<size) throw IndexOutOfBoundsException("Index $index out of range of sublist: $start..$end")
            return base.get(start + index)
        }

        private val end: Int get() = start + size

        override val size: Int = end-start

        override fun subList(fromIndex: Int, toIndex: Int): ListView<E> {
            require(toIndex in 0..size) { "toIndex out of range: $toIndex !in 0..$size" }
            require(fromIndex in 0 .. toIndex) { "fromIndex out of range: $fromIndex !in 0..$toIndex" }

            return SubList(base, start + fromIndex, start + toIndex)
        }
    }
}

