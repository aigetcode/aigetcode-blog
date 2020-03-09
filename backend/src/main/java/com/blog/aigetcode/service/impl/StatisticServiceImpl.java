package com.blog.aigetcode.service.impl;

import com.blog.aigetcode.DTO.StatisticDto;
import com.blog.aigetcode.entity.StatisticsAmountUsersByMonth;
import com.blog.aigetcode.repositories.AmountUsersRepository;
import com.blog.aigetcode.service.PostService;
import com.blog.aigetcode.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticServiceImpl implements StatisticService {

    private AmountUsersRepository amountUsersRepository;
    private PostService postService;

    @Autowired
    public StatisticServiceImpl(AmountUsersRepository amountUsersRepository,
                                PostService postService) {
        this.amountUsersRepository = amountUsersRepository;
        this.postService = postService;
    }

    @Override
    public StatisticDto getTotalVisitorsByMonth() {
        Long countPosts = postService.getCountPosts();
        List<StatisticsAmountUsersByMonth> statisticsAmountUsersByMonthTotalList = amountUsersRepository.findAllByOrderByCreateAtDesc();
        List<Long> collectAmount = new ArrayList<>();

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < statisticsAmountUsersByMonthTotalList.size() - 1; i++) {
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, -(i));
            int month = calendar.get(Calendar.MONTH);
            Optional<StatisticsAmountUsersByMonth> statByMonth = statisticsAmountUsersByMonthTotalList.stream()
                    .filter(sm -> sm.getMonth().getValue().equals(month))
                    .findFirst();
            statByMonth.ifPresent(statisticsAmountUsersByMonth -> collectAmount.add(statisticsAmountUsersByMonth.getAmount()));
        }

        Collections.reverse(collectAmount);
        Long totalCountVisit = statisticsAmountUsersByMonthTotalList.stream()
                .mapToLong(StatisticsAmountUsersByMonth::getAmount).sum();
        List<Long> countPostsByMonth = postService.getCountPostsByMonth();
        Map<String, List<Long>> statistics = new HashMap<>();
        statistics.put("Create post by month", countPostsByMonth);
        statistics.put("Count visit by month", collectAmount);

        StatisticDto statisticDTO = new StatisticDto();
        statisticDTO.setTotalCountPosts(countPosts);
        statisticDTO.setTotalVisitors(totalCountVisit);
        statisticDTO.setStatisticByMonth(statistics);
        return statisticDTO;
    }

    @Override
    public void addVisitor() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);

        List<StatisticsAmountUsersByMonth> statisticsAmountUsersByMonthTotalList = amountUsersRepository.findAllByOrderByCreateAtDesc(); // order by createAt
        List<StatisticsAmountUsersByMonth> checkExistMonth = statisticsAmountUsersByMonthTotalList.stream()
                                                .filter(amUs -> amUs.getMonth().getValue() == month)
                                                .collect(Collectors.toList());
        if (!checkExistMonth.isEmpty()) {
            StatisticsAmountUsersByMonth statisticsAmountUsersByMonth = checkExistMonth.get(0);
            Long amountVisitorByMonth = statisticsAmountUsersByMonth.getAmount() + 1;
            statisticsAmountUsersByMonth.setAmount(amountVisitorByMonth);
            amountUsersRepository.save(statisticsAmountUsersByMonth);
        } else {
            List<StatisticsAmountUsersByMonth> newStatisticsAmountUsersByMonthSite = new LinkedList<>();
            for (int i = 1; i < statisticsAmountUsersByMonthTotalList.size(); i++) {
                StatisticsAmountUsersByMonth statisticsAmountUsersByMonthFor = statisticsAmountUsersByMonthTotalList.get(i);
                if (statisticsAmountUsersByMonthTotalList.get(i).getMonth() == StatisticsAmountUsersByMonth.AmountUsersMonth.TOTAL) {
                    Long totalAmountUser = statisticsAmountUsersByMonthTotalList.stream()
                            .filter(amount -> amount.getMonth() == StatisticsAmountUsersByMonth.AmountUsersMonth.TOTAL)
                            .mapToLong(StatisticsAmountUsersByMonth::getAmount).sum();

                    statisticsAmountUsersByMonthFor.setAmount(totalAmountUser + statisticsAmountUsersByMonthTotalList.get(0).getAmount());
                }
                newStatisticsAmountUsersByMonthSite.add(statisticsAmountUsersByMonthFor);
            }
            StatisticsAmountUsersByMonth statisticsAmountUsersByMonth = new StatisticsAmountUsersByMonth(1L, StatisticsAmountUsersByMonth.AmountUsersMonth.valueOfLabel(month));
            newStatisticsAmountUsersByMonthSite.add(statisticsAmountUsersByMonth);
            amountUsersRepository.saveAll(newStatisticsAmountUsersByMonthSite);
        }
    }

}
