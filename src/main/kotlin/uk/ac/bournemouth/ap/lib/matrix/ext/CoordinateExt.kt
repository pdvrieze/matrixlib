
@file:JvmName("CoordinateKt")
@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")

package uk.ac.bournemouth.ap.lib.matrix.ext

import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.MutableSparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.impl.MatrixIndices
import uk.ac.bournemouth.ap.lib.matrix.impl.SparseMatrixIndices

/**
 * Helper function that implements [SparseMatrix.isValid] for coordinates
 */
@Deprecated("Use member function, available for ABI compatibility", level = DeprecationLevel.HIDDEN)
public fun <T> SparseMatrix<T>.isValid(pos: Coordinate): Boolean = isValid(pos.x, pos.y)

/**
 * Helper operator to get values based on a coordinate
 */
@Deprecated("Use member function, available for ABI compatibility", level = DeprecationLevel.HIDDEN)
public operator fun <T> SparseMatrix<T>.get(pos: Coordinate): T = get(pos.x, pos.y)

/**
 * Helper operator to set values based upon a coordinate
 */
@Deprecated("Use member function, available for ABI compatibility", level = DeprecationLevel.HIDDEN)
public operator fun <T> MutableSparseMatrix<T>.set(pos: Coordinate, value: T){ set(pos.x, pos.y, value) }

/** Get an iterable with all valid indices in the matrix */
@Deprecated("Use member function, available for ABI compatibility", level = DeprecationLevel.HIDDEN)
public val SparseMatrix<*>.indices: Iterable<Coordinate> get() = SparseMatrixIndices(this)

@Deprecated("Use member function, available for ABI compatibility", level = DeprecationLevel.HIDDEN)
public val Matrix<*>.indices: Iterable<Coordinate> get() = MatrixIndices(this)
