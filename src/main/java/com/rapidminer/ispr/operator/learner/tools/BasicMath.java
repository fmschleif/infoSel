/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rapidminer.ispr.operator.learner.tools;

import java.nio.file.FileVisitResult;
import java.util.Collection;
import java.util.Iterator;


/**
 *
 * @author Marcin
 */
public class BasicMath {

/*
    public static void printLabeledData(LabeledDataInterface d) {
        int anX = d.getXAttributesNumber();
        int anY = d.getYAttributesNumber();
        int dn = d.getVectorsNumber();
        for (int i = 0; i < dn; i++) {
            System.out.print("X = ");
            for (int j = 0; j < anX; j++) {
                System.out.print(d.getXElement(i, j) + " ");
            }
            System.out.print("Y = ");
            for (int j = 0; j < anY; j++) {
                System.out.print(d.getYElement(i, j) + " ");
            }
            System.out.print("\n");
        }
    }
*/
    /**
     * Sort elements in X and order elements in Y in the same order as in X
     * @param X
     * @param Y
     */
    public static void sort(double[] X, double[] Y) {
        double tmpX, tmpY;
        int size = X.length;
        for (int j = 0; j < X.length; j++) {
            for (int i = 1; i < size; i++) {
                if (X[i] < X[i - 1]) {
                    tmpX = X[i - 1];
                    X[i - 1] = X[i];
                    X[i] = tmpX;
                    tmpY = Y[i - 1];
                    Y[i - 1] = Y[i];
                    Y[i] = tmpY;
                }
            }
            size--;
        }
    }

    /**
     * Sort elements in X and order elements in Y in the same order as in X
     * @param X - array which elements will be sorted
     * @param Y - array which elements will be reordered according to the order of X
     */
    public static void sort(double[] X, int[] Y) {
        double tmpX;
        int tmpY;
        int size = X.length;
        for (int j = 0; j < X.length; j++) {
            for (int i = 1; i < size; i++) {
                if (X[i] < X[i - 1]) {
                    tmpX = X[i - 1];
                    X[i - 1] = X[i];
                    X[i] = tmpX;
                    tmpY = Y[i - 1];
                    Y[i - 1] = Y[i];
                    Y[i] = tmpY;
                }
            }
            size--;
        }
    }
        
    /**
     * Calculate arytchmetic mean value of elements in X between start and end index
     * @param X 
     * @param start - starting element
     * @param end - last accepted element
     * @return
     */
    public static double mean(double[] X, int start, int end){
        double mean = 0;
        for (int i=start;i<=end;i++)
            mean += X[i];
        mean /= end-start+1;
        return mean;
    }
    
    /**
     * Mean value of all elements in X
     * @param X
     * @return
     */
    public static double mean(double[] X){
        return mean(X,0,X.length-1);
    }

    /**
     * Calculate arythmetic mean value of elements in X between start and end index
     * @param X
     * @param start - first element in X
     * @param end - last element in X
     * @return
     */
    public static double mean(Collection<Number> X, int start, int end){
        double mean = 0;
        Iterator<Number> iter = X.iterator();        
        int i = 0;
        while (iter.hasNext()){
            if (i < start) continue;
            if (i > end) break;            
            Number x = iter.next();
            mean += x.doubleValue();
            i++;
        }            
        mean /= end-start+1;
        return mean;
    }
    
    /**
     * 
     * Mean value of all elements in X
     * @param X
     * @return
     */
    public static double mean(Collection<Number> X){
        return mean(X,0,X.size()-1);
    }

    /**     
     * square error
     * @param X
     * @param mean
     * @return
     */
    public static double simpleVariance(double[] X, double mean){
        double var = 0;
        for (int i=0; i<X.length; i++)
            var += (X[i] - mean)*(X[i] - mean);
        return var;
    }

     /**
     * Square error of elements in collection
     * @param X
     * @param mean
     * @return
     */
    public static double simpleVariance(Collection<Number> X, double mean){
        double var = 0;
        for (Number x : X)
            var += (x.doubleValue() - mean)*(x.doubleValue() - mean);
        return var;
    }

    /**
     * Standard deviation of elements in X, calculated with pre calculated mean 
     * value, and an index of first and las element
     * @param X
     * @param mean
     * @param start
     * @param end
     * @return
     */
    public static double std(double[] X, double mean, int start, int end){
        double var = 0;        
        for (int i=start; i<=end; i++)
            var += (X[i] - mean)*(X[i] - mean);
        //if (start != end)
        var /= end-start+1;
        var = Math.sqrt(var);
        return var;
    }

