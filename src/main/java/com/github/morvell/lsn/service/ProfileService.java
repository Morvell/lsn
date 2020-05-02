package com.github.morvell.lsn.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.morvell.lsn.domain.User;
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
        Set<User> subscribers = channel.getSubscribers();

        if (subscribers.contains(subscriber)) {
            subscribers.remove(subscriber);
        } else {
            subscribers.add(subscriber);
        }

        return userDetailsRepo.save(channel);
    }
}
