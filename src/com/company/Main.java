package com.company;

import java.util.LinkedList;

//FOR PRINTING:
// ~~ Means for general purpose
// ~! For debugging purpose
public class Main {
    static final String WHITESPACE = "\\s+";
    //static String testInput = "1 2 0 3\n0 5 6 4\n7 0 9 6\n0 0 0\n8 7 6 0";
    static String testInput2 = "1 0 3\n4 0 6\n0 1 2";
    static String testInput3 = "1 0 2 3\n0 3 5 0\n2 5 4 0";

    public static <T> T GetValueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static void main(String[] args) {
        //CSRMatrix Matrix initialization test
        CSRMatrix myCSRMatrix = new CSRMatrix(testInput2);
        Print(myCSRMatrix, "CSR Matrix 1 with test Input 2");

        //CSRMatrix Operation test
        CSRMatrix myCSRMatrix2 = myCSRMatrix.GetTranspose();
        
        myCSRMatrix2.PrintData();
        myCSRMatrix2.Print();
        myCSRMatrix.TimeVector(new Vector("1 2 3")).Print();
        CSRMatrix myCSRMatrix3 = new CSRMatrix(testInput3);
        if (CSRMatrix.IsSymmetric(myCSRMatrix3)){
            System.out.println("Is symmetric!");
        } else {
            System.out.println("Is NOT symmetric!");
        }

        Print(myCSRMatrix, "CSR Matrix 1");
        Print(myCSRMatrix3, "CSR Matrix 3");
        Print(myCSRMatrix.TimeMatrix(myCSRMatrix3), "CSR Matrix 1 x CSR Matrix 3");
        Matrix myMatrix3 = new Matrix(testInput3);
        Print(myCSRMatrix.TimeMatrix(myMatrix3), "CSR Matrix 1 x Matrix 3");
        Matrix myMatrix2 = new Matrix(testInput2);
        Print(myMatrix2.TimeRightMatrix(myMatrix3), "Matrix 2 x Matrix 3");

        Print(myCSRMatrix2, "CSR Matrix with Transpose of 1");
        PrintData(myCSRMatrix2, "CSR Matrix 2");
        Print(myCSRMatrix.TimeVector(new Vector("1 2 3")), "Vector result of CSR Matrix 1 times a random vector: ");
        IsCSRSymmetric(new CSRMatrix(testInput3), "Test matrix with input 3");
        IsCSRSymmetric(myCSRMatrix, "CSR Matrix 1");
        /*
        //Matrix initialization test
        Matrix myMatrix = new Matrix(3, 3, 1);
        myMatrix.Print();

        //Vector initialization test
        Vector myVector = new Vector("1 2");
        Vector myVector2 = new Vector("6 9");
        Vector myVector3 = new Vector("3 5 6");
        myVector.Print();
        myVector2.Print();
        myVector3.Print();

        //Vector operation test
        Matrix myMatrix2 = myVector.TimeRightMatrix(myVector3);
        myMatrix2.Print();
        myMatrix2 = GetValueOrDefault(myVector.TimeLeftMatrix(myVector3), myMatrix2);
        myMatrix2.Print();
        System.out.println("~~Inner product of 1 and 2: " + myVector.InnerProduct(myVector2));
        myVector = GetValueOrDefault(myVector.Add(myVector2), myVector);
        myVector.Print();
        myVector = GetValueOrDefault(myVector.Add(5), myVector);
        myVector.Print();
*/

        //LU Factorization
        myMatrix3 = new Matrix("1 2 3 4\n5 6 7 8\n9 10 11 12\n13 14 15 16");
        Print(myMatrix3, "My Matrix 3 with random test input");
        LinkedList<Matrix> luList = myMatrix3.LUFactorization();
        if (luList.size() > 0){
            System.out.print("~~L: ");
            luList.get(0).Print();
            System.out.print("~~U: ");
            luList.get(1).Print();
        }

        /*
        int testArr[] = {1, 2, 3};
        Print(testArr);
        for(int indx = 0; indx < NumberOfPermutation(testArr.length, testArr.length) - 1; indx++){
            System.out.print("Trial " + indx + " ");
            testArr = NextPermutation(testArr);
            Print(testArr);
        }
        */
        //myCSRMatrix.Add(myNormalMatrix);
        //myCSRMatrix.Print();
    }

    static void IsCSRSymmetric(CSRMatrix parm, String name){
        if (CSRMatrix.IsSymmetric(parm)){
            System.out.println("~~" + name + " is symmetric!");
        } else {
            System.out.println("~~" + name + " is NOT symmetric!");
        }
    }
    static void Print(CSRMatrix parm, String name){
        System.out.println("~~" + name);
        parm.Print();
    }
    static void PrintData(CSRMatrix parm, String name){
        System.out.println("~!" + name + " data:");
        parm.PrintData();
    }
    static void Print(Vector parm, String name){
        System.out.println("~~" + name);
        parm.Print();
    }
    static void Print(Matrix parm, String name){
        System.out.println("~~" + name);
        parm.Print();
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
        int result[] = new int[currArr.length];
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
        System.out.println("~~Arr: ");
        for(int entry: arr){
            System.out.print(entry + " ");
        }
        System.out.println();
    }
}
