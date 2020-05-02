package com.github.morvell.lsn.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Андрей Захаров
 * @created 02/05/2020
 */
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSubscriptionId implements Serializable {

    @JsonView(Views.Id.class)
    private String channelId;

    @JsonView(Views.Id.class)
    private String subscriberId;
}
