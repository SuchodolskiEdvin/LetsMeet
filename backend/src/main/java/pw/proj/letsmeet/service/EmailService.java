package pw.proj.letsmeet.service;

import pw.proj.letsmeet.dto.UserDTO;
import pw.proj.letsmeet.model.Meet;
import pw.proj.letsmeet.model.User;

import java.util.List;

public interface EmailService {

	void sendPasswordRemindEmail(User user);

	void sendRegistrationEmail(User user);

	void sendMeetCreationEmail(User user, Meet meet);

	void sendMeetInvitationEmail(List<UserDTO> users, Meet meet);
}
