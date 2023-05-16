package uk.ac.bournemouth.ap.lib.matrix

import uk.ac.bournemouth.ap.lib.matrix.impl.AbstractSparseMatrix
import java.util.function.Consumer

/**
 * An implementation of a read-only sparse matrix that is backed by an array.
 *
 * @constructor Internal version that exposes the underlying storage format.
 */
public class ArraySparseMatrix<T> @PublishedApi internal constructor(
    private val data: Array<Any?>,
    width: Int,
) : AbstractSparseMatrix<T>() {

    override val maxWidth: Int = width
    override val maxHeight: Int = data.size / width

    override val validator: (Int, Int) -> Boolean =
        { x, y -> data[x + y * maxWidth] != SPARSE_CELL }

    override fun isValid(x: Int, y: Int): Boolean {
        return x in 0 until maxWidth &&
                y in 0 until maxHeight &&
                data[x + y * maxWidth] != SPARSE_CELL
    }

    override fun doGet(x: Int, y: Int): T {
        @Suppress("UNCHECKED_CAST")
        return data[x + y * maxWidth] as T
    }

    /**
     * Optimized implementation of forEach to perform [action] for each element in the array.
     */
    override fun forEach(action: Consumer<in T>) {
        for(element in data) {
            if (element != SPARSE_CELL) {
                @Suppress("UNCHECKED_CAST")
                action.accept(element as T)
            }
        }
    }

    override fun copyOf(): SparseMatrix<T> = ArraySparseMatrix(data, maxWidth)

    internal object SPARSE_CELL : SparseMatrix.SparseValue<Nothing> {
        override val isValid: Boolean get() = false
        override val value: Nothing get() = throw IllegalStateException("Sparse cells have no value")
    }

    @JvmInline
    private value class Value<out T>(val _value: Any?) : SparseMatrix.SparseValue<T> {
        override val isValid: Boolean get() = _value != SPARSE_CELL

        @Suppress("UNCHECKED_CAST")
        override val value: T get() = _value as T
    }

    /**
     * The companion object contains factory functions to create new instances with initialization.
     */
    public companion object {

        private object ValueCreator : SparseMatrix.SparseInit<Any>() {
            override fun value(v: Any): SparseMatrix.SparseValue<Any> = Value(v)

            override val sparse: SparseMatrix.SparseValue<Nothing> = SPARSE_CELL
        }

        @Suppress("UNCHECKED_CAST")
        @PublishedApi
        internal fun <T> valueCreator(): SparseMatrix.SparseInit<T> = ValueCreator as SparseMatrix.SparseInit<T>

        /**
         * Factory function that creates and initializes a (readonly) [ArraySparseMatrix].
         * [maxWidth] and [maxHeight] are used for the underlying array dimensions. The
         * [validator] will be called exactly once for each coordinate. If it returns `true`
         * the init function is called to determine the initial value.
         *
         * @param width The maximum width allowed in the matrix
         * @param height The maximum height allowed in the matrix
         * @param validator Function that determines whether a particular location is valid or not.
         * @param init Function that determines the initial value for a location.
         */
        public inline operator fun <T> invoke(
            width: Int,
            height: Int,
            validator: (Int, Int) -> Boolean,
            init: (Int, Int) -> T
        ): ArraySparseMatrix<T> {
            return invoke(width, height) { x, y ->
                when (validator(x, y)) {
                    true -> value(init(x, y))
                    else -> sparse
                }
            }
        }

        /**
         * Factory function that creates and initializes a (readonly) [ArraySparseMatrix].
         * [maxWidth] and [maxHeight] are used for the underlying array dimensions. This
         * variant uses a sealed return type to determine whether a value is sparse or not.
         *
         * @param width The maximum width allowed in the matrix
         * @param height The maximum height allowed in the matrix
         * @param init Function that determines the initial value for a location.
         */
        public inline operator fun <T> invoke(
            width: Int,
            height: Int,
            init: SparseMatrix.SparseInit<T>.(Int, Int) -> SparseMatrix.SparseValue<T>
        ): ArraySparseMatrix<T> {
            val initContext = valueCreator<T>()
            return ArraySparseMatrix(
                Array(width * height) { idx ->
                    val x = idx % width
                    val y = idx / width
                    val newValue = initContext.init(x, y)
                    if (newValue.isValid) newValue.value else newValue
                },
                width
            )
        }

    }
}