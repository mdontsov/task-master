import org.junit.Before;
import org.junit.Test;
import root.Task;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.*;

public class TaskTest {

    private Task task;
    private File propsFile;
    private File inputFile;
    private File outputFile;

    @Before
    public void setUp() {
        task = new Task();
        propsFile = new File("example.properties");
        inputFile = new File("input.xml");
        outputFile = new File("output.xml");
    }

    @Test
    public void sourceFileTest() {
        assertTrue(propsFile.exists());
    }

    @Test
    public void inputFileTest() {
        assertTrue(inputFile.exists());
    }

    @Test
    public void outputFileTest() {
        assertTrue(outputFile.exists());
    }

    @Test
    public void sourceFileNotFoundTest() {
        propsFile = new File("src/test/example.properties");
        assertFalse(propsFile.exists());
    }

    @Test
    public void inputFileNotFoundTest() {
        inputFile = new File("src/test/input.xml");
        assertFalse(inputFile.exists());
    }

    @Test
    public void outputFileNotFoundTest() {
        outputFile = new File("src/test/output.xml");
        assertFalse(outputFile.exists());
    }

    @Test
    public void fileTransformationTest() throws IOException {
        int originalPropsHash = propsFile.hashCode();
        int originalInputHash = inputFile.hashCode();
        int originalOutputHash = outputFile.hashCode();
        task.readSourceAndProcessData();
        int processedPropsHash = propsFile.hashCode();
        int processedInputHash = inputFile.hashCode();
        int processedOutputHash = outputFile.hashCode();
        assertNotSame(originalPropsHash, processedPropsHash);
        assertNotSame(originalInputHash, processedInputHash);
        assertNotSame(originalOutputHash, processedOutputHash);
    }

    @Test
    public void timestampTest() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        String timestamp = task.createTimeStamp();
        assertTrue(timestamp.matches(now.format(formatter)));
    }

    @Test
    public void UUIDTest() {
        String uuidPattern = String.valueOf(UUID.randomUUID());
        String uuid = task.createUUID();
        assertEquals(uuid.length(), uuidPattern.length());
    }

    @Test
    public void RequestIDTest() {
        Random r = new Random();
        long mostSigBits = r.nextLong();
        long leastSigBits = r.nextLong();
        String requestIDPattern = String.valueOf(new UUID(mostSigBits, leastSigBits));
        String requestID = task.createRequestID();
        assertEquals(requestID.length(), requestIDPattern.length());
    }

}