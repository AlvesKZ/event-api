package com.eventapi.event_api.repositories;

import com.eventapi.event_api.domain.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    Optional<Address> findByEventId(UUID eventId);
}
