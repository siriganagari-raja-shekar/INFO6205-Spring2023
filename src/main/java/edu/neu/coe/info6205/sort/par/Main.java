package edu.neu.coe.info6205.sort.par;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * CONSIDER tidy it up a bit.
 */
public class Main {

    public static void main(String[] args) {
        processArgs(args);

        //Loop for different array sizes
        for (int i=21; i<=24; i++) {
            int[] array = new int[(int)Math.pow(2,i)];

            System.out.println("Array size = "+ (int)Math.pow(2,i));

            Random random = new Random();
            ArrayList<Long> timeList = new ArrayList<>();
            TreeMap<Integer, ArrayList<Long>> times = new TreeMap<>();
            for (int j = 1024; j >= 1; j/=2) {
                ParSort.cutoff = array.length/j;
                // for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);

                times.put(ParSort.cutoff, new ArrayList<>());

                for(int threads = 2; threads <= 64; threads *= 2){

                    long time;
                    long startTime = System.currentTimeMillis();
                    ForkJoinPool forkJoinPool = new ForkJoinPool(threads);
                    for (int t = 0; t < 10; t++) {

                        for (int k = 0; k < array.length; k++) array[k] = random.nextInt(10000000);
                        ParSort.sort(array, 0, array.length, forkJoinPool);
                    }
                    long endTime = System.currentTimeMillis();
                    time = (endTime - startTime);
                    times.get(ParSort.cutoff).add(time/10);

                }


            }
            StringBuilder sb = new StringBuilder();
            for (int j = 1; j <=1028 ; j*=2 ) {
                if(j==1){
                    sb.append("Cutoff/Threads,");
                }
                else {
                    sb.append(j + ",");
                }
            }
            sb.deleteCharAt(sb.length()-1);
            sb.append("\n");
            for(Map.Entry<Integer, ArrayList<Long>> entry: times.entrySet()){
                sb.append(entry.getKey()+",");
                ArrayList<Long> values = entry.getValue();
                for (int j = 0; j < values.size(); j++) {
                    sb.append(values.get(j)+",");
                }
                sb.deleteCharAt(sb.length()-1);
                sb.append("\n");
            }
            System.out.println(sb.toString());
        }

//        try {
//            FileOutputStream fis = new FileOutputStream("./src/result.csv");
//            OutputStreamWriter isr = new OutputStreamWriter(fis);
//            BufferedWriter bw = new BufferedWriter(isr);
//            int j = 0;
//            for (long i : timeList) {
//                String content = (double) 10000 * (j + 1) / 2000000 + "," + (double) i / 10 + "\n";
//                j++;
//                bw.write(content);
//                bw.flush();
//            }
//            bw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private static void processArgs(String[] args) {
        String[] xs = args;
        while (xs.length > 0)
            if (xs[0].startsWith("-")) xs = processArg(xs);
    }

    private static String[] processArg(String[] xs) {
        String[] result = new String[0];
        System.arraycopy(xs, 2, result, 0, xs.length - 2);
        processCommand(xs[0], xs[1]);
        return result;
    }

    private static void processCommand(String x, String y) {
        if (x.equalsIgnoreCase("-N")) setConfig(x, Integer.parseInt(y));
        else
            // TODO sort this out
            if (x.equalsIgnoreCase("-P")) //noinspection ResultOfMethodCallIgnored
                ForkJoinPool.getCommonPoolParallelism();
    }

    private static void setConfig(String x, int i) {
        configuration.put(x, i);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<String, Integer> configuration = new HashMap<>();


}
