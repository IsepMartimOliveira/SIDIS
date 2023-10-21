package com.example.psoft_22_23_project.subscriptionsmanagement.api;

import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class SubscriptionsViewMapper {

    @Mapping(source="plan", target= "planName")
    @Mapping(source="description", target= "planDescription")
    @Mapping(source="user", target= "username")
    @Mapping(source="subscriptions.startDate.startDate", target= "startDate")
    @Mapping(source="subscriptions.endDate.endDate", target= "endDate")
    @Mapping(source="subscriptions.paymentType.paymentType", target= "paymentType")
    @Mapping(source="subscriptions.activeStatus.active", target= "activeStatus")
    public abstract SubscriptionsView toSubscriptionView(Subscriptions subscriptions);

    public abstract Iterable<SubscriptionsView> toSubscriptionsView(Iterable<Subscriptions> subscriptions);

    public Integer mapOptInt(final Optional<Integer> i) {
        return i.orElse(null);
    }

    public Long mapOptLong(final Optional<Long> i) {
        return i.orElse(null);
    }

    public String mapOptString(final Optional<String> i) {
        return i.orElse(null);
    }
}
