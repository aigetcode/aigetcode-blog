package com.blog.aigetcode.config;

import com.blog.aigetcode.entity.StatisticsAmountUsersByMonth;
import com.blog.aigetcode.entity.Authority;
import com.blog.aigetcode.entity.User;
import com.blog.aigetcode.repositories.AmountUsersRepository;
import com.blog.aigetcode.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class DataLoader implements ApplicationRunner {

    private UserRepository userRepository;
    private AmountUsersRepository amountUsersRepository;
    private BCryptPasswordEncoder bcryptEncoder;

    @Autowired
    public DataLoader(UserRepository userRepository, AmountUsersRepository amountUsersRepository) {
        this.userRepository = userRepository;
        this.amountUsersRepository = amountUsersRepository;
        this.bcryptEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public void run(ApplicationArguments applicationArguments) {
        this.createUsers();
        this.createAmountUsers();
    }

    private void createAmountUsers() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);

        for (int i = 0; i < 7; i++) {
            if (month < 0) {
                month = 12;
            }

            StatisticsAmountUsersByMonth statisticsAmountUsersByMonth = new StatisticsAmountUsersByMonth();
            statisticsAmountUsersByMonth.setAmount(0L);
            statisticsAmountUsersByMonth.setMonth(StatisticsAmountUsersByMonth.AmountUsersMonth.valueOfLabel(month));
            checkSaveAmountUsers(statisticsAmountUsersByMonth);
            month--;
        }
        StatisticsAmountUsersByMonth statisticsAmountUsersByMonth = new StatisticsAmountUsersByMonth();
        statisticsAmountUsersByMonth.setAmount(0L);
        statisticsAmountUsersByMonth.setMonth(StatisticsAmountUsersByMonth.AmountUsersMonth.TOTAL);
        checkSaveAmountUsers(statisticsAmountUsersByMonth);
    }

    private void checkSaveAmountUsers(StatisticsAmountUsersByMonth users) {
        List<StatisticsAmountUsersByMonth> amountUser = new LinkedList<>();
        Iterable<StatisticsAmountUsersByMonth> userIterable = amountUsersRepository.findAll();
        for (StatisticsAmountUsersByMonth amount : userIterable) {
            amountUser.add(amount);
        }

        List<StatisticsAmountUsersByMonth> result = amountUser.stream()
                .filter(statisticsAmountUsersByMonth -> statisticsAmountUsersByMonth.getMonth().getValue().equals(users.getMonth().getValue()))
                .collect(Collectors.toList());
        if (result.isEmpty()) {
            amountUsersRepository.save(users);
        }
    }

    private void createUsers() {
        List<User> users = new ArrayList<>();
        Iterable<User> userIterable = userRepository.findAll();
        for (User user : userIterable) {
            users.add(user);
        }

        String passwordAdmin = bcryptEncoder.encode("123");

        User admin_1 = new User();
        admin_1.setFullName("Test test");
        admin_1.setEmail("test@test.com");
        admin_1.setLocale("ru");
        admin_1.setActive(true);
        admin_1.setPassword(passwordAdmin);

        Set<Authority> authorities = new HashSet<>();
        authorities.add(new Authority(Authority.Role.ADMIN));
        admin_1.setAuthorities(authorities);
        checkSaveUser(users, admin_1);
    }

    private void checkSaveUser(List<User> users, User admin) {
        List<User> result = users.stream()
                .filter(user -> Objects.equals(user.getFullName(), admin.getFullName()))
                .collect(Collectors.toList());
        if (result.isEmpty()) {
            userRepository.save(admin);
        }
    }
}
