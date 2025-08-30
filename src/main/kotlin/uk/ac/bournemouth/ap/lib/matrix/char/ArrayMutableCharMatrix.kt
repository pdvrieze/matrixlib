package uk.ac.bournemouth.ap.lib.matrix.char

import uk.ac.bournemouth.ap.lib.matrix.MutableListView

/**
 * An implementation of a mutable matrix backed by a [CharArray]. This matrix optimizes storing Int
 * values.
 */
public class ArrayMutableCharMatrix : ArrayMutableCharMatrixBase, MutableCharMatrix {

    public constructor(width: Int, height: Int) : super(width, height)

    public constructor(other: CharMatrix) : this(other.width, other.height, other.toFlatArray())

    private constructor(maxWidth: Int, maxHeight: Int, data: CharArray) : super(
        maxWidth,
        maxHeight,
        data
    )

    override fun fill(value: Char) {
        data.fill(value)
    }

    override fun set(x: Int, y: Int, value: Char): Char {
        return doSet(x, y, value)
    }

    override fun row(rowIndex: Int): MutableCharListView {
        return RowView(rowIndex)
    }

    override fun column(columnIndex: Int): MutableCharListView {
        return ColumnView(columnIndex)
    }

    override fun copyOf(): ArrayMutableCharMatrix {
        return ArrayMutableCharMatrix(width, height, data.copyOf())
    }

    override fun contentEquals(other: CharMatrix): Boolean {
        if (width != other.width || height != other.height) return false
        return indices.all { c -> get(c) == other.get(c) }
    }

    override fun contentEquals(other: SparseCharMatrix): Boolean = when (other) {
        is CharMatrix -> contentEquals(other)
        else -> super<MutableCharMatrix>.contentEquals(other)
    }

    private inner class ColumnView(val columnIndex: Int) : MutableCharListView {
        override fun get(index: Int): Char = doGet(columnIndex, index)

        override val size: Int get() = height

        override fun set(index: Int, element: Char): Char =
            doSet(columnIndex, index, element)
    }

    private inner class RowView(val rowIndex: Int) : MutableCharListView {
        override fun get(index: Int): Char = doGet(index, rowIndex)

        override val size: Int get() = width

        override fun set(index: Int, element: Char): Char =
            doSet(index, rowIndex, element)
    }

    /**
     * The companion object contains factory functions to create new instances with initialization.
     */
    public companion object {

        /**
         * Create a new instance with given [width], [height] and initialized according to [init].
         */
        public inline operator fun invoke(
            maxWidth: Int,
            maxHeight: Int,
            init: (Int, Int) -> Char
        ): ArrayMutableCharMatrix {
            val matrix = ArrayMutableCharMatrix(maxWidth, maxHeight)
            for (x in 0 until maxWidth) {
                for (y in 0 until maxHeight) {
                    matrix[x, y] = init(x, y)
                }
            }
            return matrix
        }
    }
}