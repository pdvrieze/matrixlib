package uk.ac.bournemouth.ap.lib.matrix.char

import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.MutableMatrix
import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix


/**
 * Create a new [CharMatrix] with the dimensions of the original where the value of each cell is
 * the result of applying the [transform] to the value of the cell in the original.
 *
 * @receiver The matrix to use as basis
 * @param transform the function used to determine the new value
 */
public inline fun <T> Matrix<T>.mapChar(transform: (T) -> Char): CharMatrix {
    return CharMatrix(width, height) { x, y -> transform(get(x, y)) }
}

/**
 * Create a new [CharMatrix] with the dimensions of the original where the value of each cell is
 * the result of applying the [transform] to the value of the cell in the original.
 *
 * @receiver The matrix to use as basis
 * @param transform the function used to determine the new value
 */
public inline fun CharMatrix.mapChar(transform: (Char) -> Char): CharMatrix {
    return CharMatrix(width, height) { x, y -> transform(get(x, y)) }
}

/**
 * Create a new [SparseCharMatrix] with the dimensions of the original where the value of each cell is
 * the result of applying the [transform] to the value of the cell in the original.
 *
 * @receiver The matrix to use as basis
 * @param transform the function used to determine the new value
 */
public inline fun <T> SparseMatrix<T>.mapChar(transform: (T) -> Char): SparseCharMatrix = when {
    validator == Matrix.VALIDATOR ||
            this is CharMatrix ->
        MutableCharMatrix(maxWidth, maxHeight).also { it.fill { x, y -> transform(get(x, y)) } }


    else -> {
        val validate: (Int, Int) -> Boolean = when (this) {
            is ArrayMutableSparseCharMatrix -> validator
            else -> { x, y -> isValid(x, y) }
        }

        MutableSparseCharMatrix(
            maxWidth,
            maxHeight,
            validator = validate
        ).also {
            it.fill { x, y -> transform(get(x, y)) }
        }
    }
}

/**
 * Create a new [Matrix] with the dimensions of the original where the value of each cell is
 * the result of applying the [transform] to the value of the cell in the original.
 *
 * @receiver The matrix to use as basis
 * @param transform the function used to determine the new value
 */
public inline fun <R> CharMatrix.mapChar(transform: (Char) -> R): Matrix<R> {
    return MutableMatrix(width, height) { x, y -> transform(get(x, y)) }
}

/**
 * Helper function to set values into a [MutableSparseCharMatrix].
 */
public inline fun MutableSparseCharMatrix.fill(setter: (Int, Int) -> Char) {
    for ((x, y) in indices) {
        this[x, y] = setter(x, y)
    }
}
