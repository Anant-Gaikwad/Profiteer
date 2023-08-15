package com.profiteer.core.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.profiteer.core.api.entity.Event;
import com.profiteer.core.api.entity.Status;

@Repository
public interface ColorNumberEventRepository extends JpaRepository<Event, Long> {

	Optional<Event> findByEventId(String eventId);

	Optional<Event> findByEventStatus(Status status);

}
