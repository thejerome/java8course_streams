package part1.exercise;

import com.google.common.io.Files;
import data.WNPResult;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
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
              .map(s -> s.split("[^а-яА-Яa-zA-Z]"))
              .flatMap(Arrays::stream)
              .map(String::toLowerCase)
              .filter(s -> s.length() >= 4)
              .collect(groupingBy(identity(), counting()))
              .entrySet()
              .stream()
              .filter(s -> s.getValue() >= 10)
              .sorted(((o1, o2) -> {
                  long res = - Long.compare(o1.getValue(), o2.getValue());
                  res = res == 0? o1.getKey().compareTo(o2.getKey()): res;
                  return (int)res;
              }))
              .map(e -> e.getKey() + " - " + e.getValue())
              .collect(joining("\n"));




        // TODO map lowercased words to its amount in text and concatenate its entries.
        // TODO If word "котик" occurred in text 23 times then its entry would be "котик - 23\n".
        // TODO Entries in final String should be also sorted by amount and then in alphabetical order if needed.
        // TODO Also omit any word with lengths less than 4 and frequency less than 10

        assertEquals(new WNPResult().result, result);
    }

}
