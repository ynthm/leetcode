package classic.multithreading;

import java.util.concurrent.Semaphore;

public class OrderPrintWithSemaphore {

    Semaphore stage2 = new Semaphore(0);
    Semaphore stage3 = new Semaphore(0);

    public void first(Runnable printFirst) throws InterruptedException {
        printFirst.run();
        stage2.release();
    }

    public void second(Runnable printSecond) throws InterruptedException {
        stage2.acquire();
        printSecond.run();
        stage3.release();
    }

    public void third(Runnable printThird) throws InterruptedException {
        stage3.acquire();
        printThird.run();
    }
}
