package pw.proj.letsmeet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	private Long id;

	private String firstName;

	private String lastName;

	private String fullName;

	private String email;

	private String password;

	public UserDTO (Long id, String email, String fullName) {
		this.id = id;
		this.email = email;
		this.fullName = fullName;
	}
}
