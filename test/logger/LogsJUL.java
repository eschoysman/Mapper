package logger;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.logging.Handler;
import java.util.logging.Level;

public class LogsJUL {

    static {
        // format
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1tc] [JUL] [%4$-12s] [%2s] - %5s%n");
        // 1 - timestamp
        // 2 - classe e metodo invocatore
        // 3 - log name
        // 4 - level
        // 5 - messaggio (la vera "stampa")
        // 6 - eccezione?
    }

    @Test
    public void testLogs() {
        Logger LOGGER = LoggerFactory.getLogger(LogsJUL.class);
        setMinLevel();

        String LOG_PATTERN = "{} log message";

        LOGGER.trace(LOG_PATTERN,"Trace");
        LOGGER.debug(LOG_PATTERN,"Debug");
        LOGGER.info( LOG_PATTERN,"Info");
        LOGGER.warn( LOG_PATTERN,"Warning");
        LOGGER.error(LOG_PATTERN,"Error");
    }

    private void setMinLevel() {
        java.util.logging.Logger root = java.util.logging.Logger.getLogger("");
        root.setLevel(Level.ALL);
        for(Handler handler : root.getHandlers()) {
            handler.setLevel(Level.ALL);
        }
    }

}
