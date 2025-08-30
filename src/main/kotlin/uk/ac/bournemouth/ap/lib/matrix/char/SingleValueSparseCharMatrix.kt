package uk.ac.bournemouth.ap.lib.matrix.char

import uk.ac.bournemouth.ap.lib.matrix.ext.SingleValueSparseMatrix

/**
 * [SparseCharMatrix] that contains a single value for all nonsparse coordinates.
 * @property maxWidth The width of the matrix
 * @property maxHeight The height of the matrix
 * @property value The value for each valid cell
 * @property validator The function that determines which cells are valid (not sparse)
 */
public class SingleValueSparseCharMatrix(
    maxWidth: Int, maxHeight: Int, value: Char,
    override val validator: (Int, Int) -> Boolean
) : SingleValueSparseMatrix<Char>(maxWidth, maxHeight, value, validator), SparseCharMatrix {

    override fun copyOf(): SingleValueSparseCharMatrix =
        SingleValueSparseCharMatrix(maxWidth, maxHeight, value, validator)
}