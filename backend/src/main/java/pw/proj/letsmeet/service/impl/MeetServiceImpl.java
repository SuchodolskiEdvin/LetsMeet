package pw.proj.letsmeet.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pw.proj.letsmeet.dao.MeetDAO;
import pw.proj.letsmeet.dto.MeetDTO;
import pw.proj.letsmeet.model.Meet;
import pw.proj.letsmeet.search.criteria.MeetSearchCriteria;
import pw.proj.letsmeet.service.MeetService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Service
class MeetServiceImpl implements MeetService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private MeetDAO meetDAO;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<MeetDTO> searchMeet(MeetSearchCriteria meetSearchCriteria) {
		List<Meet> meets = meetSearchCriteria.load(entityManager);

		return meets.stream()
				.map(meet -> {
					MeetDTO meetDTO = modelMapper.map(meet, MeetDTO.class);
					meetDTO.setCreatorsFullName(meet.getCreator().getFullName());
					return meetDTO;
				})
				.collect(Collectors.toList());
	}

	@Override
	public long searchMeetCount(MeetSearchCriteria meetSearchCriteria) {
		return meetSearchCriteria.count(entityManager);
	}

	@Transactional
	@Override
	public void createOrUpdateMeet(MeetDTO meetDTO) {
		Meet meet = new Meet();

		if (meetDTO.getId() != null) {
			meet = meetDAO.getOne(meetDTO.getId());
		}

		modelMapper.map(meetDTO, meet);

		meetDAO.save(meet);
	}

	@Transactional
	@Override
	public void deleteMeet(long meetId) {
		meetDAO.delete(meetDAO.getOne(meetId));
	}

	@Override
	public MeetDTO getMeetDTO(Long id) {
		MeetDTO meetDTO = modelMapper.map(meetDAO.getOne(id), MeetDTO.class);

		return meetDTO;
	}

}
