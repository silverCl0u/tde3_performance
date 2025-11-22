import java.util.concurrent.*;

public class DeadlockDemo {
    static final Object LOCK_A = new Object();
    static final Object LOCK_B = new Object();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            synchronized (LOCK_A) {
                System.out.println("T1: Segurando Lock A...");
                dormir(50);
                System.out.println("T1: Tentando pegar Lock B...");
                synchronized (LOCK_B) {
                    System.out.println("T1: Concluiu!"); 
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (LOCK_B) {
                System.out.println("T2: Segurando Lock B...");
                dormir(50);
                System.out.println("T2: Tentando pegar Lock A...");
                synchronized (LOCK_A) {
                    System.out.println("T2: Concluiu!"); 
                }
            }
        });

        t1.start();
        t2.start();
    }

    static void dormir(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }
    }
}