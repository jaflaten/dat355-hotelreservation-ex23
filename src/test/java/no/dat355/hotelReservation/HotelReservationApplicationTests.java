package no.dat355.hotelReservation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.print.Book;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HotelReservationApplicationTests {

	Room room;
	Hotel hotel;
	HotelCompany hCompany;
	Booking firstBooking, secondBooking;
	Person person;
	@BeforeEach
	void setup() {
		hCompany = new HotelCompany("RadCompanyName");
		hotel = new Hotel("RadHotel",  "radStreet 333", hCompany);
		room = new Room("100", "3rd, 355", hotel);
		person = new Person("Donald Duck", "Andeby");
		LocalDate now = LocalDate.now();
		firstBooking = new Booking(now.toString(), now.plusDays(2).toString(),
				"15:00", "12:00", "1", "1234-5678-1234-9876", person);
		secondBooking = new Booking(now.toString(), now.plusDays(2).toString(),
				"15:00", "12:00", "2", "1234-5678-1234-9876", person);

	}

	@Test
	public void bookAndCancelRoom() {
		boolean booked = room.addBooking(firstBooking);

		assertTrue(booked);
		assertEquals(room.getStateFullName(), "booked");
		// Room has been booked

		boolean unbooked = room.removeBooking(firstBooking);
		assertTrue(unbooked);
		assertNotEquals(room.getStateFullName(), "booked");
		assertEquals(room.getStateFullName(), "available");
		// Room has been cancelled and is now available
	}

	@Test
	public void bookRoomCheckInAndOut() {
		boolean booked = room.addBooking(secondBooking);

		assertTrue(booked);
		assertEquals(room.getStateFullName(), "booked");
		// Room has been booked

		boolean checkedIn = room.checkIn();
		assertTrue(checkedIn);
		assertEquals(room.getStateFullName(), "checkedIn");
		// checked in

		boolean checkedOut = room.checkOut();
		assertTrue(checkedOut);
		assertNotEquals(room.getStateFullName(), "chekedIn");
		assertEquals(room.getStateFullName(), "checkedOut");
		//checked out
	}
}
