package pw.proj.letsmeet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pw.proj.letsmeet.dto.MeetDTO;
import pw.proj.letsmeet.search.criteria.MeetSearchCriteria;
import pw.proj.letsmeet.service.AccessService;
import pw.proj.letsmeet.service.MeetService;


@RestController
@CrossOrigin
public class MeetController {

	@Autowired
	private MeetService meetService;

	@Autowired
	private AccessService accessService;

	@PostMapping("/search/meet")
	public ResponseEntity<?> searchMeet(@RequestBody MeetSearchCriteria searchCriteria) {
		return ResponseEntity.ok(meetService.searchMeet(searchCriteria));
	}

	@PostMapping("/search/meet/count")
	public ResponseEntity<?> searchMeetCount(@RequestBody MeetSearchCriteria searchCriteria) {
		return ResponseEntity.ok(meetService.searchMeetCount(searchCriteria));
	}

	@PreAuthorize("@accessServiceImpl.isCreator(#meetDTO.getId())")
	@PostMapping("/meet")
	public ResponseEntity<?> createOrUpdateMeet(@RequestBody MeetDTO meetDTO) {
		meetService.createOrUpdateMeet(meetDTO);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/meet/{id}")
	public ResponseEntity<?> getMeet(@PathVariable("id") Long id) {
		return ResponseEntity.ok(meetService.getMeetDTO(id));
	}

	@DeleteMapping("/meet/{id}")
	public ResponseEntity<?> deleteMeet(@PathVariable("id") Long id) {
		meetService.deleteMeet(id);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/meet/participants/{id}")
	public ResponseEntity<?> getParticipants(@PathVariable("id") Long id) {
		return ResponseEntity.ok(meetService.getParticipiants(id));
	}

}
