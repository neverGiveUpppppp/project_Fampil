package test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestLocalDate {

	@Test
	public void dateList() {
		LocalDate now = LocalDate.now().minusDays(7);
		
		List<NutrientsDate> dates = new ArrayList<>();
		
		dates.add(getDate(now));
		
		for (int i = 1; i <= 7; i++) {
			now = now.plusDays(1);
			NutrientsDate date = getDate(now);
			// 현재일자 표시
			if (i == 7) {
				date.setNow(true);
			}
			dates.add(date);
		}
		log.debug("dates : {}", dates);
	}
	
	private NutrientsDate getDate(LocalDate localDate) {
		NutrientsDate date = new NutrientsDate();
		date.setDateLabel(localDate.format(DateTimeFormatter.ofPattern("M월 dd일")));
		date.setDate(localDate.format(DateTimeFormatter.BASIC_ISO_DATE));
		date.setWeek(localDate.getDayOfWeek().name());
		date.setWeekLabel(localDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN));
		return date;
	}
}
