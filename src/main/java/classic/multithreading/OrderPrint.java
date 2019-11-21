package classic.multithreading;

public class OrderPrint {

    volatile int stage = 1;

    public void first() {
        System.out.println("one");
        stage = 2;
    }

    public void second() {
        while (stage != 2) ;
        System.out.println("two");
        stage = 3;
    }

    public void third() {
        while (stage != 3) ;
        System.out.println("three");
    }
}
