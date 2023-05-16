package uk.ac.bournemouth.ap.lib.matrix.boolean

import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix

/**
 * A map implementation that creates a boolean matrix based upon the receiver and the transformation
 * function.
 */
public inline fun <T> Matrix<T>.mapBoolean(transform: (T) -> Boolean): BooleanMatrix {
    return BooleanMatrix(width, height) { x, y -> transform(get(x, y)) }
}

/**
 * A map implementation that creates a boolean sparse matrix based upon the receiver and the
 * transformation function.
 */
public inline fun <T> SparseMatrix<T>.mapBoolean(transform: (T) -> Boolean): SparseBooleanMatrix = when {
    validator == Matrix.VALIDATOR || this is Matrix ->
        MutableBooleanMatrix(maxWidth, maxHeight) { x, y -> transform(get(x, y)) }

    else -> MutableSparseBooleanMatrix(
        maxWidth,
        maxHeight,
        validator = getValidator(),
        init = { x, y -> transform(get(x, y)) }
    )
}

@PublishedApi
internal fun <T> SparseMatrix<T>.getValidator(): (Int, Int) -> Boolean = when (this) {
    is ArrayMutableSparseBooleanMatrix -> validator
    else -> { x, y -> isValid(x, y) }
}

/**
 * Helper function to set values into a [MutableSparseBooleanMatrix].
 */
public inline fun MutableSparseBooleanMatrix.fill(setter: (Int, Int) -> Boolean) {
    for ((x, y) in indices) {
        this[x, y] = setter(x, y)
    }
}
