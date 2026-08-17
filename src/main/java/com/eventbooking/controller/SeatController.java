package com.eventbooking.controller;

import com.eventbooking.dto.request.SeatRequest;
import com.eventbooking.dto.response.SeatResponse;
import com.eventbooking.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @PostMapping
    public SeatResponse createSeat(@RequestBody SeatRequest request) {
        return seatService.createSeat(request);
    }

    @GetMapping("/event/{eventId}")
    public List<SeatResponse> getSeatsByEvent(@PathVariable Long eventId) {
        return seatService.getSeatsByEvent(eventId);
    }
}