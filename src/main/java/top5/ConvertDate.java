package top5;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertDate {
	public static String convertTimestampToDate(long l) {
		Date date = new Date(l * 1000L);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String formattedDate = sdf.format(date);
		return formattedDate;
	}
}
