package com.company;

import java.util.LinkedList;

public class Matrix {
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
    double GetEntry(int row, int col){
        return matrix[row].GetEntry(col);
    }
    void SetEntry(int row, int col, double value){
        matrix[row].SetEntry(col, value);
    }
    Vector GetRow(int row){
        return matrix[row];
    }
    boolean SetRow(int rowNumber, Vector row){
        if (GetRowSize() == 0){
            //Empty matrix  -- Set as the first row
            matrix = new Vector[1];
            matrix[0] = row;
        } else {
            if (GetColSize() != row.GetSize()){
                return false;
            }
            System.arraycopy(row.GetData(), 0, matrix[rowNumber].GetData(), 0, GetColSize());
        }
        return true;
    }

    Matrix(int rowSize, int colSize) {
        this.matrix = new Vector[rowSize];

        for(int indx = 0; indx < rowSize; ++indx) {
            matrix[indx] = new Vector(colSize, 0);
        }

    }
    Matrix(String input) {
        this.TakeInput(input);
    }
    Matrix(int rowSize, int colSize, double value) {
        this.matrix = new Vector[rowSize];

        for(int indx = 0; indx < colSize; ++indx) {
            matrix[indx] = new Vector(colSize, value);
        }

    }
    Matrix(Matrix newMatrix){
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

    public Matrix TimeRightMatrix(Matrix parm){
        //Multiply with parm on the right side
        if (GetColSize() != parm.GetRowSize()){
            System.out.println("\tSizes are different!");
            return null;
        }

        Matrix result = new Matrix(GetRowSize(), parm.GetColSize());

        for(int row = 0; row < GetRowSize(); row++){
            for(int col = 0; col < parm.GetColSize(); col++){
                int temp = 0;
                for(int indx = 0; indx < GetColSize(); indx++){
                    temp += matrix[row].GetEntry(indx) * parm.GetEntry(indx, col);
                }
                result.SetEntry(row, col, temp);
            }
        }
        return result;
    }
    public Matrix TimeLeftMatrix(Matrix parm){
        //Multiply with parm on the left side
        if (parm.GetColSize() != GetRowSize()){
            System.out.println("\tSizes are different!");
            return null;
        }

        Matrix result = new Matrix(parm.GetRowSize(), GetColSize());

        for(int row = 0; row < parm.GetRowSize(); row++){
            for(int col = 0; col < GetColSize(); col++){
                int temp = 0;
                for(int indx = 0; indx < parm.GetColSize(); indx++){
                    temp += parm.GetEntry(row, indx) * matrix[indx].GetEntry(col);
                }
                result.SetEntry(row, col, temp);
            }
        }
        return result;
    }
    public Matrix Add(Matrix parm){
        if (GetColSize() != parm.GetColSize() || GetRowSize() != parm.GetRowSize()){
            System.out.println("\tMatrices' size are different!");
            return null;
        }

        Matrix result = new Matrix(GetRowSize(), GetColSize());
        for(int row = 0; row < GetRowSize(); row++){
            for(int col = 0; col < GetColSize(); col++){
                result.SetEntry(row, col, GetEntry(row, col) + parm.GetEntry(row, col));
            }
        }
        return result;
    }
    public LinkedList<Matrix> LUFactorization(){
        if (GetRowSize() != GetColSize()){
            System.out.println("\tMatrix is not square!");
            return null;
        }

        LinkedList<Matrix> result = new LinkedList<>();
        int size = GetColSize();
        Matrix lowerMatrix = new Matrix(size, size);
        Matrix upperMatrix = new Matrix(size, size);

        int permuArr[] = new int[size];
        Vector permuVectors[] = new Vector[size];

        for(int indx = 0; indx < size; indx++){
            //Reverse order, so the next permutation will return the first permutation
            permuArr[indx] = size - indx - 1;
            permuVectors[indx] = new Vector(size, 0);
            permuVectors[indx].SetEntry(indx, 1);
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
                lowerMatrix.GetMatrix()[indx].SetEntry(indx, 1);
            }

            //The first row of U is the same as the first row of A
            for(int indx = 0; indx < size; indx++){
                upperMatrix.GetMatrix()[0].SetEntry(indx, temp.GetMatrix()[0].GetEntry(indx));
            }

            //The first column of L is firstColA / A[0][0]
            for(int indx = 0; indx < size; indx++){
                lowerMatrix.GetMatrix()[indx].SetEntry(0,
                        temp.GetMatrix()[indx].GetEntry(0) / temp.GetMatrix()[0].GetEntry(0));
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
                    double sum = 0;
                    for (int indx = col - 1; indx >= 0; indx--){
                        //Secret trick!
                        sum += lowerMatrix.GetMatrix()[row].GetEntry(indx) *
                                upperMatrix.GetMatrix()[indx].GetEntry(col);
                    }
                    lowerMatrix.SetEntry(row, col,
                            (temp.GetMatrix()[row].GetEntry(col) - sum) /
                                    upperMatrix.GetMatrix()[col].GetEntry(col));
                }

                //Then we fill out the U matrix
                for(int col = row; col < size; col++){
                    //Same thing, but in the other order
                    double sum = 0;
                    for(int indx = 0; indx < row; indx++){
                        sum += lowerMatrix.GetMatrix()[row].GetEntry(indx) *
                                upperMatrix.GetMatrix()[indx].GetEntry(col);
                    }
                    upperMatrix.SetEntry(row, col,
                            (temp.GetMatrix()[row].GetEntry(col) - sum) /
                                    lowerMatrix.GetMatrix()[row].GetEntry(row));
                }
                
                if(upperMatrix.GetEntry(row, row) == 0){
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

        if (found == true){
            result.add(lowerMatrix);
            result.add(upperMatrix);
        }
        return result;
    }
    public LinkedList<Matrix> QRFactorization(){
        
    }

    void TakeInput(String input) {
        String[] split = input.split("\n");
        if (split.length <= 0){
            return;
        }
        if (matrix == null || split.length != GetRowSize()){
            this.matrix = new Vector[split.length];
        }

        if (matrix == null || split[0].split(Main.WHITESPACE).length != GetColSize()){
            for(int indx = 0; indx < GetRowSize(); indx++){
                matrix[indx] = new Vector(split[0].split(Main.WHITESPACE).length, 0);
            }
        }

        for(int curRow = 0; curRow < GetRowSize(); ++curRow) {
            String[] entries = split[curRow].split(Main.WHITESPACE);
            for(int curCol = 0; curCol < GetColSize(); curCol++){
                matrix[curRow].SetEntry(curCol, Double.parseDouble(entries[curCol]));
            }
        }

    }
    void Print() {
        System.out.println("Matrix: ");
        for(int row = 0; row < GetRowSize(); ++row) {
            for(int col = 0; col < GetColSize(); ++col) {
                System.out.printf("%6.2f ", this.matrix[row].GetEntry(col));
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
                if (vectors[row].GetEntry(col) != vectors[col].GetEntry(row)){
                    return false;
                }
            }
        }

        return true;
    }
}
