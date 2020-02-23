package au.com.digio.task.parser;

import au.com.digio.task.parser.exeptions.ParseException;

import java.util.regex.Pattern;

public class ApacheLogParser {

    public static final int NUMBER_OF_FIELDS = 12;

    private static String  logEntryPattern = "^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(\\w+)\\s(.+)\\s(.+?)\" (\\d{3}) (\\d+) \"([^\"]+)\" \"([^\"]+)\"(.*)";
    private final  Pattern pattern;


    public ApacheLogParser() {
        this.pattern = Pattern.compile(logEntryPattern);
    }

    public LogEntry parse(String line) throws ParseException {
        var matcher = pattern.matcher(line);
        if (!matcher.matches() || NUMBER_OF_FIELDS != matcher.groupCount()) {
            throw new ParseException("Could not parse line.", line);
        }

        String ipAddress = matcher.group(1);
        String requestURL = matcher.group(6);

        return new LogEntry(ipAddress, requestURL);
    }

}
