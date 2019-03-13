package com.company;

import java.math.BigDecimal;
import java.util.LinkedList;

class CSRMatrix {
    private static final String WHITESPACE = "\\s+";

    static int STEP_LIMIT = 100;
    static double TOLERANCE = 10e-6;
    static int MAX_SEARCH_DIR = 150;
    static LinkedList<BigDecimal> residualLengthList;

    private class Data{
        BigDecimal data;
        int col;

        Data(BigDecimal data, int col){
            Change(data, col);
        }
        void Change(BigDecimal data, int col){
            this.data = data;
            this.col = col;
        }
    }
    private Data[] data;            //Data array
    private int[] row;              //Start column of each row
    private int colSize = 0;

    private int GetNNZ(){
        return data == null ? 0 : data.length;
    }
    private int GetRowSize(){
        return row == null ? 0 : row.length;
    }
    private int GetColSize() { return colSize;}
    private BigDecimal GetEntry(int row, int col){
        int rowStart = this.row[row];
        int rowEnd = row == this.row.length - 1 ? data.length - 1 : (this.row[row + 1] - 1);

        for (int i = rowStart; i <= rowEnd; i++) {
            if (data[i].col == col){
                return data[i].data;
            } else if (data[i].col > col){
                return BigDecimal.ZERO;
            }
        }

        return BigDecimal.ZERO;
    }
    private int[] GetRow(){return row;}
    private Data[] GetData(){return data;}

    private void SetRow(int indx, int newRow){row[indx] = newRow;}
    void SetEntry(int row, int col, BigDecimal value){
        if (!GetEntry(row, col).equals(BigDecimal.ZERO)){
            if (!value.equals(BigDecimal.ZERO)){
                //We are fine...
                for (int i = this.row[row];
                     i <= (row == this.row.length - 1 ? data.length - 1 : (this.row[row + 1] - 1));
                     i++) {
                    if (data[i].col == col){
                        data[i].data = value;
                        break;
                    }
                }
            } else{
                //Oh noooooo....
                //TODO: SET 0 FOR A NON ZERO ENTRY IN MATIX!!!
            }
        } else {
            if (!value.equals(BigDecimal.ZERO)){
                //Oh no...............
                //TODO: SET ENTRY FOR A ZERO ENTRY IN MATRIX!!!
            }
        }
    }
    private void SetData(int indx, BigDecimal value, int col){
        if (data[indx] == null){
            data[indx] = new Data(value, col);
        } else {
            data[indx].Change(value, col);
        }
    }

    CSRMatrix(String input){
        TakeInput(input);
    }
    private CSRMatrix(int rowSize, int colSize, int nnz){
        row = new int[rowSize];
        this.colSize = colSize;
        data = new Data[nnz];
    }
    private CSRMatrix(){};

