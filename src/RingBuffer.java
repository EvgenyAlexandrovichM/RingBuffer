import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RingBuffer<T> {
    private final Object[] buffer;
    private int readIndex = 0;
    private int writeIndex = 0;
    private int count = 0;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();

    public RingBuffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be > 0");
        }
        buffer = new Object[capacity];
    }

    public void put(T item) throws InterruptedException {
        if (item == null) {
            throw new NullPointerException("Null values not allowed");
        }
        lock.lock();
        try {
            while (count == buffer.length) {
                notFull.await();
            }
            buffer[writeIndex] = item;
            writeIndex = (writeIndex + 1) % buffer.length;
            count++;
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public T take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                notEmpty.await();
            }
            T item = (T) buffer[readIndex];
            buffer[readIndex] = null;
            readIndex = (readIndex + 1) % buffer.length;
            count--;
            notFull.signalAll();
            return item;
        } finally {
            lock.unlock();
        }
    }
}
