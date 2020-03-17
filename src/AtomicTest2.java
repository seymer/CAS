import java.sql.Time;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicTest2 {
    private static AtomicInteger index = new AtomicInteger(10);
    static AtomicStampedReference<Integer> stampedReference = new AtomicStampedReference<Integer>(10, 1);

    public static void main(String[] args) {
        new Thread(() -> {
            int stamp = stampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "First version number: " + stamp);
            stampedReference.compareAndSet(10, 11, stampedReference.getStamp(), stampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "Second version number: " + stampedReference.getStamp());
            stampedReference.compareAndSet(11, 10, stampedReference.getStamp(), stampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "Third version number: " + stampedReference.getStamp());
        }, "Bob: ").start();

        new Thread(() -> {
            try {
                int stamp = stampedReference.getStamp();
                System.out.println(Thread.currentThread().getName() + "First version number: " + stamp);
                TimeUnit.SECONDS.sleep(2);
                boolean isSuccess = stampedReference.compareAndSet(10, 12, stampedReference.getStamp(), stampedReference.getStamp() + 1);
                System.out.println(Thread.currentThread().getName() + "Does it success: " + isSuccess + ", current number : " + stampedReference.getStamp());
                System.out.println(Thread.currentThread().getName() + "Current real value: " + stampedReference.getReference());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Alice: ").start();
    }
}