    private void TakeInput(String input){
        boolean reallocate = false;     //Are we going to allocate new array?

        //Split the string into multiple regex
        String[] rows = input.split("\n");

        int rowSize = rows.length;

        if (rowSize <= 0){
            return;
        }

        colSize = rows[0].split(Main.WHITESPACE).length;
        if (row == null || rowSize != row.length){
            //Only create when take in different size
            row = new int[rowSize];
        }

        int nnz = 0;
        for(String row : rows){
            //Find the number of non zero
            for(String number: row.split(WHITESPACE)){
                if (number.equals("") || number.equals(WHITESPACE)){
                    continue;
                }
                number = number.replaceAll("\\s+","");
                if (!number.equals("0")){
                    nnz++;
                }
            }
        }

        if (data == null || nnz != data.length){
            data = new Data[nnz];
            reallocate = true;
        }

        int curRow = 0;
        int totalCol = 0;
        if (reallocate){
            while (curRow < rowSize){
                int curCol = 0;
                row[curRow] = totalCol;
                for(String number : rows[curRow++].split(WHITESPACE)){
                    if (number.equals("") || number.equals(WHITESPACE)){
                        continue;
                    }
                    number = number.replaceAll("\\s+","");
                    if (!number.equals("0")){
                        data[totalCol++] = new Data(BigDecimal.valueOf(Double.parseDouble(number)), curCol);
                    }
                    curCol++;
                }
            }
        } else {
            while (curRow < rowSize){
                int curCol = 0;
                row[curRow] = totalCol;
                for(String number : rows[curRow++].split(WHITESPACE)){
                    if (!number.equals("0")){
                        data[totalCol++].Change(BigDecimal.valueOf(Double.parseDouble(number)), curCol);
                    }
                    curCol++;
                }
            }
        }
    }
    void Print(){
        System.out.println("%Sparse matrix: ");
        for(int curRow = 0; curRow < row.length; curRow++){
            //Start and end index of the data array for the current row
            int rowStartIndx = row[curRow];
            //If we are at the last row, then the end index should be the length - 1
            int rowEndIndx = curRow == row.length - 1 ? data.length - 1 : (row[curRow + 1] - 1);

            //Magically (!!!), if the startIndex of the current row
            // is the same as the start index of the next row (which is the endIndex + 1), then
            // the endIndex - 1 < startIndex
            //This also indicates that the row is empty
            if (rowStartIndx > rowEndIndx){
                //Empty row, all 0
                for(int indx = 0; indx < colSize; indx++){
                    System.out.printf("%" + Main.MAX_SLOT + "." + Main.PRECISION + "f ", 0.0);
                }
            } else {
                int curIndx = rowStartIndx;
                for(int curCol = 0; curCol < colSize; curCol++){
                    if (curIndx < data.length && data[curIndx].col == curCol){
                        //If the current column matches the column of the non-zero entries we are looking at
                        //  then move on with the index and print out the corresponding data
                        System.out.printf("%" + Main.MAX_SLOT + "." + Main.PRECISION + "f ", data[curIndx].data);
                        if(curIndx < rowEndIndx){
                            curIndx++;
                        }
                    } else {
                        //If not then just print 0
                        System.out.printf("%" + Main.MAX_SLOT + "." + Main.PRECISION + "f ", 0.0);
                    }
                }
            }
            System.out.println();
        }
    }
    void PrintData(){
        if (data.length <= 0){
            System.out.println("%Matrix is empty!");
            return;
        }

        System.out.printf("%%%" + (Main.ENTRY_MAX_SLOT - 1) + "s " + "%" + Main.ENTRY_MAX_SLOT + "s " + "%" + Main.DATA_MAX_SLOT + "s", "Col", "Row", "Data");
        System.out.println();
        for(int curRow = 0; curRow < row.length; curRow++){
            for(int indx = row[curRow]; indx <= (curRow == row.length - 1 ? data.length - 1 : row[curRow + 1] - 1); indx++){
                System.out.printf("%" + Main.ENTRY_MAX_SLOT + "d %" + Main.ENTRY_MAX_SLOT + "d %" + Main.DATA_MAX_SLOT + "s"
                        , data[indx].col, curRow, data[indx].data);
                System.out.println();
            }
        }
    }

