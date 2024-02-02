package pw.proj.letsmeet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeetDTO {

	private Long id;

	private String name;

	private LocalDate date;

	private String timeStart;

	private String timeEnd;

	private UserDTO creator;

	private List<UserDTO> participants;

	private List<Long> participantsId;
}
