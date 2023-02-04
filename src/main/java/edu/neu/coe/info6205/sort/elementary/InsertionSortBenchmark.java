package edu.neu.coe.info6205.sort.elementary;

import edu.neu.coe.info6205.sort.SortWithHelper;
import edu.neu.coe.info6205.util.SorterBenchmark;
import edu.neu.coe.info6205.util.TimeLogger;
import edu.neu.coe.info6205.util.Utilities;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class InsertionSortBenchmark<T extends Comparable<T>> {
    /*
    Size of array
     */
    int n;

    /*
    Number of runs
     */
    int m;
    /*
    Logger for the current insertion sort
     */
    final static  TimeLogger[] timeLoggers = new TimeLogger[]{ new TimeLogger("Raw time per run", (t,x) -> t) };

    final Supplier<Integer[]> integerArraySupplier = () -> Utilities.fillRandomArray(Integer.class, new Random(), n, (random) -> random.nextInt());

    final UnaryOperator<Integer[]> fPreOrdered = (arr)-> {
        Arrays.sort(arr);
        return arr;
    };
    final UnaryOperator<Integer[]> fPreReverseOrdered = (arr)-> {
        Arrays.sort(arr, Comparator.reverseOrder());
        return arr;
    };
    final UnaryOperator<Integer[]> fPrePartiallyOrdered = (arr) -> {
        Random random = new Random();
        if(random.nextBoolean())
            Arrays.sort(arr, arr.length/2, arr.length);
        else
            Arrays.sort(arr, 0, arr.length/2);
        return arr;
    };

    public InsertionSortBenchmark(int n, int m){
        this.n = n;
        this.m = m;
    }
    public void runBenchMarksOnIntegerArrays(){
        System.out.println("InsertionSortBenchmark: N="+n);
        runBenchmarkOnIntegerArrays("Random sorted array:", arr -> arr);
        runBenchmarkOnIntegerArrays("Pre-ordered array:", fPreOrdered);
        runBenchmarkOnIntegerArrays("Partially ordered array:", fPrePartiallyOrdered);
        runBenchmarkOnIntegerArrays("Reverse ordered array:", fPreReverseOrdered);
    }
    public static void main(String[] args) {
        new InsertionSortBenchmark<Integer>(250,100).runBenchMarksOnIntegerArrays();
        new InsertionSortBenchmark<Integer>(500,50).runBenchMarksOnIntegerArrays();
        new InsertionSortBenchmark<Integer>(1000,20).runBenchMarksOnIntegerArrays();
        new InsertionSortBenchmark<Integer>(2000,10).runBenchMarksOnIntegerArrays();
        new InsertionSortBenchmark<Integer>(4000,5).runBenchMarksOnIntegerArrays();
        new InsertionSortBenchmark<Integer>(8000,3).runBenchMarksOnIntegerArrays();
        new InsertionSortBenchmark<Integer>(16000,2).runBenchMarksOnIntegerArrays();

    }

    public void runBenchmarkOnIntegerArrays(String description,UnaryOperator<Integer[]> fPre){
        System.out.println(description);
        SortWithHelper<Integer> helper = new InsertionSort<Integer>();
        SorterBenchmark<Integer> sorter = new SorterBenchmark<Integer>(Integer.class, fPre, helper, integerArraySupplier.get(),m, timeLoggers);
        sorter.run(n);
    }
}
