package com.company;

import java.util.LinkedList;

public class Main {
    public static final String WHITESPACE = "\\s+";
    static String testInput = "1 2 0\n0 5 6\n7 0 9";

    public static <T> T GetValueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static void main(String[] args) {
        String[] testSplit = testInput.split("\n");
        int rowSize = testSplit.length;
        int colSize = testSplit[0].split("\\s+").length;

        //Sparse Matrix initialization test
        //CSRMatrix myCSRMatrix = new CSRMatrix(rowSize, colSize, testInput);
        //myCSRMatrix.Print();

        //Matrix initialization test
        Matrix myMatrix = new Matrix(3, 3, 1);
        Matrix myMatrix3 = new Matrix("1 -2 -2 -3\n3 -9 0 -9\n-1 2 4 7\n-3 -6 26 2");
        myMatrix.Print();

        //Vector initialization test
        Vector myVector = new Vector("1 2");
        Vector myVector2 = new Vector("6 9");
        Vector myVector3 = new Vector("3 5 6");
        myVector.Print();
        myVector2.Print();
        myVector3.Print();

        //Vector operation test
        Matrix myMatrix2 = myVector.RightMultiplication(myVector3);
        myMatrix2.Print();
        myMatrix2 = GetValueOrDefault(myVector.LeftMultiplication(myVector3), myMatrix2);
        myMatrix2.Print();
        System.out.println("~~Inner product of 1 and 2: " + myVector.InnerProduct(myVector2));
        myVector = GetValueOrDefault(myVector.Add(myVector2), myVector);
        myVector.Print();
        myVector = GetValueOrDefault(myVector.Add(5), myVector);
        myVector.Print();

        //LU Factorization
        myMatrix3.Print();
        LinkedList<Matrix> luList = myMatrix3.LUFactorization();
        System.out.print("~~L: ");
        luList.get(0).Print();
        System.out.print("~~U: ");
        luList.get(1).Print();

        //myCSRMatrix.Add(myNormalMatrix);
        //myCSRMatrix.Print();
    }
}
