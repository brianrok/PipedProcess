package who.brianrok.pipedprocess.dataqueue;

import org.junit.Test;
import who.brianrok.pipedprocess.exception.DataQueueException;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataQueueManagerImplTest {

    private IDataQueueManager queueManager = new DataQueueManagerImpl();

    @Test
    public void testGetQueue() {
        queueManager.registerDataQueue("TEST");
        IPipedProcessDataQueue queue = queueManager.getDataQueue("TEST");
        assertNotNull(queue);
    }

    @Test (expected = DataQueueException.class)
    public void testRegisterExistingQueue() {
        queueManager.registerDataQueue("TEST");
        queueManager.registerDataQueue("TEST");
    }

    @Test
    public void testRemoveQueue() throws InterruptedException {
        queueManager.registerDataQueue("TEST");
        queueManager.getDataQueue("TEST").finish();
        queueManager.removeDataQueue("TEST");
    }

    @Test (expected = DataQueueException.class)
    public void testRemoveUnfinishedQueue() {
        queueManager.registerDataQueue("TEST");
        queueManager.removeDataQueue("TEST");
    }

    @Test
    public void testGetAllQueueNames() {
        queueManager.registerDataQueue("TEST");
        Set<String> names = queueManager.getAllQueueNames();
        assertEquals(1, names.size());
    }
}
