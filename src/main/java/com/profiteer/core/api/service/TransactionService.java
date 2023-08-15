package com.profiteer.core.api.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.profiteer.core.api.convertor.TransactionConvertor;
import com.profiteer.core.api.dto.SearchTransactionCriteria;
import com.profiteer.core.api.dto.TransactionRequest;
import com.profiteer.core.api.dto.TransactionResponse;
import com.profiteer.core.api.entity.Status;
import com.profiteer.core.api.entity.TransactionDetail;
import com.profiteer.core.api.entity.TransactionType;
import com.profiteer.core.api.entity.User;
import com.profiteer.core.api.entity.UserBankDetail;
import com.profiteer.core.api.repository.SearchCriteriaDao;
import com.profiteer.core.api.repository.TransactionDetailRepository;
import com.profiteer.core.api.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionService implements ITransactionApiService {

	@Autowired
	private TransactionDetailRepository transactionDetailRepository;

	@Autowired
	private SearchCriteriaDao searchCriteriaDao;

	@Autowired
	private UserRepository userRepository;

	@Value("${reffered_percentage}")
	private int refferedPercentage;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Boolean addorWithdrawMoney(TransactionRequest transactionRequest) throws Exception {
		try {
			log.info("Add or Withdraw Money request : {}", transactionRequest);
			Optional<User> user = userRepository.findByUserId(transactionRequest.getUserId());
			if (user.isEmpty())
				throw new Exception("User details not found");

			TransactionDetail transactionDetail = transactionDetailRepository
					.save(TransactionConvertor.convertTransactionRequestToEntity(transactionRequest));
			log.info("addorWithdrawMoney response : {}", transactionDetail);

			if (transactionDetail != null) {

				if (transactionRequest.getIsAddAmount() != null && transactionRequest.getIsAddAmount()
						&& StringUtils.hasText(user.get().getReferredBy())) {

					Optional<User> referredUser = userRepository
							.findByUserReferralCode(user.get().getReferredBy());

					if (referredUser.isPresent() && referredUser.get().getReferredUserCreditCount() < 10) {

						TransactionDetail transactionReferredDetail = new TransactionDetail();

						String transactionRefNumber = UUID.randomUUID().toString();
						transactionRefNumber = transactionRefNumber.replace("-", "");
						transactionReferredDetail.setTransactionReferenceNumber(transactionRefNumber);
						transactionReferredDetail.setUserId(referredUser.get().getUserId());
						transactionReferredDetail.setTransactionDate(new Date());

						transactionReferredDetail.setTransactionType(TransactionType.REFFRRED_USER);
						transactionReferredDetail
								.setSettleAmount((transactionRequest.getAmount() * refferedPercentage) / 100);
						transactionReferredDetail.setTransactionAmount(transactionReferredDetail.getSettleAmount());
						transactionReferredDetail.setTransactionStatus(Status.SUCCESS);
						transactionDetailRepository.save(transactionReferredDetail);

						referredUser.get()
								.setReferredUserCreditCount(referredUser.get().getReferredUserCreditCount() + 1);
						userRepository.save(referredUser.get());
						
						user.get().setReferredBy(null);
						userRepository.save(user.get());
					}
				}
				return true;
			}
			return false;

		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<TransactionResponse> getTransactionDetail(SearchTransactionCriteria searchTransactionCriteria)
			throws Exception {
		try {
			log.info("getTransactionDetail Request : {}", searchTransactionCriteria);
			List<TransactionDetail> transactionDetailList = searchCriteriaDao
					.getTransactionDetail(searchTransactionCriteria);

			if (transactionDetailList.isEmpty())
				throw new Exception("Transaction details does not exist");

			List<TransactionResponse> transactionResponseList = new ArrayList<>();
			for (TransactionDetail transactionDetail : transactionDetailList) {
				transactionResponseList.add(TransactionConvertor.convertTransactionEntityToResponse(transactionDetail));
			}

			log.info("transactionResponseList size {} for user Id : {}", transactionResponseList.size(),
					searchTransactionCriteria.getUserId());
			return transactionResponseList;

		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Boolean isBankDetailVerified(UserBankDetail bankDetail) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getWalletBalance(Long userId) {
		return transactionDetailRepository.getWalletBalanceByUserId(userId);
	}

}
