package au.com.digio.task;

import au.com.digio.task.parser.ApacheLogParser;
import au.com.digio.task.parser.exeptions.ParseException;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LogParserAppTest {

    private Path test1          = Paths.get("src", "test", "resources", "test1.log");
    private Path test2          = Paths.get("src", "test", "resources", "test2.log");
    private Path test3          = Paths.get("src", "test", "resources", "test3.log");
    private Path fileBadIP      = Paths.get("src", "test", "resources", "bad-ip.log");
    private Path fileMissingURL = Paths.get("src", "test", "resources", "missing-url.log");

    @Test
    public void testFile1() throws IOException {
        var parser = new LogParserApp();
        parser.parseAndBuildVisitMaps(Files.newBufferedReader(test1));
        assertEquals(2, parser.getNumberOfUniqueIP());

        List<String> expectedIPs = Arrays.asList("177.71.128.21", "168.41.191.40");
        List<String> actualIPs = parser.getMostActiveIP(2);
        assertEqualsWithoutOrder(expectedIPs, actualIPs);

        List<String> expectedAddresses = Arrays.asList("/intranet-analytics/", "http://example.net/faq/");
        List<String> actualAddresses = parser.getMostVisitedURLs(2);
        assertEqualsWithoutOrder(expectedAddresses, actualAddresses);
    }

    @Test
    public void testFile2() throws IOException {
        var parser = new LogParserApp();
        parser.parseAndBuildVisitMaps(Files.newBufferedReader(test2));
        assertEquals(2, parser.getNumberOfUniqueIP());

        List<String> expectedIPs = Arrays.asList("50.112.00.11", "72.44.32.10");
        List<String> actualIPs = parser.getMostActiveIP(2);
        assertEqualsWithoutOrder(expectedIPs, actualIPs);

        List<String> expectedAddresses = Arrays.asList("/hosting/", "/download/counter/", "/asset.css");
        List<String> actualAddresses = parser.getMostVisitedURLs(3);
        assertEqualsWithoutOrder(expectedAddresses, actualAddresses);
    }

    @Test
    public void testFile3NotApacheLines() throws IOException {
        var parser = new LogParserApp();
        parser.parseAndBuildVisitMaps(Files.newBufferedReader(test3));
        assertEquals(2, parser.getNumberOfUniqueIP());

        List<String> expectedIPs = Collections.singletonList("168.41.191.9");
        List<String> actualIPs = parser.getMostActiveIP(1);
        assertEqualsWithoutOrder(expectedIPs, actualIPs);

        List<String> expectedAddresses = Collections.singletonList("/docs/");
        List<String> actualAddresses = parser.getMostVisitedURLs(1);
        assertEqualsWithoutOrder(expectedAddresses, actualAddresses);
    }

    @Test(expected = ParseException.class)
    public void testFile4BadIP() throws IOException, ParseException {
        String line = Files.newBufferedReader(fileBadIP).lines().findFirst().orElseThrow();
        new ApacheLogParser().parse(line);
    }

    @Test(expected = ParseException.class)
    public void testFile5MissingURL() throws IOException, ParseException {
        String line = Files.newBufferedReader(fileMissingURL).lines().findFirst().orElseThrow();
        new ApacheLogParser().parse(line);
    }

    private void assertEqualsWithoutOrder(List<String> expected, List<String> actual) {
        Collections.sort(expected);
        Collections.sort(actual);
        assertEquals(expected, actual);
    }
}
