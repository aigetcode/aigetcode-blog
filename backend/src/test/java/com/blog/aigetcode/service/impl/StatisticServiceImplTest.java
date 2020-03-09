package com.blog.aigetcode.service.impl;

import com.blog.aigetcode.DTO.StatisticDto;
import com.blog.aigetcode.entity.StatisticsAmountUsersByMonth;
import com.blog.aigetcode.repositories.AmountUsersRepository;
import com.blog.aigetcode.service.PostService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
@SpringBootTest
class StatisticServiceImplTest {

    @Mock
    private AmountUsersRepository amountUsersRepository;

    @Autowired
    private PostService postService;

    private StatisticServiceImpl statisticService;

    @BeforeEach
    public void init() {
        initMocks(this);
        statisticService = new StatisticServiceImpl(amountUsersRepository, postService);
    }

    @Test
    void getTotalVisitorsByMonthShouldBePassedTest() {
        List<StatisticsAmountUsersByMonth> statisticsAmountUserByMonths = createAmountUsers();
        when(amountUsersRepository.findAllByOrderByCreateAtDesc()).thenReturn(statisticsAmountUserByMonths);

        StatisticDto statisticDTO = statisticService.getTotalVisitorsByMonth();
        Assert.assertNotNull(statisticDTO);
    }

    private List<StatisticsAmountUsersByMonth> createAmountUsers() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        List<StatisticsAmountUsersByMonth> statisticsAmountUsersByMonthList = new LinkedList<>();

        for (int i = 0; i < 7; i++) {
            if (month < 0) {
                month = 12;
            }

            StatisticsAmountUsersByMonth statisticsAmountUsersByMonth = new StatisticsAmountUsersByMonth();
            statisticsAmountUsersByMonth.setMonth(StatisticsAmountUsersByMonth.AmountUsersMonth.valueOfLabel(month));
            statisticsAmountUsersByMonth.setAmount(0L);
            statisticsAmountUsersByMonthList.add(statisticsAmountUsersByMonth);
            month--;
        }
        StatisticsAmountUsersByMonth statisticsAmountUsersByMonth = new StatisticsAmountUsersByMonth();
        statisticsAmountUsersByMonth.setAmount(0L);
        statisticsAmountUsersByMonth.setMonth(StatisticsAmountUsersByMonth.AmountUsersMonth.TOTAL);
        statisticsAmountUsersByMonthList.add(statisticsAmountUsersByMonth);
        return statisticsAmountUsersByMonthList;
    }

    @Test
    void addVisitorShouldBePassedTest() {
        Random random = new Random();
        LinkedList<StatisticsAmountUsersByMonth> statisticsAmountUsersByMonthList = new LinkedList<>();

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        long totalAmount = 0;
        for (int i = 0; i < 7; i++) {
            long amount = random.nextInt(100);
            totalAmount += amount;

            calendar.add(Calendar.MONTH, -i);
            int currentMonth = calendar.get(Calendar.MONTH);
            statisticsAmountUsersByMonthList.add(new StatisticsAmountUsersByMonth(amount, StatisticsAmountUsersByMonth.AmountUsersMonth.valueOfLabel(currentMonth) ));
        }
        statisticsAmountUsersByMonthList.add(new StatisticsAmountUsersByMonth(totalAmount, StatisticsAmountUsersByMonth.AmountUsersMonth.TOTAL));

        when(amountUsersRepository.findAllByMonth(any(StatisticsAmountUsersByMonth.AmountUsersMonth.class))).thenReturn(statisticsAmountUsersByMonthList.getLast());
        when(amountUsersRepository.findAllByOrderByCreateAtDesc()).thenReturn(statisticsAmountUsersByMonthList);
        when(amountUsersRepository.save((any()))).thenReturn(new StatisticsAmountUsersByMonth(100L, StatisticsAmountUsersByMonth.AmountUsersMonth.valueOfLabel(calendar.get(Calendar.MONTH)) ));
        when(amountUsersRepository.saveAll(Collections.singleton(any()))).thenReturn(statisticsAmountUsersByMonthList);

        StatisticDto oldStatisticDTO = statisticService.getTotalVisitorsByMonth();
        Long oldTotalMonth = oldStatisticDTO.getTotalVisitors() + 1L;
        statisticService.addVisitor();
        StatisticDto newStatisticDTO = statisticService.getTotalVisitorsByMonth();

        Assert.assertEquals(oldTotalMonth, newStatisticDTO.getTotalVisitors());
    }
}

