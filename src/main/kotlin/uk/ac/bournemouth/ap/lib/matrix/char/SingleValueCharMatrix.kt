package uk.ac.bournemouth.ap.lib.matrix.char

import uk.ac.bournemouth.ap.lib.matrix.ext.SingleValueMatrix


/**
 * [CharMatrix] that contains a single value for all nonsparse coordinates.
 * @property width The width of the matrix
 * @property height The height of the matrix
 * @property value The value for each valid cell
 * @property validator The function that determines which cells are valid (not sparse)
 */
public class SingleValueCharMatrix(width: Int, height: Int, value: Char) :
    SingleValueMatrix<Char>(width, height, value), CharMatrix {
    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated("Do not call this directly it is meaningless")
    override fun toFlatArray(): CharArray = CharArray(width * height) { value }

    override fun contentEquals(other: CharMatrix): Boolean = when (other) {
        is SingleValueCharMatrix -> other.value == value
        else -> other.all { it == value }
    }

    override fun copyOf(): SingleValueCharMatrix {
        return SingleValueCharMatrix(width, height, value)
    }
}

