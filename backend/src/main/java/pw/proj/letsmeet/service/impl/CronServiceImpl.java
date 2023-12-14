package pw.proj.letsmeet.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pw.proj.letsmeet.config.ApplicationProperties;
import pw.proj.letsmeet.service.CronService;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Service
public class CronServiceImpl implements CronService {

	private static final Logger LOG = LogManager.getLogger(CronServiceImpl.class);

	@Autowired
	private ApplicationProperties applicationProperties;

	// 3 sekundy po starcie serwera
	@Scheduled(initialDelay = 3000, fixedDelay = 9999999999999L)
	public void generateSwaggerAPI() {
		if (!applicationProperties.isApiGenerate()) {
			return;
		}

		LOG.info("GENEROWANIE API - START");
		try {
			String scriptsDir = applicationProperties.getProjectRoot() + "/backend/etc/scripts";

			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.directory(new File(scriptsDir));
			processBuilder.command(scriptsDir + "/swagger.sh");
			Process process = processBuilder.start();
			process.waitFor();
			LOG.info((new BufferedReader(new InputStreamReader(process.getErrorStream()))).lines().collect(Collectors.joining("\n")));
			LOG.info((new BufferedReader(new InputStreamReader(process.getInputStream()))).lines().collect(Collectors.joining("\n")));
		} catch (Exception e) {
			LOG.error("GENEROWANIE API - BŁĄD", e);
		}
		LOG.info("GENEROWANIE API - KONIEC");
	}
}
