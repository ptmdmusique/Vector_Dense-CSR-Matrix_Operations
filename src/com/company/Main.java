package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.MathContext;
import java.util.LinkedList;
import java.util.Scanner;

//FOR PRINTING:
// %~~ Means for general purpose
// ~! For debugging purpose
public class Main {
    //NOTE: LOW BIGDECIMAL_SCALE CAN LEADS TO 0 AS ROUNDING RESULT!!!
    static int BIGDECIMAL_SCALE = 15;                                           //Max decimal used
    static MathContext mathContext = new MathContext(BIGDECIMAL_SCALE);         //Max decimal used in BigDecimal

    static final String WHITESPACE = "\\s+";
    static int PRECISION = 7;                                                   //Max decimal to be displayed
    static int MAX_SLOT = Math.max(10, PRECISION);                               //Max slot to display: 5.5 counts as 3 slots

    static int ENTRY_MAX_SLOT = 5;                                              //Max slot of column and row value
    static int DATA_MAX_SLOT = 30;                                              //For CSRMatrix.PrintData() only
    private static String testInput = "1 2 0 3\n0 5 6 4\n7 0 9 6\n0 0 0 2";
    private static String testInputVector1 = "4 5 3 1";
    private static String testInput2 = "1 1 0\n1 0 1\n0 1 1";
    private static String testInput3 = "1 0 2 3\n0 3 5 0\n2 5 4 0";
    private static String iterationInput = "2 4 2 5\n5 4 1 10\n9 7 5 1\n22 48 15 1";
    private static String bIterationInput = "9 50 3 2";

    private static String outFileName = "out";
    static String iterationOutputFileName = "iterationOut";
    private static String extension = ".txt";

    public static <T> T GetValueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static void main(String[] args) throws FileNotFoundException {
        //Set output stream
        PrintStream fileOut = new PrintStream("./" + outFileName + extension);
        System.setOut(fileOut);

        //Test pre-task
        CSRMatrix csrMatrix = new CSRMatrix(testInput);
        Matrix matrix = new Matrix(testInput);
        Vector vector = new Vector(testInputVector1);
        Print(csrMatrix, "CSR Matrix 1");
        Print(matrix, "Matrix 1");
        Print(vector, "Vector 1");
        Print(csrMatrix.TimeVector(vector), "CSR 1 x Vector 1");
        Print(matrix.TimeVector(vector), "Matrix 1 x Vector 1");

        Print(csrMatrix.GetTranspose(), "CSR 1^T");
        Print(matrix.GetTranspose(), "Matrix 1^T");
//        Print(csrMatrix.GetTranspose().TimeVector(vector), "CSR^T 1 x Vector 1");
//        Print(matrix.GetTranspose().TimeVector(vector), "Matrix^T 1 x Vector 1");
    }

    private static void IsCSRSymmetric(CSRMatrix parm, String name){
        if (CSRMatrix.IsSymmetric(parm)){
            System.out.println("%~~" + name + " is symmetric!");
        } else {
            System.out.println("%~~" + name + " is NOT symmetric!");
        }
    }
    private static void Print(CSRMatrix parm, String name){
        System.out.println("%~~" + name);
        parm.Print();
    }
    private static void PrintData(CSRMatrix parm, String name){
        System.out.println("%~!" + name + " data:");
        parm.PrintData();
    }
    private static void Print(Vector parm, String name){
        System.out.println("%~~" + name);
        parm.Print();
    }
    private static void Print(Matrix parm, String name){
        System.out.println("%~~" + name);
        parm.Print();
    }
    private static void Print(LinkedList<Matrix> matrices, String name){
        if (matrices.size() <= 0){
            System.out.println("%~~Empty list!!!");
        }
        System.out.println("%~~" + name);
        for(Matrix matrix : matrices){
            matrix.Print();
        }
    }

