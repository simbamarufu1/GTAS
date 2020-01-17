package gov.gtas.services;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.inject.internal.util.Sets;

import freemarker.template.TemplateException;
import gov.gtas.email.EmailTemplateLoader;
import gov.gtas.enumtype.SignupRequestStatus;
import gov.gtas.model.SignupRequest;
import gov.gtas.repository.PhysicalLocationRepository;
import gov.gtas.repository.SignupRequestRepository;
import gov.gtas.repository.UserRepository;
import gov.gtas.services.dto.EmailDTO;
import gov.gtas.services.dto.SignupRequestDTO;
import gov.gtas.services.security.UserData;
import gov.gtas.services.security.UserServiceUtil;
import gov.gtas.services.security.*;

@Service
public class SignupRequestServiceImpl implements SignupRequestService {

	private static final Logger logger = LoggerFactory.getLogger(SignupRequestServiceImpl.class);

	private static final String SIGNUP_REQUEST_CONFIRMATION_TEMPLATE = "signupRequestConfirmation.ftl";
	private static final String SIGNUP_REQUEST_NOTIFICATION_TO_ADMIN_TEMPLATE = "signupRequestNotificationToAdmin.ftl";
	private static final String SIGNUP_REQUEST_TEMPORARY_PWD_TEMPLATE = "signupRequestTempPassword.ftl";
	private static final String SIGNUP_REQUEST_REJECTION_TEMPLATE = "signupRequestRejection.ftl";

	private final SignupRequestRepository signupRequestRepository;
	private final GtasEmailService emailService;
	private final EmailTemplateLoader emailTemplateLoader;
	private final UserRepository userRepository;
	private final PhysicalLocationRepository physicalLocationRepository;
	private final UserService userService;
	
	@Value("#{'${admin.list.of.integers}'.split(',')}")
	private List<Integer> adminIds;

	@Autowired
	public SignupRequestServiceImpl(SignupRequestRepository signupRequestRepositor, GtasEmailService emailService,
			EmailTemplateLoader emailTemplateLoader, UserRepository userRepository,
			PhysicalLocationRepository physicalLocationRepository, UserService userService) {
		this.signupRequestRepository = signupRequestRepositor;
		this.emailService = emailService;
		this.emailTemplateLoader = emailTemplateLoader;
		this.userRepository = userRepository;
		this.physicalLocationRepository = physicalLocationRepository;
		this.userService = userService;
	}

