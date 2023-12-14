package pw.proj.letsmeet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pw.proj.letsmeet.model.User;

import java.util.List;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {

	User findByEmail(String email);

	User findByResetPasswordToken(String resetPasswordToken);

	@Query("SELECT u FROM User u WHERE lower(u.email) like lower(:query)")
	List<User> findByQuery(String query);

}
