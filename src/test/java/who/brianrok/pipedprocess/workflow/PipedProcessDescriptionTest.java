package who.brianrok.pipedprocess.workflow;

import org.junit.Test;
import who.brianrok.pipedprocess.exception.PipedProcessWorkflowException;

public class PipedProcessDescriptionTest {

    private PipedProcessDescription description = new PipedProcessDescription();

    @Test (expected = PipedProcessWorkflowException.class)
    public void testAddQueue() {
        description.addQueue("test", Object.class);
        description.addQueue("test", Object.class);
    }

    @Test (expected = PipedProcessWorkflowException.class)
    public void testRegisterSubProcess() {
        description.registerSubProcess("test", null, null);
        description.registerSubProcess("test", null, null);
    }
}
