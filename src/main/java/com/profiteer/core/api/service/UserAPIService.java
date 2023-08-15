package com.profiteer.core.api.service;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.profiteer.core.api.convertor.OtpDetailConvertor;
import com.profiteer.core.api.convertor.UserConvertor;
import com.profiteer.core.api.dto.MPINRequest;
import com.profiteer.core.api.dto.OtpRequest;
import com.profiteer.core.api.dto.UserLoginRequest;
import com.profiteer.core.api.dto.UserRequest;
import com.profiteer.core.api.dto.UserResponse;
import com.profiteer.core.api.entity.OTPDetail;
import com.profiteer.core.api.entity.User;
import com.profiteer.core.api.repository.OTPDetailRepository;
import com.profiteer.core.api.repository.TransactionDetailRepository;
import com.profiteer.core.api.repository.UserRepository;
import com.profiteer.core.api.util.Base64Utils;
import com.profiteer.core.api.util.JwtUtil;
import com.profiteer.core.api.util.OtpTemplate;
import com.profiteer.core.api.util.SendTextOtp;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserAPIService implements IUserApiService {

	public static final Random randomno = new Random();

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OTPDetailRepository otpDetailRepository;

	@Value("${email_template}")
	private String emailTemplate;

	@Value("${token_minutes_time}")
	private int tokenMinutesTime;

	@Autowired
	private SendTextOtp sendTextOtp;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private TransactionDetailRepository transactionDetailRepository;

	@Autowired
	private TrackingService trackingService;

	@Override
	public UserResponse registerUser(UserRequest userRequest, HttpServletRequest httpServletRequest) throws Exception {

		try {

			log.info("Register User Request : {}", userRequest);

			Optional<User> userExist = userRepository.findByMobile(userRequest.getMobileNumber());

			if (!userExist.isEmpty())
				throw new Exception("User already exist");

			UserResponse userResponse = new UserResponse();

			String userRefferalGeneratedCode = this.generateRefferalCode();
			log.info("1: User Refferal Generated Code :: {}", generateRefferalCode());
			Optional<User> userRefferal = userRepository.findByUserReferralCode(userRefferalGeneratedCode);

			if (userRefferal.isPresent()) {
				userRefferalGeneratedCode = this.generateRefferalCode();
				log.info("2 : User Refferal Generated Code :: {}", generateRefferalCode());
				userRefferal = userRepository.findByUserReferralCode(userRefferalGeneratedCode);
				if (userRefferal.isPresent()) {
					userRefferalGeneratedCode = this.generateRefferalCode();
					log.info("3 : User Refferal Generated Code :: {}", generateRefferalCode());
				}
			}

			User user = userRepository.save(UserConvertor.mapCreateUserRequest(userRequest, userRefferalGeneratedCode));

			log.info("User Creation success with response : {}", user);
			userResponse.setFirstName(user.getFirstName());
			userResponse.setMobileNumber(user.getMobile());
			userResponse.setEmail(user.getEmail());
			userResponse.setUserId(user.getUserId());
			userResponse.setLastName(user.getLastName());
			userResponse.setIsActive(user.isActive());
			userResponse.setReferralCode(user.getUserReferralCode());

			log.info("User Resposne : {}", userResponse);
			String token = this.generateAuthorizationToken(user);
			userResponse.setToken(token);
			trackingService.saveUserSession(httpServletRequest, userResponse.getUserId(), false);
			return userResponse;

		} catch (Exception e) {
			log.info("Error getting while creating new user : {}", e);
			throw e;
		}
	}

	public String generateRefferalCode() {
		String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
		StringBuilder sb = new StringBuilder(20);
		for (int i = 0; i < 20; i++) {
			int index = (int) (alphaNumericString.length() * Math.random());
			sb.append(alphaNumericString.charAt(index));
		}
		return sb.toString();
	}

	@SuppressWarnings("deprecation")
	@Override
	public UserResponse loginUser(UserLoginRequest userRequest, HttpServletRequest httpServletRequest)
			throws Exception {

		try {
			log.info("Login User Request : {}", userRequest);

			Optional<User> user = userRepository.findByMobile(userRequest.getMobileNumber());

			if (user.isEmpty())
				throw new Exception("User details not found");

			if (userRequest.getIsLoginWithMpin() != null && userRequest.getIsLoginWithMpin()) {

				if (StringUtils.isEmpty(user.get().getMpin()))
					throw new Exception("MPIN is not set");

				log.info("User Response MPIN :{}", Base64Utils.decodeBase64(user.get().getMpin()));

				if (userRequest.getMpin().equals(Base64Utils.decodeBase64(user.get().getMpin()))) {
					log.info("MPIN is valid");
					UserResponse userResponse = UserConvertor.convertUserEntityToUserResponse(user.get(),
							this.generateAuthorizationToken(user.get()), this.getWalletBalance(user.get().getUserId()));
					trackingService.saveUserSession(httpServletRequest, userResponse.getUserId(), false);
					return userResponse;
				} else
					throw new Exception("Invalid MPIN");
			}

			if (userRequest.getIsLoginWithPassword() != null && userRequest.getIsLoginWithPassword()) {

				log.info("User Response Pass :{}", Base64Utils.decodeBase64(user.get().getPassword()));

				if (userRequest.getPassword().equals(Base64Utils.decodeBase64(user.get().getPassword()))) {
					log.info("Is password valid");
					UserResponse userResponse = UserConvertor.convertUserEntityToUserResponse(user.get(),
							this.generateAuthorizationToken(user.get()), this.getWalletBalance(user.get().getUserId()));
					trackingService.saveUserSession(httpServletRequest, userResponse.getUserId(), false);
					return userResponse;
				} else
					throw new Exception("Invalid Password");
			}

			Optional<OTPDetail> otpDetail = otpDetailRepository.findByUserIdAndMobileNumberAndOtp(
					user.get().getUserId(), userRequest.getMobileNumber(), userRequest.getOtp());

			if (!otpDetail.isPresent()) {
				throw new Exception("Invalid OTP");
			}
			if (!otpDetail.get().getValidUpto().after(new Date()))
				throw new Exception("Otp has been expired");

			UserResponse userResponse = UserConvertor.convertUserEntityToUserResponse(user.get(),
					this.generateAuthorizationToken(user.get()), this.getWalletBalance(user.get().getUserId()));
			trackingService.saveUserSession(httpServletRequest, userResponse.getUserId(), false);

			return userResponse;

		} catch (Exception e) {
			throw e;
		}
	}

	public Double getWalletBalance(Long userId) {
		return transactionDetailRepository.getWalletBalanceByUserId(userId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean isSendotp(OtpRequest otpRequest) throws Exception {

		try {

			log.info("Email template : {}", emailTemplate);

			if (otpRequest.getIsLogin() != null && otpRequest.getIsLogin()) {
				Optional<User> user = userRepository.findByMobile(otpRequest.getMobileNumber());
				if (user.isEmpty())
					throw new Exception("User details not found");
				otpRequest.setUserId(user.get().getUserId());
				otpRequest.setEmail(user.get().getEmail());
			}

			if (otpRequest.getIsRegister() != null && otpRequest.getIsRegister()) {
				Optional<User> user = userRepository.findByMobile(otpRequest.getMobileNumber());
				if (user.isPresent())
					throw new Exception("User details already exist");
			}

			int generateOtp = this.generateOtp();
			otpRequest.setOtp(Long.parseLong("" + generateOtp));

			Optional<OTPDetail> otpDetailOrm = otpDetailRepository.findByMobileNumber(otpRequest.getMobileNumber());

			if (otpDetailOrm.isPresent() && otpDetailOrm.get() != null) {
				otpDetailRepository.save(OtpDetailConvertor.optDetailDtoToOrm(otpRequest, otpDetailOrm.get()));
			} else {
				OTPDetail optDetailOrm = new OTPDetail();
				otpDetailRepository.save(OtpDetailConvertor.optDetailDtoToOrm(otpRequest, optDetailOrm));
			}

			String messageTemplte = OtpTemplate.generateSendOTPMessage("LOGIN_SMS_OTP", emailTemplate,
					"" + generateOtp);
			log.info("Message Template : {}", messageTemplte);

			boolean isSend = sendTextOtp.sendOtpOnMobile(otpRequest, messageTemplte);

			log.info("Message Send : {}", isSend);
			return isSend;

		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public boolean isValidateOtp(OtpRequest otpRequest) throws Exception {

		try {
			log.info("OTP validate request :: {}", otpRequest);
			Optional<OTPDetail> otpDetailOrm = otpDetailRepository
					.findByMobileNumberAndOtp(otpRequest.getMobileNumber(), otpRequest.getOtp());

			if (otpDetailOrm.isEmpty())
				throw new Exception("Invalid otp details");

			if (!otpDetailOrm.get().getValidUpto().after(new Date()))
				throw new Exception("Otp has been expired");

			return true;

		} catch (Exception e) {
			throw e;
		}
	}

	public int generateOtp() {
		return 1000 + randomno.nextInt(9000);
	}

	public String generateAuthorizationToken(User user) {
		long expireTime = System.currentTimeMillis() + (1000 * tokenMinutesTime * 60);
		String token = jwtUtil.generateToken("" + user.getMobile(), expireTime);
		log.info("Token generated {}", token);
		return token;
	}

	@Override
	public UserResponse getUserById(Long id) throws Exception {
		try {
			log.info("Login User Id : {}", id);

			Optional<User> user = userRepository.findById(id);

			if (user.isEmpty())
				throw new Exception("User details not found");

			return UserConvertor.convertUserEntityToUserResponse(user.get(), null,
					this.getWalletBalance(user.get().getUserId()));

		} catch (Exception e) {
			log.info("Error while getting user details :{}", e);
			throw e;
		}
	}

	@Override
	public boolean isMPINSet(MPINRequest mpinRequest) throws Exception {
		try {

			Optional<User> userExist = userRepository.findByUserId(mpinRequest.getUserId());
			if (userExist.isEmpty())
				throw new Exception("User details not found");

			userExist.get().setMpin(Base64Utils.encodeBase64("" + mpinRequest.getMpin()));
			User user = userRepository.save(userExist.get());

			if (user != null)
				return true;

			return false;
		} catch (Exception e) {
			log.info("Error while set MPIN : {}", e);
			throw new Exception("Error while set MPIN");
		}
	}

	@Override
	public boolean isValidReferralCode(String referralCode) throws Exception {

		try {
			log.info("Requested Referral Code : {}", referralCode);
			Optional<User> user = userRepository.findByUserReferralCode(referralCode);

			if (user.isPresent())
				return true;

			return false;
		} catch (Exception e) {
			log.info("Error while fetching referral code.....");
			throw e;
		}
	}
}
