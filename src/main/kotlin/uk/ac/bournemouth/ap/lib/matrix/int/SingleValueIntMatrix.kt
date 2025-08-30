package uk.ac.bournemouth.ap.lib.matrix.int

import uk.ac.bournemouth.ap.lib.matrix.ext.SingleValueMatrix


/**
 * [IntMatrix] that contains a single value for all nonsparse coordinates.
 * @property width The width of the matrix
 * @property height The height of the matrix
 * @property value The value for each valid cell
 * @property validator The function that determines which cells are valid (not sparse)
 */
public class SingleValueIntMatrix(width: Int, height: Int, value: Int) :
    SingleValueMatrix<Int>(width, height, value), IntMatrix {
    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated("Do not call this directly it is meaningless")
    override fun toFlatArray(): IntArray = IntArray(width * height) { value }

    override fun column(columnIndex: Int): IntListView {
        return ColumnView()
    }

    override fun row(rowIndex: Int): IntListView {
        return RowView()
    }

    override fun contentEquals(other: IntMatrix): Boolean = when (other) {
        is SingleValueIntMatrix -> other.value == value
        else -> other.all { it == value }
    }

    override fun copyOf(): SingleValueIntMatrix {
        return SingleValueIntMatrix(width, height, value)
    }

    private inner class ColumnView : IntListView {
        override fun get(index: Int): Int = value

        override val size: Int get() = height
    }

    private inner class RowView : IntListView {
        override fun get(index: Int): Int = value

        override val size: Int get() = width
    }
}

