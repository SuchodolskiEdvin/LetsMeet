package pw.proj.letsmeet.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
public class ZoomTokenDTO {
	@JsonProperty(value = "access_token")
	private String accessToken;

	@JsonProperty(value = "token_type")
	private String tokenType;

	@JsonProperty(value = "expires_in")
	private Long expiresIn;

	@JsonProperty(value = "scope")
	private String scope;

	@JsonIgnore
	private Date expirationDate;
}
