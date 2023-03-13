package edu.neu.coe.info6205.sort.linearithmic;

import edu.neu.coe.info6205.sort.*;
import edu.neu.coe.info6205.sort.elementary.HeapSort;
import edu.neu.coe.info6205.util.Config;
import edu.neu.coe.info6205.util.SorterBenchmark;
import edu.neu.coe.info6205.util.TimeLogger;
import edu.neu.coe.info6205.util.Utilities;

import java.util.*;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class LinearithmicSortBenchmarkMain {
    /*
    Number of elements in the array to sort
     */
    int n;

    /*
    Number of runs to average the sort time
     */
    int runs;

    /*
    Description for sort algorithm
     */
    String description;

    /*
    Supplier for integer arrays
     */
    final Supplier<Integer[]> integerArraySupplier = () -> Utilities.fillRandomArray(Integer.class, new Random(), n, (random) -> random.nextInt());

    /*
    Time loggers for benchmark
     */
    final static  TimeLogger[] timeLoggers = new TimeLogger[]{ new TimeLogger("Raw time per run", (t, x) -> t) };

    /*
    Shuffler for array before sorting
     */
    final static UnaryOperator<Integer[]> fPreShuffler = (arr) -> {
        List<Integer> list = Arrays.asList(arr);
        Collections.shuffle(list);
        return list.stream().toArray(Integer[]::new);
    };

    final static UnaryOperator<Integer[]> fOrdered = (arr) -> {
        List<Integer> list = Arrays.asList(arr);
        Collections.sort(list);
        return list.stream().toArray(Integer[]::new);
    };


    /*
    Config object for helpers
     */
    Config config;

    public LinearithmicSortBenchmarkMain() {
    }

    public LinearithmicSortBenchmarkMain(int n, int runs) {
        this.n = n;
        this.runs = runs;
    }

    public LinearithmicSortBenchmarkMain(int n, int runs, Config config) {
        this.n = n;
        this.runs = runs;
        this.config = config;
    }


    public static void main(String[] args) {

        try {
            for (int i = 10000; i <= 2560000; i*=2) {
                new LinearithmicSortBenchmarkMain(i, 100, Config.load()).runBenchMarks();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void runBenchMarks(){
        boolean instrumentationEnabled = config.getBoolean("helper", "instrument");
        Helper<Integer> helper;
        System.out.println("N="+n);
        try{
            helper = HelperFactory.create("Quick sort dual pivot helper", n, instrumentationEnabled, config);
            runBenchMark(new QuickSort_DualPivot<>(helper));


            helper = HelperFactory.create("Merge sort helper", n, instrumentationEnabled, config);
            runBenchMark(new MergeSort<>(helper));

            helper = HelperFactory.create("Heap sort helper", n, instrumentationEnabled, config);
            runBenchMark(new HeapSort<>(helper));

        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    private void runBenchMark(SortWithHelper<Integer> sortWithHelper){

        SorterBenchmark<Integer> sorterBenchmark = new SorterBenchmark<>(Integer.class, fPreShuffler,sortWithHelper, integerArraySupplier.get(),runs,timeLoggers);
        sorterBenchmark.run(n);

        System.out.println("Stats= " + sortWithHelper.getHelper().showStats());
    }
}
