package part1.exercise;

import data.Employee;
import data.JobHistoryEntry;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static data.Generator.generateEmployeeList;
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
        List<Employee> epamEmployees = null;
        // TODO all persons with experience in epam
        // assuming we get only employees
        // whose career was in epam exclusively
        epamEmployees = generateEmployeeList().stream()
                .filter(
                        empl -> empl.getJobHistory().stream()
                                .allMatch(
                                        job -> "epam".equals(job.getEmployer())
                                )
                )
                .collect(Collectors.toList());


        epamEmployees.forEach(e -> assertTrue(
                        "employee doesn't have experience in Epam",
                        e.toString().contains("employer=epam")
                        )
                );
    }

    @Test
    public void getEmployeesStartedFromEpam() {
        List<Employee> epamEmployees = null;
        // TODO all persons with first experience in epam
        // assuming we get employee-entities,
        // not person-entities as in TODO
        epamEmployees = generateEmployeeList().stream()
                .filter(emp -> !emp.getJobHistory().isEmpty())
                .filter(
                        emp -> "epam".equals(
                                emp.getJobHistory().get(0).getEmployer()
                        )
                )
                .collect(Collectors.toList());

        assertNotNull(epamEmployees);
        assertFalse(epamEmployees.isEmpty());

        for (Employee epamEmployee : epamEmployees) {
            assertEquals("epam", epamEmployee.getJobHistory().get(0).getEmployer());
        }
    }

    @Test
    public void sumEpamDurations() {
        final List<Employee> employees = generateEmployeeList();

        Integer expected = 0;

        for (Employee e : employees) {
            for (JobHistoryEntry j : e.getJobHistory()) {
                if (j.getEmployer().equals("epam")) {
                    expected += j.getDuration();
                }
            }
        }

         Integer result = null;//TODO sum of all durations in epam job histories
        result = employees.stream()
                .flatMap(empl -> empl.getJobHistory().stream())
                .filter(job -> "epam".equals(job.getEmployer()))
                .map(JobHistoryEntry::getDuration)
                .reduce(0, Integer::sum);
         assertEquals(expected, result);
    }

}
