package pw.proj.letsmeet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pw.proj.letsmeet.model.Meet;
import pw.proj.letsmeet.model.User;

import java.util.List;

@Repository
public interface MeetDAO extends JpaRepository<Meet, Long> {

	@Query("SELECT u FROM User u JOIN u.meets m WHERE m.id = :meetId")
	List<User> findUsersByMeetId(Long meetId);
}