	/**
	 * TODO: add unit test
	 * 
	 * @return
	 */
	@Transactional
	public List<SignupRequestDTO> getAllNewSignupRequests() {

		List<SignupRequest> newSignupRequests = this.signupRequestRepository
				.findNewSignupRequests(SignupRequestStatus.NEW);

		return newSignupRequests.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	private SignupRequest convertToEntity(SignupRequestDTO dto) {
		SignupRequest signupRequest = new SignupRequest();
		org.springframework.beans.BeanUtils.copyProperties(dto, signupRequest);
		signupRequest.setPhysicalLocation(
				this.physicalLocationRepository.findById(signupRequest.getPhysicalLocationId()).orElse(null));
		dto.setPhysicalLocation(signupRequest.getPhysicalLocation().getName());
		return signupRequest;
	}

	private SignupRequestDTO convertToDTO(SignupRequest model) {
		SignupRequestDTO dto = new SignupRequestDTO();
		org.springframework.beans.BeanUtils.copyProperties(model, dto);
		dto.setPhysicalLocation(model.getPhysicalLocation().getName());
		return dto;
	}

	public void approveSignupRequest(SignupRequest signupRequest) {
		this.signupRequestRepository.save(signupRequest);
	}

	@Transactional
	public SignupRequest save(SignupRequestDTO signupRequestDTO) {

		SignupRequest signupRequest = this.convertToEntity(signupRequestDTO);
		return this.signupRequestRepository.save(signupRequest);
	}

	@Override
	public Boolean signupRequestExists(SignupRequestDTO signupRequest) {
		return this.signupRequestRepository.existsSignupRequestByEmailOrUsername(signupRequest.getEmail(),
				signupRequest.getUsername());
	}

	@Override
	public void sendConfirmationEmail(SignupRequestDTO signupRequestDTO)
			throws MessagingException, IOException, TemplateException {
		EmailDTO email = new EmailDTO();

		email.setTo(new String[] { signupRequestDTO.getEmail() });
		email.setSubject("Signup Request Confirmation");
		email.setBody(emailTemplateLoader.signupRequestEmailHtmlString(SIGNUP_REQUEST_CONFIRMATION_TEMPLATE,
				signupRequestDTO));
		this.emailService.sendHTMLEmail(email);
	}

	@Override
	public void sendEmailNotificationToAdmin(SignupRequestDTO signupRequestDTO)
			throws MessagingException, IOException, TemplateException {
		EmailDTO email = new EmailDTO();
		
		String[] adminEmails = this.userRepository.findEmailsByRoleIds(adminIds).toArray(new String[0]);
		email.setTo(adminEmails);
		email.setSubject("Signup Request Confirmation");
		email.setBody(emailTemplateLoader.signupRequestEmailHtmlString(SIGNUP_REQUEST_NOTIFICATION_TO_ADMIN_TEMPLATE,
				signupRequestDTO));
		this.emailService.sendHTMLEmail(email);
	}

	@Override
	public SignupRequest findById(Long id) {
		return this.signupRequestRepository.findById(id).orElse(null);
	}

	@Transactional
	@Override
	public void approve(SignupRequestDTO signupRequest, String approvedBy)
			throws IOException, TemplateException, MessagingException {

		signupRequest.setUpdatedBy(approvedBy);
		signupRequest.setStatus(SignupRequestStatus.APPROVED);

		//
		logger.debug("update sign up request to approved status");
		this.save(signupRequest);

		// Generate a random password that wi
		String password = new UserServiceUtil().generateRandomPassword();

		// Create temporary password
		UserData userData = new UserData(signupRequest.getUsername(), password, signupRequest.getFirstName(),
				signupRequest.getLastName(), 1, Sets.newHashSet(), signupRequest.getEmail(), false, false);

		logger.debug("create a new user");
		this.userService.create(userData);

		// Send temporary password to user
		logger.debug("Sending email with temporary password");
		this.sendEmailWithTemporaryPassword(signupRequest, password);

		logger.debug("Email sent successfully");
	}

	@Transactional
	@Override
	public void reject(SignupRequestDTO signupRequest, String rejectedBy)
			throws MessagingException, IOException, TemplateException {

		signupRequest.setUpdatedBy(rejectedBy);
		signupRequest.setStatus(SignupRequestStatus.REJECTED);

		//
		logger.debug("update sign up request to rejected status");
		this.save(signupRequest);

		logger.debug("Sending rejection email");
		this.sendRejectionEmail(signupRequest);

		logger.debug("Email sent successfully");
	}

	private void sendRejectionEmail(SignupRequestDTO signupRequest)
			throws MessagingException, IOException, TemplateException {
		EmailDTO email = new EmailDTO();
		email.setTo(new String[] { signupRequest.getEmail() });
		email.setSubject("Status updated [REJECTED] - GTAS Account");
		email.setBody(
				emailTemplateLoader.signupRequestEmailHtmlString(SIGNUP_REQUEST_REJECTION_TEMPLATE, signupRequest));
		this.emailService.sendHTMLEmail(email);
	}

	private void sendEmailWithTemporaryPassword(SignupRequestDTO signupRequestDTO, String password)
			throws IOException, TemplateException, MessagingException {

		String body = emailTemplateLoader.signupRequestEmailHtmlString(SIGNUP_REQUEST_TEMPORARY_PWD_TEMPLATE,
				signupRequestDTO, password);
		EmailDTO email = new EmailDTO();
		email.setTo(new String[] { signupRequestDTO.getEmail() });
		email.setSubject("Temporary Password - GTAS Account");
		email.setBody(body);
		this.emailService.sendHTMLEmail(email);
	}

}