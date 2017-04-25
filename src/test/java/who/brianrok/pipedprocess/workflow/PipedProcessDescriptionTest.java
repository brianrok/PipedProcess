package who.brianrok.pipedprocess.workflow;

import org.junit.Test;
import who.brianrok.pipedprocess.exception.PipedProcessWorkflowException;

public class PipedProcessDescriptionTest {

    private PipedProcessDescription description = new PipedProcessDescription();

    @Test
    public void testNormalCase() {
        NormalHandlerObj handler = new NormalHandlerObj();
        description.register(handler);
    }

}

