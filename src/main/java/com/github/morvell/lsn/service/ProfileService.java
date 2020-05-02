package com.github.morvell.lsn.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.morvell.lsn.domain.User;
import com.github.morvell.lsn.domain.UserSubscription;
import com.github.morvell.lsn.repo.UserDetailsRepository;
import com.github.morvell.lsn.repo.UserSubscriptionRepository;

/**
 * @author Андрей Захаров
 * @created 29/04/2020
 */
@Service
public class ProfileService {
    private final UserDetailsRepository userDetailsRepo;

    private final UserSubscriptionRepository userSubscriptionRepo;

    @Autowired
    public ProfileService(UserDetailsRepository userDetailsRepo,
            UserSubscriptionRepository userSubscriptionRepo) {
        this.userDetailsRepo = userDetailsRepo;
        this.userSubscriptionRepo = userSubscriptionRepo;
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

    public List<UserSubscription> getSubscribers(User channel) {
        return userSubscriptionRepo.findByChannel(channel);
    }

    public UserSubscription changeSubscriptionStatus(User channel, User subscriber) {
        UserSubscription subscription = userSubscriptionRepo.findByChannelAndSubscriber(channel, subscriber);
        subscription.setActive(!subscription.isActive());

        return userSubscriptionRepo.save(subscription);
    }
}
