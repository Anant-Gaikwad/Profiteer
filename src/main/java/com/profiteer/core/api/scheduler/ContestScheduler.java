package com.profiteer.core.api.scheduler;

import com.profiteer.core.api.entity.*;
import com.profiteer.core.api.repository.ContestDetailRepository;
import com.profiteer.core.api.repository.ContestOrderDetailRepository;
import com.profiteer.core.api.repository.ContestRepository;
import com.profiteer.core.api.repository.ContestSearchCriteriaDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Component
@Slf4j
public class ContestScheduler {

    private final Random random = new Random();
    List<Long> appUserList = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L);
    @Autowired
    private ContestRepository contestRepository;
    @Autowired
    private ContestDetailRepository contestDetailRepository;
    @Autowired
    private ContestSearchCriteriaDao contestSearchCriteriaDao;
    @Autowired
    @Qualifier("taskScheduler")
    private TaskScheduler taskScheduler;
    @Autowired
    private ContestOrderDetailRepository contestOrderDetailRepository;

    // @Scheduled(fixedDelay = 900000)
    public void createContest() {

        Contest contest = new Contest();
        contest.setContestId("P-500");
        contest.setFirstWinner("5X");
        contest.setSecondWinner("2.5X");
        contest.setThirdWinner("1.5X");
        contest.setPoolPrize(500.00);
        contest.setPoolSize(50);
        contest.setEntryFee(50.0);
        contest.setProbability(69.0);
        contest.setProcessingFee(5.00);
        contest.setUpdatedOn(new Date());
        contest.setRefreshTime(30);
        contestRepository.save(contest);
    }

    @Scheduled(initialDelay = 1000, fixedDelay = 900000)
    public void startContest() {
        try {
            List<Contest> contests = contestRepository.findAll();

            for (Contest contest : contests) {

                List<ContestDetail> contestDetails = contestSearchCriteriaDao.getContestDetail(contest.getContestId(), Status.INPROCESS);

                if (CollectionUtils.isEmpty(contestDetails)) {

                    ContestDetail contestDetail = new ContestDetail();
                    contestDetail.setEventStartTime(new Date());


                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.MINUTE, contest.getRefreshTime());

                    contestDetail.setEventEndTime(new Date(calendar.getTimeInMillis()));
                    contestDetail.setEntryFee(contest.getEntryFee());
                    contestDetail.setContestId(contest.getContestId());
                    contestDetail.setContestDistributedAmount(0.0);
                    contestDetail.setContestProfitAmount(0.0);
                    //contestDetail.setFirstWinnerAmount(0.0);
                    //contestDetail.setSecondWinnerAmount(0.0);
                    //contestDetail.setThirdWinnerAmount(0.0);
                    contestDetail.setStatus(Status.INPROCESS);
                    //contestDetail.setFourthWinnerAmount(0.0);
                    contestDetail.setTotalContestAmount(0.0);

                    contestDetailRepository.save(contestDetail);


                    taskScheduler.schedule(() -> {
                        log.info("isWinningEvent Method STARTED BY {}::", calendar.getTime());
                        this.defaultUserAdded();
                    }, new Date(5000));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void defaultUserAdded() {

        ContestOrderDetail contestOrderDetail = new ContestOrderDetail();
        contestOrderDetail.setStatus(Status.INPROCESS);
        contestOrderDetail.setContestChooseNumber(7L);
        contestOrderDetail.setContestPoolAmount(10.0);
        contestOrderDetail.setContestId("P100");
        contestOrderDetail.setContestDetailId(1L);
        contestOrderDetail.setContestWinningAmount(0.0);
        contestOrderDetail.setOrderTime(new Date());
        contestOrderDetail.setUserId(2L);
        contestOrderDetail.setUserWin(false);

        contestOrderDetail = contestOrderDetailRepository.save(contestOrderDetail);
    }

    //@Scheduled(initialDelay = 1000, fixedDelay = 900000)
    public void winnerContest() {
        try {

            List<ContestDetail> contestDetails = contestSearchCriteriaDao.getContestDetail(null, Status.INPROCESS);

            if (CollectionUtils.isEmpty(contestDetails)) {
                log.info("No contest find");
                return;
            }

            for (ContestDetail contestDetail : contestDetails) {

                //List<ContestOrderDetail> contestOrderDetails = contestSearchCriteriaDao.getContestOrderDetail(contestDetail.getContestId(), contestDetail.getId(), Status.INPROCESS, null);
                List<ContestOrderDetail> contestOrderDetails = contestDetail.getOrderDetails();
                if (CollectionUtils.isEmpty(contestOrderDetails)) {
                    contestDetail.setReason("No users played this contest");
                    contestDetail.setStatus(Status.COMPLETE);
                    contestDetail.setContestDistributedAmount(0.0);
                    contestDetail.setTotalContestAmount(0.0);
                    contestDetailRepository.save(contestDetail);
                    continue;
                }

                Double totalAmount = contestOrderDetails.get(0).getContestPoolAmount() * contestOrderDetails.size();

                List<ContestOrderDetail> automatedUsers = contestOrderDetails.stream().filter(x -> "AT".equalsIgnoreCase(x.getUserType())).collect(Collectors.toList());
                List<ContestOrderDetail> originalUsers = contestOrderDetails.stream().filter(x -> !"AT".equalsIgnoreCase(x.getUserType())).collect(Collectors.toList());

                log.info("Auto Users details :: {}", automatedUsers);
                log.info("Actual Played User Details: {}", originalUsers);

                switch (originalUsers.size()) {

                    case 1:
                        List<ContestOrderDetail> finalAutomatedList1 = automatedUsers.stream().filter(x -> originalUsers.stream().noneMatch(y -> Objects.equals(y.getContestChooseNumber(), x.getContestChooseNumber()))).collect(Collectors.toList());
                        populateAllAutomatedUsersWinner(contestDetail, finalAutomatedList1);
                        break;

                    case 2:
                        List<ContestOrderDetail> finalAutomatedList2 = automatedUsers.stream().filter(x -> originalUsers.stream().noneMatch(y -> Objects.equals(y.getContestChooseNumber(), x.getContestChooseNumber()))).collect(Collectors.toList());
                        if (Objects.equals(originalUsers.get(0).getContestChooseNumber(), originalUsers.get(1).getContestChooseNumber())) {
                            populateAllAutomatedUsersWinner(contestDetail, finalAutomatedList2);
                        } else {
                            populateOneUsersWinner(contestDetail, finalAutomatedList2, originalUsers);
                        }
                        break;

                    case 3:

                        //SC1 : if two has chosen same number
                        Map<Long, List<ContestOrderDetail>> totalMap = originalUsers.stream().collect(groupingBy(ContestOrderDetail::getContestChooseNumber));

                        List<ContestOrderDetail> twoUserSelectedSameNumberList = totalMap.values().stream().filter(x -> x.size() > 1).flatMap(List::stream).collect(Collectors.toList());

                        if (!CollectionUtils.isEmpty(twoUserSelectedSameNumberList)) {

                            List<ContestOrderDetail> finalAutomatedList3 = automatedUsers.stream().filter(x -> twoUserSelectedSameNumberList.stream().noneMatch(y -> Objects.equals(y.getContestChooseNumber(), x.getContestChooseNumber()))).collect(Collectors.toList());

                            List<Long> automatedTwoWinners = getRandomWinnerUsers(finalAutomatedList3, 2);
                            List<ContestOrderDetail> tempAutomatedList = automatedUsers.stream().filter(x -> x.getId().equals(automatedTwoWinners.get(0))).collect(Collectors.toList());

                            List<Long> sameNumberUser = twoUserSelectedSameNumberList.stream().map(ContestOrderDetail::getId).collect(Collectors.toList());
                            List<ContestOrderDetail> winnerUser = originalUsers.stream().filter(x -> !sameNumberUser.contains(x.getId())).collect(Collectors.toList());
                            winnerUser.get(0).setUserWin(true);
                            winnerUser.add(automatedUsers.stream().filter(x -> x.getUserId().equals(automatedTwoWinners.get(1))).findFirst().get());

                            populateRandomOneUsersWinner(contestDetail, tempAutomatedList, winnerUser);

                        } else {
                            //SC2  user might chose diff number
                            List<ContestOrderDetail> finalAutomatedList3 = automatedUsers.stream().filter(x -> originalUsers.stream().noneMatch(y -> Objects.equals(y.getContestChooseNumber(), x.getContestChooseNumber()))).collect(Collectors.toList());

                            List<Long> originalWinners = getRandomWinnerUsers(originalUsers, 1);
                            List<Long> automatedTwoWinners = getRandomWinnerUsers(finalAutomatedList3, 2);

                            List<ContestOrderDetail> winnerUser = originalUsers.stream().filter(x -> originalWinners.contains(x.getUserId())).collect(Collectors.toList());
                            winnerUser.get(0).setUserWin(true);
                            winnerUser.add(automatedUsers.stream().filter(x -> x.getUserId().equals(automatedTwoWinners.get(1))).findFirst().get());
                            List<ContestOrderDetail> tempAutomatedList = automatedUsers.stream().filter(x -> x.getId().equals(automatedTwoWinners.get(0))).collect(Collectors.toList());
                            populateRandomOneUsersWinner(contestDetail, tempAutomatedList, winnerUser);

                        }

                        break;

                    case 4:

                        Map<Long, List<ContestOrderDetail>> fourUserMap = originalUsers.stream().collect(groupingBy(ContestOrderDetail::getContestChooseNumber));


                        List<ContestOrderDetail> pairList = fourUserMap.values().stream().filter(x -> x.size() == 2).map(x -> x.stream().findFirst().get()).collect(Collectors.toList());
                        if (pairList.size() == 2) {
                            //SC1 pair of user selected same number
                            // 1 1 2 2
                            List<Long> choseNumberList = pairList.stream().map(ContestOrderDetail::getContestChooseNumber).collect(Collectors.toList());
                            Long randomWiningNumber = getRandomWiningNumber(choseNumberList, 1).get(0);
                            List<ContestOrderDetail> twoUserHaveSameNumber = originalUsers.stream().filter(x -> x.getContestChooseNumber().equals(randomWiningNumber)).collect(Collectors.toList());

                            List<ContestOrderDetail> finalAutomatedList3 = automatedUsers.stream().filter(x -> twoUserHaveSameNumber.stream().noneMatch(y -> Objects.equals(y.getContestChooseNumber(), x.getContestChooseNumber()))).collect(Collectors.toList());

                            populateRandomTwoUsersWinner(contestDetail, finalAutomatedList3, twoUserHaveSameNumber);

                        } else if (pairList.size() == 1) {
                            //SC2 pair of users- > one pair having same number and other pair having diff number
                            //1 2 3 3
                            List<Long> chosePairNumberList = pairList.stream().map(ContestOrderDetail::getContestChooseNumber).collect(Collectors.toList());
                            List<ContestOrderDetail> nonPairUsers = originalUsers.stream().filter(x -> chosePairNumberList.stream().noneMatch(y -> Objects.equals(y, x.getContestChooseNumber()))).collect(Collectors.toList());

                            List<ContestOrderDetail> finalAutomatedList3 = automatedUsers.stream().filter(x -> originalUsers.stream().noneMatch(y -> Objects.equals(y.getContestChooseNumber(), x.getContestChooseNumber()))).collect(Collectors.toList());

                            populateThreeWinners(contestDetail, finalAutomatedList3, nonPairUsers, pairList);


                        } else {
                            //SC3 all having diff number
                            //1 2 3 3
                            populateIfFourUserSelectedDiffNumbers(contestDetail, automatedUsers, originalUsers);

                        }


                    default:

                }

                contestOrderDetailRepository.saveAll(contestOrderDetails);
                contestDetailRepository.save(contestDetail);
            } // End here contest details

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateIfFourUserSelectedDiffNumbers(ContestDetail contestDetail, List<ContestOrderDetail> automatedUsers, List<ContestOrderDetail> originalUsers) {
        List<ContestOrderDetail> finalAutomatedList3 = automatedUsers.stream().filter(x -> originalUsers.stream().noneMatch(y -> Objects.equals(y.getContestChooseNumber(), x.getContestChooseNumber()))).collect(Collectors.toList());

        List<Long> randomAutoWinnerUsers = getRandomWinnerUsers(finalAutomatedList3, 2);
        List<Long> randomOriginalWinnerUsers = getRandomWinnerUsers(originalUsers, 1);

        List<ContestOrderWinner> winnerList = new ArrayList<>();

        int count = 1;
        for (long userId : randomAutoWinnerUsers) {
            if (count == 1) {
                winnerList.add(ContestOrderWinner.builder().winnerUserId(userId).winningRank(WinningRank.FIRST).winnerAmount(((contestDetail.getEntryFee() * 5) * 95) / 100).build());
                count++;
            } else {
                winnerList.add(ContestOrderWinner.builder().winnerUserId(userId).winningRank(WinningRank.SECOND).winnerAmount(((contestDetail.getEntryFee() * 2.5) * 95) / 100).build());
            }
        }
        ContestOrderDetail secondOriginalWinner = originalUsers.stream().filter(x -> x.getUserId().equals(randomOriginalWinnerUsers.get(0))).findFirst().get();
        secondOriginalWinner.setUserWin(true);
        winnerList.add(ContestOrderWinner.builder().winnerUserId(secondOriginalWinner.getUserId()).winningRank(WinningRank.THIRD).winnerAmount(((contestDetail.getEntryFee() * 1.5) * 95) / 100).build());
        contestDetail.setOrderWinnerList(winnerList);
    }

    private void populateThreeWinners(ContestDetail contestDetail, List<ContestOrderDetail> finalAutomatedList3, List<ContestOrderDetail> nonPairUsers, List<ContestOrderDetail> pairList) {

        List<ContestOrderWinner> winnerList = new ArrayList<>();

        List<Long> firstAutomatedWinner = getRandomWinnerUsers(finalAutomatedList3, 1);
        List<Long> secondOriginalWinner = getRandomWinnerUsers(nonPairUsers, 1);

        //Automated should be first
        winnerList.add(ContestOrderWinner.builder().winnerUserId(firstAutomatedWinner.get(0)).winningRank(WinningRank.FIRST).winnerAmount(((contestDetail.getEntryFee() * 5) * 95) / 100).build());

        nonPairUsers.stream().filter(x -> Objects.equals(x.getUserId(), secondOriginalWinner.get(0))).findFirst().get().setUserWin(true);
        winnerList.add(ContestOrderWinner.builder().winnerUserId(secondOriginalWinner.get(0)).winningRank(WinningRank.SECOND).winnerAmount(((contestDetail.getEntryFee() * 2.5) * 95) / 100).build());

        // set 3rd Actual user
        for (ContestOrderDetail orderDetail : pairList) {
            orderDetail.setUserWin(true);
            winnerList.add(ContestOrderWinner.builder().winnerUserId(orderDetail.getUserId()).winningRank(WinningRank.THIRD).winnerAmount(((contestDetail.getEntryFee() * 1.5) * 95) / 100).build());
        }
        contestDetail.setOrderWinnerList(winnerList);

    }

    private void populateAllAutomatedUsersWinner(ContestDetail contestDetail, List<ContestOrderDetail> automatedUsers) {

        List<Long> winnerUserIds = getRandomWinnerUsers(automatedUsers, 3);
        int count = 1;
        List<ContestOrderWinner> winnerList = new ArrayList<>();
        for (Long userId : winnerUserIds) {

            if (count == 1) {
                winnerList.add(ContestOrderWinner.builder().winnerUserId(userId).winningRank(WinningRank.FIRST).winnerAmount(((contestDetail.getEntryFee() * 5) * 95) / 100).build());
                count++;
            }

            if (count == 2) {
                winnerList.add(ContestOrderWinner.builder().winnerUserId(userId).winningRank(WinningRank.SECOND).winnerAmount(((contestDetail.getEntryFee() * 2.5) * 95) / 100).build());
                count++;
            }

            if (count == 3) {
                winnerList.add(ContestOrderWinner.builder().winnerUserId(userId).winningRank(WinningRank.THIRD).winnerAmount(((contestDetail.getEntryFee() * 1.5) * 95) / 100).build());
                count++;
            }
        }
        contestDetail.setOrderWinnerList(winnerList);
    }

    private void populateOneUsersWinner(ContestDetail contestDetail, List<ContestOrderDetail> automatedUsers, List<ContestOrderDetail> originalUsers) {
        List<Long> originalWinners = getRandomWinnerUsers(originalUsers, 1);
        List<Long> automatedWinners = getRandomWinnerUsers(automatedUsers, 2);
        List<ContestOrderWinner> winnerList = new ArrayList<>();
        //set automated 1st and 2nd
        int count = 1;
        for (Long userId : automatedWinners) {

            if (count == 1) {
                winnerList.add(ContestOrderWinner.builder().winnerUserId(userId).winningRank(WinningRank.FIRST).winnerAmount(((contestDetail.getEntryFee() * 5) * 95) / 100).build());
                count++;
            }

            if (count == 2) {
                winnerList.add(ContestOrderWinner.builder().winnerUserId(userId).winningRank(WinningRank.SECOND).winnerAmount(((contestDetail.getEntryFee() * 2.5) * 95) / 100).build());
                count++;
            }
        }
        // set 3rd Actual user
        for (Long userId : originalWinners) {
            originalUsers.stream().filter(x -> x.getId().equals(userId)).forEach(x -> x.setUserWin(true));
            winnerList.add(ContestOrderWinner.builder().winnerUserId(userId).winningRank(WinningRank.THIRD).winnerAmount(((contestDetail.getEntryFee() * 1.5) * 95) / 100).build());
            count++;
        }
        contestDetail.setOrderWinnerList(winnerList);
    }

    //originalUsers list having one actual winner and one automated user
    //automatedUsers list having single random automated record
    private void populateRandomOneUsersWinner(ContestDetail contestDetail, List<ContestOrderDetail> automatedUsers, List<ContestOrderDetail> originalUsers) {


        List<ContestOrderWinner> winnerList = new ArrayList<>();
        //set automated 1st
        winnerList.add(ContestOrderWinner.builder().winnerUserId(automatedUsers.get(0).getUserId()).winningRank(WinningRank.FIRST).winnerAmount(((contestDetail.getEntryFee() * 5) * 95) / 100).build());

        List<Long> randomOneUserWinner = getRandomWinnerUsers(originalUsers, 2);

        //set automated 2nd and 3rd
        int count = 1;
        for (Long userId : randomOneUserWinner) {

            if (count == 1) {
                winnerList.add(ContestOrderWinner.builder().winnerUserId(userId).winningRank(WinningRank.SECOND).winnerAmount(((contestDetail.getEntryFee() * 2.5) * 95) / 100).build());
                count++;
            }

            if (count == 2) {
                winnerList.add(ContestOrderWinner.builder().winnerUserId(userId).winningRank(WinningRank.THIRD).winnerAmount(((contestDetail.getEntryFee() * 1.5) * 95) / 100).build());
                count++;
            }
        }

        contestDetail.setOrderWinnerList(winnerList);
    }

    private void populateRandomTwoUsersWinner(ContestDetail contestDetail, List<ContestOrderDetail> automatedUsers, List<ContestOrderDetail> originalUsers) {

        List<ContestOrderWinner> winnerList = new ArrayList<>();
        List<Long> automatedWinners = getRandomWinnerUsers(automatedUsers, 2);
        //set automated 1st and 2nd
        int count = 1;
        for (Long userId : automatedWinners) {

            if (count == 1) {
                winnerList.add(ContestOrderWinner.builder().winnerUserId(userId).winningRank(WinningRank.FIRST).winnerAmount(((contestDetail.getEntryFee() * 5) * 95) / 100).build());
                count++;
            }

            if (count == 2) {
                winnerList.add(ContestOrderWinner.builder().winnerUserId(userId).winningRank(WinningRank.SECOND).winnerAmount(((contestDetail.getEntryFee() * 2.5) * 95) / 100).build());
                count++;
            }

        }

        //two original user chosen same number then both are 3 rd winner
        for (ContestOrderDetail user : originalUsers) {
            user.setUserWin(true);
            winnerList.add(ContestOrderWinner.builder().winnerUserId(user.getUserId()).winningRank(WinningRank.THIRD).winnerAmount(((contestDetail.getEntryFee() * 1.5) * 95) / 100).build());

        }

        contestDetail.setOrderWinnerList(winnerList);
    }

    /*private void populatePairUsersWinnerAsThird(ContestDetail contestDetail, List<ContestOrderDetail> automatedUsers, List<ContestOrderDetail> originalUsers) {

        List<ContestOrderWinner> winnerList = new ArrayList<>();
        //set automated 1st
        winnerList.add(ContestOrderWinner.builder().winnerUserId(automatedUsers.get(0).getUserId()).winningRank(WinningRank.FIRST).winnerAmount(((contestDetail.getEntryFee() * 5) * 95) / 100).build());

        List<Long> randomOneUserWinner = getRandomWinnerUsers(originalUsers, 2);

        //set automated 2nd and 3rd
        int count = 2;
        for (ContestOrderDetail orderDetail : originalUsers) {

            if (count == 2) {
                winnerList.add(ContestOrderWinner.builder().winnerUserId(orderDetail.getUserId()).winningRank(WinningRank.SECOND).winnerAmount(((contestDetail.getEntryFee() * 2.5) * 95) / 100).build());
                count++;
            } else if (count == 3) {
                winnerList.add(ContestOrderWinner.builder().winnerUserId(orderDetail.getUserId()).winningRank(WinningRank.THIRD).winnerAmount(((contestDetail.getEntryFee() * 1.5) * 95) / 100).build());
                count++;
            }
        }
        contestDetail.setOrderWinnerList(winnerList);
    }*/

    public List<Long> getRandomWinnerUsers(List<ContestOrderDetail> contestOrderDetails, int winnerCount) {

        List<Long> uniqueUserList = new ArrayList<>();
        int count = 1;

        for (int i = 0; i < contestOrderDetails.size(); i++) {

            ContestOrderDetail contestOrder = contestOrderDetails.get(random.nextInt(contestOrderDetails.size()));

            if (!uniqueUserList.contains(contestOrder.getUserId())) {
                uniqueUserList.add(contestOrder.getUserId());
            }

            if (uniqueUserList.size() == winnerCount) return uniqueUserList;
        }
        return uniqueUserList;
    }

    public List<Long> getRandomWiningNumber(List<Long> longList, int winnerCount) {

        List<Long> uniqueUserList = new ArrayList<>();
        int count = 1;

        for (int i = 0; i < longList.size(); i++) {

            Long randomNumber = longList.get(random.nextInt(longList.size()));

            if (!uniqueUserList.contains(randomNumber)) {
                uniqueUserList.add(randomNumber);
            }

            if (uniqueUserList.size() == winnerCount) return uniqueUserList;
        }
        return uniqueUserList;
    }
}
