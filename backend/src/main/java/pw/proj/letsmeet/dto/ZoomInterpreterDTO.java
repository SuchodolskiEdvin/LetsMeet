package pw.proj.letsmeet.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class ZoomInterpreterDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	public String email;

	public String languages;

	@Override
	public String toString() {
		return "ZoomInterpreterDTO [email=" + getEmail() + ", languages=" + getLanguages() + "]";
	}

}