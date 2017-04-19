package who.brianrok.pipedprocess.annotation;

import java.lang.annotation.*;

/**
 * Annotation for PipedProcess handler
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PipedProcessHandler {
    String process() ;
}
