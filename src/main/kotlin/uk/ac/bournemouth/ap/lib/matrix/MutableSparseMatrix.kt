package uk.ac.bournemouth.ap.lib.matrix

import uk.ac.bournemouth.ap.lib.matrix.ext.indices
import uk.ac.bournemouth.ap.lib.matrix.impl.MutableSparseMatrixCompanion

/**
 * A mutable version of [SparseMatrix] that adds a setter ([set]) to allow for changing the values
 * in the matrix.
 */
interface MutableSparseMatrix<T> : SparseMatrix<T> {
    /**
     * Set a specific value in the matrix
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @param value The new value
     */
    operator fun set(x: Int, y: Int, value: T)

    /**
     * Helper function to set values
     */
    fun fill(value: T) {
        for ((x, y) in indices) {
            this[x, y] = value
        }
    }

    /**
     * Creates a copy of the matrix of an appropriate type with the same content.
     */
    override fun copyOf(): MutableSparseMatrix<T>

    /**
     * The companion object contains factory functions to create new instances. There is no
     * guarantee as to the specific type returned for the interface (but always an instance of
     * [MutableSparseMatrix]).
     */
    companion object : MutableSparseMatrixCompanion<Any?> {
        /**
         * Create a copy of the original matrix. Note that the returned type may differ depending
         * on the type of the parameter. In particular if the parameter is an instance of [Matrix]
         * the returned type is a [MutableMatrix] instance.
         *
         * @param original The matrix to create a copy of
         */
        override fun <T> invoke(original: SparseMatrix<T>): MutableSparseMatrix<T> {
            return when (original) {
                is ArrayMutableSparseMatrix -> ArrayMutableSparseMatrix(original)
                is Matrix -> MutableMatrix(original)
                else -> invoke(
                    original.maxWidth,
                    original.maxHeight,
                    original.validator,
                    original::get
                )
            }
        }

        /**
         * Create a sparse matrix that has the given maximum X and Y values, is initialized with the parameter
         * value, and uses the function to determine whether the cell exists.
         *
         * Note that all coordinates are initialized with [initValue], even when currently sparse. If the validator
         * returns that the cell is no longer sparse this initial value is returned.
         *
         * @param maxWidth The maximum width of the matrix
         * @param maxHeight The maximum height of the matrix
         * @param initValue The initial value for each cell in the matrix.
         * @param validator Function that determines whether the cell at the given coordinates exists.
         * @return The resulting matrix.
         */
        override fun <T> invoke(
            maxWidth: Int,
            maxHeight: Int,
            initValue: T,
            validator: (Int, Int) -> Boolean
        ): MutableSparseMatrix<T> {
            return ArrayMutableSparseMatrix(maxWidth, maxHeight, initValue, validator)
        }

        /**
         * Create a mutable sparse matrix that has the given maximum X and Y values, has the given validator to
         * determine sparseness and uses the given function to initialize the matrix (values set on construction).
         *
         * @param maxWidth The width of the matrix
         * @param maxHeight The height of the matrix
         * @param validator A function that is used to determine whether a particular coordinate is contained
         *                 in the matrix.
         * @param init An initialization function that sets the values for the matrix.
         * @return The resulting matrix.
         */
        @Suppress("OVERRIDE_BY_INLINE")
        override inline operator fun <T> invoke(
            maxWidth: Int,
            maxHeight: Int,
            noinline validator: (Int, Int) -> Boolean,
            init: (Int, Int) -> T
        ): MutableSparseMatrix<T> {
            return ArrayMutableSparseMatrix(maxWidth, maxHeight, validator, init)
        }

        /**
         * Create a mutable sparse matrix with given maximum X and Y that is initialized using the given "constructor"
         * function. This function invokes the initializer on construction only.
         *
         * Example (creating a matrix with a hole in the middle):
         * ```kotlin
         * val donut = SparseMatrix(3, 3) { x, y ->
         *     when {
         *       y == 0 -> value(x + 1)
         *       y == 2 -> value(7-x)
         *       x == 0 -> value(8)
         *       x == 2 -> value(4)
         *       else -> sparse
         *     }
         * }
         * ```
         *
         * @param maxWidth The width of the matrix
         * @param maxHeight The height of the matrix
         * @param init The function used to initialize the matrix.
         * @return A matrix initialized according to the init function
         */
        @Suppress("OVERRIDE_BY_INLINE")
        override inline operator fun <T> invoke(
            maxWidth: Int,
            maxHeight: Int,
            init: SparseMatrix.SparseInit<T>.(Int, Int) -> SparseMatrix.SparseValue<T>
        ): MutableSparseMatrix<T> {
            return CompactArrayMutableSparseMatrix<T>(
                maxWidth,
                maxHeight,
                init
            )
        }

        /**
         * Create a sparse mutable matrix from the given matrix with special `SparseValue` instances that indicate either a
         * value or sparseness.
         *
         * @param source The matrix providing the initial values and sparseness of the matrix.
         * @return A new sparse matrix initialized from the source.
         */
        override fun <T> fromSparseValueMatrix(source: Matrix<SparseMatrix.SparseValue<T>>): MutableSparseMatrix<T> {
            return CompactArrayMutableSparseMatrix(source)
        }

    }

}