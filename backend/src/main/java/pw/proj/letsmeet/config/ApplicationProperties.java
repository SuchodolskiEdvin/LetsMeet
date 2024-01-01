package pw.proj.letsmeet.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class ApplicationProperties {

	@Value("${frontend.secret}")
	private String frontendSecret;

	@Value("${token.validity}")
	private int tokenValidity;

	@Value("${project.root}")
	private String projectRoot;

	@Value("${frontend.address}")
	private String frontendAddress;

	@Value("${api.generate}")
	private boolean apiGenerate;

	@Value("${zoom.api.key}")
	private String zoomApiKey;

	@Value("${zoom.api.secret}")
	private String zoomApiSecret;

	@Value("${zoom.account.id}")
	private String zoomAccountId;
}
