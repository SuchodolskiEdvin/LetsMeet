package pw.proj.letsmeet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pw.proj.letsmeet.model.Meet;

@Repository
public interface MeetDAO extends JpaRepository<Meet, Long> {

}