    Matrix GetMatrixForm(){
        //Return the CSR matrix in a normal form
        Matrix result = new Matrix(row.length, colSize);
        for(int curRow = 0; curRow < row.length; curRow++){
            //Start and end index of the data array for the current row
            int rowStartIndx = row[curRow];
            //If we are at the last row, then the end index should be the length - 1
            int rowEndIndx = curRow == row.length - 1 ? data.length - 1 : (row[curRow + 1] - 1);

            //Magically (!!!), if the startIndex of the current row
            // is the same as the start index of the next row (which is the endIndex + 1), then
            // the endIndex - 1 < startIndex
            //This also indicates that the row is empty
            if (rowStartIndx <= rowEndIndx){
                int curIndx = rowStartIndx;
                for(; curIndx <= rowEndIndx; curIndx++){
                    result.SetEntry(curRow, data[curIndx].col, data[curIndx].data);
                }
            }
        }
        return result;
    }
    CSRMatrix GetTranspose(){
        CSRMatrix result = new CSRMatrix(colSize, row.length, data.length);

        //Find the number of element in each rows
        int[] rowCurIndx = new int[row.length];

        for(int curRow = 0; curRow < rowCurIndx.length - 1; curRow++){
            //Either the row is empty or has something in it
            rowCurIndx[curRow] = row[curRow] == row[curRow + 1] ?  -1 : row[curRow];
        }
        rowCurIndx[row.length - 1] = row[row.length - 1] == data.length ? -1 : row[row.length - 1];

        int filledElement = 0;
        int parmCurRow = 0;

        //Repeat until the transpose has the same nnz
        while(filledElement < GetNNZ()){
            //Mark the beginning of this trip as the parm's current row's start
            result.row[parmCurRow] = filledElement;

            //Iterate through each row, if the element has the same column as the row we are checking then add it
            /*Ex:
                Row:    0 0 0 0 | 1 1 1 1 | 2 2 2 2
                Data:   1 2 3 4 | 4 2 1 3 | 7 1 2 3
                Col:    1 2 3 5 | 4 2 1 3 | 4 5 6 7

                Since we need to fill out the parm.data with the data that have the column in increasing order
                   (meaning parm.data[i].col > parm.data[i-1].col if their row are the same),
                   if we iterate from row (which will become parm.data.col) 0 to row.length - 1 of this.data,
                   we will be able to satisfy the condition of the increasing order
             */
            for(int curRow = 0; curRow < row.length; curRow++){
                if (rowCurIndx[curRow] < data.length && rowCurIndx[curRow] > -1  && data[rowCurIndx[curRow]].col == parmCurRow){
                    result.data[filledElement++] = new Data(data[rowCurIndx[curRow]].data, curRow);
                    if ((curRow == row.length - 1 && rowCurIndx[curRow] == row.length - 1) || (curRow < row.length - 1 && rowCurIndx[curRow] >= row[curRow + 1] - 1)){
                        //Only do this if we haven't reached the boundary
                        rowCurIndx[curRow] = -1;
                    } else {
                        //Else just disable the check
                        rowCurIndx[curRow]++;
                    }
                }
            }
            parmCurRow++;
        }
        return result;
    }
    Vector TimeVector(Vector parm){
        Vector result = new Vector(row.length, BigDecimal.ZERO);

        for(int curRow = 0; curRow < row.length; curRow++){
            BigDecimal temp = BigDecimal.ZERO;
            for(int indx = row[curRow]; indx <= (curRow == row.length - 1 ? data.length - 1 : (row[curRow + 1] - 1)); indx++){
                temp = temp.add(data[indx].data.multiply(parm.GetEntry(data[indx].col), Main.mathContext), Main.mathContext);
            }
            result.SetEntry(curRow, temp);
        }
        return result;
    }
    CSRMatrix TimeMatrix(CSRMatrix matrix){
        if (matrix.GetRowSize() != colSize){
            System.out.println("Different size!");
            return null;
        }
        int[] rowCurCol = new int[colSize];
        int[] parmRow = matrix.GetRow();
        Data[] parmData = matrix.GetData();
        int[] resultRow = new int[row.length];  //This will be used as the row arr of the result matrix

        //rowCurCol is used to keep track of the current col that we are reading on each row
        //  so that we won't have to search for the correct element each time we move on to the next row every single time
        //Additional space complexity: O(rowSize) which is very insignificant comparing to the total number of elements.
        for(int curRow = 0; curRow < parmRow.length - 1; curRow++){
            //Either the row is empty (-1) or has something in it (which we will store the start index)
            rowCurCol[curRow] = parmRow[curRow] == parmRow[curRow + 1] ?  -1 : parmRow[curRow];
        }
        //Is last row empty?
        rowCurCol[parmRow.length - 1] = parmRow[parmRow.length - 1] == parmData.length ? -1 : parmRow[parmRow.length - 1];

        for(int curRow = 0; curRow < row.length; curRow++){
            resultRow[curRow] = 0;
        }

        int totalSize = 0;
        LinkedList<Data>[] tempResult = new LinkedList[row.length];     //Temporarily store the data here
        for(int indx = 0; indx < row.length; indx++){
            tempResult[indx] = new LinkedList<>();
        }
        //I will use naive multiplication here
        //  rowA * colB until the very end

        //Since accessing row-wise is much easier, we will put it in the inner loop
        for(int parmCol = 0; parmCol < matrix.GetColSize(); parmCol++){
            for(int curRow = 0; curRow < row.length; curRow++){
                BigDecimal temp = BigDecimal.ZERO;
                //k will go from the index of the first element in the current row to the end index
                //  then data[k].col will be used as the row of parmData and
                // we will use rowCurCol[data[k].col] to look aside the current element in parm
                //Moreover, because we use the outer loop to iterate the parmCol,
                // if parmCol != parmData[rowCurCol[data[k].col]].col
                //  then it means parm[data[k].col][parmCol] == 0. Thus, we don't need to count it in the actual calculation
                for(int k = row[curRow];
                    k <= (curRow == row.length - 1 ? data.length - 1 : row[curRow + 1] - 1);
                    k++){
                    //Only multiply non zero!
                    if (rowCurCol[data[k].col] > -1 && parmData[rowCurCol[data[k].col]].col == parmCol){
                        //Since we iterate the parmCol in the outer loop one by one,
                        //  if the col of the rowCurCol doesn't match the outer loop,
                        //  it means the parm[parmData[rowCurCol[data[k].col]].col][parmCol] == 0
                        //Thus, we don't need to include it in our calculation
                        temp = temp.add(data[k].data.multiply(parmData[rowCurCol[data[k].col]].data, Main.mathContext), Main.mathContext);
                    }
                }
                if (!temp.equals(BigDecimal.ZERO)){
                    //If the calculation is not 0, then we can simply add it to the list
                    tempResult[curRow].add(new Data(temp, parmCol));
                    totalSize++;
                }
            }

            //Increase the curCol
            for(int indx = 0; indx < rowCurCol.length; indx++){
                if (rowCurCol[indx] > -1 && parmCol == parmData[rowCurCol[indx]].col){
                    if ((indx >= parmRow.length - 1 && rowCurCol[indx] + 1 == parmData.length)
                            || (indx < parmRow.length - 1 && rowCurCol[indx] + 1 == parmRow[indx + 1])){
                        rowCurCol[indx] = -1;
                    } else {
                        rowCurCol[indx]++;
                    }
                }
            }
        }

        //Create new matrix
        //CSRMatrix result = new CSRMatrix(row.length, matrix.GetColSize(),nnz);
        CSRMatrix result = new CSRMatrix();
        result.row = resultRow;
        result.colSize = matrix.GetColSize();
        result.data = new Data[totalSize];

        //Copy the result from the temp linked list to the new matrix
            //Since our array is a an array of pointer to data, even though the data will be scattered in the memory
            // we won't use any additional memory. Thus, this will make sure that the total data used is O(nnz)
            //However, this still ensure that the access is O(1)
        int curIndx = 0;
        for(int indx = 0; indx < tempResult.length; indx++){
            resultRow[indx] = curIndx;

            int rowSize = tempResult[indx].size();
            for(int indx2 = 0; indx2 < rowSize; indx2++){
                result.data[curIndx++] = tempResult[indx].removeFirst();
            }
        }
        return result;
    }
    CSRMatrix TimeMatrix(Matrix matrix){
        if (colSize != matrix.GetRowSize()){
            return null;
        }

        Vector[] parmData = matrix.GetMatrix();

        int nnz = 0;
        for(int curRow = 0; curRow < row.length; curRow++){
            for(int curCol = 0; curCol < matrix.GetColSize(); curCol++){
                BigDecimal temp = BigDecimal.ZERO;
                for(int indx = row[curRow]; indx <= (curRow == row.length - 1 ? data.length - 1 : (row[curRow + 1] - 1)); indx++){
                    temp = temp.add(data[indx].data.multiply(parmData[data[indx].col].GetEntry(curCol)), Main.mathContext);
                }
                if (!temp.equals(BigDecimal.ZERO)){
                    nnz++;
                }
            }
        }

        CSRMatrix result = new CSRMatrix(row.length, matrix.GetColSize(), nnz);

        int curDataIndx = 0;
        for(int curRow = 0; curRow < row.length; curRow++){
            result.row[curRow] = curDataIndx;
            for(int curCol = 0; curCol < matrix.GetColSize(); curCol++){
                BigDecimal temp = BigDecimal.ZERO;
                for(int indx = row[curRow]; indx <= (curRow == row.length - 1 ? data.length - 1 : (row[curRow + 1] - 1)); indx++){
                    temp = temp.add(data[indx].data.multiply(parmData[data[indx].col].GetEntry(curCol)), Main.mathContext);
                }
                if (!temp.equals(BigDecimal.ZERO)){
                    result.data[curDataIndx++] = new Data(temp, curCol);
                }
            }
        }

        return result;
    }

