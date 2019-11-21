package classic.multithreading;

import java.util.concurrent.CountDownLatch;

public class OrderPrintWithCountDownLatch {
    CountDownLatch stage1 = new CountDownLatch(1);
    CountDownLatch stage2 = new CountDownLatch(1);

    public void first(Runnable printFirst) throws InterruptedException {
        printFirst.run();
        stage1.countDown();
    }

    public void second(Runnable printSecond) throws InterruptedException {
        stage1.await();
        printSecond.run();
        stage2.countDown();
    }

    public void third(Runnable printThird) throws InterruptedException {
        stage2.await();
        printThird.run();
    }
}
