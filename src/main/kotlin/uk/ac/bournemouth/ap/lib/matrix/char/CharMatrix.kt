package uk.ac.bournemouth.ap.lib.matrix.char
import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.impl.ImmutableMatrixCompanion

public interface CharMatrix : Matrix<Char>, SparseCharMatrix {
    public fun toFlatArray(): CharArray
    public fun contentEquals(other: CharMatrix): Boolean
    override fun copyOf(): CharMatrix

    /**
     * The companion object contains factory functions to create new instances with initialization.
     */
    public companion object : ImmutableMatrixCompanion<Char> {
        @Suppress("UNCHECKED_CAST")
        override fun <T : Char> invoke(original: Matrix<T>): Matrix<T> =
            invoke(source = original) as Matrix<T>

        public operator fun invoke(source: Matrix<Char>): CharMatrix = when (source) {
            is CharMatrix -> ArrayMutableCharMatrix(source)
            else -> ArrayMutableCharMatrix(source.width, source.height, source::get)
        }

        public operator fun invoke(source: CharMatrix): CharMatrix =
            ArrayMutableCharMatrix(source)

        @Suppress("UNCHECKED_CAST")
        override fun <T : Char> invoke(width: Int, height: Int, initValue: T): Matrix<T> =
            invoke(width, height, initValue) as Matrix<T>

        /**
         * Create an [CharMatrix] initialized with the given value
         *
         * @param width Width of the matrix
         * @param height Height of the matrix
         * @param initValue The value of all cells
         */
        public operator fun invoke(width: Int, height: Int, initValue: Char): CharMatrix {
            return SingleValueCharMatrix(width, height, initValue)
        }

        @Suppress("OVERRIDE_BY_INLINE")
        override inline fun <T : Char> invoke(
            width: Int,
            height: Int,
            init: (Int, Int) -> T
        ): Matrix<T> {
            @Suppress("UNCHECKED_CAST")
            return ArrayMutableCharMatrix(width, height, init) as Matrix<T>
        }

        /**
         * Create an [CharMatrix] initialized according to the init function.
         * @param width Width of the matrix
         * @param height Height of the matrix
         * @param init Function used to initialise each cell.
         */
        public inline operator fun invoke(width: Int, height: Int, init: (Int, Int) -> Char):
                CharMatrix {
            return ArrayMutableCharMatrix(width, height, init)
        }

    }

}


