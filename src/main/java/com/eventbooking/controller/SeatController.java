package com.eventbooking.controller;

import com.eventbooking.dto.request.SeatRequest;
import com.eventbooking.dto.response.SeatResponse;
import com.eventbooking.service.SeatLockService;
import com.eventbooking.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;
    private final SeatLockService seatLockService;

    @PostMapping
    public SeatResponse createSeat(@RequestBody SeatRequest request) {
        return seatService.createSeat(request);
    }

    @GetMapping("/event/{eventId}")
    public List<SeatResponse> getSeatsByEvent(@PathVariable Long eventId) {
        return seatService.getSeatsByEvent(eventId);
    }

    @PostMapping("/{eventId}/{seatId}/lock")
    public Map<String, Object> lockSeat(@PathVariable Long eventId,
                                        @PathVariable Long seatId,
                                        Authentication authentication) {
        String userEmail = authentication.getName();
        boolean success = seatLockService.tryLockSeat(eventId, seatId, userEmail);

        if (!success) {
            String currentHolder = seatLockService.getLockHolder(eventId, seatId);
            throw new RuntimeException("Seat already locked by another user: " + currentHolder);
        }

        return Map.of(
                "eventId", eventId,
                "seatId", seatId,
                "status", "LOCKED",
                "lockedBy", userEmail
        );
    }

    @DeleteMapping("/{eventId}/{seatId}/lock")
    public Map<String, String> releaseSeat(@PathVariable Long eventId,
                                           @PathVariable Long seatId,
                                           Authentication authentication) {
        String userEmail = authentication.getName();
        seatLockService.releaseLock(eventId, seatId, userEmail);
        return Map.of("status", "RELEASED");
    }
}