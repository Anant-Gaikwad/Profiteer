package com.profiteer.core.api.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.profiteer.core.api.dto.ContestOrderResponse;
import com.profiteer.core.api.dto.ContestRequest;
import com.profiteer.core.api.entity.ContestOrderDetail;
import com.profiteer.core.api.entity.Status;
import com.profiteer.core.api.entity.TransactionDetail;
import com.profiteer.core.api.entity.TransactionType;
import com.profiteer.core.api.repository.ContestOrderDetailRepository;
import com.profiteer.core.api.repository.ContestSearchCriteriaDao;
import com.profiteer.core.api.repository.TransactionDetailRepository;
import com.profiteer.core.api.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ContestService implements IContestService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TransactionDetailRepository transactionDetailRepository;

	@Autowired
	private ContestOrderDetailRepository contestOrderDetailRepository;

	@Autowired
	private ContestSearchCriteriaDao contestSearchCriteriaDao;

	@Override
	public ContestOrderResponse isPlaceOrderForContest(ContestRequest contestRequest) throws Exception {
		try {

			log.info("isPlaceColorOrNumberEventByUserId Request: {}", contestRequest);

			if (userRepository.findByUserId(contestRequest.getUserId()).isEmpty())
				throw new Exception("User details not found");

			List<ContestOrderDetail> contestOrderDetails = contestSearchCriteriaDao.getContestOrderDetail(
					contestRequest.getContestId(), contestRequest.getContestDetailPkId(), Status.INPROCESS,
					contestRequest.getUserId());

			if (!CollectionUtils.isEmpty(contestOrderDetails)) {
				throw new Exception("User already in contest");
			}

			ContestOrderDetail contestOrderDetail = new ContestOrderDetail();
			contestOrderDetail.setStatus(Status.INPROCESS);
			contestOrderDetail.setContestChooseNumber(contestRequest.getSelectedNumber());
			contestOrderDetail.setContestPoolAmount(contestRequest.getAmount());
			contestOrderDetail.setContestId(contestRequest.getContestId());
			contestOrderDetail.setContestDetailId(contestRequest.getContestDetailPkId());
			contestOrderDetail.setContestWinningAmount(0.0);
			contestOrderDetail.setOrderTime(new Date());
			contestOrderDetail.setUserId(contestRequest.getUserId());
			contestOrderDetail.setUserWin(false);

			contestOrderDetail = contestOrderDetailRepository.save(contestOrderDetail);
			ContestOrderResponse contestOrderResponse = new ContestOrderResponse();
			
			if (contestOrderDetail.getId() > 0) {
				
				transactionDetailRepository.save(this.makeEntityToPlaceTransactionOrderForContest(contestOrderDetail,
						TransactionType.CONTEST_PLAYED));

				contestOrderResponse.setUserId(contestOrderDetail.getUserId());
				contestOrderResponse.setSelectedNumber(contestOrderDetail.getContestChooseNumber());
				contestOrderResponse.setAmount(contestOrderDetail.getContestPoolAmount());
				contestOrderResponse.setOrderStatus(true);
			}

			return contestOrderResponse;

		} catch (Exception e) {
			log.info("");
			throw e;
		}
	}

	public TransactionDetail makeEntityToPlaceTransactionOrderForContest(ContestOrderDetail contestOrderDetail,
			TransactionType transactionType) {

		TransactionDetail transactionDetail = new TransactionDetail();

		String transactionRefNumber = UUID.randomUUID().toString();
		transactionRefNumber = transactionRefNumber.replace("-", "");

		transactionDetail.setTransactionReferenceNumber(transactionRefNumber);
		transactionDetail.setTransactionAmount(contestOrderDetail.getContestPoolAmount());
		transactionDetail.setUserId(contestOrderDetail.getUserId());
		transactionDetail.setTransactionDate(new Date());
		transactionDetail.setSettleAmount(0 - transactionDetail.getTransactionAmount());

		transactionDetail.setTransactionType(transactionType);
		transactionDetail.setTransactionStatus(Status.SUCCESS);

		transactionDetail.setEventContestOrderId(contestOrderDetail.getId());
		transactionDetail.setEventContestId(contestOrderDetail.getContestId());

		return transactionDetail;
	}

}
