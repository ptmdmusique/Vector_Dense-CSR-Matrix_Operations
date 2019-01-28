package com.company;

public class Matrix {
    private static final String WHITESPACE = "\\s+";

    private class Data{
        int data;
        int col;

        Data(int data, int col){
            Change(data, col);
        }
        void Change(int data, int col){
            this.data = data;
            this.col = col;
        }
    }
    private Data[] data;            //Data array
    private int row[];              //Start column of each row
    private int rowSize = -1;
    private int colSize = -1;
    private int nnz;                //Total number of nonzero

    Matrix(int rowSize, int colSize, String input){
        TakeInput(rowSize, colSize, input);
    }

    public void TakeInput(int rowSize, int colSize, String input){
        if (this.rowSize != rowSize){
            this.rowSize = rowSize;
            row = new int[rowSize];
        }
        this.colSize = colSize;

        //Split the string into multiple regex
        String[] split = input.split("\n");
        for(int indx = 0; indx < rowSize; indx++){
            String curString = split[indx];
            //Find the number of non zero
            for(String number: split[indx].split(WHITESPACE)){
                if (!number.equals("0")){
                    nnz++;
                }
            }
        }

        data = new Data[nnz];
        int curRow = 0;
        int totalCol = 0;
        while (curRow < rowSize){
            int curCol = 0;
            row[curRow] = totalCol;
            for(String number : split[curRow++].split(WHITESPACE)){
                if (!number.equals("0")){
                    data[totalCol++] = new Data(Integer.valueOf(number), curCol);
                }
                curCol++;
            }
        }
    }

    public int nnz(){
        //Make sure our nnz is correct
        return data.length == nnz ? nnz : (nnz = data.length);
    }

    public void Print(){
        for(int curRow = 0; curRow < row.length; curRow++){
            //Start and end index of the data array for the current row
            int startIndx = row[curRow];
            int endIndx = curRow == row.length - 1 ? data.length - 1 : (row[curRow + 1] - 1);

            if (startIndx > endIndx){
                //Empty row, all 0
                for(int indx = 0; indx < colSize; indx++){
                    System.out.print("0 ");
                }
            } else {
                int curIndx = startIndx;
                for(int curCol = 0; curCol < colSize; curCol++){
                    if (curIndx < data.length && data[curIndx].col == curCol){
                        //If the current column matches the column of the non-zero entries we are looking at
                        //  then move on with the index and print out the corresponding data
                        System.out.print(data[curIndx].data + " ");
                        if(curIndx < endIndx){
                            curIndx++;
                        }
                    } else {
                        //If not then just print 0
                        System.out.print("0 ");
                    }
                }
            }
            System.out.println();
        }
    }
}
