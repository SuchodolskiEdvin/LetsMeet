package pw.proj.letsmeet.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class ZoomMeetingsListResponseDTO implements Serializable {

	private static final long serialVersionUID = -218290644483495371L;

	private Integer page_size;

	private Integer page_number;

	private Integer page_count;

	public Integer total_records;

	public String next_page_token;

	public List<ZoomMeetingObjectDTO> meetings;

	@Override
	public String toString() {
		return "ZoomMeetingsListResponseDTO [meetings=" + meetings + ", next_page_token=" + next_page_token
				+ ", page_count=" + page_count + ", page_number=" + page_number + ", page_size=" + page_size
				+ ", total_records=" + total_records + "]";
	}
}
