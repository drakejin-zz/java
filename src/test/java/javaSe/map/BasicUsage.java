package javaSe.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BasicUsage {
	@Test
	public void iteratingOverMap() throws Exception {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(0, "Ala");
		map.put(1, "John");
		map.put(2, "Ela");

		int i = 0;
		for (Map.Entry<Integer, String> entry : map.entrySet()) {
			assertThat(entry.getKey(), is(i));
			i++;
		}
	}

	@Test
	public void howToStringOnMapWorks() throws Exception {
		Map<String, Double> items = new HashMap<>();
		items.put("cherry", 2.32);
		items.put("orange", 4.21);
		items.put("apple", 0.90);

		System.out.println(items.toString());
		assertThat(items.toString(), equalTo("{orange=4.21, cherry=2.32, apple=0.9}"));

		List<String> itemList = new ArrayList<>();
		itemList.add("cherry");
		itemList.add("orange");
		itemList.add("apple");

		System.out.println(itemList.toString());
		assertThat(itemList.toString(), equalTo("[cherry, orange, apple]"));
	}
}