    static int NumberOfPermutation(int spot, int numberOfChoice){
        if (spot > numberOfChoice){
            return 0;
        }
        int result = 1;
        for(int indx = 0; indx < spot; indx++){
            result *= numberOfChoice--;
        }
        return result;
    }
    static int[] NextPermutation(int[] currArr){
        //Lexicological order
        if (currArr == null || currArr.length < 2){
            return currArr;
        }
        //Find the first element that is less than its previous element from right to left
        int[] result = new int[currArr.length];
        System.arraycopy(currArr, 0, result, 0, currArr.length);
        int indx = currArr.length - 2, indx2 = currArr.length - 1;
        for(; indx > 0; indx--){
            if(result[indx] < result[indx + 1]) break;
        }

        //Find an element from right to left that is greater than the previous mentioned element
        //  then swap
        for(; indx2 > indx; indx2--){
            if(result[indx] < result[indx2]){
                break;
            }
        }

        if (indx2 == 0 && indx == 0){
            Reverse(result, 0, currArr.length - 1);
            return result;
        }

        //Swap
        result[indx] = currArr[indx2];
        result[indx2] = currArr[indx];
        //Then reverse from indx + 1 to length - 1
        if (indx < result.length - 1){
            Reverse(result, indx + 1, result.length - 1);
        }
        return result;
    }
    private static void Reverse(int[] nums, int left, int right){
        while(left<right){
            int temp = nums[left];
            nums[left]=nums[right];
            nums[right]=temp;
            left++;
            right--;
        }
    }
    private static void Print(int[] arr){
        System.out.println("%%~~Arr: ");
        for(int entry: arr){
            System.out.print(entry + " ");
        }
        System.out.println();
    }

    private static String ReadCSRFromFile(String fileName) throws FileNotFoundException {
        //Index start at 1!!!
        //https://math.nist.gov/MatrixMarket/formats.html#mtx
        StringBuilder result = new StringBuilder();

        File file =
                new File(fileName);
        Scanner sc = new Scanner(file);

        //Initialization
        String tempLine;
        while((tempLine = sc.nextLine()) != null && tempLine.startsWith("%"));
        String[] temps = tempLine.split(" ");
        int row = Integer.parseInt(temps[0]);
        int col = Integer.parseInt(temps[1]);
        int totalLine = Integer.parseInt(temps[2]);
        StringBuilder[] rows = new StringBuilder[row];
        int[] lastCol = new int[row];
        for(int indx = 0; indx < row; indx++){
            rows[indx] = new StringBuilder();
            lastCol[indx] = -1;
        }

        //Taking input in
        for(int indx = 0; indx < totalLine; indx++){
            int curRow = sc.nextInt() - 1;
            int curCol = sc.nextInt() - 1;
            String curData = sc.nextLine();

            if (lastCol[curRow] != curCol - 1){
                //Some 0s are missing
                for(int indx2 = lastCol[curRow] + 1; indx2 < curCol; indx2++){
                    rows[curRow].append(" " + 0);
                }
            }
            rows[curRow].append(curData);
            lastCol[curRow] = curCol;
        }

        //Fill out 0s
        for(int curRow = 0; curRow < row; curRow++){
            for(int indx = lastCol[curRow] + 1; indx < col; indx++){
                rows[curRow].append(" 0");
            }
        }

        for(int indx = 0; indx < row; indx++){
            result.append(rows[indx]);
            result.append("\n");
        }

        return result.toString();
    }
    private static String ReadVectorFromFile(String fileName) throws FileNotFoundException {
        //Index start at 1!!!
        //https://math.nist.gov/MatrixMarket/formats.html#mtx
        StringBuilder result = new StringBuilder();

        File file =
                new File(fileName);
        Scanner sc = new Scanner(file);

        //Initialization
        String tempLine;
        while((tempLine = sc.nextLine()) != null && tempLine.startsWith("%"));
        int row = Integer.parseInt(tempLine.split(" ")[0]);

        //Taking input in
        for(int indx = 0; indx < row; indx++){
            String curData = sc.nextLine().replaceAll(WHITESPACE, "");

            if (curData.equals(WHITESPACE)) continue;
            result.append(curData);
            result.append(" ");
        }

        return result.toString();
    }

}
