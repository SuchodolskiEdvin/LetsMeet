package pw.proj.letsmeet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pw.proj.letsmeet.dto.UserDTO;
import pw.proj.letsmeet.request.PasswordChangeRequest;
import pw.proj.letsmeet.request.PasswordRemindRequest;
import pw.proj.letsmeet.search.criteria.UserSearchCriteria;
import pw.proj.letsmeet.service.UserService;

import javax.persistence.EntityNotFoundException;

@RestController
@CrossOrigin
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/search/user")
	public ResponseEntity<?> searchUser(@RequestBody UserSearchCriteria searchCriteria) {
		return ResponseEntity.ok(userService.searchUser(searchCriteria));
	}

	@PostMapping("/search/user/count")
	public ResponseEntity<?> searchUserCount(@RequestBody UserSearchCriteria searchCriteria) {
		return ResponseEntity.ok(userService.searchUserCount(searchCriteria));
	}

	@PostMapping("/user/password/remind")
	public ResponseEntity<?> remindPassword(@RequestBody PasswordRemindRequest passwordRemindRequest) {
		this.userService.remindPassword(passwordRemindRequest.getEmail());

		return ResponseEntity.ok().build();
	}

	@PostMapping("/user/password/change")
	public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
		boolean success = this.userService.changePassword(passwordChangeRequest.getNewPassword(), passwordChangeRequest.getResetPasswordToken());

		return ResponseEntity.ok(success);
	}

	@PostMapping("/user")
	public ResponseEntity<?> createOrUpdateUser(@RequestBody UserDTO userDTO) {
		try {
			userService.createOrUpdateUser(userDTO);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().build();
	}

	@GetMapping("/user/{id}")
	public ResponseEntity<?> getUser(@PathVariable("id") Long id) {
		try {
			return ResponseEntity.ok(userService.getUserDTO(id));
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/users")
	public ResponseEntity<?> getUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
	}
}
