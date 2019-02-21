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

        int testArr[] = {1, 2, 3};
        Print(testArr);
        for(int indx = 0; indx < NumberOfPermutation(testArr.length, testArr.length) - 1; indx++){
            System.out.print("Trial " + indx + " ");
            testArr = NextPermutation(testArr);
            Print(testArr);
        }
        //myCSRMatrix.Add(myNormalMatrix);
        //myCSRMatrix.Print();
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
