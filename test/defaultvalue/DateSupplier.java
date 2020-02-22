package defaultvalue;

import java.util.Calendar;
import java.util.Date;
import java.util.function.Supplier;

public class DateSupplier implements Supplier<Date> {

	@Override
	public Date get() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		cal.set(2019,Calendar.DECEMBER,24);
		return cal.getTime();
	}

}
