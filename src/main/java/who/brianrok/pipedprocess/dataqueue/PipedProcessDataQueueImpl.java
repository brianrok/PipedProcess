package who.brianrok.pipedprocess.dataqueue;

import who.brianrok.pipedprocess.exception.DataQueueException;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Implementation of IPipedProcessDataQueue
 */
public class PipedProcessDataQueueImpl implements IPipedProcessDataQueue {

    private Class<?> elemClass;
    private BlockingQueue<Optional<?>> queue;

    PipedProcessDataQueueImpl(Class elemClass, int capacity) {
        this.elemClass = elemClass;
        queue = new LinkedBlockingQueue<>(capacity);
    }

    @Override
    public void put(Object elem) throws InterruptedException {
        if (!elemClass.isAssignableFrom(elem.getClass())) {
            throw new DataQueueException("Put unsupported data to data queue!");
        }
        queue.put(Optional.of(elem));
    }

    @Override
    public Object take() throws InterruptedException {
        Optional<?> elemOptional = queue.take();
        return elemClass.cast(elemOptional.orElse(null));
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
        synchronized (this) {
            return queue.size() == 1 && !queue.peek().isPresent();
        }
    }

    @Override
    public Class<?> getElementType() {
        return elemClass;
    }
}
