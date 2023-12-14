package pw.proj.letsmeet.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pw.proj.letsmeet.config.JwtTokenProvider;
import pw.proj.letsmeet.dao.UserDAO;
import pw.proj.letsmeet.dto.UserDTO;
import pw.proj.letsmeet.model.User;
import pw.proj.letsmeet.search.criteria.UserSearchCriteria;
import pw.proj.letsmeet.service.EmailService;
import pw.proj.letsmeet.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@Service
class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private EmailService emailService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PersistenceContext
	private EntityManager entityManager;


	@Override
	public ResponseEntity<?> login(String email, String password) {

		try {
			User user = this.userDAO.findByEmail(email);
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
			String token = jwtTokenProvider.createToken(email, Collections.singletonList(user.getSystemRole().toString()));
			Map<Object, Object> model = new HashMap<>();
			model.put("email", email);
			model.put("token", token);
			model.put("role", user.getSystemRole());
			model.put("fullName", user.getFullName());
			return ok(model);
		} catch (AuthenticationException e) {
			throw new BadCredentialsException("Invalid username/password supplied");
		}
	}

	@Override
	public void remindPassword(String email) {
		User user = userDAO.findByEmail(email);

		if (user != null) {
			String token = (new BigInteger(340, new SecureRandom())).toString(32).substring(0, 64);
			user.setResetPasswordToken(token);

			userDAO.save(user);
			emailService.sendPasswordRemindEmail(user);
		}
	}

	@Override
	public boolean changePassword(String newPassword, String resetPasswordToken) {
		if (StringUtils.isBlank(resetPasswordToken)) {
			return false;
		}

		User user = userDAO.findByResetPasswordToken(resetPasswordToken);

		if (user != null) {
			user.setPassword(passwordEncoder.encode(newPassword));
			user.setResetPasswordToken(null);
			userDAO.save(user);

			return true;
		}

		return false;
	}

	@Override
	public List<UserDTO> searchUser(UserSearchCriteria userSearchCriteria) {
		List<User> users = userSearchCriteria.load(entityManager);

		return users.stream()
				.map(user -> modelMapper.map(user, UserDTO.class))
				.peek(user -> user.setPassword(null))
				.collect(Collectors.toList());
	}

	@Override
	public long searchUserCount(UserSearchCriteria userSearchCriteria) {
		return userSearchCriteria.count(entityManager);
	}

	@Transactional
	@Override
	public void createOrUpdateUser(UserDTO userDTO) {
		User user = new User();
		String passwordHash = null;

		// W formularzu podano hasło, a więc zmieniamy userowi hasło
		if (StringUtils.isNotBlank(userDTO.getPassword())) {
			passwordHash = passwordEncoder.encode(userDTO.getPassword());
		}

		if (userDTO.getId() != null) {
			user = userDAO.findById(userDTO.getId()).orElseThrow(
					() -> new RuntimeException("There is no user with given id"));

			// Nie podano hasła, zostaje stare
			if (StringUtils.isBlank(userDTO.getPassword())) {
				passwordHash = user.getPassword();
			}
		}

		// Zmieniamy email na małe litery
		userDTO.setEmail(userDTO.getEmail().toLowerCase());

		modelMapper.map(userDTO, user);
		user.setPassword(passwordHash);

		userDAO.save(user);
	}

	@Override
	public UserDTO getUserDTO(Long id) {
		User user = userDAO.findById(id).orElseThrow(
				() -> new RuntimeException("User with given id has not been found"));
		UserDTO userDTO = modelMapper.map(user, UserDTO.class);
		userDTO.setPassword(null);

		return userDTO;
	}

	@Override
	public List<UserDTO> toSelectUserValues(String query) {
		List<User> users = userDAO.findByQuery("%" + query + "%");

		return users.stream().map(u -> new UserDTO(u.getId(), u.getEmail())).collect(Collectors.toList());
	}
}
