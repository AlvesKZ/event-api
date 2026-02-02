package com.eventapi.event_api.services;

import com.eventapi.event_api.domain.coupon.Coupon;
import com.eventapi.event_api.domain.coupon.CouponRequestDTO;
import com.eventapi.event_api.domain.event.Event;
import com.eventapi.event_api.repositories.CouponRepository;
import com.eventapi.event_api.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private EventRepository eventRepository;

    public Coupon addCouponToEvent(UUID eventId, CouponRequestDTO data) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        Coupon coupon = new Coupon();
        coupon.setCode(data.code());
        coupon.setDiscount(data.discount());
        coupon.setValid(new Date(data.valid()));
        coupon.setEvent(event);

        return couponRepository.save(coupon);
    }
}
