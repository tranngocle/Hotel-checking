package com.lake.lakeSidehotel.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lake.lakeSidehotel.exception.InvalidBookingRequestException;
import com.lake.lakeSidehotel.exception.ResourceNotFoundException;
import com.lake.lakeSidehotel.model.BookedRoom;
import com.lake.lakeSidehotel.model.Room;
import com.lake.lakeSidehotel.repository.BookingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService{
	
	private final BookingRepository bookingRepository;
	private final IRoomService roomService;

	public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
		
		return bookingRepository.findByRoomId(roomId);
	}

	@Override
	public void cancelBooking(Long bookingId) {
		bookingRepository.deleteById(bookingId);
	}

	@Override
	public String saveBooking(Long roomId, BookedRoom bookingRequest) {
		if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
			throw new InvalidBookingRequestException("Check in date must come before check-out date");
		}
		Room room = roomService.getRoomById(roomId).get();
		List<BookedRoom> existingBookings = room.getBookings();
		boolean roomIsAvailable = roomIsAvailable(bookingRequest,existingBookings);
		if(roomIsAvailable) {
			room.addBooking(bookingRequest);
			bookingRepository.save(bookingRequest);
		}else {
			throw new InvalidBookingRequestException("Sorry, This room is not available for the selected dates");
		}
		return bookingRequest.getBookingConfirmationCode();
	}

	private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
		
		return existingBookings.stream()
				.noneMatch(existingBooking ->
				bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
				|| bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
				|| (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
				&& bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
				|| (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
						
						&& bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
				|| (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
						&& bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))
				|| (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
						&& bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))
				|| (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
						&& bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))
				|| (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
						&& bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
				);
	}

	@Override
	public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
		
		return bookingRepository.findByBookingConfirmationCode(confirmationCode)
				.orElseThrow(() -> new ResourceNotFoundException("No booking found with booking code : "+confirmationCode));
	}

	@Override
	public List<BookedRoom> getAllBookings() {
		
		return bookingRepository.findAll();
	}

	@Override
	public List<BookedRoom> getBookingsByUserEmail(String email) {
		
		return bookingRepository.findByGuestEmail(email);
	}

}
