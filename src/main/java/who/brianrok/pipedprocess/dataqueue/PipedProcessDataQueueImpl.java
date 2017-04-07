package who.brianrok.pipedprocess.dataqueue;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Implementation of IPipedProcessDataQueue
 */
public class PipedProcessDataQueueImpl<E> implements IPipedProcessDataQueue<E> {

    private BlockingQueue<Optional<E>> queue;

    public PipedProcessDataQueueImpl(int capacity) {
        queue = new LinkedBlockingQueue<>(capacity);
    }

    @Override
    public void put(E elem) throws InterruptedException {
        queue.put(Optional.of(elem));
    }

    @Override
    public E take() throws InterruptedException {
        Optional<E> elemOptional = queue.take();
        return elemOptional.orElse(null);
    }

    @Override
    public int remainingCapacity() {
        return queue.remainingCapacity();
    }

    @Override
    public void finish() throws InterruptedException {
        queue.put(Optional.empty());
    }

    @Override
    public boolean isFinished() {
        return queue.size() == 1 && !queue.peek().isPresent();
    }
}
