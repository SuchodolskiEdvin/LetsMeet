package pw.proj.letsmeet.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class ZoomMeetingRecurrenceDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer type;

	private Integer repeat_interval;

	private String weekly_days;

	private Integer monthly_day;

	private Integer monthly_week;

	private Integer monthly_week_day;

	private Integer end_times;

	private String end_date_time;

	@Override
	public String toString() {
		return "ZoomMeetingRecurrenceDTO [end_date_time=" + end_date_time + ", end_times=" + end_times
				+ ", monthly_day=" + monthly_day + ", monthly_week=" + monthly_week + ", monthly_week_day="
				+ monthly_week_day + ", repeat_interval=" + repeat_interval + ", type=" + type + ", weekly_days="
				+ weekly_days + "]";
	}

}