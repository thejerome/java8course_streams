package part1.exercise;

import com.google.common.io.Files;
import data.WNPResult;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class StreamsExercise3 {

    @Test
    public void warAndPeace() throws IOException {
        String result = Stream.of(
                Paths.get("WAP12.txt"),
                Paths.get("WAP34.txt")
        )
                .flatMap(p -> {
                    try {
                        return Files.readLines(p.toFile(), Charset.forName("windows-1251")).stream();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }})
                .map(line -> line.split("[^а-яА-Яa-zA-Z]"))
                .flatMap(Arrays::stream)
                .filter(w -> w.length() >= 4)
                .map(String::toLowerCase)
                .collect(
                        HashMap<String, Integer>::new,
                        (map, w) -> map.put(w, map.computeIfAbsent(w, key -> (0)) + 1),
                        Map::putAll
                ).entrySet().stream()
                .filter( e -> e.getValue() >= 10)
                .sorted( (e1, e2) -> {
                    if(Integer.compare(e1.getValue(), e2.getValue()) == 0)
                        return e1.getKey().compareTo(e2.getKey());
                    else
                        return Long.compare(e2.getValue(), e1.getValue());
                })
                .map(entry -> entry.getKey() + " - " + entry.getValue())
                .collect(Collectors.joining("\n"));

        // TODO map lowercased words to its amount in text and concatenate its entries.
        // TODO If word "котик" occurred in text 23 times then its entry would be "котик - 23\n".
        // TODO Entries in final String should be also sorted by amount and then in alphabetical order if needed.
        // TODO Also omit any word with lengths less than 4 and frequency less than 10

        assertEquals(new WNPResult().result, result);
    }

}
