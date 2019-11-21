package classic.multithreading;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 只要左右手边有人吃饭就等待
 */
public class DiningPhilosophersWithCondition {

    public static void main(String[] args) {

        ReentrantLock table = new ReentrantLock();

        // five philosophers
        PhilosopherWithCondition[] philosophers = new PhilosopherWithCondition[5];

        // init philosophers
        for (int i = 0; i < 5; ++i) {
            philosophers[i] = new PhilosopherWithCondition(i + 1, table);
        }
        for (int i = 0; i < 5; ++i) {
            philosophers[i].setLeft(philosophers[4 - i]);
            philosophers[i].setRight(philosophers[(i + 1) % 5]);
        }

        for (int i = 0; i < 5; ++i) {
            philosophers[i].start();
        }
    }
}
