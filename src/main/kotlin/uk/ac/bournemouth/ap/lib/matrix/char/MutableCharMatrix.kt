package uk.ac.bournemouth.ap.lib.matrix.char

import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.MutableMatrix
import uk.ac.bournemouth.ap.lib.matrix.impl.MutableMatrixCompanion

/**
 * Interface representing a specialised mutable matrix type for storing integers.
 */
public interface MutableCharMatrix : CharMatrix, MutableSparseCharMatrix, MutableMatrix<Char> {
    override fun copyOf(): MutableCharMatrix


    /**
     * The companion object contains factory functions to create new instances with initialization.
     */
    public companion object : MutableMatrixCompanion<Char> {
        @Suppress("UNCHECKED_CAST")
        override fun <T : Char> invoke(original: Matrix<T>): MutableMatrix<T> =
            invoke(source = original) as MutableMatrix<T>

        public operator fun invoke(source: Matrix<Char>): MutableCharMatrix = when (source) {
            is CharMatrix -> ArrayMutableCharMatrix(source)
            else -> ArrayMutableCharMatrix(source.width, source.height, source::get)
        }

        public operator fun invoke(source: CharMatrix): MutableCharMatrix =
            ArrayMutableCharMatrix(source)

        /**
         * Create a [MutableCharMatrix] initialized with the default value of the [Int] type (`0`)
         *
         * @param width Width of the matrix
         * @param height Height of the matrix
         */
        public operator fun invoke(width: Int, height: Int): MutableCharMatrix {
            return ArrayMutableCharMatrix(width, height)
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T : Char> invoke(width: Int, height: Int, initValue: T): MutableMatrix<T> =
            invoke(width, height, initValue) as MutableMatrix<T>

        /**
         * Create a [MutableCharMatrix] initialized with the default value of the [Int] type (`0`)
         *
         * @param width Width of the matrix
         * @param height Height of the matrix
         * @param initValue The initial value of the elements of the matrix
         */
        public operator fun invoke(width: Int, height: Int, initValue: Char): MutableCharMatrix {
            return ArrayMutableCharMatrix(width, height).apply { fill(initValue) }
        }

        override fun <T : Char> invoke(
            width: Int,
            height: Int,
            init: (Int, Int) -> T
        ): MutableMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return ArrayMutableCharMatrix(width, height, init) as MutableMatrix<T>
        }

        public inline operator fun invoke(width: Int, height: Int, init: (Int, Int) -> Char):
                MutableCharMatrix {
            return ArrayMutableCharMatrix(width, height, init)
        }

    }

}