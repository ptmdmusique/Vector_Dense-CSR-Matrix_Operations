package com.company;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.LinkedList;
import java.util.Scanner;

//FOR PRINTING:
// \n%~~ Means for general purpose
// ~! For debugging purpose
//FOR JSON: use GSON: https://github.com/google/gson
public class Main {
    //NOTE: LOW BIGDECIMAL_SCALE CAN LEADS TO 0 AS ROUNDING RESULT!!!
    //NOTE: USE HIGHER BIGDECIMAL_SCALE IF RESIDUAL GET BIGGER INSTEAD OF GETTING MINIMIZED
    //NOTE: Data can be found here: https://math.nist.gov/MatrixMarket/
    static int BIGDECIMAL_SCALE = 50;                                           //Max decimal used
    static MathContext mathContext = new MathContext(BIGDECIMAL_SCALE);         //Max decimal used in BigDecimal

    static final String WHITESPACE = "\\s+";
    static int PRECISION = 7;                                                   //Max decimal to be displayed
    static int MAX_SLOT = Math.max(10, PRECISION);                               //Max slot to display: 5.5 counts as 3 slots

    static int ENTRY_MAX_SLOT = 5;                                              //Max slot of column and row value
    static int DATA_MAX_SLOT = 30;                                              //For CSRMatrix.PrintData() only
    private static String testInput = "1 2 0 3\n0 5 6 4\n7 0 9 6\n0 0 0 0";
    private static String testInputVector1 = "4 5 3 1";
    private static String testInput2 = "1 1 0 4\n1 0 1 5\n0 1 11 9";
    private static String testInput3 = "1 0 2 3\n0 3 5 0\n2 5 4 0\n1 2 5 99";
    private static String testInput4 = "2 3 1 4\n1 2 9 0\n0 0 2 0";
    private static String iterationInput = "97110 4452 5789 542\n4112 4457 0 112\n0 97213 11778 0\n124 8872 345 781";
    private static String bIterationInput = "442 0 879 6647";

    //Outputing
    static String inputPath = ".\\bigMatrices\\";
    static String outputPath = ".\\solutionOutput\\";
    static String solutionOutName = "solutionOut";
    private static String textExtension = ".txt";
    private static String jsonExtension = ".json";
    private static PrintStream stdout = System.out;
    private static PrintStream fileOut;
    private static Gson gson = new Gson();
    private static class SolutionOutput{
        int maxStep;
        Long runTime;
        BigDecimal error;
        LinkedList<BigDecimal> residualLength;

        SolutionOutput(BigDecimal error, Long runTime, int maxStep, LinkedList<BigDecimal> residualLength) {
            this.error = error;
            this.runTime = runTime;
            this.maxStep = maxStep;
            this.residualLength = residualLength;
        }
    }
    public static <T> T GetValueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static void main(String[] args) throws FileNotFoundException {
        //Set output stream
        fileOut = new PrintStream("./preTask" + textExtension);
        System.setOut(fileOut);

        //Test pre-task
        System.out.println("%``````````````Pretask``````````````%");
        CheckCSRTimeVector(testInput, testInputVector1);
        CheckCSRMultiplication(testInput2, testInput3);

        //Task 1
        Task1();

        //Task 2
        Task2();
    }

    private static void IsCSRSymmetric(CSRMatrix parm, String name){
        if (CSRMatrix.IsSymmetric(parm)){
            System.out.println("\n%~~" + name + " is symmetric!");
        } else {
            System.out.println("\n%~~" + name + " is NOT symmetric!");
        }
    }
    private static void Print(CSRMatrix parm, String name){
        System.out.println("\n%~~" + name);
        parm.Print();
    }
    private static void PrintData(CSRMatrix parm, String name){
        System.out.println("\n%~!" + name + " data:");
        parm.PrintData();
    }
    private static void Print(Vector parm, String name){
        System.out.println("\n%~~" + name);
        parm.Print();
    }
    private static void Print(Matrix parm, String name){
        System.out.println("\n%~~" + name);
        parm.Print();
    }
    private static void Print(LinkedList<Matrix> matrices, String name){
        if (matrices.size() <= 0){
            System.out.println("\n%~~Empty list!!!");
        }
        System.out.println("\n%~~" + name);
        for(Matrix matrix : matrices){
            matrix.Print();
        }
    }

