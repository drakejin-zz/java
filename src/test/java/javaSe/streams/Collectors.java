package javaSe.streams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collector;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Collectors {
	@Test
	public void streamCanBeStoredInOtherCollection() throws Exception {
		Map<String, Integer> persons = new HashMap<>();
		persons.put("Max", 18);
		persons.put("Peter", 35);
		persons.put("Pamela", 33);
		persons.put("Ewa", 24);
		persons.put("Jarek", 29);

		Map<String, Integer> filteredPersons =
				persons.entrySet().stream()
						.filter(e -> e.getKey().startsWith("P"))
						.collect(java.util.stream.Collectors.toMap(e -> e.getKey(), Map.Entry::getValue));

		assertThat(filteredPersons.toString(), is("{Pamela=33, Peter=35}"));
	}
	private class Person {
		Integer age;
		String name;
		public Person(String name, Integer age) {
			this.name = name;
			this.age = age;
		}

		@Override
		public String toString() {
			return "Person{" +
					"age=" + age +
					", name='" + name + '\'' +
					'}';
		}
	};

	@Test
	public void streamCanBeConvertedToOtherTypeCollection() throws Exception {
		List<Person> persons = new ArrayList<>();
		persons.add(new Person("Max", 18));
		persons.add(new Person("Peter", 35));
		persons.add(new Person("Pamela", 33));
		persons.add(new Person("Ewa", 24));
		persons.add(new Person("Jarek", 29));

		Map<Integer, List<Person>> personsByAge = persons
				.stream()
				.filter(p -> p.age >= 29)
				.collect(java.util.stream.Collectors.groupingBy(p -> p.age));

		assertThat(personsByAge.toString(),
				is("{33=[Person{age=33, name='Pamela'}], 35=[Person{age=35, name='Peter'}], 29=[Person{age=29, name='Jarek'}]}"));

		Double averageAge = persons
				.stream()
				.collect(java.util.stream.Collectors.averagingInt(p -> p.age));

		assertThat(averageAge, is(27.8));
	}

	@Test
	public void moreSofisticateStatisticsCanBeCounted() throws Exception {
		List<Person> persons = new ArrayList<>();
		persons.add(new Person("Max", 18));
		persons.add(new Person("Peter", 35));
		persons.add(new Person("Pamela", 33));
		persons.add(new Person("Ewa", 24));
		persons.add(new Person("Jarek", 29));

		IntSummaryStatistics ageSummary =
				persons
						.stream()
						.collect(java.util.stream.Collectors.summarizingInt(p -> p.age));

		assertThat(ageSummary.toString(), is("IntSummaryStatistics{count=5, sum=139, min=18, average=27.800000, max=35}"));
	}

	@Test
	public void joiningCollectors() throws Exception {
		List<Person> persons = new ArrayList<>();
		persons.add(new Person("Max", 18));
		persons.add(new Person("Peter", 35));
		persons.add(new Person("Pamela", 33));
		persons.add(new Person("Ewa", 24));
		persons.add(new Person("Jarek", 29));

		String phrase = persons
				.stream()
				.filter(p -> p.age >= 30)
				.map(p -> p.name)
				.collect(java.util.stream.Collectors.joining(" and ", "In Germany ", " are of legal age."));

		System.out.println(phrase);
	}

	@Test
	public void convertToMapWithTheSameKeyHandling() throws Exception {
		List<Person> persons = new ArrayList<>();
		persons.add(new Person("Max", 18));
		persons.add(new Person("Peter", 35));
		persons.add(new Person("Pamela", 35));
		persons.add(new Person("Ewa", 24));
		persons.add(new Person("Jarek", 29));

//		Keep in mind that the mapped keys must be unique, otherwise an IllegalStateException is thrown.
//		You can optionally pass a merge function as an additional parameter to bypass the exception
		Map<Integer, String> map = persons
				.stream()
				.collect(java.util.stream.Collectors.toMap(
						p -> p.age,
						p -> p.name,
						(name1, name2) -> name1 + ";" + name2));

		assertThat(map.toString(), is("{18=Max, 35=Peter;Pamela, 24=Ewa, 29=Jarek}"));
	}

	@Test
	public void customCollectorCanBeDefined() throws Exception {
		List<Person> persons = new ArrayList<>();
		persons.add(new Person("Max", 18));
		persons.add(new Person("Peter", 35));
		persons.add(new Person("Pamela", 35));
		persons.add(new Person("Ewa", 24));
		persons.add(new Person("Jarek", 29));

		Collector<Person, StringJoiner, String> personNameCollector =
				Collector.of(
						() -> {
							System.out.println("step 1");
							return new StringJoiner(" | ");
						},          // supplier
						(j, p) -> {
							System.out.println("step 2" + p.name);
							j.add(p.name.toUpperCase());
						},  // accumulator
						(j1, j2) -> { // step 3 is not performed !!
							System.out.println("step 3" + "j1: "+ j1.toString() + "j2:" + j2.toString() );
							return j1.merge(j2);
						},               // combiner
						(java.util.function.Function<StringJoiner, String>) (stringJoiner) -> {
							System.out.println("step 4");
							return stringJoiner.toString();
						});                // finisher

		String names = persons
				.stream()
				.collect(personNameCollector);

		assertThat(names, is("MAX | PETER | PAMELA | EWA | JAREK"));

	}
}
