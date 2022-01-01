package com.juc.juc2020.c00_threadbasic;

import org.springframework.util.StopWatch;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * 多线程vs单线程
 * 线程上下文切换
 */
public class T01_MultiVSSingle_contextSwith {

    private static double[] nums = new double[1_0000_0000];
    private static Random r = new Random();
    private static DecimalFormat df = new DecimalFormat("0.00");
    static {
        for (int i=0; i<nums.length; i++){
            nums[i] = r.nextDouble();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        m1();
        m2();
        m3(2);
        m3(4);
        m3(8);
        m3(16);
        m3(32);
        m3(100);
        m3(1000);
        m3(2000);
        m3(5000);
    }

    private static void m1(){
        StopWatch sw = new StopWatch();
        sw.start("a");

        double result = 0.00;
        for (int i=0; i< nums.length; i++){
            result += nums[i];
        }

        sw.stop();
        System.out.println("mi time:" + sw.getLastTaskTimeMillis() + " result:" + result);
    }

    static double result1 = 0.0, result2 = 0.0, result = 0.0;
    private static void m2() throws InterruptedException {
        Thread t1 = new Thread(()->{
            for (int i=0; i< nums.length/2; i++){
                result1 += nums[i];
            }
        });

        Thread t2 = new Thread(()->{
            for (int i=nums.length/2; i< nums.length; i++){
                result2 += nums[i];
            }
        });

        StopWatch sw = new StopWatch();
        sw.start("b");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        result = result1 + result2;

        sw.stop();
        System.out.println("m2 time:" + sw.getLastTaskTimeMillis() + " result:" + result);
    }

    private static void m3(int threadCount) throws InterruptedException {
        Thread[] threads = new Thread[threadCount];
        double[] results = new double[threadCount];
        //每个线程要处理的数量
        final int segmentCount = nums.length/threadCount;

        for (int i=0; i<threadCount; i++){
            int m = i;
            threads[i] = new Thread(()->{
                for (int j = m * segmentCount; j < (m+1) * segmentCount && j< nums.length; j++){
                    results[m] += nums[j];
                }

                //System.out.printf("reusults[%s] = %s\n", m, results[m]);
            });

        }

        double resultsM3 = 0.0;

        StopWatch sw = new StopWatch();
        sw.start("c"+threadCount);

        for (Thread t:threads){
            t.start();
        }

        for (Thread t:threads){
            t.join();
        }

        for (int i=0; i<results.length; i++){
            resultsM3 += results[i];
        }


        sw.stop();
        System.out.println("m3 threadCount:" + threadCount + " time:" + sw.getLastTaskTimeMillis() + " result:" + resultsM3);
    }
}
