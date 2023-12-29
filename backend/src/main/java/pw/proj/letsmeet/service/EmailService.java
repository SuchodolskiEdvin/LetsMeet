package pw.proj.letsmeet.service;

import pw.proj.letsmeet.dto.UserDTO;
import pw.proj.letsmeet.model.User;

import java.util.List;

public interface EmailService {

	void sendPasswordRemindEmail(User user);

	void sendRegistrationEmail(User user);

	void sendMeetCreationEmail(User user);

	void sendMeetInvitationEmail(List<UserDTO> users);
}
