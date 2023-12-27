package pw.proj.letsmeet.service;

import org.springframework.http.ResponseEntity;
import pw.proj.letsmeet.dto.UserDTO;
import pw.proj.letsmeet.search.criteria.UserSearchCriteria;

import java.util.List;

public interface UserService {

	ResponseEntity<?> login(String email, String password);

	List<UserDTO> searchUser(UserSearchCriteria userSearchCriteria);

	long searchUserCount(UserSearchCriteria userSearchCriteria);

	void remindPassword(String email);

	boolean changePassword(String newPassword, String resetPasswordToken);

	void createOrUpdateUser(UserDTO userDTO);

	UserDTO getUserDTO(Long id);

	UserDTO getLoggedUser();

	List<UserDTO> toSelectUserValues(String query);

	List<UserDTO> getAllUsers();
}
