package test.com.linkallcloud.comm.security.sm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

/**
 * 
 * JUnit无法测试多线程的功能, 异常捕获后无法抛异常 (有其他扩展包支撑,如p-unit, GroboUtils)，暂时用最简单的callable来判断
 * 
 */
public class SignatureMessageThreadSafeTest {
    private ExecutorService exec = Executors.newFixedThreadPool(100);
    private int taskCount = 10000;

    // UncaughtExceptionHandler handel = new UncaughtExceptionHandler() {
    // @Override
    // public void uncaughtException(Thread t, Throwable e) {
    // System.out.println("catch catch catch");
    // System.out.println("catch catch catch1");
    // }
    // };
    //
    // private ExecutorService exec = Executors.newFixedThreadPool(2, new ThreadFactory() {
    // public Thread newThread(Runnable r) {
    // Thread thread = new Thread(r);
    // thread.setUncaughtExceptionHandler(handel);
    // return thread;
    // }
    // });

    /**
     * 简单的文本消息，明文传输方式
     * 
     * @throws Exception
     */
    @Test
    public void testMethodInSignatureMessageTest() throws Exception {
        List<Future<Boolean>> result = new ArrayList<Future<Boolean>>();
        for (int i = 0; i < taskCount; i++) {
            Callable<Boolean> t = new TestTask();
            result.add(exec.submit(t));
        }
        exec.shutdown();
        while (!exec.isTerminated()) {
            // Thread.yield();
        }
        for (int i = 0; i < result.size(); i++) {
            if(!(Boolean) result.get(i).get()){
                throw new IllegalArgumentException(i + "行错误");
            }
        }
    }

    public class TestTask implements Callable<Boolean> {
        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Runnable#run()
         */
        public void run() {
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.concurrent.Callable#call()
         */
        public Boolean call() throws Exception {
            try {
                SignatureMessageTest v = new SignatureMessageTest();
                v.testEncryptMessage();
                v.testMessage();
                v.testSimpleMessage();
                return true;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
