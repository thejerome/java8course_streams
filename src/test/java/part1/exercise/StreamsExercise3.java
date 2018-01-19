package part1.exercise;

import data.WNPResult;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import static java.nio.file.Files.lines;
import static org.junit.Assert.assertEquals;

public class StreamsExercise3 {

    @Test
    public void warAndPeace() throws IOException {
        final Stream<Path> pathStream = Stream.of(
                Paths.get("WAP12.txt"),
                Paths.get("WAP34.txt")
        );


        String result = pathStream
                .flatMap(p -> {
                    try {
                        return lines(p);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(String::toLowerCase)
                .flatMap(s -> Arrays.stream(s.split("[^\\p{javaLowerCase}]+")))
                .filter(s -> s.length() >= 4)
                .collect(
                        HashMap<String, Integer>::new,
                        (m, k) -> m.put(k, m.containsKey(k) ? m.get(k) + 1 : 1),
                        HashMap::putAll)
                .entrySet().stream()
                .filter(e -> e.getValue() >= 10)
                .sorted((e1, e2) -> {
                    int res = Integer.compare(e2.getValue(), e1.getValue());
                    if (res == 0) {
                        return e1.getKey().compareTo(e2.getKey());
                    }
                    return res;
                })
                .map(e -> e.getKey() + " - " + e.getValue())
                .collect(Collectors.joining("\n"));

                // TODO map lowercased words to its amount in text and concatenate its entries.
                // TODO If word "котик" occurred in text 23 times then its entry would be "котик - 23\n".
                // TODO Entries in final String should be also sorted by amount and then in alphabetical order if needed.
                // TODO Also omit any word with lengths less than 4 and frequency less than 10

                assertEquals(new WNPResult().result, result);
    }

}
