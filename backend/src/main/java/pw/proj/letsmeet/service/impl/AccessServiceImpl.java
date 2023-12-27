package pw.proj.letsmeet.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pw.proj.letsmeet.dao.MeetDAO;
import pw.proj.letsmeet.model.User;
import pw.proj.letsmeet.service.AccessService;

@Service
public class AccessServiceImpl implements AccessService {

	@Autowired
	private MeetDAO meetDAO;

	@Override
	public boolean isCreator(Long meetId) {
		if (meetId != null) {
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			User creator = meetDAO.getOne(meetId).getCreator();

			return user.getId().equals(creator.getId());
		} else {
			return true;
		}
	}
}
