package classic.multithreading;

import java.util.ArrayList;
import java.util.List;

public class OrderPrintTest {

    public static void main(String[] args) {
        OrderPrint op = new OrderPrint();
        Runnable r1 = () -> {
            op.first();
        };
        Runnable r2 = () -> {
            op.second();
        };
        Runnable r3 = () -> {
            op.third();
        };

        List<Runnable> list = new ArrayList<>();
        list.add(r1);
        list.add(r2);
        list.add(r3);


        new Thread(list.get(2)).start();
        new Thread(list.get(1)).start();
        new Thread(list.get(0)).start();


    }
}
