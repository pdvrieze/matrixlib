package uk.ac.bournemouth.ap.lib.matrix.boolean

import uk.ac.bournemouth.ap.lib.matrix.MutableListView
import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.ext.Coordinate

/**
 * An implementation of a mutable matrix backed by a [BooleanArray]. This matrix optimizes storing
 * Boolean values.
 */
public class ArrayMutableBooleanMatrix :
    ArrayMutableBooleanMatrixBase,
    MutableBooleanMatrix {

    public constructor(width: Int, height: Int) : super(width, height)

    public constructor(
        width: Int,
        height: Int,
        initValue: Boolean
    ) : this(
        width,// Note that for a false value filling is not needed
        BooleanArray(width * height).apply { if (initValue) fill(initValue) })

    public constructor(other: BooleanMatrix) : this(other.width, other.toFlatArray())

    @Suppress("unused", "UNUSED_PARAMETER")
    @PublishedApi
    @Deprecated("Height is not needed, left purely due to inline constructor", level = DeprecationLevel.HIDDEN)
    internal constructor(width: Int, height: Int, data: BooleanArray) :
            super(width, data)

    @PublishedApi
    internal constructor(width: Int, data: BooleanArray) :
            super(width, data)

    override fun row(rowIndex: Int): MutableBooleanListView {
        return RowView(rowIndex)
    }

    override fun column(columnIndex: Int): MutableBooleanListView {
        return ColumnView(columnIndex)
    }

    override fun set(x: Int, y: Int, value: Boolean): Boolean {
        return doSet(x, y, value)
    }

    override fun copyOf(): ArrayMutableBooleanMatrix {
        return ArrayMutableBooleanMatrix(width, data.copyOf())
    }

    override fun contentEquals(other: BooleanMatrix): Boolean {
        if (width != other.width || height != other.height) return false
        return indices.all { c -> get(c) == other.get(c) }
    }

    override fun contentEquals(other: SparseMatrix<*>): Boolean = when (other) {
        is BooleanMatrix -> contentEquals(other)
        else -> super<ArrayMutableBooleanMatrixBase>.contentEquals(other)
    }

    private inner class RowView(private val rowIdx: Int): MutableBooleanListView {
        override val size: Int get() = width

        override fun get(index: Int): Boolean {
            return doGet(index, rowIdx)
        }

        override fun set(index: Int, element: Boolean): Boolean {
            return doSet(index, rowIdx, element)
        }


    }

    private inner class ColumnView(private val colIdx: Int): MutableBooleanListView {
        override val size: Int get() = width

        override fun get(index: Int): Boolean {
            return doGet(colIdx, index)
        }

        override fun set(index: Int, element: Boolean): Boolean {
            return doSet(colIdx, index, element)
        }
    }

    /**
     * The companion object contains factory functions to create new instances with initialization.
     */
    public companion object {
        /**
         * Create a new instance with given [width], [height] and initialized according to [init].
         */
        public inline operator fun invoke(
            width: Int,
            height: Int,
            init: (Int, Int) -> Boolean
        ): ArrayMutableBooleanMatrix {
            return ArrayMutableBooleanMatrix(
                width,
                BooleanArray(width * height) { i -> init(i % width, i / width) })
        }


    }
}