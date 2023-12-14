package pw.proj.letsmeet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pw.proj.letsmeet.enums.SystemRole;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	private Long id;

	private String firstName;

	private String lastName;

	private String email;

	private SystemRole systemRole;

	private String password;

	public UserDTO (Long id, String email) {
		this.id = id;
		this.email = email;
	}
}
