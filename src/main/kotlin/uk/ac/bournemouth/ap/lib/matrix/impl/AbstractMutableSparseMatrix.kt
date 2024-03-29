package uk.ac.bournemouth.ap.lib.matrix.impl

import uk.ac.bournemouth.ap.lib.matrix.MutableSparseMatrix

/**
 * Abstract base class for [MutableSparseMatrix] implementations.
 * @suppress
 */
public abstract class AbstractMutableSparseMatrix<T> protected constructor() : AbstractSparseMatrix<T>(),
    MutableSparseMatrix<T> {

    /** Implement setting by validating and the delegating to doSet */
    final override fun set(x: Int, y: Int, value: T) {
        validate(x, y)
        doSet(x, y, value)
    }

    /** Actual implementation of setting values, does not need validation */
    protected abstract fun doSet(x: Int, y: Int, value: T)

    /** Get a string representation of the matrix (line wrapped) */
    override fun toString(): String = toString("MutableSparseMatrix")

}