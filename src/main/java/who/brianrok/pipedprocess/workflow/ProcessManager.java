package who.brianrok.pipedprocess.workflow;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Manager of process runners
 */
public class ProcessManager {

    private Map<String, Callable<?>> callableMap;
    private Map<String, List<Future>> futureMap;

    public ProcessManager() {
        callableMap = new HashMap<>();
        futureMap = new HashMap<>();
    }

    public void registerRunner(String process, Method method, Object instant) {

    }
}
