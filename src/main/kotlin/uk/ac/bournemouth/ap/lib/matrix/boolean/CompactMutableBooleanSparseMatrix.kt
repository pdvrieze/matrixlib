package uk.ac.bournemouth.ap.lib.matrix.boolean

import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.impl.validate

/**
 * Implementation of [MutableSparseBooleanMatrix] where the same matrix is used to store
 * the sparseness as the actual value.
 */
public class CompactMutableBooleanSparseMatrix internal constructor(
    override val maxWidth: Int,
    private val data: ByteArray
) : MutableSparseBooleanMatrix {
    override val maxHeight: Int = data.size / maxWidth

    override val validator: (Int, Int) -> Boolean = { x, y ->
        data[x + y * maxWidth] >= 0
    }

    override fun isValid(x: Int, y: Int): Boolean {
        return x in 0 until maxWidth &&
                y in 0 until maxHeight &&
                data[x + y * maxWidth] >= 0
    }

    override fun get(x: Int, y: Int): Boolean {
        if (x !in 0 until maxWidth &&
            y !in 0 until maxHeight
        ) {
            throw IndexOutOfBoundsException("($x,$y) out of range: ([0,$maxWidth), [0,$maxHeight))")
        }
        return when (data[x + y * maxWidth].toInt()) {
            -1 -> throw IndexOutOfBoundsException("($x, $y) is a sparse index")
            0 -> false
            else -> true
        }
    }

    override fun set(x: Int, y: Int, value: Boolean) {
        validate(x, y)
        data[x + y * maxWidth] = if (value) 1 else 0
    }

    override fun copyOf(): MutableSparseBooleanMatrix {
        return CompactMutableBooleanSparseMatrix(maxWidth, data.copyOf())
    }

    private fun SparseMatrix<*>.getState(x: Int, y: Int) = when {
        x >= maxWidth ||
                y >= maxHeight ||
                !isValid(x, y) -> SPARSE
        else -> when (get(x, y)) {
            true -> TRUE
            false -> FALSE
            else -> NON_BOOLEAN
        }
    }

    override fun contentEquals(other: SparseMatrix<*>): Boolean {
        for(x in (maxWidth+1) until other.maxWidth) {
            for(y in 0 until other.maxHeight) {
                if (other.isValid(x, y)) return false
            }
        }
        for(x in other.maxWidth+1 until maxWidth) {
            for(y in 0 until maxHeight) {
                if (data[x+y*maxWidth].toInt() >= 0) return false
            }
        }
        val width = minOf(maxWidth, other.maxWidth)
        for(y in (maxHeight+1) until other.maxHeight) {
            for (x in 0 until width) {
                if (other.isValid(x, y)) return false
            }
        }
        for(y in (other.maxHeight+1) until maxHeight) {
            for (x in 0 until width) {
                if (isValid(x, y)) return false
            }
        }
        val height = minOf(maxHeight, other.maxHeight)

        for (col in 0 until width) {
            for (row in 0 until height) {
                if (data[col+row*maxWidth].toInt()!=other.getState(col, row)) return false
            }
        }
        return true
    }

    override fun toString(): String {
        return toString("CompactMutableBooleanSparseMatrix")
    }
}

private const val TRUE: Int = 1
private const val FALSE: Int = 0
private const val SPARSE: Int = -1
private const val NON_BOOLEAN: Int = -2
