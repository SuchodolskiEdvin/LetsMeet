package pw.proj.letsmeet.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pw.proj.letsmeet.dao.MeetDAO;
import pw.proj.letsmeet.dto.MeetDTO;
import pw.proj.letsmeet.dto.UserDTO;
import pw.proj.letsmeet.dto.ZoomMeetingObjectDTO;
import pw.proj.letsmeet.model.Meet;
import pw.proj.letsmeet.model.User;
import pw.proj.letsmeet.search.criteria.MeetSearchCriteria;
import pw.proj.letsmeet.service.EmailService;
import pw.proj.letsmeet.service.MeetService;
import pw.proj.letsmeet.service.ZoomService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
class MeetServiceImpl implements MeetService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private MeetDAO meetDAO;

	@Autowired
	private EmailService emailService;

	@Autowired
	private ZoomService zoomService;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<MeetDTO> searchMeet(MeetSearchCriteria meetSearchCriteria) {
		List<Meet> meets = meetSearchCriteria.load(entityManager);
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		return meets.stream()
				.filter(meet -> meet.getParticipants().contains(user))
				.map(meet -> {
					MeetDTO meetDTO = modelMapper.map(meet, MeetDTO.class);
					meetDTO.setParticipantsId(meet.getParticipants().stream().map(User::getId).toList());
					meetDTO.getCreator().setPassword(null);
					meetDTO.setParticipants(null);
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
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (meetDTO.getId() != null) {
			meet = meetDAO.getOne(meetDTO.getId());
			meet.getParticipants().clear();
			if (meetDTO.getIsOnline()) {
//				zoomService.editMeeting(meetDTO);
			}
		} else {
			meet.setCreator(user);
			if (meetDTO.getIsOnline()) {
				ZoomMeetingObjectDTO zoomMeet = zoomService.createMeeting(meetDTO);
				meet.setZoomUrlJoinLink(zoomMeet.getJoin_url());
				meet.setZoomId(zoomMeet.getId());
			}
		}

		meet.setName(meetDTO.getName());
		meet.setDate(meetDTO.getDate());
		meet.setTimeStart(meetDTO.getTimeStart());
		meet.setDuration(meetDTO.getDuration());
		meet.setIsOnline(meetDTO.getIsOnline());

		Set<User> participants = meetDTO.getParticipants().stream()
				.map(p -> modelMapper.map(p, User.class))
				.collect(Collectors.toSet());

		participants.add(user);

		meet.setParticipants(participants);

		meetDAO.save(meet);

		if (meetDTO.getId() == null) {
			emailService.sendMeetCreationEmail(user, meet);
			meetDTO.getParticipants().remove(user.getId());
			emailService.sendMeetInvitationEmail(meetDTO.getParticipants(), meet);
		}
	}

	@Transactional
	@Override
	public void deleteMeet(long meetId) {
		meetDAO.delete(meetDAO.getOne(meetId));
	}

	@Override
	public MeetDTO getMeetDTO(Long id) {
		return modelMapper.map(meetDAO.getOne(id), MeetDTO.class);
	}

	@Override
	public List<UserDTO> getParticipiants(Long id) {
		return meetDAO.findUsersByMeetId(id).stream()
				.map(u -> new UserDTO(u.getId(), u.getEmail(), u.getFullName()))
				.collect(Collectors.toList());
	}
}