    Vector IterationMethod(Vector rightSide){
        LinkedList<Vector> searchDirList = new LinkedList<>();
        int searchDirIndx = 0;                                                      //Is it time to restart the search dir yet?
        //Vector solution = new Vector(rightSide);    //Create an initial vector
        Vector solution = new Vector(rightSide.GetSize(), BigDecimal.ZERO);
        Vector residual = rightSide.Add(this.TimeVector(solution.Scale(BigDecimal.valueOf(-1))));       //Calculate the first residue

        BigDecimal norm = residual.GetLength();                                         //Calculate the norm

        //For stat only
        residualLengthList = new LinkedList<>();
        residualLengthList.add(norm);

        BigDecimal initialNorm = norm;
        Vector curSearchDir;
        searchDirList.add(curSearchDir = new Vector(residual.Normalize()));                        //Calculate the initial search direction
        Matrix pMatrix = new Matrix(curSearchDir, 2);
        Matrix bMatrix = new Matrix(this.TimeVector(curSearchDir), 2);

        for(int step = 1; step < STEP_LIMIT && norm.compareTo(initialNorm.multiply(BigDecimal.valueOf(TOLERANCE), Main.mathContext)) > 0; step++){
            //|r - bMatrix * alpha| -> min
            //<=> least square(r = bMatrix * alpha)
            //<=> least square(r = QR * alpha)
            //<=> least square(Q^T r = R alpha)
            //<=> beta = R * alpha => least square!
            LinkedList<Matrix> qrFactor = bMatrix.QRFactorization();
            if (qrFactor.size() < 2){
                System.out.println("Can't do QR Factorization!!!");
                return null;
            }

            //Calculate alpha
            Vector alpha = qrFactor.get(1).BackwardSubstitution(qrFactor.get(0).GetTranspose().Multiply(residual));
            //Calculate next iterate
            solution = solution.Add(pMatrix.Multiply(alpha));
            //Calculate next residual
            residual = residual.Add(bMatrix.Multiply(alpha).Scale(BigDecimal.valueOf(-1)));
            //Calculate residual norm
            BigDecimal tempNorm = residual.GetLength();
            if (tempNorm.equals(norm)){
                //No solution
                return null;
            }
            norm = tempNorm;

            //For stat only!
            residualLengthList.add(norm);

            //Calculate next search dir less than max, or else move back to the first one
            if (searchDirIndx  < MAX_SEARCH_DIR){
                searchDirList.add((curSearchDir = new Vector(residual)));
                searchDirIndx++;

                //r - Sigma (k = 1->searchDirIndx)((r,p(k)) * p(k))
                for(int k = 0; k < searchDirIndx; k++){
                    Vector preSearchDir = searchDirList.get(k);
                    //+ (- (r,p(k)) * p(k))
                    curSearchDir = curSearchDir.Add(preSearchDir.Scale(preSearchDir.InnerProduct(residual)).Scale(BigDecimal.valueOf(-1)));
                }
                curSearchDir = curSearchDir.Normalize();

                //Augment B and Q
                pMatrix = pMatrix.AugmentVectorAtEnd(curSearchDir);
                bMatrix = bMatrix.AugmentVectorAtEnd(this.TimeVector(curSearchDir));
            } else {
                //Restart
                searchDirIndx = 0;
                curSearchDir = searchDirList.get(searchDirIndx);
                curSearchDir.Copy(residual.Normalize());

                //Create new B and P vector from the first search dir again
                pMatrix = new Matrix(curSearchDir, 2);
                bMatrix = new Matrix(this.TimeVector(curSearchDir), 2);
            }
        }

        return solution;
    }

