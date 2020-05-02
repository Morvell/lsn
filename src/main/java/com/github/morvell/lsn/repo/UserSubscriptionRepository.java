package com.github.morvell.lsn.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.morvell.lsn.domain.User;
import com.github.morvell.lsn.domain.UserSubscription;
import com.github.morvell.lsn.domain.UserSubscriptionId;

/**
 * @author Андрей Захаров
 * @created 02/05/2020
 */
public interface UserSubscriptionRepository
        extends JpaRepository<UserSubscription, UserSubscriptionId> {

    List<UserSubscription> findBySubscriber(User user);

    List<UserSubscription> findByChannel(User channel);

    UserSubscription findByChannelAndSubscriber(User channel, User subscriber);
}
