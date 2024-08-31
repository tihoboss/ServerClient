import com.tiler.ClientHandler;
import com.tiler.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.net.Socket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServerTest {
    private Server mockServer;
    private Socket mockSocket;
    private BufferedReader mockIn;
    private PrintWriter mockOut;
    private ClientHandler clientHandler;

    @BeforeEach
    public void setUp() throws IOException {
        mockServer = mock(Server.class);
        mockSocket = mock(Socket.class);
        mockIn = mock(BufferedReader.class);

        when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream("testUser\n".getBytes()));
        when(mockSocket.getOutputStream()).thenReturn(new ByteArrayOutputStream());

        clientHandler = new ClientHandler(mockServer, mockSocket);
        clientHandler.initConnection();
    }

    @Test
    public void testInitConnection() throws IOException {
        String username = "testUser";
        when(mockIn.readLine()).thenReturn(username);
        clientHandler.initConnection();
        Mockito.verify(mockServer).broadcast(username + " присоединился к чату.", clientHandler);
    }

    @Test
    public void testCloseConnection() throws IOException {
        clientHandler.closeConnection();
        Mockito.verify(mockServer).removeClient(clientHandler);
        Mockito.verify(mockServer).broadcast("testUser покинул чат.", clientHandler);
        Mockito.verify(mockSocket).close();
    }

    @Test
    public void testSendMessage() {
        clientHandler.sendMessage("Привет!");
        Assertions.assertDoesNotThrow(() -> clientHandler.sendMessage("Привет!"));
    }

    @Test
    public void testProcessMessages() throws IOException {
        String username = "testUser";
        String message = "Проверка сообщения";

        when(mockIn.readLine()).thenReturn(username).thenReturn(message);
        clientHandler.initConnection();
        clientHandler.processMessages();
        Mockito.verify(mockServer).broadcast(username + ": " + message, clientHandler);
    }

    @Test
    public void testProcessMessagesExit() throws IOException {
        String username = "testUser";
        when(mockIn.readLine()).thenReturn(username).thenReturn("/exit");

        clientHandler.initConnection();
        clientHandler.processMessages();
        clientHandler.closeConnection();
        Mockito.verify(mockServer).removeClient(clientHandler);
        Mockito.verify(mockServer).broadcast(username + " покинул чат.", clientHandler);
    }
}