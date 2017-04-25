package who.brianrok.pipedprocess.annotation;

import java.lang.annotation.*;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Annotation for PipedProcess handler
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PipedProcessHandler {
    String process();
    String inputQueue() default EMPTY;
    String outputQueue() default EMPTY;
    int runnerCount() default 1;
}
