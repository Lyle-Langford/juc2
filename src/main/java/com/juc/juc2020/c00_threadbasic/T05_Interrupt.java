package com.juc.juc2020.c00_threadbasic;

public class T05_Interrupt {

    public static void main(String[] args) throws InterruptedException {
        m2();
    }


    static void m1() throws InterruptedException {
        Thread t = new Thread(()->{
            for (;;){
                if (Thread.currentThread().isInterrupted()){
                    System.out.println("Thread is interrupted");
                    break;
                }
            }
        });

        t.start();
        Thread.sleep(2000);
        t.interrupt();
    }

    /**
     * 线程睡眠时，被设置interrupt会抛出InterruptedException异常.
     */
    static void m2(){
        Thread t = new Thread(()->{
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("thread is interrupted!");
                System.out.println(Thread.currentThread().isInterrupted());
            }
        });

        t.start();
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.interrupt();
    }

}
