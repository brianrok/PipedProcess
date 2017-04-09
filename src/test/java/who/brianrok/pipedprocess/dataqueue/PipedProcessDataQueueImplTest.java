package who.brianrok.pipedprocess.dataqueue;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PipedProcessDataQueueImplTest {

    private IPipedProcessDataQueue queue = new PipedProcessDataQueueImpl(String.class, 10);

    @Test
    public void testOperation() throws InterruptedException {
        queue.put("1");
        queue.put("2");
        assertEquals("1", queue.take());
        assertFalse(queue.isFinished());
    }

    @Test
    public void testFinish() throws InterruptedException {
        queue.finish();
        assertEquals(9, queue.remainingCapacity());
        assertTrue(queue.isFinished());
    }
}
