package classic.multithreading;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PhilosopherWithCondition extends Thread {

    private int name;
    private boolean eating;
    private PhilosopherWithCondition left;
    private PhilosopherWithCondition right;
    private ReentrantLock table;
    private Condition condition;
    private Random random;
    private int eatTimes;

    public PhilosopherWithCondition(int name, ReentrantLock table) {
        this.name = name;
        eating = false;
        this.table = table;
        condition = table.newCondition();
        random = new Random();
    }

    public void setLeft(PhilosopherWithCondition left) {
        this.left = left;
    }

    public void setRight(PhilosopherWithCondition right) {
        this.right = right;
    }

    public void run() {
        try {
            while (true) {
                think();
                eat();
            }
        } catch (InterruptedException e) {
        }
    }

    private void think() throws InterruptedException {
        table.lock();
        try {
            eating = false;
            left.condition.signal();
            right.condition.signal();
        } finally {
            table.unlock();
        }

        int i = random.nextInt(3);
        TimeUnit.SECONDS.sleep(i);
        System.out.println("\033[1;36m" + this + "Philosopher " + name + " has thought " + i + " seconds" + "\033[0m");
    }

    private void eat() throws InterruptedException {
        table.lock();
        try {
            while (left.eating || right.eating)
                condition.await();
            eating = true;
            ++eatTimes;
            System.out.println("\033[1;33m" + this + "Philosopher " + name + " has Eat " + eatTimes + " times" + "\033[0m");
            TimeUnit.SECONDS.sleep(random.nextInt(3));
        } finally {
            table.unlock();
        }
    }
}