package who.brianrok.pipedprocess.dataqueue;

/**
 * Data Queue for piped process
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public interface IPipedProcessDataQueue<E> {

    /**
     * Put an element to data queue
     * @param elem Element to put to queue
     * @throws InterruptedException
     */
    void put(E elem) throws InterruptedException;

    /**
     * Get an element from data queue
     * @return First element in the queue
     * @throws InterruptedException
     */
    E take() throws InterruptedException;

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
}
