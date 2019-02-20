package com.company;

public class Vector {
    //Treat this as neutral matrix, no row or col major
    private double data[];

    double[] GetData(){
        return data;
    }
    double GetEntry(int indx){
        return data[indx];
    }
    int GetSize(){
        return data.length;
    }
    void SetEntry(int indx, double value){
        data[indx] = value;
    }
    double GetLength(){
        double result = 0;
        for(int indx = 0; indx < data.length; indx++){
            result += Math.pow(data[indx], 2);
        }
        return Math.sqrt(result);
    }

    Vector(double data[]){
        this.data = new double[data.length];

        System.arraycopy(data, 0, this.data, 0, data.length);
    }
    Vector(String data){
        String splitData[] = data.split(Main.WHITESPACE);

        this.data = new double[splitData.length];
        for(int indx = 0; indx < splitData.length; indx++){
            this.data[indx] = Integer.parseInt(splitData[indx]);
        }
    }
    Vector(int size, double data){
        this.data = new double[size];

        for(int indx = 0; indx < size; indx++){
            this.data[indx] = data;
        }
    }
    Vector(Vector parm){
        data = new double[parm.GetSize()];
        for(int indx = 0; indx < data.length; indx++){
            data[indx] = parm.GetEntry(indx);
        }
    }

    Vector Add(double data){
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
    Vector Scale(double value){
        //Scale vector with a given value
        Vector result = new Vector(this);
        for(int indx = 0 ; indx < data.length; indx++){
            result.SetEntry(indx, result.GetEntry(indx) * value);
        }
        return result;
    }
    Integer InnerProduct(Vector parm){
        if (parm.GetSize() != data.length){
            //Make sure the size are equal
            System.out.println("\tSizes are different!");
            return null;
        }

        int result = 0;
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

    void TakeInput(String input){
        String entries[] = input.split(Main.WHITESPACE);
        if (entries.length != data.length){
            data = new double[entries.length];
        }
        for(int indx = 0; indx < data.length; indx++){
            data[indx] = Double.parseDouble(entries[indx]);
        }
    }
    void Print(){
        System.out.println("~~Vector: ");
        for(double entry : data){
            System.out.printf("%6.2f ", entry);
        }
        System.out.println();
    }
}
