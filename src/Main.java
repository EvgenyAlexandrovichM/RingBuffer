
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public Main() {
    }

    public static void main(String[] var0) {
        BlockingQueue<Integer> buffer = new ArrayBlockingQueue<>(5);
        Thread producer = new Thread(() -> {
            try {
                for(int i = 1; i <= 10; i++) {
                    buffer.put(i);
                    System.out.println("Producer: " + i);
                }
            } catch (InterruptedException var2) {
                Thread.currentThread().interrupt();
            }

        });
        Thread consumer = new Thread(() -> {
            try {
                for(int i = 1; i <= 10; i++) {
                    int value = buffer.take();
                    System.out.println("Consumer: " + value);
                }
            } catch (InterruptedException var3) {
                Thread.currentThread().interrupt();
            }

        });
        producer.start();
        consumer.start();
    }
}