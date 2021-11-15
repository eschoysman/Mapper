package es.utils.mapper.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static es.utils.mapper.logger.LogConstant.LEVEL;
import static es.utils.mapper.logger.LogConstant.TYPE;

public class MapperLogger {

    private Logger log;

    public static Map<TYPE,LEVEL> enabled = new HashMap<>();

    public static <T> MapperLogger forType(Class<T> type) {
        return new MapperLogger(type);
    }

	private MapperLogger(Class<?> type) {
	    this.log = LoggerFactory.getLogger(type);
	}

    public void error(LogConstant logType, String format, Object... arguments) {
        if(check(logType)) {
            log.error(format, arguments);
        }
    }
    public void error(LogConstant logType, String format, Throwable throwable) {
        if(check(logType)) {
            log.error(format, throwable);
        }
    }

    public void warning(LogConstant logType, String format, Object... arguments) {
        if(check(logType)) {
            log.warn(format, arguments);
        }
    }
    public void warning(LogConstant logType, String format, Throwable throwable) {
        if(check(logType)) {
            log.warn(format, throwable);
        }
    }

    public void info(LogConstant logType, String format, Object... arguments) {
        if(check(logType)) {
            log.info(format, arguments);
        }
    }
    public void info(LogConstant logType, String format, Throwable throwable) {
        if(check(logType)) {
            log.info(format, throwable);
        }
    }

    public void debug(LogConstant logType, String format, Object... arguments) {
        if(check(logType)) {
            log.debug(format, arguments);
        }
    }
    public void debug(LogConstant logType, String format, Throwable throwable) {
        if(check(logType)) {
            log.debug(format, throwable);
        }
    }

    public void trace(LogConstant logType, String format, Object... arguments) {
        if(check(logType)) {
            log.trace(format, arguments);
        }
    }
    public void trace(LogConstant logType, String format, Throwable throwable) {
        if(check(logType)) {
            log.trace(format, throwable);
        }
    }
    
    private boolean check(LogConstant logConstant) {
        return enabled.containsKey(logConstant.getType())
                && enabled.get(logConstant.getType()).getPrority() >= logConstant.getLevel().getPrority();
    }

}