    //Pretask
    private static void CheckCSRMultiplication(String input1, String input2){
        System.out.println("%~~~~~~~~~~~~~~~~~~CSR Multiplication~~~~~~~~~~~~~~~~~~%");
        //CSR x CSR
        CSRMatrix csrMatrix2 = new CSRMatrix(input1);
        CSRMatrix csrMatrix3 = new CSRMatrix(input2);
        Matrix matrix2 = new Matrix(input1);
        Matrix matrix3 = new Matrix(input2);
        Print(csrMatrix2, "CSR 1");
        Print(csrMatrix3, "CSR 2");
        Print(matrix2, "Matrix 1");
        Print(matrix3, "Matrix 2");

        CSRMatrix result1 = csrMatrix2.TimeMatrix(csrMatrix3);
        Matrix result2 = matrix2.TimeRightMatrix(matrix3);
        Print(result1, "CSR 1 x CSR 2");
        Print(result2, "Matrix 1 x Matrix 2");

        System.out.println();
        if (result1.Equal(result2)){
            System.out.println("%Test Passed!!!");
        } else {
            System.out.println("%Test failed!!!");
        }

        System.out.println("%~~~~~~~~~~~~~~~~~~CSR Multiplication~~~~~~~~~~~~~~~~~~%");
        System.out.println();
    }
    private static void CheckCSRTimeVector(String input1, String input2){
        System.out.println("%~~~~~~~~~~~~~~~~~~CSR and Transpose Time Vector~~~~~~~~~~~~~~~~~~%");
        //CSR x Vector
        CSRMatrix csrMatrix = new CSRMatrix(input1);
        Matrix matrix = new Matrix(input1);
        Vector vector = new Vector(input2);
        Print(csrMatrix, "CSR Matrix 1");
        Print(matrix, "Matrix 1");
        Print(vector, "Vector 1");

        Vector result1 = csrMatrix.TimeVector(vector);
        Vector result2 = matrix.TimeVector(vector);
        Print(result1, "CSR 1 x Vector 1");
        Print(result2, "Matrix 1 x Vector 1");
        System.out.println();
        if (result1.Equal(result2)){
            System.out.println("%CSR x Vector Test Passed!!!");
        } else {
            System.out.println("%CSR x Vector Test failed!!!");
        }

        //CSR^T x Vector
        Print(csrMatrix.GetTranspose(), "CSR 1^T");
        Print(matrix.GetTranspose(), "Matrix 1^T");

        Vector result3 = csrMatrix.GetTranspose().TimeVector(vector);
        Vector result4 = matrix.GetTranspose().TimeVector(vector);
        Print(result3, "CSR^T 1 x Vector 1");
        Print(result4, "Matrix^T 1 x Vector 1");
        System.out.println();
        if (result3.Equal(result4)){
            System.out.println("%CSR^T x Vector Test Passed!!!");
        } else {
            System.out.println("%CSR^T x Vector Test failed!!!");
        }

        System.out.println("%~~~~~~~~~~~~~~~~~~CSR and Transpose Time Vector~~~~~~~~~~~~~~~~~~%");
        System.out.println();
    }
    //Task 1
    private static void Task1(){
        System.out.println("%``````````````Task 1``````````````%");

        String testInput = "1 0 0\n0 1 0\n0 0 1";
        CSRMatrix matrix1 = new CSRMatrix(testInput);
        CSRMatrix transpose1 = matrix1.GetTranspose();
        Print(matrix1, "CSR Matrix 1");
        Print(transpose1, "CSR Transpose 1");

        System.out.println("%This matrix x matrix^T should be symmetric...");
        if (CSRMatrix.IsSymmetric(matrix1.TimeMatrix(transpose1))){
            System.out.println("%Matrix x Matrix^T is Symmetric! Test Passed!!!");
        } else {
            System.out.println("%Matrix x Matrix^T is not Symmetric! Test failed!!!");
        }

        String testInput2 = "55 16 97\n16 5213 24\n97 24 210\n0 91 2";
        CSRMatrix matrix2 = new CSRMatrix(testInput2);
        CSRMatrix transpose2 = matrix2.GetTranspose();
        Print(matrix2, "CSR Matrix 2");
        Print(transpose2, "CSR Transpose 2");


        System.out.println("%This matrix x matrix^T should NOT be symmetric...");
        if (!CSRMatrix.IsSymmetric(matrix2.TimeMatrix(transpose2))){
            System.out.println("%Matrix x Matrix^T is not Symmetric! Test Passed!!!");
        } else {
            System.out.println("%Matrix x Matrix^T is Symmetric! Test Failed!!!");
        }
        System.out.println("%``````````````Task 1``````````````%");
        System.out.println();
    }
    private static void Task2() throws FileNotFoundException {

        System.setOut(stdout);
        System.out.println("%``````````````Task 2``````````````%");

        String csrFromFile1 = ReadCSRFromFile(inputPath + "e05r0000.mtx");
        String rhsFromFile1 = ReadVectorFromFile(inputPath + "e05r0000_rhs1.mtx");
        CSRMatrix fileCSR1 = new CSRMatrix(csrFromFile1);
        Vector fileVector1 = new Vector(rhsFromFile1);
//        CSRMatrix fileCSR1 = new CSRMatrix(iterationInput);
//        Vector fileVector1 = new Vector(bIterationInput);

        LinkedList<SolutionOutput> solutionList = new LinkedList<>();

        for(int maxStep = 5; maxStep <= 150; maxStep += 5){

            CSRMatrix.STEP_LIMIT = maxStep;

            long runTime = -System.currentTimeMillis();     //Start the time counter
            Vector solution = fileCSR1.IterationMethod(fileVector1);
            if (solution == null){
                System.out.println("%No solution!");
                break;
            }
            runTime += System.currentTimeMillis();          //End the time counter
            //Find out the error
            BigDecimal error = fileVector1.Add(fileCSR1.TimeVector(solution).Scale(BigDecimal.valueOf(-1))).GetLength();

            //Temporarily store the solution in a list
            solutionList.add(new SolutionOutput(error, runTime, maxStep, CSRMatrix.residualLengthList));
            System.out.println("~!Maxstep=" + maxStep + " completed under " + runTime + " milliseconds with error=" + error + "!");
            System.out.print("\t");
            for (int indx = 0; indx < solution.GetSize(); indx++) {
                //Print the residual
                System.out.print(" " + solution.GetEntry(indx));
            }
            System.out.println();
        }

        System.out.println("%``````````````Task 2``````````````%");
        System.out.println();

        //Set up new output stream
        fileOut = new PrintStream(outputPath + solutionOutName + jsonExtension);
        System.setOut(fileOut);
        //Report in a json file
        //Convert to json
        String json = gson.toJson(solutionList);
        System.out.println(json);
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
        System.out.println("%\n%~~Arr: ");
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
        assert tempLine != null;
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
        assert tempLine != null;
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
