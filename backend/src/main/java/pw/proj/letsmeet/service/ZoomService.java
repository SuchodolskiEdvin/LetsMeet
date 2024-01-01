package pw.proj.letsmeet.service;

import pw.proj.letsmeet.dto.ZoomMeetingObjectDTO;
import pw.proj.letsmeet.dto.ZoomMeetingsListResponseDTO;

public interface ZoomService {
	ZoomMeetingObjectDTO createMeeting(ZoomMeetingObjectDTO zoomMeetingObjectDTO);

	ZoomMeetingsListResponseDTO listMeetings(String userIdOrEmail, String meetingType);

	ZoomMeetingObjectDTO getZoomMeetingById(String meetingId);
}
