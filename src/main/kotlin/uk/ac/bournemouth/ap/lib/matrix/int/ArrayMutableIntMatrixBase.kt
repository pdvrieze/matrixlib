package uk.ac.bournemouth.ap.lib.matrix.int

import uk.ac.bournemouth.ap.lib.matrix.impl.AbstractMutableSparseMatrix

/**
 * Helper base class for array based int matrices.
 */
public abstract class ArrayMutableIntMatrixBase internal constructor(
    override val maxWidth: Int,
    override val maxHeight: Int,
    protected val data: IntArray
) : AbstractMutableSparseMatrix<Int>(), MutableSparseIntMatrix {

    public constructor(maxWidth: Int, maxHeight: Int) :
            this(maxWidth, maxHeight, IntArray(maxWidth * maxHeight))

    /**
     * Get a onedimensional array that stores all elements of the matrix.
     */
    public fun toFlatArray(): IntArray {
        return data.copyOf()
    }

    final override fun doGet(x: Int, y: Int): Int = data[y * maxWidth + x]

    override fun set(x: Int, y: Int, value: Int): Int {
        return doSet(x, y, value)
    }

    @Suppress("INAPPLICABLE_JVM_NAME")
    @Deprecated("Binary compatibility only", level = DeprecationLevel.HIDDEN)
    @JvmSynthetic
    @JvmName("set")
    public override fun `$set2`(x: Int, y: Int, value: Int) {
        set(x, y, value)
    }

    override fun doSet(x: Int, y: Int, value: Int): Int {
        val idx = y * maxHeight + x
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
    override fun `$doSet`(x: Int, y: Int, value: Int) {
        doSet(x, y, value)
    }

    override fun contentEquals(other: SparseIntMatrix): Boolean {
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