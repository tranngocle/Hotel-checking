package com.lake.lakeSidehotel.reponse;

import java.math.BigDecimal;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class RoomResponse {
	
	private Long Id;
	private String roomType;
	private BigDecimal roomPrice;
	private boolean isBooked;
	private String photo;

	private List<BookingResponse> bookings;
	
	
	public RoomResponse(Long id, String roomType, BigDecimal roomPrice) {
		this.Id =id;
		this.roomType = roomType;
		this.roomPrice = roomPrice;
	}
	
	
	public RoomResponse(Long id, String roomType, BigDecimal roomPrice, boolean isBooked,
			byte[] photoBytes, List<BookingResponse> bookings) {
		this.Id = id;
		this.roomType = roomType;
		this.roomPrice = roomPrice;
		this.isBooked = isBooked;
		this.photo = photoBytes != null ? Base64.encodeBase64String(photoBytes) : null;
		this.bookings = bookings;
	}
	
}
