package pw.proj.letsmeet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pw.proj.letsmeet.request.AuthenticationRequest;
import pw.proj.letsmeet.service.UserService;

@RestController
@CrossOrigin
public class LoginController {

	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
		return userService.login(authenticationRequest.getLogin(), authenticationRequest.getPassword());
	}

	@GetMapping("/is-token-valid")
	public ResponseEntity<?> isTokenValid() {
		return ResponseEntity.ok().build();
	}
}