    /**
     * Standard deviation of elements in X, calculated within the elements between  first and last element
     * @param X
     * @param start
     * @param end
     * @return
     */
    public static double std(double[] X, int start, int end){
        double m = mean(X,start,end);
        return BasicMath.std(X, m, start, end);
    }

    /**
     * Standard deviation of elements in X
     * @param X
     * @return
     */
    public static double std(double[] X){
        return BasicMath.std(X,0,X.length-1);
    }
    
    /**
     * Calculate standard deviation of elements in X
     * @param X - input collection
     * @param mean - mean value
     * @param start - index of first element in collection taken into account
     * @param end - index of last element in collection taken into account
     * @return value of standard deviation
     */
    public static double std(Collection<Number> X, double mean, int start, int end){
        double var = 0;                
        Iterator<Number> iter = X.iterator();        
        int i = 0;
        while (iter.hasNext()){
            if (i < start) continue;
            if (i > end) break;            
            Number x = iter.next();
            var += (x.doubleValue() - mean)*(x.doubleValue() - mean);
            i++;
        }        
        //if (start != end)
        var /= end-start+1;
        var = Math.sqrt(var);
        return var;
    }

    /**
     * Calculate standard deviation of elements in X
     * @param X - input collection    
     * @param start - index of first element in collection taken into account
     * @param end - index of last element in collection taken into account
     * @return value of standard deviation
     */
    public static double std(Collection<Number> X, int start, int end){
        double m = mean(X,start,end);
        return BasicMath.std(X, m, start, end);
    }

    /**
     * Calculate standard deviation of elements in X
     * @param X - input collection     
     * @return value of standard deviation
     */
    public static double std(Collection<Number> X){
        return BasicMath.std(X,0,X.size()-1);
    }

    /**
     * 
     * @param b
     * @return
     */
    public static boolean[] not(boolean[] b){
        for (int i = 0; i<b.length; i++)
            b[i] = !b[i];
        return b;
    }

    /**
     * 
     * @param b
     * @return
     */
    public static int sum(boolean[] b){
        int sum = 0;
        for (int i = 0; i<b.length; i++)
            if (b[i]) sum++;
        return sum;
    }

    /**
     * 
     * @param b
     * @return
     */
    public static double sum(double[] b){
        double sum = 0;
        for (int i = 0; i<b.length; i++)
            sum += b[i];
        return sum;
    }

    /**
     * 
     * @param b
     * @return
     */
    public static double sum(float[] b){
        double sum = 0;
        for (int i = 0; i<b.length; i++)
            sum += b[i];
        return sum;
    }

    /**
     * 
     * @param b
     * @return
     */
    public static int sum(int[] b){
        int sum = 0;
        for (int i = 0; i<b.length; i++)
            sum += b[i];
        return sum;
    }

    /**
     * 
     * @param b
     * @return
     */
    public static long sum(long[] b){
        long sum = 0;
        for (int i = 0; i<b.length; i++)
            sum += b[i];
        return sum;
    }

    /**
     * 
     * @param x1
     * @param x2
     * @return
     */
    public static double[] concatenate(double[] x1, double[] x2){
        double[] y = new double[x1.length + x2.length];
        System.arraycopy(x1, 0, y, 0, x1.length);
        System.arraycopy(x2, 0, y, x1.length, x2.length);
        return y;
    }
    
    public static double log2(double x){        
        return Math.log(x)/Math.log(2);
    }
    
    
    public static double sigmoid(double x){
        return 1.0/(1 + Math.exp(-x));
    }
/*
    public static double classificationAccuracy(IndexedData y1, IndexedData y2){
        if (y1.getRowsNumber() != y2.getRowsNumber()){
            System.err.println("Incorect data sizes y1 and y2");
            return 0;
        }
        double acc = 0;
        for (int i=0; i<y1.getRowsNumber(); i++)
            acc += (Math.round(y2.getElement(i, 0)) == y1.getElement(i, 0)) ? 1 : 0;
        acc /= y1.getRowsNumber();
        return acc;
    }
 */
    public static void main(String[] args){
        for (double i = -1; i < 1; i+=0.01) {
            System.out.println(sigmoid(i));
        }
    }
}