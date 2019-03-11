package com.company;

import java.math.BigDecimal;
import java.util.LinkedList;

class Matrix {
    private Vector[] matrix;

    int GetRowSize() {
        return matrix.length;
    }
    int GetColSize() {
        return matrix.length == 0 || matrix[0] == null ? 0 : matrix[0].GetSize();
    }
    Vector[] GetMatrix() {
        return this.matrix;
    }
    BigDecimal GetEntry(int row, int col){
        return matrix[row].GetEntry(col);
    }
    void SetEntry(int row, int col, BigDecimal value){
        matrix[row].SetEntry(col, value);
    }
    Vector GetRow(int row){
        return matrix[row];
    }
    void SetRow(int rowNumber, Vector row){
        if (GetRowSize() == 0){
            //Empty matrix  -- Set as the first row
            matrix = new Vector[1];
            matrix[0] = row;
        } else {
            if (GetColSize() != row.GetSize()){
                return;
            }
            System.arraycopy(row.GetData(), 0, matrix[rowNumber].GetData(), 0, GetColSize());
        }
    }
    Matrix GetTranspose(){
        Matrix result = new Matrix(GetColSize(), GetRowSize());
        for(int curRow = 0; curRow < GetRowSize(); curRow++){
            for(int curCol = 0; curCol < GetColSize(); curCol++){
                result.SetEntry(curCol, curRow, matrix[curRow].GetEntry(curCol));
            }
        }
        return result;
    }
    private Vector[] GetColVector(){
        return GetTranspose().GetMatrix();
    }

    Matrix(int rowSize, int colSize) {
        this.matrix = new Vector[rowSize];

        for(int indx = 0; indx < rowSize; ++indx) {
            matrix[indx] = new Vector(colSize, BigDecimal.ZERO);
        }

    }
    Matrix(String input) {
        this.TakeInput(input);
    }
    Matrix(int rowSize, int colSize, BigDecimal value) {
        this.matrix = new Vector[rowSize];

        for(int indx = 0; indx < colSize; ++indx) {
            matrix[indx] = new Vector(colSize, value);
        }

    }
    private Matrix(Matrix newMatrix){
        matrix = new Vector[newMatrix.GetRowSize()];
        for(int row = 0; row < GetRowSize(); row++){
            matrix[row] = new Vector(newMatrix.GetRow(row));
        }
    }
    Matrix(Vector vector, int option){
        //Option: 1 -- Row-wise vector
        //Option: 2 -- Column-wise vector
        if (option == 1){
            matrix = new Vector[1];
            matrix[0] = new Vector(vector);
        } else {
            matrix = new Vector[vector.GetSize()];
            for(int indx = 0; indx < matrix.length; indx++){
                matrix[indx] = new Vector(1, vector.GetEntry(indx));
            }
        }
    }
    private Matrix(Vector[] vectors){
        matrix = new Vector[vectors.length];
        for(int indx = 0; indx < vectors.length; indx++){
            matrix[indx] = new Vector(vectors[indx]);
        }
    }

