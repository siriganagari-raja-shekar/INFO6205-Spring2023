package edu.neu.coe.info6205.union_find;

import java.util.Random;

public class UF_HWQUPC_Benchmark {

    public static void main(String[] args) {

        for (int i = 250,j=1000; i < 1e7;i*=2, j/=2) {
            if(j == 0 )
                j = 1;
            long c = 0;
            for (int k = 0; k < j; k++) {
                c += count(i);
            }
            c = c/j;
            System.out.println("Number of pairs generated for N=" + i + " objects to be connected is " + c);
        }
    }

    public static long count(int n){
        long c = 0;
        UF_HWQUPC uf = new UF_HWQUPC(n);
        Random random = new Random();
        int connections = 0;
        while(uf.components() != 1){
            int a = random.nextInt(n), b = random.nextInt(n);
            if(!uf.connected(a,b)){
                uf.union(a,b);
                connections++;
            }
            c++;
        }
//        Uncomment the following line to print the number of connections
//        System.out.println("Number of connections for N=" + n + " is "+connections);
        return c;
    }
}
