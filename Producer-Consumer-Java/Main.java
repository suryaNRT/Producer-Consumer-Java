
import java.util.LinkedList;

class PC {
    private final LinkedList<Integer> buffer = new LinkedList<>();
    private final int capacity = 2;

    public synchronized void produce(int value)
            throws InterruptedException {
        while (buffer.size() == capacity) {
            wait();
        }

        buffer.add(value);
        System.out.println("Producer produced-" + value);
        notifyAll();
    }

    public synchronized void consume()
            throws InterruptedException {
        while (buffer.isEmpty()) {
            wait();
        }

        int value = buffer.removeFirst();
        System.out.println("Consumer consumed-" + value);
        notifyAll();
    }
}

public class Main {
    public static void main(String[] args)
            throws InterruptedException {
        PC pc = new PC();

        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    pc.produce(i);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    pc.consume();
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }
}
