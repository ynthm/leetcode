package classic.multithreading;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 管程模型是传说中的万能模型
 */
public class OrderPrintWithLock {
    Lock lock = new ReentrantLock();
    volatile int stage = 1;
    Condition stage2 = lock.newCondition();
    Condition stage3 = lock.newCondition();

    public void first(Runnable printFirst) throws InterruptedException {
        lock.lock();
        try {
            printFirst.run();
            stage = 2;
            stage2.signal();
        } finally {
            lock.unlock();
        }
    }

    public void second(Runnable printSecond) throws InterruptedException {
        lock.lock();
        try {
            while (stage != 2) {
                stage2.await();
            }
            printSecond.run();
            stage = 3;
            stage3.signal();
        } finally {
            lock.unlock();
        }
    }

    public void third(Runnable printThird) throws InterruptedException {
        lock.lock();
        try {
            while (stage != 3) {
                stage3.await();
            }
            printThird.run();
        } finally {
            lock.unlock();
        }
    }
}
