package pw.proj.letsmeet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pw.proj.letsmeet.dto.ZoomTokenDTO;

@RestController
@CrossOrigin
public class ZoomController {

	@Autowired
	private ZoomTokenDTO zoomTokenDTO;

	@RequestMapping("/zoom/callback")
	public ResponseEntity<?> getAuthenticationCode(@RequestParam(name = "code") String code) {
		zoomTokenDTO.setAccessToken(code);
		return ResponseEntity.ok().build();
	}
}
