package uk.ac.bournemouth.ap.lib.matrix.impl

import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix

/**
 * Abstract base class for [SparseMatrix] implementations.
 * @suppress
 */
public abstract class AbstractSparseMatrix<T> protected constructor() : SparseMatrix<T> {

    final override fun get(x: Int, y: Int): T {
        validate(x, y)
        return doGet(x, y)
    }

    /**
     * Function that provides the retrieval function for [get].
     */
    protected abstract fun doGet(x: Int, y: Int): T

    /** Get a string representation of the matrix (line wrapped) */
    override fun toString(): String = toString("SparseMatrix")
}