@file:Suppress("UNCHECKED_CAST")

package uk.ac.bournemouth.ap.lib.matrix

import uk.ac.bournemouth.ap.lib.matrix.impl.AbstractMutableMatrix
import uk.ac.bournemouth.ap.lib.matrix.impl.fillWith
import java.util.function.Consumer

/**
 *  Mutable matrix class backed by an array.
 *
 *  @constructor This is an internal constructor that exposes the data format. Use the factory
 *               functions.
 */
public class ArrayMutableMatrix<T> @PublishedApi internal constructor(
    override val width: Int,
    private val data: Array<T?>
) : AbstractMutableMatrix<T>(),
    MutableMatrix<T> {
    override val height: Int = data.size / width

    /**
     * Create a matrix that is initialized with a single value.
     *
     * @param width The width of the matrix
     * @param height The (initial) height of the matrix
     * @param initValue The initial value for each cell
     */
    public constructor(width: Int, height: Int, initValue: T) : this(
        width,
        (arrayOfNulls<Any?>(width * height) as Array<T?>).fillWith(initValue)
    )

    /**
     * Create a matrix by copying the [original] matrix. Note that
     * this doesn't do a deep copy (copy the value objects).
     * @param original The matrix to copy from.
     */
    public constructor(original: Matrix<T>) : this(
        original.maxWidth,
        when (original) {
            is ArrayMutableMatrix -> original.data.copyOf()
            else -> {
                val w = original.width
                val h = original.height
                Array<Any?>(w * h) { original[it % w, it / w] } as Array<T?>
            }
        })

    override fun doSet(x: Int, y: Int, value: T): T {
        val idx = x + y * maxWidth
        val old = data[idx]
        data[idx] = value
        return old as T
    }

    @Deprecated("Binary compatibility only, don't use doSet", ReplaceWith("set(x, y, value)"), level = DeprecationLevel.HIDDEN)
    @JvmSynthetic
    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("doSet")
    override fun `$doSet`(x: Int, y: Int, value: T) {
        doSet(x, y, value)
    }

    override fun doGet(x: Int, y: Int): T {
        @Suppress("UNCHECKED_CAST")
        return data[x + y * maxWidth] as T
    }

    override fun row(rowIndex: Int): MutableListView<T> = RowView(rowIndex)

    override fun column(columnIndex: Int): MutableListView<T> = ColumnView(columnIndex)

    /**
     * Creates a copy of the matrix of an appropriate type with the same content.
     */
    override fun copyOf(): ArrayMutableMatrix<T> =
        ArrayMutableMatrix(this)

    /**
     * Optimized implementation of forEach.
     */
    @Suppress("NewApi")
    override fun forEach(action: Consumer<in T>) {
        for(element in data) {
            action.accept(element as T)
        }
    }

    /**
     * The companion object contains factory functions to create new instances with initialization.
     */
    public companion object {
        /**
         * Create and initialize a matrix.
         *
         * @param width The width of the matrix to create.
         * @param height The height of the matrix to create.
         * @param init Initializer that determines the initial value for the given coordinate.
         */
        public inline operator fun <T> invoke(
            width: Int,
            height: Int,
            init: (Int, Int) -> T
        ): ArrayMutableMatrix<T> {
            val data = Array<Any?>(width * height) { init(it % width, it / width) } as Array<T?>
            return ArrayMutableMatrix(width, data)
        }
    }


    private inner class RowView(private val rowIdx: Int): MutableListView<T> {
        override val size: Int get() = width

        override fun get(index: Int): T {
            return doGet(index, rowIdx)
        }

        override fun set(index: Int, element: T): T {
            return doSet(index, rowIdx, element)
        }


    }

    private inner class ColumnView(private val colIdx: Int): MutableListView<T> {
        override val size: Int get() = width

        override fun get(index: Int): T {
            return doGet(colIdx, index)
        }

        override fun set(index: Int, element: T): T {
            return doSet(colIdx, index, element)
        }
    }

}
