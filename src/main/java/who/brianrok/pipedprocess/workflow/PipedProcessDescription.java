package who.brianrok.pipedprocess.workflow;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import who.brianrok.pipedprocess.dataqueue.IDataQueueManager;
import who.brianrok.pipedprocess.exception.PipedProcessWorkflowException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Description of process
 */
@Getter
public class PipedProcessDescription {
    private static int DEFAULT_RUNNER_COUNT = 1;

    private final Map<String, QueueInfo> dataQueues;
    private final Map<String, SubProcessInfo> subProcesses;
    private final Map<String, Method> subProcessHandlers;

    public PipedProcessDescription() {
        dataQueues = new HashMap<>();
        subProcesses = new HashMap<>();
        subProcessHandlers = new HashMap<>();
    }

    /**
     * Add queue by name and class
     * @param name Name of the queue
     * @param elemClass Class of the content of the queue
     */
    public void addQueue(String name, Class<?> elemClass) {
        addQueue(name, elemClass, IDataQueueManager.DEFAULT_CAPACITY);
    }

    /**
     * Add queue by name, class and capacity
     * @param name Name of the queue
     * @param elemClass Class of the content of the queue
     * @param capacity Capacity of the queue
     */
    public void addQueue(String name, Class<?> elemClass, int capacity) {
        synchronized (dataQueues) {
            if (dataQueues.containsKey(name)) {
                throw new PipedProcessWorkflowException(String.format("Adding an existing queue %s", name));
            }
            final QueueInfo info = QueueInfo.builder().elemClass(elemClass).capacity(capacity).build();
            dataQueues.put(name, info);
        }

    }

    /**
     * Register sub-process by name, in-queue and out-queue
     * @param name Name of the sub-process
     * @param inQueue Input data queue
     * @param outQueue Output data queue
     */
    public void registerSubProcess(String name, String inQueue, String outQueue) {
        registerSubProcess(name, inQueue, outQueue, DEFAULT_RUNNER_COUNT);
    }

    /**
     * Register sub-process by name, in-queue and out-queue, and define the runner count of the sub-process
     * @param name Name of the sub-process
     * @param inQueue Input data queue
     * @param outQueue Output data queue
     * @param runnerCount Count of the runners
     */
    public void registerSubProcess(String name, String inQueue, String outQueue, int runnerCount) {
        synchronized (subProcesses) {
            if (subProcesses.containsKey(name)) {
                throw new PipedProcessWorkflowException(String.format("Registering an existing subProcess %s", name));
            }
            final SubProcessInfo info = SubProcessInfo.builder().inQueue(inQueue).outQueue(outQueue).runnerCount(runnerCount)
                    .build();
            subProcesses.put(name, info);
        }
    }

    /**
     * Register the handler object for sub-process
     * @param name Name of the sub-process
     * @param handler Handler object (if handle method is a static method, pass a class object)
     */
    public void registerSubProcessHandler(String name, Object handler) {
        synchronized (subProcessHandlers) {
            synchronized (subProcesses) {
                if (subProcesses.containsKey(name)) {
                    return;
                }
            }
        }
        throw new PipedProcessWorkflowException(String.format("Registering handler for a process not exists %s", name));
    }

}

@Data
@Builder
class QueueInfo {
    Class<?> elemClass;
    int capacity;
}

@Data
@Builder
class SubProcessInfo {
    private String inQueue;
    private String outQueue;
    private int runnerCount;
}