    Matrix TimeRightMatrix(Matrix parm){
        //Multiply with parm on the right side
        if (GetColSize() != parm.GetRowSize()){
            System.out.println("\tSizes are different!");
            return null;
        }

        Matrix result = new Matrix(GetRowSize(), parm.GetColSize());

        for(int row = 0; row < GetRowSize(); row++){
            for(int col = 0; col < parm.GetColSize(); col++){
                BigDecimal temp = BigDecimal.ZERO;
                for(int indx = 0; indx < GetColSize(); indx++){
                    temp = temp.add(matrix[row].GetEntry(indx).multiply(parm.GetEntry(indx, col), Main.mathContext), Main.mathContext);
                }
                result.SetEntry(row, col, temp);
            }
        }
        return result;
    }
    Matrix TimeLeftMatrix(Matrix parm){
        //Multiply with parm on the left side
        if (parm.GetColSize() != GetRowSize()){
            System.out.println("\tSizes are different!");
            return null;
        }

        Matrix result = new Matrix(parm.GetRowSize(), GetColSize());

        for(int row = 0; row < parm.GetRowSize(); row++){
            for(int col = 0; col < GetColSize(); col++){
                BigDecimal temp = BigDecimal.ZERO;
                for(int indx = 0; indx < parm.GetColSize(); indx++){
                    temp = temp.add(parm.GetEntry(row, indx).multiply(matrix[indx].GetEntry(col), Main.mathContext), Main.mathContext);
                }
                result.SetEntry(row, col, temp);
            }
        }
        return result;
    }
    Vector TimeVector(Vector parm){
        if (parm.GetSize() != GetColSize()){
            System.out.println("Different size!");
            return null;
        }
        //Vector on the right side
        Vector result = new Vector(GetRowSize(), BigDecimal.ZERO);
        for(int curRow = 0; curRow < GetRowSize(); curRow++){
            BigDecimal temp = BigDecimal.ZERO;
            for(int curCol = 0; curCol < GetColSize(); curCol++){
                temp = temp.add(GetEntry(curRow, curCol).multiply(parm.GetEntry(curCol), Main.mathContext), Main.mathContext);
            }
            result.SetEntry(curRow, temp);
        }

        return result;
    }
    Matrix Add(Matrix parm){
        if (GetColSize() != parm.GetColSize() || GetRowSize() != parm.GetRowSize()){
            System.out.println("\tMatrices' size are different!");
            return null;
        }

        Matrix result = new Matrix(GetRowSize(), GetColSize());
        for(int row = 0; row < GetRowSize(); row++){
            for(int col = 0; col < GetColSize(); col++){
                result.SetEntry(row, col, GetEntry(row, col).add(parm.GetEntry(row, col), Main.mathContext));
            }
        }
        return result;
    }
    Matrix AugmentVectorAtEnd(Vector parm){
        if (parm.GetSize() != GetRowSize()){
            System.out.println("Different size to augment!");
            return null;
        }
        //Augment the matrix with a col vector at the end
        Matrix result = new Matrix(GetRowSize(), GetColSize() + 1);
        Vector[] resultMatrix = result.GetMatrix();
        for(int curRow = 0; curRow < GetRowSize(); curRow++){
            resultMatrix[curRow] = new Vector(GetColSize() + 1, BigDecimal.ZERO);
            for(int curCol = 0; curCol < GetColSize(); curCol++){
                resultMatrix[curRow].SetEntry(curCol, GetEntry(curRow, curCol));
            }
            resultMatrix[curRow].SetEntry(GetColSize(), parm.GetEntry(curRow));
        }

        return result;
    }
    LinkedList<Matrix> LUFactorization(){
        if (GetRowSize() != GetColSize()){
            System.out.println("\tMatrix is not square!");
            return null;
        }

        LinkedList<Matrix> result = new LinkedList<>();
        int size = GetColSize();
        Matrix lowerMatrix = new Matrix(size, size);
        Matrix upperMatrix = new Matrix(size, size);

        int[] permuArr = new int[size];
        Vector[] permuVectors = new Vector[size];

        for(int indx = 0; indx < size; indx++){
            //Reverse order, so the next permutation will return the first permutation
            permuArr[indx] = size - indx - 1;
            permuVectors[indx] = new Vector(size, BigDecimal.ZERO);
            permuVectors[indx].SetEntry(indx, BigDecimal.ONE);
        }

        //Try with all permutation to make sure we got the right permutation
        boolean found = false;
        for(int permu = 0; permu < Main.NumberOfPermutation(size, size) - 1; permu++){
            permuArr = Main.NextPermutation(permuArr);
            Matrix permuMatrix = new Matrix(size, size);
            for(int indx = 0; indx < size; indx++){
                //Set up the permutation matrix
                permuMatrix.SetRow(indx, permuVectors[permuArr[indx]]);
            }
            Matrix temp = new Matrix(this.TimeRightMatrix(permuMatrix));
            //temp.Print();

            //The diagonal of the L matrix is 0
            for(int indx = 0; indx < temp.GetMatrix().length; indx++){
                lowerMatrix.GetMatrix()[indx].SetEntry(indx, BigDecimal.ONE);
            }

            //The first row of U is the same as the first row of A
            for(int indx = 0; indx < size; indx++){
                upperMatrix.GetMatrix()[0].SetEntry(indx, temp.GetMatrix()[0].GetEntry(indx));
            }

            //The first column of L is firstColA / A[0][0]
            for(int indx = 0; indx < size; indx++){
                lowerMatrix.GetMatrix()[indx].SetEntry(0,
                        temp.GetMatrix()[indx].GetEntry(0).divide(temp.GetMatrix()[0].GetEntry(0), Main.BIGDECIMAL_SCALE, BigDecimal.ROUND_HALF_UP));
            }

            //Fill out the rest, starting from L to U
            int row = 1;
            for(; row < size; row++){
                //Fill only from 1 to diagonal (which has the same index as "row") for L
                // and from diagonal to size - 1 for U
                for(int col = 1; col < row; col++){
                    //Since the sum need to add up to the original matrix, we can first find the sum by
                    // go backward and find the multiple so that the current entry can add up to the rest
                    // and then subtract from the original entry to find the correct entry of the L matrix

                    //L[row][0] * U[0][row] + L[row][1] * U[1][row] + ... + ?[row][lIndx] * U[lIndx][row] + 0
                    //  = A[row][lIndx]
                    // => ?[row][lIndx] = A[row][lIndx] - ...
                    BigDecimal sum = BigDecimal.ZERO;
                    for (int indx = col - 1; indx >= 0; indx--){
                        //Secret trick!
                        sum = sum.add(lowerMatrix.GetMatrix()[row].GetEntry(indx).multiply(
                                upperMatrix.GetMatrix()[indx].GetEntry(col), Main.mathContext), Main.mathContext);
                    }
                    //System.out.println(upperMatrix.GetMatrix()[col].GetEntry(col) + "     " + temp.GetMatrix()[row].GetEntry(col).add(sum.multiply(BigDecimal.valueOf(-1))));
                    if (upperMatrix.GetMatrix()[col].GetEntry(col).compareTo(BigDecimal.ZERO) == 0){
                        break;
                        //lowerMatrix.SetEntry(row, col, BigDecimal.valueOf(100));
                    } else {
                        lowerMatrix.SetEntry(row, col,
                                (temp.GetMatrix()[row].GetEntry(col).subtract(sum, Main.mathContext)).divide(
                                        upperMatrix.GetMatrix()[col].GetEntry(col), Main.BIGDECIMAL_SCALE, BigDecimal.ROUND_HALF_UP));
                    }
                }

                //Then we fill out the U matrix
                for(int col = row; col < size; col++){
                    //Same thing, but in the other order
                    BigDecimal sum = BigDecimal.ZERO;
                    for(int indx = 0; indx < row; indx++){
                        sum = sum.add(lowerMatrix.GetMatrix()[row].GetEntry(indx).multiply(
                                upperMatrix.GetMatrix()[indx].GetEntry(col), Main.mathContext), Main.mathContext);
                    }
                    upperMatrix.SetEntry(row, col,
                            (temp.GetMatrix()[row].GetEntry(col).subtract(sum, Main.mathContext)).divide(
                                    lowerMatrix.GetMatrix()[row].GetEntry(row), Main.BIGDECIMAL_SCALE, BigDecimal.ROUND_HALF_UP));
                }

                if(upperMatrix.GetEntry(row, row).compareTo(BigDecimal.ZERO) == 0){
                    //Now, this gets tricky, we need permutation!
                    break;
                }
            }

            if (row == size){
                //We reach the end, no more permutation needed
                found = true;
                break;
            }
        }

        if (found){
            result.add(lowerMatrix);
            result.add(upperMatrix);
        } else {
            System.out.println("Can't do LU Factorization!");
        }
        return result;
    }
    LinkedList<Matrix> QRFactorization(){
        Vector[] rowVectors = GetColVector();
        Vector[] qList = new Vector[GetColSize()];
        LinkedList<Matrix> result = new LinkedList<>();
        Matrix rMatrix = new Matrix(GetColSize(), GetColSize());

        //Q matrix
        for(int indx = 0; indx < GetColSize(); indx++){
            Vector temp = new Vector(rowVectors[indx]);
            for(int indx2 = 0; indx2 < indx; indx2++){
                BigDecimal scaler = rowVectors[indx].InnerProduct(qList[indx2]);
                temp = temp.Add(qList[indx2].Scale(scaler.multiply(BigDecimal.valueOf(-1), Main.mathContext)));
            }
            temp = temp.Normalize();
            qList[indx] = temp;
        }

        result.add((new Matrix(qList).GetTranspose()));

        //R matrix
        for(int curRow = 0; curRow < GetColSize(); curRow++){
            for(int curCol = curRow; curCol < GetColSize(); curCol++){
                rMatrix.SetEntry(curRow, curCol, rowVectors[curCol].InnerProduct(qList[curRow]));
            }
        }
        result.add(rMatrix);

        return result;
    }
    Vector BackwardSubstitution(Vector b){
        //Should only do this for square matrix
        if (GetColSize() != GetRowSize()){
            System.out.println("Not a square matrix!");
            return null;
        }

        //Ax = b where A is an upper triangular matrix
        //Check if our matrix is an upper triangular matrix
        for(int curRow = 1; curRow < matrix.length; curRow++){
            for(int curCol = 0; curCol < curRow; curCol++){
                if (GetEntry(curRow, curCol).compareTo(BigDecimal.ZERO) != 0){
                    System.out.println("Matrix is not an upper triangular matrix!!!");
                    return null;
                }
            }
        }

        Vector result = new Vector(GetColSize(), BigDecimal.ZERO);

        for(int curRow = GetRowSize() - 1; curRow >= 0; curRow--){
            BigDecimal temp = b.GetEntry(curRow);
            for(int curCol = curRow + 1; curCol < GetRowSize(); curCol++){
//                temp = temp.add(this.GetEntry(curRow, curCol).multiply(result.GetEntry(curCol), Main.mathContext).
//                        multiply(BigDecimal.valueOf(-1), Main.mathContext), Main.mathContext);
                temp = temp.subtract(this.GetEntry(curRow, curCol).multiply(result.GetEntry(curCol), Main.mathContext), Main.mathContext);
            }
            temp = temp.divide(this.GetEntry(curRow, curRow), Main.BIGDECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
            result.SetEntry(curRow, temp);
        }

        return result;
    }

    private void TakeInput(String input) {
        String[] split = input.split("\n");

        if (split.length <= 0){
            return;
        }
        if (matrix == null || split.length != GetRowSize()){
            this.matrix = new Vector[split.length];
        }

        if (matrix == null || split[0].split(Main.WHITESPACE).length != GetColSize()){
            for(int indx = 0; indx < GetRowSize(); indx++){
                matrix[indx] = new Vector(split[0].split(Main.WHITESPACE).length, BigDecimal.ZERO);
            }
        }

        for(int curRow = 0; curRow < GetRowSize(); ++curRow) {
            String[] entries = split[curRow].split(Main.WHITESPACE);
            for(String entry: entries){
                entry = entry.replaceAll("\\s+","");
            }
            for(int curCol = 0; curCol < GetColSize(); curCol++){
                matrix[curRow].SetEntry(curCol, BigDecimal.valueOf(Double.parseDouble(entries[curCol])));
            }
        }

    }
    void Print() {
        System.out.println("%Matrix: ");
        for(int row = 0; row < GetRowSize(); ++row) {
            for(int col = 0; col < GetColSize(); ++col) {
                System.out.printf("%" + Main.MAX_SLOT + "." + Main.PRECISION + "f ", this.matrix[row].GetEntry(col));
            }
            System.out.println();
        }
    }

    static boolean IsSymmetric(Matrix parm){
        if (parm.GetRowSize() != parm.GetColSize()){
            return false;
        }

        Vector[] vectors = parm.GetMatrix();

        for(int row = 0; row < parm.GetRowSize(); row++){
            for(int col = 0; col < row; col++){
                if (vectors[row].GetEntry(col).compareTo(vectors[col].GetEntry(row)) != 0){
                    return false;
                }
            }
        }

        return true;
    }
}
