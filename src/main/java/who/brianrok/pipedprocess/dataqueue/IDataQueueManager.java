package who.brianrok.pipedprocess.dataqueue;

import who.brianrok.pipedprocess.exception.DataQueueException;

import java.util.Set;

/**
 * Interface of data queue manager
 */
public interface IDataQueueManager {

    int DEFAULT_CAPACITY = 1000;

    /**
     * Register a new queue with given name
     * @param queueName Name of queue
     * @param elemClass Class of the element
     * @throws DataQueueException When the queue is already exists
     */
    void registerDataQueue(String queueName, Class<?> elemClass) throws DataQueueException;

    /**
     * Register a new queue with given name and fixed capacity
     * @param queueName Name of queue
     * @param capacity Capacity of the queue
     * @param elemClass Class of the element
     * @throws DataQueueException When the queue is already exists
     */
    void registerDataQueue(String queueName, Class<?> elemClass, int capacity) throws DataQueueException;

    /**
     * Get a queue with given name
     * @param queueName Name of queue
     * @throws DataQueueException When the queue does not exists
     */
    IPipedProcessDataQueue getDataQueue(String queueName) throws DataQueueException;

    /**
     * Remove a finished queue
     * @param queueName Name of queue
     * @throws DataQueueException When the queue does not exists or is not finished
     */
    void removeDataQueue(String queueName) throws DataQueueException;

    /**
     * Get all names of registered queues
     * @return All registered queue names
     */
    Set<String> getAllQueueNames();
}
