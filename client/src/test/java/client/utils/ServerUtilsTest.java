package client.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerUtilsTest {

    @Test
    void setServerURL() {
        ServerUtils s = new ServerUtils();

        s.setServerURL("asd");
        assertEquals("asd", s.getServerURL());
    }

    @Test
    void setServerURLInvalid() {
        ServerUtils s = new ServerUtils();

        s.setServerURL("");
        assertEquals(ServerUtils.getDefaultURL(), s.getServerURL());
    }
}