package pw.proj.letsmeet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeetDTO {

	private Long id;

	private LocalDate creationDate;

	private LocalDate modificationDate;

	private String name;

	private LocalDate date;

	private String time;

	private String creatorsFullName;

	private Set<UserDTO> participants;

	private List<Long> participantsId;
}
