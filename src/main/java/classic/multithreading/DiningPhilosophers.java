package classic.multithreading;

/**
 * 哲学家就餐主类
 */
public class DiningPhilosophers {

    public static void main(String[] args) throws InterruptedException {
        //five philosophers
        Philosopher[] philosophers = new Philosopher[5];
        //five chopsticks
        Chopstick[] chopsticks = new Chopstick[5];

        //init philosophers and chopsticks
        for (int i = 0; i < 5; ++i)
            chopsticks[i] = new Chopstick(i);
        for (int i = 0; i < 5; ++i) {
            philosophers[i] = new Philosopher(chopsticks[i], chopsticks[(i + 1) % 5]);
            philosophers[i].start();
        }
        for (int i = 0; i < 5; ++i) {
            philosophers[i].join();
        }
    }
}
