package who.brianrok.pipedprocess.exception;

/**
 * Exception happened in workflow
 */
public class PipedProcessWorkflowException extends RuntimeException {

    public PipedProcessWorkflowException() {
        super();
    }

    public PipedProcessWorkflowException(String msg) {
        super(msg);
    }
}
