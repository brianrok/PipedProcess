package who.brianrok.pipedprocess.dataqueue;

import who.brianrok.pipedprocess.exception.DataQueueException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of data queue manager
 */
public class DataQueueManagerImpl implements IDataQueueManager {

    private final Map<String, IPipedProcessDataQueue<?>> dataQueues = new HashMap<>();

    public void registerDataQueue(String queueName) throws DataQueueException {
        registerDataQueue(queueName, DEFAULT_CAPACITY);
    }

    public void registerDataQueue(String queueName, int capacity) throws DataQueueException {
        // Queue with this name should not exists
        synchronized (dataQueues) {
            validateDataQueueExistent(queueName, false);
            dataQueues.put(queueName, new PipedProcessDataQueueImpl<>(capacity));
        }
    }

    public IPipedProcessDataQueue<?> getDataQueue(String queueName) throws DataQueueException {
        synchronized (dataQueues) {
            validateDataQueueExistent(queueName, true);
            return dataQueues.get(queueName);
        }
    }

    public void removeDataQueue(String queueName) throws DataQueueException {
        synchronized (dataQueues) {
            validateDataQueueExistent(queueName, true);
            if (!getDataQueue(queueName).isFinished()) {
                throw new DataQueueException();
            }
            dataQueues.remove(queueName);
        }
    }

    public Set<String> getAllQueueNames() {
        synchronized (dataQueues) {
            return dataQueues.keySet();
        }
    }

    private void validateDataQueueExistent(String queueName, boolean shouldExists) throws DataQueueException {
        boolean existent = dataQueues.containsKey(queueName);
        if (existent != shouldExists) {
            throw new DataQueueException();
        }
    }
}
