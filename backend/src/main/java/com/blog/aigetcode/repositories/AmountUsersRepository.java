package com.blog.aigetcode.repositories;

import com.blog.aigetcode.entity.StatisticsAmountUsersByMonth;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AmountUsersRepository extends CrudRepository<StatisticsAmountUsersByMonth, Long> {

    StatisticsAmountUsersByMonth findAllByMonth(StatisticsAmountUsersByMonth.AmountUsersMonth month);
    List<StatisticsAmountUsersByMonth> findAllByOrderByCreateAtDesc();

}
