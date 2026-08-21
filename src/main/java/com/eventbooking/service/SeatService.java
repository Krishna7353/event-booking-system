package com.eventbooking.service;

import com.eventbooking.dto.request.SeatRequest;
import com.eventbooking.dto.response.SeatResponse;
import com.eventbooking.entity.Event;
import com.eventbooking.entity.Seat;
import com.eventbooking.entity.SeatStatus;
import com.eventbooking.repository.EventRepository;
import com.eventbooking.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final EventRepository eventRepository;

    public SeatResponse createSeat(SeatRequest request) {
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + request.getEventId()));

        Seat seat = new Seat();
        seat.setEvent(event);
        seat.setSeatLabel(request.getSeatLabel());
        seat.setPrice(request.getPrice());
        seat.setStatus(SeatStatus.AVAILABLE);   // business rule lives here, not in the entity

        Seat saved = seatRepository.save(seat);
        return toResponse(saved);
    }

    public List<SeatResponse> getSeatsByEvent(Long eventId) {
        return seatRepository.findByEventId(eventId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Seat getSeatEntityById(Long seatId) {
        return seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found with id: " + seatId));
    }

    private SeatResponse toResponse(Seat seat) {
        return new SeatResponse(
                seat.getId(),
                seat.getSeatLabel(),
                seat.getPrice(),
                seat.getStatus(),
                seat.getEvent().getId()
        );
    }
}