    Boolean Equal(Matrix parm){
        if (parm.GetColSize() != colSize || parm.GetRowSize() != GetRowSize()){
            return false;
        }

        //Check one by one
        int curIndx = 0;
        for(int curRow = 0; curRow < GetRowSize(); curRow++){
            for(int curCol = 0; curCol < colSize; curCol++){
                if (curCol == data[curIndx].col){
                    if (!parm.GetEntry(curRow, curCol).equals(data[curIndx].data)){
                        return false;
                    } else{
                        curIndx++;
                    }
                } else if (!parm.GetEntry(curRow, curCol).equals(BigDecimal.ZERO)){
                    return false;
                }
            }
        }

        return true;
    }
    Boolean Equal(CSRMatrix parm){
        if (parm.GetColSize() != colSize || parm.GetRowSize() != GetRowSize()){
            return false;
        }

        for(int indx = 0; indx < GetRowSize(); indx++){
            if (parm.row[indx] != row[indx]){
                return false;
            }
        }

        for(int indx = 0; indx < colSize; indx++){
            if (parm.data[indx].col != data[indx].col || !parm.data[indx].data.equals(data[indx].data)){
                return false;
            }
        }
        return true;
    }
    static boolean IsSymmetric(CSRMatrix parm){
        if (parm.GetRowSize() != parm.GetColSize()){
            //Not a square matrix!
            return false;
        }

        int[] row = parm.GetRow();
        Data[] data = parm.GetData();

        for(int curRow = 0; curRow < parm.GetRowSize(); curRow++){
            //We only need to check until the middle entries
            for(int indx = row[curRow];
                indx <= (curRow == row.length - 1 ? data.length - 1 : (row[curRow + 1] - 1)) && data[indx].col < curRow;
                indx++){
                //Find the correct entry
                for(int search = row[data[indx].col];
                    search <= (data[indx].col == row.length - 1 ? data.length - 1 : (row[data[indx].col + 1] - 1));
                    search++
                ){
                    if (data[search].col > curRow || (data[search].col == curRow && !data[indx].data.equals(data[search].data))){
                        //Different data
                        return false;
                    }
                }

            }
        }
        return true;
    }
}
