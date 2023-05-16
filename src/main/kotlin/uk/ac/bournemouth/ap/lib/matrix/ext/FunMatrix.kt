package uk.ac.bournemouth.ap.lib.matrix.ext

import uk.ac.bournemouth.ap.lib.matrix.impl.AbstractMatrix

/**
 * [Matrix] where values are determined by a function, not storage.
 * @property width The width of the matrix
 * @property height The height of the matrix
 * @property valueFun The function that determines the value of a cell
 */
public class FunMatrix<T>(
    override val width: Int,
    override val height: Int,
    public val valueFun: (Int, Int) -> T
) : AbstractMatrix<T>() {
    override fun doGet(x: Int, y: Int): T {
        return valueFun(x, y)
    }

    override fun copyOf(): FunMatrix<T> {
        return FunMatrix(width, height, valueFun)
    }

    public fun <R> map(transform: (T) -> R): FunMatrix<R> {
        return FunMatrix(width, height) { x, y -> transform(valueFun(x, y)) }
    }

}