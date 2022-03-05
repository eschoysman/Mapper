package logger;

import es.utils.mapper.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class Logs {

    private static final String LOG_PATTERN = "{} log message";

    @Test
    public void testLogs() {
        log.trace(LOG_PATTERN,"Trace");
        log.debug(LOG_PATTERN,"Debug");
        log.info( LOG_PATTERN,"Info");
        log.warn( LOG_PATTERN,"Warning");
        log.error(LOG_PATTERN,"Error");
    }

}
