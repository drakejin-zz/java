package javaSe.streams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class StreamsUsage {
	@Test
	public void testOperationPipelines() throws Exception {
		List<String> myList =
				Arrays.asList("a1", "a2", "b1", "c2", "c1");

		myList.stream() //operation pipeline
				.filter(s -> s.startsWith("c"))
					.map(String::toUpperCase)
						.sorted()
							.forEach(System.out::println);
	}

	@Test
	public void operationInStreamsAreChained() throws Exception {
		Stream.of("a1", "a2", "a3")
				.findFirst()
					.map(String::toUpperCase)
						.ifPresent(System.out::println /*This is Consumer*/);  // A1
	}

	@Test
	public void noExecutionWhenThereIsNoTerminateOperation() throws Exception {
		List<String> list = new ArrayList<String>();
		Stream.of("d2", "a2", "b1", "b3", "c")
				.filter(s -> {
					list.add(s);
					return true;
				});
		assertTrue(list.isEmpty());

		Stream.of("d2", "a2", "b1", "b3", "c")
				.filter(s -> {
					list.add(s);
					return true;
				}).count();

		assertFalse(list.isEmpty());

	}

	@Test
	public void canChangeStreamTypeDuringProcessing() throws Exception {
		assertThat(
		Stream.of("a1", "a2", "a3")
				.map(s -> s.substring(1))
				.mapToInt(Integer::parseInt)
				.max()
				,
				is(OptionalInt.of(3))
		);

		IntStream.range(1, 4)
				.mapToObj(i -> "a" + i)
				.forEach(System.out::println);
	}

	@Test
	public void averageAndSumOperations() throws Exception {
		assertThat(
		Arrays.stream(new int[] {1, 2, 3})
				.map(n -> 2 * n + 1)
				.average()
				.getAsDouble()
				,
				is(5.0)
		);

		assertThat(
				Arrays.stream(new int[]{1, 2, 3})
						.map(n -> 2 * n + 1)
						.sum()
				,
				is(15)
		);

	}

	@Test
	public void canReplaceLoop() throws Exception {
		List<Integer> list = new ArrayList<Integer>();

		IntStream.range(1, 4)
				.forEach(list::add);

		list.stream().forEach(System.out::println);
		assertThat(list.size(), is(3));
	}

	@Test
	public void testFilters() throws Exception {
		List<String> myList =
				Arrays.asList("a1", "a2", "b1", "c2", "c1");

		assertThat(myList.stream() //operation pipeline
				.filter(s -> s.startsWith("c"))
				.map(String::toUpperCase)
				.sorted()
				.count(), is(2L));

		assertThat(myList.size(), is(5));

		List<String> list = new ArrayList<>();
		Map<String, Integer> map = new HashMap<>();
		map.put("ID", 0);
		map.put("PASSENGER_TYPES", 1);
		map.put("PASSENGER", 2);

		map.entrySet().stream()
				.filter(entry -> entry.getKey() != "PASSENGER_TYPES")
				.forEach(entry -> list.add(entry.getKey()));

		assertThat(list.size(), is(2));
		list.stream().forEach(System.out::println);

	}

	@Test
	public void elementsAreStreamed_eachOneIsMovedInAChain() throws Exception {
		List<Boolean> list = new ArrayList<>();
		Stream.of("d2", "a2", "b1", "b3", "c")
				.filter(s -> {
					list.add(true);
					return true;
				})
				.forEach(s -> list.add(false));

		assertTrue(list.get(0));
		assertFalse(list.get(1));
		assertTrue(list.get(2));
		assertFalse(list.get(3));
		assertTrue(list.get(4));
		//and so on..

	}

	@Test
	public void ifAnyMatchReturnsTrue_elementsStopBeingProcessed() throws Exception {
		List<Boolean> list = new ArrayList<>();

		Stream.of("d2", "a2", "b1", "b3", "a2", "c")
				.map(s -> {
					list.add(true);
					System.out.println("map: " + s);
					return s.toUpperCase();
				})
				.anyMatch(s -> { //anyMatch
					System.out.println("anyMatch: " + s);
					return s.startsWith("A");
				});

		assertThat(list.size(), is(2));
	}

	@Test
	public void sortsArePerformedHorizontally() throws Exception {
		//sorts are performed on the entire input collection.
		List<Boolean> list = new ArrayList<>();

		Stream.of("d2", "a2", "b1", "b3", "c")
				.sorted((s1, s2) -> {
					list.add(true);
					System.out.printf("sort: %s; %s\n", s1, s2);
					return s1.compareTo(s2);
				})
				.filter(s -> {
					System.out.println("filter: " + s);
					return s.startsWith("a");
				})
				.map(s -> {
					System.out.println("map: " + s);
					return s.toUpperCase();
				})
				.forEach(s -> System.out.println("forEach: " + s));

		assertThat(list.size(), is(8));
	}

	@Test
	public void inLambdasObjectsCannotBeReassign() throws Exception {
		int numberOfInvocation = 0;
		final int[] finalNumberOfInvocation = {0};
		List<Boolean> list = new ArrayList<>();
		Stream.of(1, 2, 3, 4, 5)
				.forEach( s -> {
//							numberOfInvocation++; compilation ERROR
							finalNumberOfInvocation[0]++;
							list.add(true);
						}
				);
		assertThat(list.size(), is(5));
		assertThat(finalNumberOfInvocation[0], is(5));
	}

	@Test(expected = IllegalStateException.class)
	public void streamsCannotBeReused_CallingAnyTerminateOperationCloseTheStream() throws Exception {
		Stream<String> stream =
				Stream.of("d2", "a2", "b1", "b3", "c")
						.filter(s -> s.startsWith("a"));

		stream.anyMatch(s -> true);    // ok
		stream.noneMatch(s -> true);   // exception
	}

	@Test
	public void toReuseIntermediateOperation_SupplierNeedsToBeUsed() throws Exception {
		Supplier<Stream<String>> streamSupplier =
				() -> Stream.of("d2", "a2", "b1", "a3", "c")
						.filter(s -> s.startsWith("a"));

		streamSupplier.get().anyMatch(s -> true);   // ok
		streamSupplier.get().noneMatch(s -> true);  // ok

	}

	@Test
	public void NoneMatch_AnyMatch_BehaviorsIdentically() throws Exception {
		List<Boolean> list = new ArrayList<>();
		Stream.of("d2", "a2", "b1", "b3", "a2", "c")
				.map(s -> {
					list.add(true);
					System.out.println("map: " + s);
					return s;
				})
				.noneMatch(s -> {
					System.out.println("anyMatch: " + s);
					return s.startsWith("a2");
				});

		List<Boolean> list2 = new ArrayList<>();
		Stream.of("d2", "a2", "b1", "b3", "a2", "c")
				.map(s -> {
					list2.add(true);
					System.out.println("map: " + s);
					return s;
				})
				.anyMatch(s -> {
					System.out.println("anyMatch: " + s);
					return s.startsWith("a2");
				});
		assertThat(list, is(equalTo(list2)));
	}
}
