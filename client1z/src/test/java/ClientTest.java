import com.tiler.Client;
import com.tiler.Config;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

public class ClientTest {
    @Test
    public void testFormatLog() {
        Config config = new Config();
        Client client = new Client(config, "testUser");
        String message = "проверка сообщения";
        String logMessage = client.formatLog(message);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String expectedTime = now.format(formatter);
        String expectedLogMessage = String.format("[%s] %s%n", expectedTime, message);

        assertEquals(expectedLogMessage.trim(), logMessage.trim());
    }
}