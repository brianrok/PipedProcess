package who.brianrok.pipedprocess.dataqueue;

import who.brianrok.pipedprocess.exception.DataQueueException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of data queue manager
 */
public class DataQueueManagerImpl implements IDataQueueManager {

    private static final String REGISTER = "register";
    private static final String GET = "get";
    private static final String REMOVE = "remove";

    private final Map<String, IPipedProcessDataQueue> dataQueues = new HashMap<>();

    @Override
    public void registerDataQueue(String queueName, Class<?> elemClass) throws DataQueueException {
        registerDataQueue(queueName, elemClass, DEFAULT_CAPACITY);
    }

    @Override
    public void registerDataQueue(String queueName, Class<?> elemClass, int capacity) throws DataQueueException {
        // Queue with this name should not exists
        synchronized (dataQueues) {
            validateDataQueueExistent(queueName, REGISTER, false);
            dataQueues.put(queueName, new PipedProcessDataQueueImpl(elemClass, capacity));
        }
    }

    @Override
    public IPipedProcessDataQueue getDataQueue(String queueName) throws DataQueueException {
        synchronized (dataQueues) {
            validateDataQueueExistent(queueName, GET, true);
            return dataQueues.get(queueName);
        }
    }

    @Override
    public void removeDataQueue(String queueName) throws DataQueueException {
        synchronized (dataQueues) {
            validateDataQueueExistent(queueName, REMOVE, true);
            if (!getDataQueue(queueName).isFinished()) {
                throw new DataQueueException();
            }
            dataQueues.remove(queueName);
        }
    }

    @Override
    public Set<String> getAllQueueNames() {
        synchronized (dataQueues) {
            return dataQueues.keySet();
        }
    }

    private void validateDataQueueExistent(String queueName, String op, boolean shouldExists) throws DataQueueException {
        boolean existent = dataQueues.containsKey(queueName);
        if (existent != shouldExists) {
            throw new DataQueueException(String.format("Existent of queue %s should be %b for operation %s", queueName,
                    shouldExists, op));
        }
    }
}
