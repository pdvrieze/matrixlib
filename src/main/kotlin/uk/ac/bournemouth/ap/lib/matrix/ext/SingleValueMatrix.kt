package uk.ac.bournemouth.ap.lib.matrix.ext

import uk.ac.bournemouth.ap.lib.matrix.ListView
import uk.ac.bournemouth.ap.lib.matrix.impl.AbstractMatrix

/**
 * Matrix that contains a single value for all coordinates.
 * @property width The width of the matrix
 * @property height The height of the matrix
 * @property value The value for each cell
 */
public open class SingleValueMatrix<T>(
    override val width: Int,
    override val height: Int,
    protected val value: T
) : AbstractMatrix<T>() {
    override fun doGet(x: Int, y: Int): T = value

    override fun copyOf(): SingleValueMatrix<T> {
        return SingleValueMatrix(width, height, value)
    }

    override fun row(rowIndex: Int): ListView<T> {
        return RowView()
    }

    override fun column(columnIndex: Int): ListView<T> {
        return ColumnView()
    }

    private inner class RowView: ListView<T> {
        override fun get(index: Int): T = value

        override val size: Int get() = width
    }

    private inner class ColumnView: ListView<T> {
        override fun get(index: Int): T = value

        override val size: Int get() = height
    }

}

