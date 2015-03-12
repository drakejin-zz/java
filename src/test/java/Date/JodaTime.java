package Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class JodaTime {

	@Before
	public void setUp() throws Exception {
		//It is needed for tests. Thanks to that you can compare milliseconds.
		DateTimeUtils.setCurrentMillisFixed(System.currentTimeMillis());
	}

	@After
	public void tearDown() throws Exception {
		restoreSystemMilliseconds();
	}

	private void restoreSystemMilliseconds() {
		DateTimeUtils.setCurrentMillisSystem();
	}

	@Test
    public void basicUsage() throws Exception {
        DateTime now = new DateTime(DateTimeZone.UTC);
        DateTime in10Minutes = now.plusMinutes(10);
		System.out.println("Date in 10 minutes :" + in10Minutes);
        assertFalse(areTwoDaysEqualsWithSecondsAccuracy(in10Minutes, now));
    }

	@Test
	public void daysBetweenDates() throws Exception {
		DateTime jareksBirthday = new DateTime().withDate(1986, 3, 6);
		DateTime ewasBirthday = new DateTime().withDate(1989, 9, 14);

		assertThat(Days.daysBetween(jareksBirthday, ewasBirthday).getDays(), is(1288));

	}

	private boolean areTwoDaysEqualsWithSecondsAccuracy(DateTime expectedDate, DateTime testedDate) {

		return (expectedDate.getYear() == testedDate.getYear())
				&& (expectedDate.getMonthOfYear() == testedDate.getMonthOfYear())
				&& (expectedDate.getDayOfMonth() == testedDate.getDayOfMonth())
				&& (expectedDate.getHourOfDay() == testedDate.getHourOfDay())
				&& (expectedDate.getMinuteOfHour() == testedDate.getMinuteOfHour())
				&& (expectedDate.getSecondOfMinute() == testedDate.getSecondOfMinute())
				&& (expectedDate.getMillisOfSecond() == testedDate.getMillisOfSecond()) ;
	}

}
