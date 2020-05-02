package com.github.morvell.lsn.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.morvell.lsn.domain.User;
import com.github.morvell.lsn.domain.UserSubscription;
import com.github.morvell.lsn.repo.UserDetailsRepository;

/**
 * @author Андрей Захаров
 * @created 29/04/2020
 */
@Service
public class ProfileService {
    private final UserDetailsRepository userDetailsRepo;

    @Autowired
    public ProfileService(UserDetailsRepository userDetailsRepo) {
        this.userDetailsRepo = userDetailsRepo;
    }

    public User changeSubscription(User channel, User subscriber) {
        List<UserSubscription> subcriptions = channel.getSubscribers()
                .stream()
                .filter(subscription ->
                        subscription.getSubscriber().equals(subscriber)
                )
                .collect(Collectors.toList());

        if (subcriptions.isEmpty()) {
            UserSubscription subscription = new UserSubscription(channel, subscriber);
            channel.getSubscribers().add(subscription);
        } else {
            channel.getSubscribers().removeAll(subcriptions);
        }

        return userDetailsRepo.save(channel);
    }
}
