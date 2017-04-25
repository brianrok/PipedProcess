package who.brianrok.pipedprocess.workflow;

import who.brianrok.pipedprocess.annotation.PipedProcessHandler;

public class NormalHandlerObj {

    @PipedProcessHandler(process = "first", outputQueue = "q1", runnerCount = 2)
    public static String first() {
        return "TEST";
    }

    @PipedProcessHandler(process = "second", inputQueue = "q1")
    public void second(String input) {}
}
