# MTH343_SparseMatrix
*Vectors, Dense Matrices, CSR Matrices* and their operations.

## Getting Started
This project objective is to create a set of tools to that mainly help with operating and manipulating on sparse matrices in compressed sparse row (CSR) format. The library I chose to implement cover most of the basic operations for Vectors, Dense Matrices and CSR Matrices. The focus of this project is the Generalized Minimal Residual (GMRES) method/algorithm.

## Library Used:
### In Java:
```
import com.google.gson.Gson;

//IO handler
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
//Number handler - This will support number with a lot of decimals
import java.math.BigDecimal;            
import java.math.MathContext;
import java.math.RoundingMode;
//Database 
import java.util.LinkedList;
```
### In NodeJS:
```
require('fs');        //For file reading/writing
require('path');      //For file searching
require('plotly');    //For graphing
```

## Note about input and how to create objects:
### Vectors: vectors can be created using
* A string:
```
Vector myVector = new Vector("1 2 3 4 5");  //will create a vector with entries 1, 2, 3, 4, 5
```
* A representative number and a size:
```
Vector myVector = new Vector(5, BigDecimal.valueOf(200));  //will create a vector with 5 entries with the value of 200
```
* A array of BigDecimal:
```
BigDecimal[] arr = {BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.TEN};
Vector myVector = new Vector(arr);    //will create a vector with entries 0, 1, 10
```
* Another vector:
```
Vector myVector = new Vector("1 2 3 4 5");  //will create a vector with entries 1, 2, 3, 4, 5
Vector newVector = new Vector(myVector);    //will also create a vector with entries 1, 2, 3, 4, 5
```

### Dense Matrices: matrices can be created using
* A string:
```
Matrix myMatrix = new Matrix("1 2 3 4\n5 6 7 8");
/* will create a 2x2 matrix:
      1 2 3 4
      5 6 7 8
    (extra spaces after the last and before the first numbers of each row can lead to bugs!)
*/
```
* A given row size and column size:
```
Matrix myMatrix = new Matrix(2, 3);
/*  will create a 2x3 matrix:
      0 0 0 
      0 0 0  
*/
```
* A given row size, column size and a representative value:
```
Matrix myMatrix = new Matrix(3, 3, BigDecimal.valueOf(20));
/*  will create a 3x3 matrix:
      20 20 20 
      20 20 20  
      20 20 20
*/
```
* A matrix:
```
Matrix myMatrix = new Matrix(3, 3, BigDecimal.valueOf(20));
/*  will create a 3x3 matrix:
      20 20 20 
      20 20 20  
      20 20 20
*/
Matrix newMatrix = new Matrix(myMatrix);
/*  will also create a 2x3 matrix:
      20 20 20 
      20 20 20  
      20 20 20 
*/
```
* A vector with option:
```
Vector myVector = new Vector("1 2 3");    //will create a vector with entries 1, 2, 3
Matrix myMatrix = new Matrix(myVector, 1);
/*  will create a 1x3 matrix:
      1 2 3
*/
myMatrix = new Matrix(myVector, 2);
/*  will create a 3x1 matrix:
      1
      2
      3
*/
```
* A array of vectors:
```
Vector[] myArr = new Vector[2];
myArr[0] = new Vector("1 2 3");       //Vector: 1, 2, 3
myArr[1] = new Vector("4 5 6");       //Vector: 4, 5, 6
Matrix myVector = new Matrix(myArr);  
/*  will create a 2x3 matrix:
      1 2 3
      4 5 6
*/
```

## Currently Support Operations:
### For vectors: 
* Vector Add(BigDecimal parm): return a vector with its entries equal the sum of the old data and the paramater/input
```
Example: myVector.Add(BigDecimal.valueOf(5));     //will add all entries of myVector with 5
```
* Vector Add(BigDecimal parm): add the original vector and the parm vector up and return the result.
```
Example: myVector.Add(anotherVector);             //will add myVector with anotherVector
```
* Vector Scale(BigDecimal parm): scale all entries of the current vector with parm and return the result.
```
Example: myVector.Scale(BigDecimal.valueOf(-1));  //will multiply all entries of myVector with -1
```
* BigDecimal InnerProduct(Vector parm): calculate inner product of the two vector.
```
Example: myVector.InnerProduct(anotherVector);  //will calculate the inner product of myVector and anotherVector
```
* Matrix Multiply(Vector parm): multiply 2 vector with the original vector as column vector on the left and parm as the row vector on the right and return the result matrix.
```
Example: myVector.Multiply(anotherVector);  //will multiply myVector with anotherVector
```
* Vector Normalize(): return a new vector which is the normalized version of the current vector.
```
Example: myVector.Normalize();              //will return a unit vector based on myVector
```
* void Copy(Vector parm): replace all entries with the data from parm.
```
Example: myVector.Copy(anotherVector);      //will copy data from anotherVector to myVector
```
* boolean Equal(Vector parm): check if two vectors are equal.
```
Example: myVector.Equal(anotherVector);     //will return true if anotherVector has the same entries as myVector  
```
* void TakeInput(String input): process an input string and convert it to a corresponding vector
```
Example: myVector.TakeInput("1 2 3 4 5");   //will produce a vector with entries: 1, 2, 3, 4, 5
```


## External library use
Gson for json file writing in Java: https://github.com/google/gson

Plotly for graphing in NodeJS: https://plot.ly/nodejs/

## TODO:
* Complete this readme!
* Finish the report!
