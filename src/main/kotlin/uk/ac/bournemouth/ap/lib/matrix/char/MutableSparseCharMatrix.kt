package uk.ac.bournemouth.ap.lib.matrix.char

import uk.ac.bournemouth.ap.lib.matrix.ArraySparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.MutableSparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.boolean.BooleanMatrix
import uk.ac.bournemouth.ap.lib.matrix.boolean.mapBoolean
import uk.ac.bournemouth.ap.lib.matrix.impl.MutableSparseMatrixCompanion

/**
 * A mutable sparse matrix for integers. This interface supports mutating the values.
 */
public interface MutableSparseCharMatrix : SparseCharMatrix, MutableSparseMatrix<Char> {
    override operator fun set(x: Int, y: Int, value: Char)

    override fun copyOf(): MutableSparseCharMatrix

    public fun contentEquals(other: SparseCharMatrix): Boolean

    /**
     * The companion object contains factory functions to create new instances with initialization.
     */
    public companion object : MutableSparseMatrixCompanion<Char> {
        @Deprecated("This version is only there for inheritance", level = DeprecationLevel.ERROR)
        override fun <T : Char> invoke(original: SparseMatrix<T>): MutableSparseMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return invoke(source = original) as MutableSparseMatrix<T>
        }

        /**
         * Create a new mutable matrix initialized from the source.
         */
        public operator fun invoke(source: SparseMatrix<Char>): MutableSparseCharMatrix =
            when (source) {
                is CharMatrix -> ArrayMutableCharMatrix(source)
                else -> ArrayMutableSparseCharMatrix(
                    source.maxWidth,
                    source.maxHeight,
                    source.validator,
                    source::get
                )
            }

        /**
         * Factory function that creates and initializes a (readonly) [MutableSparseCharMatrix].
         * [maxWidth] and [maxHeight] are used for the underlying array dimensions. This
         * variant uses a sealed return type to determine whether a value is sparse or not.
         *
         * @param maxWidth The maximum width allowed in the matrix
         * @param maxHeight The maximum height allowed in the matrix
         * @param init Function that determines the initial value for a location.
         */
        override fun <T : Char> invoke(
            maxWidth: Int,
            maxHeight: Int,
            init: SparseMatrix.SparseInit<T>.(Int, Int) -> SparseMatrix.SparseValue<T>
        ): MutableSparseMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return invoke(
                maxWidth,
                maxHeight,
                init as SparseMatrix.SparseInit<Char>.(Int, Int) -> SparseMatrix.SparseValue<Char>
            ) as MutableSparseMatrix<T>
        }

        override fun <T : Char> fromSparseValueMatrix(source: Matrix<SparseMatrix.SparseValue<T>>): MutableSparseMatrix<T> {
            val validData = source.mapBoolean { it.isValid }
            return invoke<T>(
                source.maxWidth,
                source.maxHeight,
                { x, y -> validData[x, y] },
                { x, y -> source[x, y].value })
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
        ): MutableSparseCharMatrix {
            //TODO("Make a compact implementation for this")
            val context = ArraySparseMatrix.valueCreator<Char>()
            val data = Matrix(maxWidth, maxHeight) { x, y -> context.init(x, y) }
            val validatorData = BooleanMatrix(maxWidth, maxHeight) { x, y -> data[x, y].isValid }

            return invoke(
                maxWidth,
                maxHeight,
                validator = { x, y -> validatorData[x, y] },
                init = { x, y -> data[x, y].value }
            )
        }

        override fun <T : Char> invoke(
            maxWidth: Int,
            maxHeight: Int,
            initValue: T,
            validator: (Int, Int) -> Boolean
        ): MutableSparseMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return invoke(maxWidth, maxHeight, initValue, validator) as MutableSparseMatrix<T>
        }

        /**
         * Create a [MutableCharMatrix] initialized with the default value of the [Int] type (`0`)
         *
         * @param maxWidth Width of the matrix
         * @param maxHeight Height of the matrix
         * @param validator Function that determines which cells are part of the matrix
         */
        public operator fun invoke(
            maxWidth: Int,
            maxHeight: Int,
            validator: (Int, Int) -> Boolean
        ): MutableSparseCharMatrix {
            return ArrayMutableSparseCharMatrix(maxWidth, maxHeight, validator)
        }

        /**
         * Create a [MutableSparseCharMatrix] initialized with the given value
         *
         * @param maxWidth Width of the matrix
         * @param maxHeight Height of the matrix
         * @param initValue The initial value for the elements
         * @param validator The function that determines whether a given cell is valid.
         */
        public operator fun invoke(
            maxWidth: Int,
            maxHeight: Int,
            initValue: Char,
            validator: (Int, Int) -> Boolean
        ): MutableSparseCharMatrix {
            val data = CharArray(maxWidth * maxHeight).apply { fill(initValue) }
            return ArrayMutableSparseCharMatrix(maxWidth, maxHeight, data, validator)
        }

        @Suppress("OVERRIDE_BY_INLINE")
        override inline fun <T : Char> invoke(
            maxWidth: Int,
            maxHeight: Int,
            noinline validator: (Int, Int) -> Boolean,
            init: (Int, Int) -> T
        ): MutableSparseMatrix<T> {
            @Suppress("UNCHECKED_CAST")
            return invoke(maxWidth, maxHeight, validator, init) as MutableSparseMatrix<T>
        }

        /**
         * Create a [MutableSparseCharMatrix] initialized with the given value
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
        ): MutableSparseCharMatrix {
            return ArrayMutableSparseCharMatrix(
                maxWidth,
                maxHeight,
                validator = validator,
                init = init
            )
        }
    }
}