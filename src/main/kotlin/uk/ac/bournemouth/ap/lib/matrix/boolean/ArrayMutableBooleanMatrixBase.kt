package uk.ac.bournemouth.ap.lib.matrix.boolean

import uk.ac.bournemouth.ap.lib.matrix.impl.AbstractMutableSparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix

/**
 * Helper base class for array based int matrices.
 * @suppress
 */
public abstract class ArrayMutableBooleanMatrixBase internal constructor(
    final override val maxWidth: Int,
    protected val data: BooleanArray
) : AbstractMutableSparseMatrix<Boolean>(), MutableSparseBooleanMatrix {

    init {
        require(data.size % maxWidth == 0) { "The data size is not a multiple of the width" }
    }

    final override val maxHeight: Int = data.size / maxWidth

    public constructor(maxWidth: Int, maxHeight: Int) :
            this(maxWidth, BooleanArray(maxWidth * maxHeight))

    public fun toFlatArray(): BooleanArray {
        return data.copyOf()
    }

    override fun fill(value: Boolean) {
        data.fill(value)
    }

    override fun doGet(x: Int, y: Int): Boolean {
        return data[y * maxWidth + x]
    }

    override fun doSet(x: Int, y: Int, value: Boolean): Boolean {
        val idx = y * maxWidth + x
        return data[idx].also {
            data[idx] = value
        }
    }

    @Deprecated(
        "Binary compatibility only, don't use doSet",
        replaceWith = ReplaceWith("set(x, y, value)"),
        level = DeprecationLevel.HIDDEN
    )
    @JvmSynthetic
    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("doSet")
    override fun `$doSet`(x: Int, y: Int, value: Boolean) {
        doSet(x, y, value)
    }


    override fun contentEquals(other: SparseMatrix<*>): Boolean {
        val maxX = maxOf(maxWidth, other.maxWidth)
        val maxY = maxOf(maxHeight, other.maxHeight)
        for (x in 0 until maxX) {
            for (y in 0 until maxY) {
                val valid = isValid(x, y)
                val otherValid = other.isValid(x, y)
                if (valid != otherValid) return false
                if (valid) {
                    if (get(x, y) != other.get(x, y)) return false
                }
            }
        }
        return true
    }

}