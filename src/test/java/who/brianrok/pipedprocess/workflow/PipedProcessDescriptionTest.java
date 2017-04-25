package who.brianrok.pipedprocess.workflow;

import org.junit.Test;
import who.brianrok.pipedprocess.annotation.PipedProcessHandler;
import who.brianrok.pipedprocess.exception.PipedProcessWorkflowException;

import static org.junit.Assert.assertEquals;

public class PipedProcessDescriptionTest {

    private PipedProcessDescription description = new PipedProcessDescription();

    @Test
    public void testNormalCase() {
        class NormalHandlerObj {
            @PipedProcessHandler(process = "first", outputQueue = "q1", runnerCount = 2)
            public String first() {
                return "TEST";
            }

            @PipedProcessHandler(process = "second", inputQueue = "q1", outputQueue = "q2")
            public String second(String input) {
                return "TEST";
            }

            @PipedProcessHandler(process = "third", inputQueue = "q2")
            public void third(String input) {}
        }
        NormalHandlerObj handler = new NormalHandlerObj();
        description.register(handler);
        assertEquals(String.class, description.getDataQueues().get("q1").getElemClass());
        assertEquals("q1", description.getSubProcesses().get("first").getOutQueue());
        assertEquals("q1", description.getSubProcesses().get("second").getInQueue());
    }

    @Test (expected = PipedProcessWorkflowException.class)
    public void testAbnormalCase_loop() {
        class Loop {
            @PipedProcessHandler(process = "first", inputQueue = "q3", outputQueue = "q1", runnerCount = 2)
            public String first(String input) {
                return "TEST";
            }

            @PipedProcessHandler(process = "second", inputQueue = "q1", outputQueue = "q2")
            public String second(String input) {
                return "TEST";
            }

            @PipedProcessHandler(process = "third", inputQueue = "q2", outputQueue = "q3")
            public String third(String input) {
                return "TEST";
            }
        }
        description.register(new Loop());
    }

    @Test (expected = PipedProcessWorkflowException.class)
    public void testAbnormalCase_NoInputQueue() {
        class NoInput {
            @PipedProcessHandler (process = "test")
            public void test(String s) {}
        }
        description.register(new NoInput());
    }

    @Test (expected = PipedProcessWorkflowException.class)
    public void testAbnormalCase_VoidWithOutputQueue() {
        class Output {
            @PipedProcessHandler (process = "test", outputQueue = "q1")
            public void test() {}
        }
        description.register(new Output());
    }

    @Test (expected = PipedProcessWorkflowException.class)
    public void testAbnormalCase_MoreThanOneInputParameter() {
        class MoreThanOneParameter {
            @PipedProcessHandler(process = "test", inputQueue = "q2")
            public void test(String s, String s2) {}
        }
        description.register(new MoreThanOneParameter());
    }

}

