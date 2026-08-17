package com.eventbooking.dto.response;

import com.eventbooking.entity.SeatStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class SeatResponse {
    private Long id;
    private String seatLabel;
    private Double price;
    private SeatStatus status;
    private Long eventId;
}