package uk.ac.bournemouth.ap.lib.matrix

import uk.ac.bournemouth.ap.lib.matrix.ext.Coordinate
import uk.ac.bournemouth.ap.lib.matrix.ext.FunMatrix
import uk.ac.bournemouth.ap.lib.matrix.ext.SingleValueMatrix
import uk.ac.bournemouth.ap.lib.matrix.impl.ImmutableMatrixCompanion
import uk.ac.bournemouth.ap.lib.matrix.impl.MatrixIndices

/**
 * A matrix type (based upon [SparseMatrix]) but it has values at all coordinates.
 */
public interface Matrix<out T> : SparseMatrix<T> {

    /** The width of the matrix. This is effectively the same as [maxWidth]. */
    public val width: Int get() = maxWidth

    /** The height of the matrix. This is effectively the same as [maxWidth]. */
    public val height: Int get() = maxHeight

    /**
     * The indices of all columns in the matrix
     */
    public val columnIndices: IntRange get() = 0 until width

    /**
     * The indices of all rows in the matrix
     */
    public val rowIndices: IntRange get() = 0 until height

    /**
     * Specialised version of indices that doesn't check validity.
     */
    override val indices: Iterable<Coordinate>
        get() = MatrixIndices(this)

    /**
     * This implementation will just check that the coordinates are in range. There should be no
     * reason to no use this default implementation.
     */
    override fun isValid(x: Int, y: Int): Boolean {
        return x in 0 until width && y in 0 until height
    }

    /**
     * Provide a row view for the matrix. It maps the original matrix.
     */
    public fun row(rowIndex: Int): ListView<T>

    /**
     * Provide a column view for the matrix. It maps the original
     */
    public fun column(columnIndex: Int): ListView<T>

    override fun copyOf(): Matrix<T>

    /**
     * @inheritDoc
     *
     * This version special cases other being a matrix for efficiency.
     */
    override fun contentEquals(other: SparseMatrix<*>): Boolean = when (other) {
        is Matrix -> contentEquals(other)
        else -> super.contentEquals(other)
    }

    /**
     * Determine whether the content of this matrix is the same as the other by checking equality
     * on the cell values. Sparse matrices with different dimensions, but the same valid indices
     * can be equal.
     */
    public fun contentEquals(other: Matrix<*>): Boolean {
        if (width != other.width || height != other.height) return false
        return indices.all { c -> get(c) == other.get(c) }
    }

    override val validator: (Int, Int) -> Boolean
        get() = VALIDATOR

    /**
     * The companion object contains factory functions to create new instances. There is no
     * guarantee as to the specific type returned for the interface (but always an instance of
     * [Matrix]).
     */
    public companion object : ImmutableMatrixCompanion<Any?> {
        /**
         * Create a copy of the parameter using the [`copyOf`](Matrix.copyOf) member function.
         *
         * @param original The matrix to create a copy of
         * @return The copy (from [copyOf]), depending on the type this may be the same object as the original.
         */
        override fun <T> invoke(original: Matrix<T>): Matrix<T> {
            return original.copyOf()
        }

        /**
         * A matrix that is contains the given "initial" value. The implementation is immutable.
         *
         * @param width The width of the matrix
         * @param height The height of the matrix
         * @param initValue The value for each cell in the matrix.
         * @return The resulting matrix.
         */
        override fun <T> invoke(width: Int, height: Int, initValue: T): Matrix<T> {
            return SingleValueMatrix(width, height, initValue)
        }

        /**
         * A simple array-like matrix that contains the values given by the function. The values are assigned at
         * construction.
         *
         * ```kotlin
         * val multiplicationTable = Matrix(12, 12) { x, y ->
         *     (x * y).toString()
         * }
         * ```
         *
         * @param width The width of the matrix
         * @param height The height of the matrix
         * @param init Function that allows setting each cell in the matrix. Its parameters are the x and y coordinates.
         * @return The resulting matrix (storing all values)
         */
        @Suppress("OVERRIDE_BY_INLINE")
        override inline operator fun <T> invoke(
            width: Int,
            height: Int,
            init: (Int, Int) -> T
        ): Matrix<T> {
            return ArrayMatrix(width, height, init)
        }

        /**
         * A matrix backed directly by the function. The function is invoked on each read of the matrix.
         * @param width The width of the matrix
         * @param height The height of the matrix
         * @param valueFun The function used to determine the value. The implementation does allow this value to change
         *                 over time.
         */
        override fun <T> fromFunction(width: Int, height: Int, valueFun: (Int, Int) -> T): Matrix<T> {
            return FunMatrix(width, height, valueFun)
        }

        @PublishedApi
        internal val VALIDATOR: (Int, Int) -> Boolean = { _, _ -> true }
    }
}

