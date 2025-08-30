package uk.ac.bournemouth.ap.lib.matrix

import uk.ac.bournemouth.ap.lib.matrix.ext.Coordinate
import uk.ac.bournemouth.ap.lib.matrix.impl.MutableMatrixCompanion

/**
 * An extension to Matrix that is mutable. This is effectively a 2D array.
 */
public interface MutableMatrix<T> : MutableSparseMatrix<T>, Matrix<T> {
    override fun copyOf(): MutableMatrix<T>

    override fun row(rowIndex: Int): MutableListView<T>

    override fun column(columnIndex: Int): MutableListView<T>

    override fun set(x: Int, y: Int, value: T): T

    override fun set(pos: Coordinate, value: T): T = set(pos.x, pos.y, value)

    /**
     * The companion object contains factory functions to create new instances. There is no
     * guarantee as to the specific type returned for the interface (but always an instance of
     * [MutableMatrix]).
     */
    public companion object : MutableMatrixCompanion<Any?> {
        /**
         * Create a new *mutable* matrix that is initialized from the given matrix. Unlike the `Matrix`
         * equivalent this version is guaranteed to create a new matrix, and it does not invoke `[copyOf]`.
         *
         * @param original The matrix to create a copy of
         * @return A new `MutableMatrix` implementation that contains the same content as the original.
         */
        override fun <T> invoke(original: Matrix<T>): MutableMatrix<T> {
            return ArrayMutableMatrix(original)
        }

        /**
         * A simple array-like mutable matrix that contains the given initial value.
         *
         * @param width The width of the matrix
         * @param height The height of the matrix
         * @param initValue The inititial value for each cell in the matrix.
         */
        override fun <T> invoke(width: Int, height: Int, initValue: T): MutableMatrix<T> {
            return ArrayMutableMatrix(width, height, initValue)
        }

        /**
         * A simple array-like mutable matrix that contains the values given by the function. The values are assigned
         * at construction.
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
        ): MutableMatrix<T> {
            return ArrayMutableMatrix(width, height, init = init)
        }

    }

}