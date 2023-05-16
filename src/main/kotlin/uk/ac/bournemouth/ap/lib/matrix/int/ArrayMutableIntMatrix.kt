package uk.ac.bournemouth.ap.lib.matrix.int

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

    override fun contentEquals(other: IntMatrix): Boolean {
        if (width != other.width || height != other.height) return false
        return indices.all { c -> get(c) == other.get(c) }
    }

    override fun contentEquals(other: SparseIntMatrix): Boolean = when (other) {
        is IntMatrix -> contentEquals(other)
        else -> super<MutableIntMatrix>.contentEquals(other)
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