package uk.ac.bournemouth.ap.lib.matrix.char

import uk.ac.bournemouth.ap.lib.matrix.ArraySparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.boolean.BooleanMatrix
import uk.ac.bournemouth.ap.lib.matrix.impl.ImmutableSparseMatrixCompanion

/** A sparse matrix for storing integers. It works just like SparseMatrix but optimizes int storage.
 * This particular interface only provides read access. The matrix needs to be initialized
 * appropriately or used on a class that is actually mutable.
 */
public interface SparseCharMatrix : SparseMatrix<Char> {
    override operator fun get(x: Int, y: Int): Char

    override fun copyOf(): SparseCharMatrix

    /**
     * The companion object contains factory functions to create new instances with initialization.
     */
    public companion object : ImmutableSparseMatrixCompanion<Char> {
        /**
         * Factory that creates a concrete implementation
         */
        @Deprecated("Use the type specific factory, this one is only there for compatibility", level = DeprecationLevel.ERROR)
        override fun <T : Char> invoke(original: SparseMatrix<T>): SparseMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return invoke(source = original) as SparseMatrix<T>
        }

        /**
         * Create  a new matrix that is a copy of the original
         *
         * @param source The source for the matrix data
         */
        public operator fun invoke(source: SparseMatrix<Char>): SparseCharMatrix =
            when (source) {
                is CharMatrix -> ArrayMutableCharMatrix(source)
                else -> ArrayMutableSparseCharMatrix(
                    source.maxWidth,
                    source.maxHeight,
                    source.validator,
                    source::get
                )
            }

        @Suppress("OVERRIDE_BY_INLINE")
        override inline fun <T : Char> invoke(
            maxWidth: Int,
            maxHeight: Int,
            init: SparseMatrix.SparseInit<T>.(Int, Int) -> SparseMatrix.SparseValue<T>
        ): SparseMatrix<T> {
            @Suppress("UNCHECKED_CAST", "MoveLambdaOutsideParentheses")
            return invoke(
                maxWidth,
                maxHeight,
                { x, y -> (this as SparseMatrix.SparseInit<T>).init(x, y) }
            ) as SparseMatrix<T>
        }

        /**
         * Create a [SparseCharMatrix] initialized with the given value
         *
         * @param maxWidth Width of the matrix
         * @param maxHeight Height of the matrix
         * @param init The function initializing the matrix
         */
        public inline operator fun invoke(
            maxWidth: Int,
            maxHeight: Int,
            init: SparseMatrix.SparseInit<Char>.(Int, Int) -> SparseMatrix.SparseValue<Char>
        ): SparseCharMatrix {
            // TODO("Make a compact implementation for this")
            val context = ArraySparseMatrix.valueCreator<Char>()
            val data = Matrix(maxWidth, maxHeight) { x, y -> context.init(x, y) }
            val validatorData = BooleanMatrix(maxWidth, maxHeight) { x, y -> data[x, y].isValid }

            return SparseCharMatrix(
                maxWidth,
                maxHeight,
                validator = { x, y -> validatorData[x, y] },
                init = { x, y -> data[x, y].value }
            )
        }

        override fun <T : Char> fromSparseValueMatrix(source: Matrix<SparseMatrix.SparseValue<T>>): SparseMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return invoke(source.width, source.height) { x, y -> source[x, y] } as SparseMatrix<T>
        }

        override fun <T : Char> invoke(
            maxWidth: Int,
            maxHeight: Int,
            initValue: T,
            validator: (Int, Int) -> Boolean
        ): SparseMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return invoke(maxWidth, maxHeight, initValue, validator) as SparseMatrix<T>
        }

        /**
         * Create a [SparseCharMatrix] initialized with the given value
         *
         * @param maxWidth Width of the matrix
         * @param maxHeight Height of the matrix
         * @param initValue The value of all cells
         * @param validator The function that determines whether a given cell is valid.
         */
        public operator fun invoke(
            maxWidth: Int,
            maxHeight: Int,
            initValue: Char,
            validator: (Int, Int) -> Boolean
        ): SparseCharMatrix {
            return SingleValueSparseCharMatrix(maxWidth, maxHeight, initValue, validator)
        }

        @Suppress("OVERRIDE_BY_INLINE")
        override inline fun <T : Char> invoke(
            maxWidth: Int,
            maxHeight: Int,
            noinline validator: (Int, Int) -> Boolean,
            init: (Int, Int) -> T
        ): SparseMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return invoke(maxWidth, maxHeight, validator, init) as SparseMatrix<T>
        }

        /**
         * Create a [SparseCharMatrix] initialized with the given value
         *
         * @param maxWidth Width of the matrix
         * @param maxHeight Height of the matrix
         * @param init The function initializing the matrix
         * @param validator The function that determines whether a given cell is valid.
         */
        public inline operator fun invoke(
            maxWidth: Int,
            maxHeight: Int,
            noinline validator: (Int, Int) -> Boolean,
            init: (Int, Int) -> Char
        ): SparseCharMatrix {
            return ArrayMutableSparseCharMatrix(
                maxWidth,
                maxHeight,
                validator = validator,
                init = init
            )
        }
    }

}