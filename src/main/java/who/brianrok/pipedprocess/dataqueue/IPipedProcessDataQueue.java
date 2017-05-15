package who.brianrok.pipedprocess.dataqueue;

import java.util.Optional;

/**
 * Data Queue for piped process
 */
public interface IPipedProcessDataQueue {

    /**
     * Put an element to data queue
     * @param elem Element to put to queue
     * @throws InterruptedException
     */
    void put(Object elem) throws InterruptedException;

    /**
     * Get an element from data queue
     * @return First element in the queue
     * @throws InterruptedException
     */
    Optional<?> take() throws InterruptedException;

    /**
     * Get the remaining capacity of the queue
     * @return
     */
    int remainingCapacity();

    /**
     * Put the finish signal to the data queue
     * @throws InterruptedException
     */
    void finish() throws InterruptedException;

    /**
     * Check is the queue is finished
     * @return Finished
     */
    boolean isFinished();

    /**
     * Get the type of elements
     * @return Tyoe of elements
     */
    Class<?> getElementType();
}
