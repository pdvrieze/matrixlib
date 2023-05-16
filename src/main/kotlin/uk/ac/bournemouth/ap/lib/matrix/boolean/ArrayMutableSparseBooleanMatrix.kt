package uk.ac.bournemouth.ap.lib.matrix.boolean

/**
 * A mutable sparse boolean matrix implementation backed by an array.
 */
public class ArrayMutableSparseBooleanMatrix : ArrayMutableBooleanMatrixBase {

    override val validator: (Int, Int) -> Boolean

    public constructor(maxWidth: Int, maxHeight: Int, validator: (Int, Int) -> Boolean) :
            super(maxWidth, maxHeight) {
        this.validator = validator
    }

    public constructor(
        maxWidth: Int,
        data: BooleanArray,
        validator: (Int, Int) -> Boolean
    ) : super(maxWidth, data) {
        this.validator = validator
    }

    override fun copyOf(): MutableSparseBooleanMatrix {
        return ArrayMutableSparseBooleanMatrix(maxWidth, data.copyOf(), validator)
    }

    override fun fill(value: Boolean) {
        data.fill(value)
    }

    /**
     * The companion object contains factory functions to create new instances with initialization.
     */
    public companion object {

        /**
         * Create a new instance with given [maxWidth], [maxHeight], [validator] and initialized according
         * to [init].
         * @param validator This function determines whether a particular cell is part of the matrix.
         *                  Note that the implementation expect this function to return the same
         *                  result for all invocations.
         */
        public inline operator fun invoke(
            maxWidth: Int,
            maxHeight: Int,
            noinline validator: (Int, Int) -> Boolean,
            init: (Int, Int) -> Boolean
        ): ArrayMutableSparseBooleanMatrix {
            return ArrayMutableSparseBooleanMatrix(maxWidth, maxHeight, validator).apply {
                fill(init)
            }
        }

    }
}