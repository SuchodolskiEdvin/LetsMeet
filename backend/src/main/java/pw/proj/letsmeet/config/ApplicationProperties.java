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

	@Value("${backend.address}")
	private String backendAddress;

	@Value("${api.generate}")
	private boolean apiGenerate;

	@Value("${zoom.account.id}")
	private String zoomAccountId;

	@Value("${zoom.client.id}")
	private String zoomClientId;

	@Value("${zoom.client.secret")
	private String zoomClientSecret;
}
