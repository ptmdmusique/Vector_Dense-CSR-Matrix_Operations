package com.company;

public class CSRMatrix {
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

    CSRMatrix(int rowSize, int colSize, String input){
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
    /*
    public Boolean Add(Matrix parm) {
        if (parm.GetColSize() != this.colSize || parm.GetRowSize() != this.rowSize){
            return false;
        }

        int nnz = 0;                //Total number of non zero

        //First calculate the nnz
        for(int curRow = 0; curRow < row.length; curRow++){
            int rowStartIndx = row[curRow]; //Start index in the data array
            int rowEndIndx = curRow == row.length - 1 ? data.length - 1 : row[curRow + 1] - 1;  //End index in the data array
            //If we are at the end then use the last index

            if (rowEndIndx < rowStartIndx){
                //Empty row -- All 0 row
                for()
            }
        }

        if (parm.GetColSize() == this.colSize && parm.GetRowSize() == this.rowSize) {
            int nnz = 0;            //Total number of non zero

            int colIndx;
            int curRow;
            int curIndx;
            int rowEnd;

            for(curRow = 0; curRow < this.rowSize; ++curRow) {
                colIndx = this.row[curRow];
                curRow = curRow == this.row.length - 1 ? this.data.length - 1 : this.row[curRow + 1] - 1;
                if (colIndx > curRow) {
                    for(curIndx = 0; curIndx < this.colSize; ++curIndx) {
                        if (parm.Get(curRow, curIndx) != 0) {
                            ++nnz;
                        }
                    }
                } else {
                    curIndx = colIndx;

                    for(rowEnd = 0; rowEnd < this.colSize; ++rowEnd) {
                        if (curIndx <= curRow && curIndx < this.data.length && this.data[curIndx].col == rowEnd) {
                            if (this.data[curIndx].data + parm.Get(curRow, rowEnd) != 0) {
                                ++nnz;
                            }

                            ++curIndx;
                        } else if (parm.Get(curRow, rowEnd) != 0) {
                            ++nnz;
                        }
                    }
                }
            }

            CSRMatrix.Data[] temp = new CSRMatrix.Data[nnz];
            colIndx = 0;

            for(curRow = 0; curRow < this.rowSize; ++curRow) {
                curIndx = this.row[curRow];
                rowEnd = curRow == this.row.length - 1 ? this.data.length - 1 : this.row[curRow + 1] - 1;
                this.row[curRow] = colIndx;
                if (curIndx > rowEnd) {
                    for(curIndx = 0; curIndx < this.colSize; ++curIndx) {
                        if (parm.Get(curRow, curIndx) != 0) {
                            temp[colIndx++] = new CSRMatrix.Data(parm.Get(curRow, curIndx), colIndx % this.colSize);
                        }
                    }
                } else {
                    curIndx = curIndx;

                    for(int curCol = 0; curCol < this.colSize; ++curCol) {
                        if (curIndx <= rowEnd && curIndx < this.data.length && this.data[curIndx].col == curCol) {
                            if (this.data[curIndx].data + parm.Get(curRow, curCol) != 0) {
                                temp[colIndx] = new CSRMatrix.Data(parm.Get(curRow, curCol) + this.data[curIndx].data, colIndx % this.colSize);
                                ++colIndx;
                            }

                            ++curIndx;
                        } else if (parm.Get(curRow, curCol) != 0) {
                            temp[colIndx] = new CSRMatrix.Data(parm.Get(curRow, curCol), colIndx % this.colSize);
                            ++colIndx;
                        }
                    }
                }
            }

            this.data = temp;
            return true;
        } else {

        }
    }
*/
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
