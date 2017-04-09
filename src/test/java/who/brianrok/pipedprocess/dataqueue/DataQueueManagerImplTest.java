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
        queueManager.registerDataQueue("TEST", String.class);
        IPipedProcessDataQueue queue = queueManager.getDataQueue("TEST");
        assertNotNull(queue);
    }

    @Test (expected = DataQueueException.class)
    public void testRegisterExistingQueue() {
        queueManager.registerDataQueue("TEST", String.class);
        queueManager.registerDataQueue("TEST", String.class);
    }

    @Test
    public void testRemoveQueue() throws InterruptedException {
        queueManager.registerDataQueue("TEST", String.class);
        queueManager.getDataQueue("TEST").finish();
        queueManager.removeDataQueue("TEST");
    }

    @Test (expected = DataQueueException.class)
    public void testRemoveUnfinishedQueue() {
        queueManager.registerDataQueue("TEST", String.class);
        queueManager.removeDataQueue("TEST");
    }

    @Test
    public void testGetAllQueueNames() {
        queueManager.registerDataQueue("TEST", String.class);
        Set<String> names = queueManager.getAllQueueNames();
        assertEquals(1, names.size());
    }
}
