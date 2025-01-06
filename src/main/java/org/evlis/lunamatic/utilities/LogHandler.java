package org.evlis.lunamatic.utilities;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LogHandler extends ConsoleHandler {
    public static final String CON_YELLOW = "\033[38;5;220m [Lunamatic] ";
    public static final String CON_RESET = "\033[0m";

    public LogHandler() {
        setFormatter(new LogFormatter());
    }
}
