package who.brianrok.pipedprocess.workflow;

import who.brianrok.pipedprocess.dataqueue.IPipedProcessDataQueue;
import who.brianrok.pipedprocess.exception.PipedProcessWorkflowException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Generator of callable process
 */
public class RunnerGenerator {

    static void generateStartData(IPipedProcessDataQueue output, Method method, Object instance,
                                         Object... args) {
        try {
            Object data = method.invoke(instance, args);
            output.put(data);
        } catch (IllegalAccessException | InterruptedException | InvocationTargetException e) {
            throw new PipedProcessWorkflowException("Error happened when generating start data");
        }
    }

    static void finishStartData(IPipedProcessDataQueue output) {
        try {
            output.finish();
        } catch (InterruptedException e) {
            throw new PipedProcessWorkflowException("Error happened when finishing starting queue");
        }
    }

    static Callable<Boolean> generateCallable(IPipedProcessDataQueue input, IPipedProcessDataQueue output, Method
            method, Object instance) {
        return () -> {
            if (input == null) {
                throw new PipedProcessWorkflowException("Should not generate callable for starting process");
            }
            while(true) {
                Optional<?> elem = input.take();
                if (!elem.isPresent()) {
                    break;
                }
                Object result = method.invoke(instance, input.getElementType().cast(elem.get()));
                output.put(output.getElementType().cast(result));
            }
            input.finish();
            return true;
        };
    }
}
