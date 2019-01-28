package com.company;

public class Main {
    public static final String WHITESPACE = "\\s+";
    static String testInput = "1 2 0\n0 5 6\n7 0 9";

    public static void main(String[] args) {
        String testSplit[] = testInput.split("\n");
        int rowSize = testSplit.length;
        int colSize = testSplit[0].split(WHITESPACE).length;

	    Matrix myMatrix = new Matrix(rowSize, colSize, testInput);
        myMatrix.Print();
    }
}
