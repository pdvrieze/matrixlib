package uk.ac.bournemouth.ap.lib.matrix.ext

import uk.ac.bournemouth.ap.lib.matrix.ArraySparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix

/**
 * [SparseMatrix] where values are determined by a function, not storage.
 * @property maxWidth The width of the matrix
 * @property maxHeight The height of the matrix
 * @param dataFunctions object that provides access to the actual data.
 */
public class FunSparseMatrix<T> private constructor(
    override val maxWidth: Int,
    override val maxHeight: Int,
    private val dataFunctions: DataAccess<T>
) : SparseMatrix<T> {

    /**
     * Constructor where validity and value are determined by validator and valueFun separately.
     * This is somewhat more efficient in the case that [valueFun] is expensive as [validator] is
     * called separately.
     */
    public constructor(
        maxWidth: Int,
        maxHeight: Int,
        validator: (Int, Int) -> Boolean,
        valueFun: (Int, Int) -> T
    ) : this(maxWidth, maxHeight, AccessImpl2(validator, valueFun))

    /**
     * Constructor where validity and value are determined by the same underlying function. Note
     * that valueFun is always called, results are not cached.
     */
    public constructor(
        maxWidth: Int,
        maxHeight: Int,
        valueFun: SparseMatrix.SparseInit<T>.(Int, Int) -> SparseMatrix.SparseValue<T>
    ) : this(
        maxWidth,
        maxHeight,
        AccessImpl1(valueFun)
    )

    override fun get(x: Int, y: Int): T {
        return dataFunctions.get(this, x, y)
    }

    override val validator: (Int, Int) -> Boolean
        get() = dataFunctions.validator

    override fun copyOf(): FunSparseMatrix<T> {
        return FunSparseMatrix(maxWidth, maxHeight, dataFunctions)
    }

    public fun <R> map(transform: (T) -> R): FunSparseMatrix<R> {
        return FunSparseMatrix(maxWidth, maxHeight, dataFunctions.map(transform))
    }

    private interface DataAccess<T> {
        val validator: (Int, Int) -> Boolean
        fun get(matrix: FunSparseMatrix<T>, x: Int, y: Int): T
        fun isValid(x: Int, y: Int): Boolean
        fun <R> map(transform: (T)->R): DataAccess<R>
    }

    /**
     * Implementation that supports access in a way that reduces double query on get.
     */
    private class AccessImpl1<T>(
        private val valueFun: SparseMatrix.SparseInit<T>.(Int, Int) -> SparseMatrix.SparseValue<T>
    ) : DataAccess<T> {
        private val creator = ArraySparseMatrix.valueCreator<T>()

        override val validator: (Int, Int) -> Boolean = ::isValid

        override fun isValid(x: Int, y: Int): Boolean = creator.valueFun(x, y).isValid

        override fun get(matrix: FunSparseMatrix<T>, x: Int, y: Int): T {
            if (x !in 0 until matrix.maxWidth ||
                y !in 0 until matrix.maxHeight
            ) throw IndexOutOfBoundsException("($x,$y) out of range: ([0,${matrix.maxWidth}), [0,${matrix.maxHeight}))")

            val v = creator.valueFun(x, y)
            if (!v.isValid) throw IndexOutOfBoundsException("($x, $y) is a sparse index")

            return v.value
        }

        override fun <R> map(transform: (T) -> R): DataAccess<R> {
            return AccessImpl1 { x, y ->
                creator.valueFun(x, y).map(transform)
            }
        }
    }

    /**
     * Access implementation for the separate function case.
     */
    private class AccessImpl2<T>(
        override val validator: (Int, Int) -> Boolean,
        private val valueFun: (Int, Int) -> T
    ) : DataAccess<T> {
        override fun get(matrix: FunSparseMatrix<T>, x: Int, y: Int): T = when {
            x !in 0 until matrix.maxWidth ||
                    y !in 0 until matrix.maxHeight ->
                throw IndexOutOfBoundsException("($x,$y) out of range: ([0,${matrix.maxWidth}), [0,${matrix.maxHeight}))")

            !validator(x, y) ->
                throw IndexOutOfBoundsException("($x, $y) is a sparse index")

            else -> valueFun(x, y)
        }

        override fun <R> map(transform: (T) -> R): DataAccess<R> {
            return AccessImpl2(validator) { x, y -> transform(valueFun(x, y)) }
        }

        override fun isValid(x: Int, y: Int): Boolean = validator(x, y)
    }
}