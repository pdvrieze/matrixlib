package uk.ac.bournemouth.ap.lib.matrix.boolean

import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.impl.ImmutableMatrixCompanion

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