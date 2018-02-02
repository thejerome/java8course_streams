package part2.exercise;

import com.google.common.collect.ImmutableMap;
import data.Employee;
import data.JobHistoryEntry;
import data.Person;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectorsExercise1 {

    @Test
    public void testPersonToHisLongestJobDuration() {

        Map<Person, Integer> collected = getEmployees()
                .stream()
                .collect(Collectors
                        .toMap(
                                Employee::getPerson,
                                employee -> employee.getJobHistory()
                                        .stream()
                                        .map(JobHistoryEntry::getDuration)
                                        .reduce(Math::max)
                                        .orElse(0)
                        )
                );

        Map<Person, Integer> expected = ImmutableMap.<Person, Integer>builder()
                .put(new Person("John", "Galt", 20), 3)
                .put(new Person("John", "Doe", 21), 4)
                .put(new Person("John", "White", 22), 6)
                .put(new Person("John", "Galt", 23), 3)
                .put(new Person("John", "Doe", 24), 4)
                .put(new Person("John", "White", 25), 6)
                .put(new Person("John", "Galt", 26), 3)
                .put(new Person("Bob", "Doe", 27), 4)
                .put(new Person("John", "White", 28), 6)
                .put(new Person("John", "Galt", 29), 3)
                .put(new Person("John", "Doe", 30), 5)
                .put(new Person("Bob", "White", 31), 6)
                .build();
        Assert.assertEquals(expected, collected);
    }

    @Test
    public void testPersonToHisTotalJobDuration() {

        Map<Person, Integer> collected = getEmployees()
                .stream()
                .collect(Collectors.toMap(
                        Employee::getPerson,
                        e -> e.getJobHistory()
                                .stream()
                                .map(JobHistoryEntry::getDuration)
                                .reduce(Integer::sum)
                                .orElse(0)
                ));


        Map<Person, Integer> expected = ImmutableMap.<Person, Integer>builder()
                .put(new Person("John", "Galt", 20), 5)
                .put(new Person("John", "Doe", 21), 8)
                .put(new Person("John", "White", 22), 6)
                .put(new Person("John", "Galt", 23), 5)
                .put(new Person("John", "Doe", 24), 8)
                .put(new Person("John", "White", 25), 6)
                .put(new Person("John", "Galt", 26), 4)
                .put(new Person("Bob", "Doe", 27), 8)
                .put(new Person("John", "White", 28), 6)
                .put(new Person("John", "Galt", 29), 4)
                .put(new Person("John", "Doe", 30), 11)
                .put(new Person("Bob", "White", 31), 6)
                .build();

        Assert.assertEquals(expected, collected);
    }

    private class PersonWithJobHistory{

        private String person;
        private List<JobHistoryEntry> jobHistoryEntry;

        PersonWithJobHistory(String person, List<JobHistoryEntry> jobHistoryEntry) {
            this.person = person;
            this.jobHistoryEntry = jobHistoryEntry;
        }

        public String getPerson() {
            return person;
        }

        public void setPerson(String person) {
            this.person = person;
        }

        public List<JobHistoryEntry> getJobHistoryEntry() {
            return jobHistoryEntry;
        }

        public void setJobHistoryEntry(List<JobHistoryEntry> jobHistoryEntry) {
            this.jobHistoryEntry = jobHistoryEntry;
        }
    }

    @Test
    public void testTotalJobDurationPerNameAndSurname(){

        //Implement custom Collector
        Map<String, Integer> collected = getEmployees()
                .stream()
                .flatMap(employee -> Stream.of(
                        new PersonWithJobHistory(
                                employee.getPerson().getFirstName(),
                                employee.getJobHistory()),
                        new PersonWithJobHistory(
                                employee.getPerson().getLastName(),
                                employee.getJobHistory())
                        ))
                .collect(new Collector<PersonWithJobHistory, HashMap<String, Integer>, HashMap<String, Integer>>() {
                    @Override
                    public Supplier<HashMap<String, Integer>> supplier() {
                        return HashMap::new;
                    }

                    @Override
                    public BiConsumer<HashMap<String, Integer>, PersonWithJobHistory> accumulator() {
                        return (h, p) -> {
                            if(h.containsKey(p.person))
                                h.put(p.getPerson(), p.jobHistoryEntry
                                    .stream().mapToInt(JobHistoryEntry::getDuration).sum() + h.get(p.person));
                            else
                                h.put(p.getPerson(), p.jobHistoryEntry
                                        .stream().mapToInt(JobHistoryEntry::getDuration).sum());
                        };
                    }

                    @Override
                    public BinaryOperator<HashMap<String, Integer>> combiner() {
                        return (map1, map2) -> {
                            final HashMap<String, Integer> map = new HashMap<>(map2);
                            map1.forEach((key, value) -> map.merge(key, value, (v1, v2) -> v1 + v2));
                            return map;
                        };
                    }

                    @Override
                    public Function<HashMap<String, Integer>, HashMap<String, Integer>> finisher() {
                        return HashMap::new;
                    }

                    @Override
                    public Set<Characteristics> characteristics() {
                        return Collections.emptySet();
                    }
                });

        Map<String, Integer> expected = ImmutableMap.<String, Integer>builder()
                .put("John", 5 + 8 + 6 + 5 + 8 + 6 + 4 + 8 + 6 + 4 + 11 + 6 - 8 - 6)
                .put("Bob", 8 + 6)
                .put("Galt", 5 + 5 + 4 + 4)
                .put("Doe", 8 + 8 + 8 + 11)
                .put("White", 6 + 6 + 6 + 6)
                .build();

        Assert.assertEquals(expected, collected);
    }

    private List<Employee> getEmployees() {
        return Arrays.asList(
                new Employee(
                        new Person("John", "Galt", 20),
                        Arrays.asList(
                                new JobHistoryEntry(3, "dev", "epam"),
                                new JobHistoryEntry(2, "dev", "google")
                        )),
                new Employee(
                        new Person("John", "Doe", 21),
                        Arrays.asList(
                                new JobHistoryEntry(4, "BA", "yandex"),
                                new JobHistoryEntry(2, "QA", "epam"),
                                new JobHistoryEntry(2, "dev", "abc")
                        )),
                new Employee(
                        new Person("John", "White", 22),
                        Collections.singletonList(
                                new JobHistoryEntry(6, "QA", "epam")
                        )),
                new Employee(
                        new Person("John", "Galt", 23),
                        Arrays.asList(
                                new JobHistoryEntry(3, "dev", "epam"),
                                new JobHistoryEntry(2, "dev", "google")
                        )),
                new Employee(
                        new Person("John", "Doe", 24),
                        Arrays.asList(
                                new JobHistoryEntry(4, "QA", "yandex"),
                                new JobHistoryEntry(2, "BA", "epam"),
                                new JobHistoryEntry(2, "dev", "abc")
                        )),
                new Employee(
                        new Person("John", "White", 25),
                        Collections.singletonList(
                                new JobHistoryEntry(6, "QA", "epam")
                        )),
                new Employee(
                        new Person("John", "Galt", 26),
                        Arrays.asList(
                                new JobHistoryEntry(3, "dev", "epam"),
                                new JobHistoryEntry(1, "dev", "google")
                        )),
                new Employee(
                        new Person("Bob", "Doe", 27),
                        Arrays.asList(
                                new JobHistoryEntry(4, "QA", "yandex"),
                                new JobHistoryEntry(2, "QA", "epam"),
                                new JobHistoryEntry(2, "dev", "abc")
                        )),
                new Employee(
                        new Person("John", "White", 28),
                        Collections.singletonList(
                                new JobHistoryEntry(6, "BA", "epam")
                        )),
                new Employee(
                        new Person("John", "Galt", 29),
                        Arrays.asList(
                                new JobHistoryEntry(3, "dev", "epam"),
                                new JobHistoryEntry(1, "dev", "google")
                        )),
                new Employee(
                        new Person("John", "Doe", 30),
                        Arrays.asList(
                                new JobHistoryEntry(4, "QA", "yandex"),
                                new JobHistoryEntry(2, "QA", "epam"),
                                new JobHistoryEntry(5, "dev", "abc")
                        )),
                new Employee(
                        new Person("Bob", "White", 31),
                        Collections.singletonList(
                                new JobHistoryEntry(6, "QA", "epam")
                        ))
        );
    }

}
