package uk.ac.bournemouth.ap.lib.matrix.char

import uk.ac.bournemouth.ap.lib.matrix.ext.Coordinate
import uk.ac.bournemouth.ap.lib.matrix.impl.AbstractMutableSparseMatrix

/**
 * Helper base class for array based int matrices.
 */
public abstract class ArrayMutableCharMatrixBase internal constructor(
    override val maxWidth: Int,
    override val maxHeight: Int,
    protected val data: CharArray
) : AbstractMutableSparseMatrix<Char>(), MutableSparseCharMatrix {

    public constructor(maxWidth: Int, maxHeight: Int) :
            this(maxWidth, maxHeight, CharArray(maxWidth * maxHeight))

    /**
     * Get a onedimensional array that stores all elements of the matrix.
     */
    public fun toFlatArray(): CharArray {
        return data.copyOf()
    }

    final override fun doGet(x: Int, y: Int): Char = data[y * maxWidth + x]

    override fun doSet(x: Int, y: Int, value: Char): Char {
        val idx = y * maxWidth + x
        return data[idx].also { data[idx] = value }
    }

    @Deprecated("Binary compatibility only, don't use doSet", ReplaceWith("set(x, y, value)"), level = DeprecationLevel.HIDDEN)
    @JvmSynthetic
    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("doSet")
    override fun `$doSet`(x: Int, y: Int, value: Char) {
        doSet(x, y, value)
    }

    override fun set(x: Int, y: Int, value: Char): Char? {
        return doSet(x, y, value)
    }

    @Suppress("INAPPLICABLE_JVM_NAME")
    @Deprecated("Binary compatibility only", level = DeprecationLevel.HIDDEN)
    @JvmSynthetic
    @JvmName("set")
    public override fun `$set`(x: Int, y: Int, value: Char) {
        set(x, y, value)
    }

    override fun contentEquals(other: SparseCharMatrix): Boolean {
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