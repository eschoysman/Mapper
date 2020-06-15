package logger;

import es.utils.mapper.Mapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.logging.Handler;
import java.util.logging.Level;

public class Logs {

    private static final Logger LOGGER = LoggerFactory.getLogger(Mapper.class);

    private static final String LOG_PATTERN = "{} log message";

    @Test
    public void testLogs() {
        LOGGER.trace(LOG_PATTERN,"Trace");
        LOGGER.debug(LOG_PATTERN,"Debug");
        LOGGER.info( LOG_PATTERN,"Info");
        LOGGER.warn( LOG_PATTERN,"Warning");
        LOGGER.error(LOG_PATTERN,"Error");
    }

}
