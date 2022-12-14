# Module MatrixLib
MatrixLib contains a number of 2-dimensional matrix classes and types that work mostly analogous
to the `Array` type in the standard library. Of particular interest is that it supports sparse matrix
types that allow for certain coordinates not to have a value.

A difference with the `Array` type is that the matrix types are split into read interfaces
and mutable interfaces.

# Package uk.ac.bournemouth.ap.lib.matrix
The overall package for the main `Matrix`, `MutableMatrix`, `SparseMatrix` and `MutableSparseMatrix`
classes as well as pseudo-constructor factory functions declared on the type companions.

For example, to create a `Matrix`, the following code can be used:
```kotlin
val matrix = Matrix(3, 4) { x, y -> x + y }
```

| Type                  | Example                                                                                                                              | Description                                                                                                                                                                                                  |
|-----------------------|--------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [Matrix]              | [`Matrix(orig)`](Matrix.Companion.invoke)                                                                                            | Create a copy of the parameter using the [`copyOf`](Matrix.copyOf) member function.                                                                                                                          |
|                       | [`Matrix(3, 4, "Y")`](Matrix.Companion.invoke)                                                                                       | A matrix that is contains the given "initial" value. The implementation is immutable.                                                                                                                        |
|                       | [`Matrix(3, 4) { x, y -> x * y }`](Matrix.Companion.invoke)                                                                          | A simple array-like matrix that contains the values given by the function. The values are assigned at construction.                                                                                          |
|                       | [`Matrix.fromFunction(2,5) { x, y -> x * y }`](Matrix.Companion.fromFunction)                                                        | A matrix backed directly by the function. The function is invoked on each read of the matrix.                                                                                                                |                                                                                            
| [MutableMatrix]       | [`MutableMatrix(orig)`](MutableMatrix.Companion.invoke)                                                                              | Create a new *mutable* matrix that is initialized from the given matrix.                                                                                                                                     |
|                       | [`MutableMatrix(3, 4, "X")`](MutableMatrix.Companion.invoke)                                                                         | A simple array-like mutable matrix that contains the given initial value `"X"`.                                                                                                                              |
|                       | [`MutableMatrix(3, 4) { x, y -> x * y }`](MutableMatrix.Companion.invoke)                                                            | A simple array-like mutable matrix that contains the values given by the function. The values are assigned at construction.                                                                                  |
| [SparseMatrix]        | [`SparseMatrix(orig)`](SparseMatrix.Companion.invoke)                                                                                | Create a copy of the parameter using the [`copyOf`](SparseMatrix.copyOf) member function.                                                                                                                    |
|                       | [`SparseMatrix(maxX, maxY, initValue) { x, y -> x + y % 2 == 0}`](SparseMatrix.Companion.invoke)                                     | Create a sparse matrix that has the given maximum X and Y values, is initialized with the parameter value, and uses the function to determine whether the cell exists.                                       |
|                       | [`SparseMatrix(maxX, maxY, validator) { x, y -> "($x, $y)" }`](SparseMatrix.Companion.invoke)                                        | Create a sparse matrix that has the given maximum X and Y values, has the given validator to determine sparseness and uses the given function to initialize the matrix (values set on construction).         |
|                       | [`SparseMatrix(maxX, maxY) { x, y -> if (x + y %3 == 0) sparse else value("($x, $y)")`](SparseMatrix.Companion.invoke)               | Create a sparse matrix with given maximum X and Y that is initialized using the given "constructor" function.                                                                                                |
|                       | [`SparseMatrix.fromSparseValueMatrix(source)`](SparseMatrix.Companion.fromSparseValueMatrix)                                         | Create a sparse matrix from the given matrix with special `SparseValue` instances that indicate either a value or sparseness.                                                                                |
| [MutableSparseMatrix] | [`MutableSparseMatrix(orig)`](MutableSparseMatrix.Companion.invoke)                                                                  | Create a mutable matrix initialised with the original sparse matrix                                                                                                                                          |
|                       | [`MutableSparseMatrix(maxX, maxY, initValue) { x, y -> x + y % 2 == 0}`](MutableSparseMatrix.Companion.invoke)                       | Create a sparse mutable matrix that has the given maximum X and Y values, is initialized with the parameter value, and uses the function to determine whether the cell exists.                               |
|                       | [`MutableSparseMatrix(maxX, maxY, validator) { x, y -> "($x, $y)" }`](MutableSparseMatrix.Companion.invoke)                          | Create a sparse mutable matrix that has the given maximum X and Y values, has the given validator to determine sparseness and uses the given function to initialize the matrix (values set on construction). |
|                       | [`MutableSparseMatrix(maxX, maxY) { x, y -> if (x + y %3 == 0) sparse else value("($x, $y)")`](MutableSparseMatrix.Companion.invoke) | Create a sparse mutable matrix with given maximum X and Y that is initialized using the given "constructor" function.                                                                                        |
|                       | [`MutableSparseMatrix.fromSparseValueMatrix(source)`](MutableSparseMatrix.Companion.fromSparseValueMatrix)                           | Create a sparse mutable matrix from the given matrix with special `SparseValue` instances that indicate either a value or sparseness.                                                                        |

# Package uk.ac.bournemouth.ap.lib.matrix.boolean
A package that contains specialized classes for `Boolean` types. It works analogous to the main
matrix package (but specialized for boolean values).

# Package uk.ac.bournemouth.ap.lib.matrix.int
A package that contains specialized classes for `Int` types. It works analogous to the main
matrix package (but specialized for integer values).

# Package uk.ac.bournemouth.ap.lib.matrix.ext
A package with additional functionality including a `Coordinate` class for matrix coordinates.
In adition there are specialized matrix types for single values or function based values. 
