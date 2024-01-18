package pw.proj.letsmeet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class ZoomTokenDTO {
	@JsonProperty(value = "authorization_token")
	private String authorizationToken;

	@JsonProperty(value = "access_token")
	private String accessToken;

	@JsonProperty(value = "token_type")
	private String tokenType;

	@JsonProperty(value = "expires_in")
	private Long expiresIn;

	private String scope;
}
