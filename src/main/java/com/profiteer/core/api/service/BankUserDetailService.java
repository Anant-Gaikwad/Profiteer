package com.profiteer.core.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.profiteer.core.api.convertor.BankDetailConvertor;
import com.profiteer.core.api.dto.BankDetailResponse;
import com.profiteer.core.api.dto.BankDetailsResponseTemp;
import com.profiteer.core.api.dto.UserBankDetailRequest;
import com.profiteer.core.api.entity.UserBankDetail;
import com.profiteer.core.api.repository.BankStatus;
import com.profiteer.core.api.repository.SearchCriteriaDao;
import com.profiteer.core.api.repository.UserBankDetailRepository;
import com.profiteer.core.api.repository.UserRepository;
import com.profiteer.core.api.util.BankDetailRest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BankUserDetailService implements IBankUserDetailApiService {

	@Autowired
	private BankDetailRest bankDetailRest;

	@Autowired
	private UserBankDetailRepository bankDetailRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SearchCriteriaDao searchCriteriaDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean addUserBankDetails(UserBankDetailRequest userBankDetailRequest) throws Exception {
		try {

			if (userRepository.findByUserId(userBankDetailRequest.getUserId()).isEmpty())
				throw new Exception("User details not found");

			Optional<UserBankDetail> userBankDetailEntity = bankDetailRepository
					.findByUserIdAndIfscCodeAndStatusAndAccountNumber(userBankDetailRequest.getUserId(),
							userBankDetailRequest.getIfscCode(), BankStatus.VERIFIED,
							userBankDetailRequest.getBankUserAccountNumber());

			if (userBankDetailEntity.isPresent())
				throw new Exception("Bank details already exist");

			UserBankDetail userBankDetail = new UserBankDetail();
			userBankDetail.setId(userBankDetailRequest.getId() != null && userBankDetailRequest.getId() > 0
					? userBankDetailRequest.getId()
					: null);
			userBankDetail.setUserId(userBankDetailRequest.getUserId());
			userBankDetail.setAccountHolderName(userBankDetailRequest.getBankUserName());
			userBankDetail.setAccountNumber(userBankDetailRequest.getBankUserAccountNumber());
			userBankDetail.setBankName(userBankDetailRequest.getBankName());
			userBankDetail.setIsDefault(userBankDetailRequest.getIsDefault());
			userBankDetail.setBankRegMobileNumber(userBankDetailRequest.getBankRegMobileNumber());
			userBankDetail.setBranch(userBankDetailRequest.getBranchName());
			userBankDetail.setMicrCode(userBankDetailRequest.getMicrCode());
			userBankDetail.setIfscCode(userBankDetailRequest.getIfscCode());
			userBankDetail.setStatus(BankStatus.PENDING);
			userBankDetail.setBankAddress(userBankDetailRequest.getBankAddress());
			userBankDetail.setUpdatedBy(userBankDetailRequest.getUserId());

			UserBankDetail entityResponse = bankDetailRepository.save(userBankDetail);
			if (entityResponse.getId() > 0)
				return true;

			return false;

		} catch (Exception e) {
			log.info("Error while persist the bank details : {}", e);
			throw e;
		}
	}

	@Override
	public BankDetailsResponseTemp getBankdetailByIfscCode(String ifscCode) throws Exception {
		try {
			log.info("In service method get Bank detail By Ifsc Code :: {}", ifscCode);
			BankDetailsResponseTemp bankDetailsResponse = bankDetailRest.getBankDetails(ifscCode);
			log.info("Get Bank details response ifsc code : {} : response: {} ", ifscCode, bankDetailsResponse);
			return bankDetailsResponse;

		} catch (Exception e) {
			throw new Exception("IFSC code details not found");
		}
	}

	@Override
	public List<BankDetailResponse> getUserBankdetails(Long userId) throws Exception {
		try {

			List<UserBankDetail> userBankDetails = searchCriteriaDao.getUserBankDetails(userId);

			if (CollectionUtils.isEmpty(userBankDetails))
				throw new Exception("User bank details not found");

			List<BankDetailResponse> bankDetailResponseList = new ArrayList<>();

			if (userBankDetails.size() == 1 && !userBankDetails.get(0).getIsDefault()) {
				userBankDetails.get(0).setIsDefault(true);
				this.isUpdateStatus(userBankDetails.get(0));

				bankDetailResponseList.add(BankDetailConvertor.convertEntityToBankResponse(userBankDetails.get(0)));
				return bankDetailResponseList;
			}

			for (UserBankDetail userBankDetail : userBankDetails) {
				bankDetailResponseList.add(BankDetailConvertor.convertEntityToBankResponse(userBankDetail));
			}
			return bankDetailResponseList;

		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean isUpdateStatus(UserBankDetail bankDetail) {
		bankDetailRepository.save(bankDetail);
		return true;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean isDeleteBankUserdetail(Long id, Long userId) throws Exception {

		try {
			log.info("isDeleteBankUserdetail :: Id -- {}  :: User Id -- {}", id, userId);
			Optional<UserBankDetail> bankDetail = bankDetailRepository.findById(id);

			if (bankDetail.isEmpty())
				throw new Exception("Bank details not found");

			log.info("Existing user id : {}", bankDetail.get().getUserId());

			if (!bankDetail.get().getUserId().equals(userId))
				throw new Exception("Invalid user details");

			UserBankDetail userBankDetail = bankDetail.get();
			userBankDetail.setStatus(BankStatus.INACTIVE);
			userBankDetail = bankDetailRepository.save(userBankDetail);

			log.info("Updated bank user details : {}", userBankDetail);
			if (userBankDetail != null)
				return true;

			return false;
		} catch (Exception e) {
			throw e;
		}
	}
}
