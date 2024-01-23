package utils;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// https://www.baeldung.com/junit-asserting-logs
public class MemoryAppender extends ListAppender<ILoggingEvent> {

	//region custom methods
	public void init(Class<?> loggerClass) {
		Logger logger = (Logger) LoggerFactory.getLogger(loggerClass);
		this.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
		logger.setLevel(ch.qos.logback.classic.Level.DEBUG);
		logger.addAppender(this);
		start();
	}
	public boolean contains(String string, org.slf4j.event.Level level) {
		return contains(string,Level.convertAnSLF4JLevel(level));
	}
	public void end() {
		reset();
		stop();
	}
	//endregion

	public void reset() {
		this.list.clear();
	}

	public boolean contains(String string, Level level) {
		return this.list.stream()
				.anyMatch(event -> event.toString().contains(string)
						&& event.getLevel().equals(level));
	}

	public int countEventsForLogger(String loggerName) {
		return (int) this.list.stream()
				.filter(event -> event.getLoggerName().contains(loggerName))
				.count();
	}

	public List<ILoggingEvent> search(String string) {
		return this.list.stream()
				.filter(event -> event.toString().contains(string))
				.collect(Collectors.toList());
	}

	public List<ILoggingEvent> search(String string, Level level) {
		return this.list.stream()
				.filter(event -> event.toString().contains(string)
						&& event.getLevel().equals(level))
				.collect(Collectors.toList());
	}

	public int getSize() {
		return this.list.size();
	}

	public List<ILoggingEvent> getLoggedEvents() {
		return Collections.unmodifiableList(this.list);
	}

}
