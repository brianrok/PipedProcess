package who.brianrok.pipedprocess.workflow;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import who.brianrok.pipedprocess.annotation.PipedProcessHandler;
import who.brianrok.pipedprocess.dataqueue.IDataQueueManager;
import who.brianrok.pipedprocess.exception.PipedProcessWorkflowException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Description of process
 */
@Getter
public class PipedProcessDescription {
    private final Map<String, QueueInfo> dataQueues = new HashMap<>();
    private final Map<String, SubProcessInfo> subProcesses = new HashMap<>();

    /**
     * Setup process descriptions use
     * @param handlerObj The handler object (use class object if handlers are static)
     */
    public void register(Object handlerObj) throws PipedProcessWorkflowException {
        Class<?> clazz;
        if (handlerObj instanceof Class) {
            clazz = (Class<?>) handlerObj;
        } else {
            clazz = handlerObj.getClass();
        }
        while (clazz != Object.class) {
            Arrays.stream(clazz.getDeclaredMethods()).filter(method -> Modifier.isPublic(method.getModifiers()))
                    .filter(this::isValidHandlerMethod).forEach(method -> doProcessMethod(handlerObj, method));
            clazz = clazz.getSuperclass();
        }
        validate();
    }

    private void validate() throws PipedProcessWorkflowException {
    }

    /**
     * Add queue by name and class
     * @param name Name of the queue
     * @param elemClass Class of the content of the queue
     */
    private void addQueue(String name, Class<?> elemClass) {
        addQueue(name, elemClass, IDataQueueManager.DEFAULT_CAPACITY);
    }

    /**
     * Add queue by name, class and capacity
     * @param name Name of the queue
     * @param elemClass Class of the content of the queue
     * @param capacity Capacity of the queue
     */
    private void addQueue(String name, Class<?> elemClass, int capacity) {
        synchronized (this) {
            if (dataQueues.containsKey(name) && elemClass != dataQueues.get(name).getElemClass()) {
                throw new PipedProcessWorkflowException(String.format("Adding an existing queue %s with different type %s",
                        name,elemClass.getSimpleName()));
            }
            final QueueInfo info = QueueInfo.builder().elemClass(elemClass).capacity(capacity).build();
            dataQueues.put(name, info);
        }

    }

    /**
     * Register sub-process by name, in-queue and out-queue, and define the runner count of the sub-process
     * @param name Name of the sub-process
     * @param inQueue Input data queue
     * @param outQueue Output data queue
     * @param handlerInstance Handler instance (if handle method is a static method, pass a class object)
     * @param method The handler method
     * @param runnerCount Count of the runners
     */
    private void registerSubProcess(String name, String inQueue, String outQueue, Object handlerInstance, Method method, int runnerCount) {
        synchronized (this) {
            if (subProcesses.containsKey(name)) {
                throw new PipedProcessWorkflowException(String.format("Registering an existing subProcess %s", name));
            }
            checkQueueName(inQueue);
            checkQueueName(outQueue);

            final SubProcessInfo info = SubProcessInfo.builder().inQueue(inQueue).outQueue(outQueue)
                    .runnerCount(runnerCount).handler(method).handlerInstance(handlerInstance).build();
            subProcesses.put(name, info);
        }
    }

    private void checkQueueName(String queueName) {
        if (StringUtils.isEmpty(queueName)) {
            return;
        }
        if (!dataQueues.containsKey(queueName)) {
            throw new PipedProcessWorkflowException(String.format("Registering a sub-process on a non-existing queue %s", queueName));
        }
    }

    private boolean isValidHandlerMethod(Method method) {
        // Handler method should contains annotation PipedProcessHandler
        if (!method.isAnnotationPresent(PipedProcessHandler.class)) {
            return false;
        }

        PipedProcessHandler annotation = method.getAnnotation(PipedProcessHandler.class);

        // Process name should not be empty
        if (StringUtils.isEmpty(annotation.process())) {
            return false;
        }

        // If no input queue, there should be no parameter in this method
        if (StringUtils.isEmpty(annotation.inputQueue()) && method.getParameterCount() > 0) {
            return false;
        }

        // If input queue exists, there should be only one parameter in this method
        if (StringUtils.isNotEmpty(annotation.inputQueue()) && method.getParameterCount() != 1) {
            return false;
        }

        // If output queue exists, the return type should not be void
        return !(StringUtils.isNotEmpty(annotation.outputQueue()) && Void.TYPE.equals(method.getReturnType()));
    }

    private void doProcessMethod(Object handlerObj, Method method) throws PipedProcessWorkflowException {
        PipedProcessHandler annotation = method.getAnnotation(PipedProcessHandler.class);
        if (StringUtils.isNotEmpty(annotation.inputQueue())) {
            addQueue(annotation.inputQueue(), method.getParameterTypes()[0]);
        }
        if (StringUtils.isNotEmpty(annotation.outputQueue())) {
            addQueue(annotation.outputQueue(), method.getReturnType());
        }
        Object handlerInstance = (handlerObj instanceof Class || Modifier.isStatic(method.getModifiers()))? null: handlerObj;
        registerSubProcess(annotation.process(), annotation.inputQueue(), annotation.outputQueue(), handlerInstance, method, annotation.runnerCount());
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
    private Method handler;
    private Object handlerInstance;
}
