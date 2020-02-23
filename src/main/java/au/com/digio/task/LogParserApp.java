package au.com.digio.task;

import au.com.digio.task.parser.ApacheLogParser;
import au.com.digio.task.parser.exeptions.ParseException;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.System.*;

public class LogParserApp {

    public static final int                  TOP_3           = 3;
    private             ApacheLogParser      apacheLogParser = new ApacheLogParser();
    private             Map<String, Integer> ipVisits        = new HashMap<>();
    private             Map<String, Integer> urlVisits       = new HashMap<>();

    public int getNumberOfUniqueIP() {
        return ipVisits.size();
    }

    public List<String> getMostActiveIP(int limit) {
        return getTopVisits(ipVisits, limit);
    }

    public List<String> getMostVisitedURLs(int limit) {
        return getTopVisits(urlVisits, limit);
    }

    public void parseAndBuildVisitMaps(BufferedReader reader) {
        reader.lines().forEach(line -> {
            try {
                var entry = apacheLogParser.parse(line);
                ipVisits.merge(entry.getIP(), 0, Integer::sum);
                urlVisits.merge(entry.getURL(), 0, Integer::sum);
            }
            catch (ParseException e) {
                err.printf("Could not parse log entry. The entry is skipped. Log: %s", e.getLogLine());
            }
        });
    }

    private List<String> getTopVisits(Map<String, Integer> map, int limit) {
        var sortedByValue = map.entrySet().stream()
                .sorted((v1, v2) -> Math.max(v1.getValue(), v2.getValue()));

        return sortedByValue
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        if (args == null || args.length < 1) {
            err.print("File required. Please specify file name.");
            return;
        }

        var name = args[0];
        var path = Paths.get(name);
        try {
            var parser = new LogParserApp();
            parser.parseAndBuildVisitMaps(Files.newBufferedReader(path));
            out.printf("Number of unique IP addresses: %d\n", parser.getNumberOfUniqueIP());
            out.printf("Top 3 most visited URLs: %s\n", String.join(", ", parser.getMostVisitedURLs(TOP_3)));
            out.printf("Top 3 most active IP addresses: %s\n", String.join(", ", parser.getMostActiveIP(TOP_3)));
        }
        catch (Exception e) {
            err.println("Cannot open file. Error: " + e.getLocalizedMessage());
        }
    }

}

