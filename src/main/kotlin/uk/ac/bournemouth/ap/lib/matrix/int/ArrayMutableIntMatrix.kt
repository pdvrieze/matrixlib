package uk.ac.bournemouth.ap.lib.matrix.int

import uk.ac.bournemouth.ap.lib.matrix.MutableListView

/**
 * An implementation of a mutable matrix backed by an [IntArray]. This matrix optimizes storing Int
 * values.
 */
public class ArrayMutableIntMatrix :
    ArrayMutableIntMatrixBase,
    MutableIntMatrix {

    public constructor(width: Int, height: Int) : super(width, height)

    public constructor(other: IntMatrix) : this(other.width, other.height, other.toFlatArray())

    private constructor(maxWidth: Int, maxHeight: Int, data: IntArray) : super(
        maxWidth,
        maxHeight,
        data
    )

    override fun fill(value: Int) {
        data.fill(value)
    }

    override fun copyOf(): ArrayMutableIntMatrix {
        return ArrayMutableIntMatrix(width, height, data.copyOf())
    }

    override fun column(columnIndex: Int): MutableIntListView {
        return ColumnView(columnIndex)
    }

    override fun row(rowIndex: Int): MutableIntListView {
        return RowView(rowIndex)
    }

    override fun contentEquals(other: IntMatrix): Boolean {
        if (width != other.width || height != other.height) return false
        return indices.all { c -> get(c) == other.get(c) }
    }

    override fun contentEquals(other: SparseIntMatrix): Boolean = when (other) {
        is IntMatrix -> contentEquals(other)
        else -> super<MutableIntMatrix>.contentEquals(other)
    }

    private inner class ColumnView(val columnIndex: Int): MutableIntListView {
        override val size: Int get() = height

        override fun get(index: Int): Int = doGet(columnIndex, index)

        override fun set(index: Int, element: Int): Int =
            doSet(columnIndex, index, element)
    }

    private inner class RowView(val rowIndex: Int): MutableIntListView {
        override val size: Int get() = width

        override fun get(index: Int): Int = doGet(index, rowIndex)

        override fun set(index: Int, element: Int): Int =
            doSet( index, rowIndex, element)
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
            init: (Int, Int) -> Int
        ): ArrayMutableIntMatrix {
            val matrix = ArrayMutableIntMatrix(maxWidth, maxHeight)
            for (x in 0 until maxWidth) {
                for (y in 0 until maxHeight) {
                    matrix[x, y] = init(x, y)
                }
            }
            return matrix
        }
    }
}
