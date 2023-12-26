package pw.proj.letsmeet.service;

import pw.proj.letsmeet.dto.MeetDTO;
import pw.proj.letsmeet.dto.UserDTO;
import pw.proj.letsmeet.search.criteria.MeetSearchCriteria;

import java.util.List;

public interface MeetService {
	
	List<MeetDTO> searchMeet(MeetSearchCriteria MeetSearchCriteria);

	long searchMeetCount(MeetSearchCriteria MeetSearchCriteria);
	
	void createOrUpdateMeet(MeetDTO MeetDTO);

	void deleteMeet(long meetId);

	MeetDTO getMeetDTO(Long id);

	List<UserDTO> getParticipiants(Long id);
}
