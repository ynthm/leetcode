package classic.multithreading;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class PhilosopherWithSemaphore extends Thread {
    private int name;
    private ReentrantLock leftChopstick, rightChopstick;
    /**
     * 最多只有4个哲学家持有筷子，这里最好3
     */
    private Semaphore eatLimit;
    private Random random;
    private int eatTimes;


    public PhilosopherWithSemaphore(int name, ReentrantLock leftChopstick, ReentrantLock rightChopstick, Semaphore eatLimit) {
        this.name = name;
        this.leftChopstick = leftChopstick;
        this.rightChopstick = rightChopstick;
        this.eatLimit = eatLimit;
        random = new Random();
    }

    @Override
    public void run() {
        try {
            while (true) {
                int i = random.nextInt(3);
                TimeUnit.SECONDS.sleep(i); // Think for a while
                System.out.println("\033[1;36m" + this + "Philosopher " + name + " has thought " + i + " seconds" + "\033[0m");
                eatLimit.acquire();
                leftChopstick.lock();
                try {
                    rightChopstick.lock();
                        // Got the right chopstick
                        try {
                            TimeUnit.SECONDS.sleep(random.nextInt(3)); // Eat for a while
                            ++eatTimes;
                            // \033[1;31;40m    <!--1-高亮显示 31-前景色红色  40-背景色黑色-->
                            // \033[0m          <!--采用终端默认设置，即取消颜色设置-->
                            System.out.println("\033[1;33m" + this + "Philosopher " + name + " has Eat " + eatTimes + " times" + "\033[0m");
                        } finally {
                            rightChopstick.unlock();
                        }
                } finally {
                    leftChopstick.unlock();
                }
                eatLimit.release();
            }
        } catch (InterruptedException e) {
        }
    }
}
