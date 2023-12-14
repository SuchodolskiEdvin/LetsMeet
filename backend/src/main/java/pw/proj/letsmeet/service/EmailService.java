package pw.proj.letsmeet.service;

import pw.proj.letsmeet.model.User;

public interface EmailService {

	void sendPasswordRemindEmail(User user);
}
