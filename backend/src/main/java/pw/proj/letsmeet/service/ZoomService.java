package pw.proj.letsmeet.service;

import pw.proj.letsmeet.dto.MeetDTO;
import pw.proj.letsmeet.dto.ZoomMeetingObjectDTO;
import pw.proj.letsmeet.dto.ZoomMeetingsListResponseDTO;

public interface ZoomService {
	ZoomMeetingObjectDTO createMeeting(MeetDTO meetDTO);

	ZoomMeetingsListResponseDTO listMeetings(String userIdOrEmail, String meetingType);

	ZoomMeetingObjectDTO getZoomMeetingById(String meetingId);
}
