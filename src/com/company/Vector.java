package com.company;

import java.math.BigDecimal;

public class Vector {
    //Treat this as neutral matrix, no row or col major
    private BigDecimal data[];

    BigDecimal[] GetData(){
        return data;
    }
    BigDecimal GetEntry(int indx){
        return data[indx];
    }
    int GetSize(){
        return data.length;
    }
    void SetEntry(int indx, BigDecimal value){
        data[indx] = value;
    }
    BigDecimal GetLength(){
        BigDecimal result = BigDecimal.ZERO;
        for(int indx = 0; indx < data.length; indx++){
            result = result.add(data[indx].multiply(data[indx]));
        }
        return sqrt(result, );
    }
    public static BigDecimal sqrt(BigDecimal A, final int SCALE) {
        BigDecimal x0 = new BigDecimal("0");
        BigDecimal x1 = new BigDecimal(Math.sqrt(A.doubleValue()));
        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = A.divide(x0, SCALE, BigDecimal.ROUND_HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(BigDecimal.valueOf(2), SCALE, BigDecimal.ROUND_HALF_UP);

        }
        return x1;
    }
    Vector(BigDecimal data[]){
        this.data = new BigDecimal[data.length];

        System.arraycopy(data, 0, this.data, 0, data.length);
    }
    Vector(String data){
        String splitData[] = data.split(Main.WHITESPACE);

        this.data = new BigDecimal[splitData.length];
        for(int indx = 0; indx < splitData.length; indx++){
            this.data[indx] = Integer.parseInt(splitData[indx]);
        }
    }
    Vector(int size, BigDecimal data){
        this.data = new BigDecimal[size];

        for(int indx = 0; indx < size; indx++){
            this.data[indx] = data;
        }
    }
    Vector(Vector parm){
        data = new BigDecimal[parm.GetSize()];
        for(int indx = 0; indx < data.length; indx++){
            data[indx] = parm.GetEntry(indx);
        }
    }

    Vector Add(BigDecimal data){
        //Add all entries in the vector with a number
        Vector result = new Vector(this);
        for(int indx = 0; indx < this.data.length; indx++){
            result.SetEntry(indx, result.GetEntry(indx) + data);
        }
        return result;
    }
    Vector Add(Vector parm){
        //Add another vector
        if (parm.GetSize() != data.length){
            System.out.println("\tSizes are different!");
            return null;
        }
        Vector result = new Vector(this);
        for(int indx = 0; indx < data.length; indx++){
            result.SetEntry(indx, result.GetEntry(indx) + parm.GetEntry(indx));
        }
        return result;
    }
    Vector Scale(BigDecimal value){
        //Scale vector with a given value
        Vector result = new Vector(this);
        for(int indx = 0 ; indx < data.length; indx++){
            result.SetEntry(indx, result.GetEntry(indx) * value);
        }
        return result;
    }
    BigDecimal InnerProduct(Vector parm){
        if (parm.GetSize() != data.length){
            //Make sure the size are equal
            System.out.println("\tSizes are different!");
            return null;
        }

        BigDecimal result = 0.0;
        for(int indx = 0; indx < data.length; indx++){
            result += parm.GetEntry(indx) * data[indx];
        }

        return result;
    }
    Matrix RightMultiplication(Vector parm){
        //Multiply 2 vector with parm on the right side
        Matrix result = new Matrix(data.length, parm.GetSize());

        for(int curRow = 0; curRow < data.length; curRow++){
            result.SetRow(curRow, parm.Scale(data[curRow]));
        }
        return result;
    }
    Matrix LeftMultiplication(Vector parm){
        //Multiply 2 vector with parm on the left side
        Matrix result = new Matrix(parm.GetSize(), data.length);

        for(int curRow = 0; curRow < parm.GetSize(); curRow++){
            result.SetRow(curRow, this.Scale(parm.GetEntry(curRow)));
        }
        return result;
    }
    Vector Normalize(){
        return new Vector(this.Scale(1.0 / GetLength()));
    }
    void Copy(Vector parm){
        if (parm.GetSize() != GetSize()){
            data = new BigDecimal[parm.GetSize()];
        }

        for(int indx = 0; indx < GetSize(); indx++){
            data[indx] = parm.GetEntry(indx);
        }
    }

    void TakeInput(String input){
        String entries[] = input.split(Main.WHITESPACE);
        for(String entry: entries){
            entry = entry.replaceAll("\\s+","");
        }

        if (entries.length != data.length){
            data = new BigDecimal[entries.length];
        }
        for(int indx = 0; indx < data.length; indx++){
            data[indx] = BigDecimal.parseBigDecimal(entries[indx]);
        }
    }
    void Print(){
        System.out.println("Vector: ");
        for(BigDecimal entry : data){
            System.out.printf("%6.2f ", entry);
        }
        System.out.println();
    }
}
