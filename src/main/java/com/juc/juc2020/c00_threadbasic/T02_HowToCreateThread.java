package com.juc.juc2020.c00_threadbasic;

import java.util.concurrent.*;

public class T02_HowToCreateThread {

    static class MyThread extends Thread{
        @Override
        public void run() {
            System.out.println("extends Thread");
        }
    }

    static class MyRun implements Runnable{
        @Override
        public void run() {
            System.out.println("implements Runnable");
        }
    }

    static class MyCall implements Callable<String>{
        @Override
        public String call() throws Exception {
            System.out.println("implements mycall");
            return "success";
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new MyThread().start();
        new Thread(new MyRun()).start();
        new Thread(new FutureTask<>(new MyCall())).start();

        //线程池
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(()->{
            System.out.println("ThreadPool");
        });

        //拿到Callable的返回值
        Future<String> result = service.submit(new MyCall());
        System.out.println("future result:" + result.get());
        service.shutdown();

        //单独线程使用FutureTask
        FutureTask<String> task = new FutureTask<>(new MyCall());
        new Thread(task).start();
        System.out.println("FutureTask result:" + task.get());
    }

}
