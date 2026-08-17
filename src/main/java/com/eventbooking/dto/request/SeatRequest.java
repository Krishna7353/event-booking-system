package com.eventbooking.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatRequest {
    private Long eventId;
    private String seatLabel;
    private Double price;
}