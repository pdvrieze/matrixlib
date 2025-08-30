package uk.ac.bournemouth.ap.lib.matrix.impl

import uk.ac.bournemouth.ap.lib.matrix.MutableSparseMatrix

/**
 * Abstract base class for [MutableSparseMatrix] implementations.
 * @suppress
 */
public abstract class AbstractMutableSparseMatrix<T> protected constructor() : AbstractSparseMatrix<T>(),
    MutableSparseMatrix<T> {

    /** Implement setting by validating and the delegating to doSet */
    override fun set(x: Int, y: Int, value: T): T? {
        validate(x, y)
        return doSet(x, y, value)
    }

    @Deprecated("Binary compatibility only", level = DeprecationLevel.HIDDEN)
    override fun `$set`(x: Int, y: Int, value: T) {
        set(x, y, value)
    }

    /** Actual implementation of setting values, does not need validation */
    protected abstract fun doSet(x: Int, y: Int, value: T): T?


    @Deprecated("Binary compatibility only, don't use doSet", ReplaceWith("set(x, y, value)"), level = DeprecationLevel.HIDDEN)
    @JvmSynthetic
    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("doSet")
    protected open fun `$doSet`(x: Int, y: Int, value: T) {
        doSet(x, y, value)
    }

    /** Get a string representation of the matrix (line wrapped) */
    override fun toString(): String = toString("MutableSparseMatrix")

}