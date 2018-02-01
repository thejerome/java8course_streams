package part1.exercise;

import data.Employee;
import data.Generator;
import data.JobHistoryEntry;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class StreamsExercise1 {
    // https://youtu.be/kxgo7Y4cdA8 Сергей Куксенко и Алексей Шипилёв — Через тернии к лямбдам, часть 1
    // https://youtu.be/JRBWBJ6S4aU Сергей Куксенко и Алексей Шипилёв — Через тернии к лямбдам, часть 2

    // https://youtu.be/O8oN4KSZEXE Сергей Куксенко — Stream API, часть 1
    // https://youtu.be/i0Jr2l3jrDA Сергей Куксенко — Stream API, часть 2

    @Test
    public void getAllEpamEmployees() {
        List<Employee> epamEmployees = Generator.generateEmployeeListWithEpamExperience()
                .stream()
                .filter(
                        e -> e.getJobHistory().stream()
                        .anyMatch(j -> "epam".equals(j.getEmployer()))
                )
                .collect(Collectors.toList());
        // TODO all persons with experience in epam


        epamEmployees.forEach(e -> assertTrue(
                        "employee doesn't have experience in Epam",
                        e.toString().contains("employer=epam")
                        )
                );
    }

    @Test
    public void getEmployeesStartedFromEpam() {
        List<Employee> epamEmployees = Generator.generateEmployeeListWithEpamExperience()
                .stream()
                .filter(
                        e -> "epam".equals(e.getJobHistory().iterator().next().getEmployer())
                )
                .collect(Collectors.toList());
        // TODO all persons with first experience in epam

        assertNotNull(epamEmployees);
        assertFalse(epamEmployees.isEmpty());

        for (Employee epamEmployee : epamEmployees) {
            assertEquals("epam", epamEmployee.getJobHistory().get(0).getEmployer());
        }
    }

    @Test
    public void sumEpamDurations() {
        final List<Employee> employees = Generator.generateEmployeeListWithEpamExperience();

        Integer expected = 0;

        for (Employee e : employees) {
            for (JobHistoryEntry j : e.getJobHistory()) {
                if (j.getEmployer().equals("epam")) {
                    expected += j.getDuration();
                }
            }
        }

         Integer result = employees.stream()
                 .flatMap(e -> e.getJobHistory().stream())
                 .filter(j -> "epam".equals(j.getEmployer()))
                 .mapToInt(JobHistoryEntry::getDuration)
                 .sum();//TODO sum of all durations in epam job histories
         assertEquals(expected, result);
    }

}
