package classic.multithreading;

import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophersWithTimeout {

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock[] lockList = {new ReentrantLock(),
                new ReentrantLock(),
                new ReentrantLock(),
                new ReentrantLock(),
                new ReentrantLock()};


        //five philosophers
        PhilosopherWithTimeout[] philosophers = new PhilosopherWithTimeout[5];


        //init philosophers and chopsticks
        for (int i = 0; i < 5; ++i) {
            philosophers[i] = new PhilosopherWithTimeout(i + 1, lockList[i], lockList[(i + 1) % 5]);
            philosophers[i].start();
        }
        for (int i = 0; i < 5; ++i) {
            philosophers[i].join();
        }
    }
}
