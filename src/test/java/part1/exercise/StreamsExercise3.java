package part1.exercise;

import com.google.common.io.Files;
import data.WNPResult;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class StreamsExercise3 {

    @Test
    public void warAndPeace() throws IOException {
      String result = Stream.of(
          Paths.get("WAP12.txt"),
          Paths.get("WAP34.txt"))
      .flatMap(p -> {
          try {
          return Files.readLines(p.toFile(),
                 Charset.forName("windows-1251")).stream();
          } catch (IOException e) {
          throw new RuntimeException(e);
          }})
      .flatMap(s -> Arrays.stream(s.split("[^а-яА-Яa-zA-Z]+")))
      .map(String::toLowerCase)
      .filter(s -> s.length() >= 4)
      .collect(Collectors.groupingBy(
          Function.identity(), Collectors.counting()))
      .entrySet()
      .stream()
      .filter(e -> e.getValue() > 9)
      .sorted((o1, o2) -> {
          long resultOfCompare = 0;
          resultOfCompare = - Long.compare(o1.getValue(), o2.getValue());
          if (resultOfCompare == 0) {
             resultOfCompare = o1.getKey().compareTo(o2.getKey());
          }
          return (int) resultOfCompare;
      })
      .map(p -> p.getKey() + " - " + p.getValue())
      .collect(Collectors.joining("\n"));

      assertEquals(new WNPResult().result, result);
    }
}
