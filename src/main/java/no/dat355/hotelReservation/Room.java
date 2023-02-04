package no.dat355.hotelReservation;/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// line 14 "model.ump"
// line 42 "model.ump"
public class Room {

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //Room Attributes
    private String costPerDay;
    private String floorArea;


    //Room Associations
    private List<Booking> bookings;
    private Hotel hotel;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Room(String aCostPerDay, String aFloorArea, Hotel aHotel) {
        costPerDay = aCostPerDay;
        floorArea = aFloorArea;
        setState(State.available);
        bookings = new ArrayList<Booking>();
        boolean didAddHotel = setHotel(aHotel);
        if (!didAddHotel) {
            throw new RuntimeException("Unable to create room due to hotel. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
        }
    }

    //------------------------
    // INTERFACE
    //------------------------

    public boolean setCostPerDay(String aCostPerDay) {
        boolean wasSet = false;
        costPerDay = aCostPerDay;
        wasSet = true;
        return wasSet;
    }

    public boolean setFloorArea(String aFloorArea) {
        boolean wasSet = false;
        floorArea = aFloorArea;
        wasSet = true;
        return wasSet;
    }

    public String getCostPerDay() {
        return costPerDay;
    }

    public String getFloorArea() {
        return floorArea;
    }

    /* Code from template association_GetMany */
    public Booking getBooking(int index) {
        Booking aBooking = bookings.get(index);
        return aBooking;
    }

    public List<Booking> getBookings() {
        List<Booking> newBookings = Collections.unmodifiableList(bookings);
        return newBookings;
    }

    public int numberOfBookings() {
        int number = bookings.size();
        return number;
    }

    public boolean hasBookings() {
        boolean has = bookings.size() > 0;
        return has;
    }

    public int indexOfBooking(Booking aBooking) {
        int index = bookings.indexOf(aBooking);
        return index;
    }

    /* Code from template association_GetOne */
    public Hotel getHotel() {
        return hotel;
    }

    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfBookings() {
        return 0;
    }

    /* Code from template association_AddManyToManyMethod */
    public boolean addBooking(Booking aBooking) {
        boolean wasAdded = false;
        if (bookings.contains(aBooking)) {
            return false;
        }
        bookings.add(aBooking);
        boolean booked = book();
        if (aBooking.indexOfRoom(this) != -1 && booked) {
            wasAdded = true;
        } else {
            wasAdded = aBooking.addRoom(this);
            if (!wasAdded) {
                bookings.remove(aBooking);
                cancel();
            }
        }
        return wasAdded;
    }

    /* Code from template association_RemoveMany */
    public boolean removeBooking(Booking aBooking) {
        boolean wasRemoved = false;
        if (!bookings.contains(aBooking)) {
            cancel();
            return wasRemoved;
        }

        int oldIndex = bookings.indexOf(aBooking);
        bookings.remove(oldIndex);
        cancel();
        if (aBooking.indexOfRoom(this) == -1) {
            wasRemoved = true;
        } else {
            wasRemoved = aBooking.removeRoom(this);
            if (!wasRemoved) {
                bookings.add(oldIndex, aBooking);
            }
        }
        return wasRemoved;
    }

    /* Code from template association_AddIndexControlFunctions */
    public boolean addBookingAt(Booking aBooking, int index) {
        boolean wasAdded = false;
        if (addBooking(aBooking)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfBookings()) {
                index = numberOfBookings() - 1;
            }
            bookings.remove(aBooking);
            bookings.add(index, aBooking);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean addOrMoveBookingAt(Booking aBooking, int index) {
        boolean wasAdded = false;
        if (bookings.contains(aBooking)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfBookings()) {
                index = numberOfBookings() - 1;
            }
            bookings.remove(aBooking);
            bookings.add(index, aBooking);
            wasAdded = true;
        } else {
            wasAdded = addBookingAt(aBooking, index);
        }
        return wasAdded;
    }

    /* Code from template association_SetOneToMany */
    public boolean setHotel(Hotel aHotel) {
        boolean wasSet = false;
        if (aHotel == null) {
            return wasSet;
        }

        Hotel existingHotel = hotel;
        hotel = aHotel;
        if (existingHotel != null && !existingHotel.equals(aHotel)) {
            existingHotel.removeRoom(this);
        }
        hotel.addRoom(this);
        wasSet = true;
        return wasSet;
    }

    public void delete() {
        ArrayList<Booking> copyOfBookings = new ArrayList<Booking>(bookings);
        bookings.clear();
        for (Booking aBooking : copyOfBookings) {
            aBooking.removeRoom(this);
        }
        Hotel placeholderHotel = hotel;
        this.hotel = null;
        if (placeholderHotel != null) {
            placeholderHotel.removeRoom(this);
        }
    }


    public String toString() {
        return super.toString() + "[" +
                "costPerDay" + ":" + getCostPerDay() + "," +
                "floorArea" + ":" + getFloorArea() + "]" + System.getProperties().getProperty("line.separator") +
                "  " + "hotel = " + (getHotel() != null ? Integer.toHexString(System.identityHashCode(getHotel())) : "null");
    }

    //Room State Machines
    public enum State {available, booked, checkedIn, checkedOut}

    private State state;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Room() {
        setState(State.available);
    }

    //------------------------
    // INTERFACE
    //------------------------

    public String getStateFullName() {
        String answer = state.toString();
        return answer;
    }

    public State getState() {
        return state;
    }

    public boolean book() {
        boolean wasEventProcessed = false;

        State aState = state;
        switch (aState) {
            case available:
                setState(State.booked);
                wasEventProcessed = true;
                break;
            default:
                // Other states do respond to this event
        }

        return wasEventProcessed;
    }

    public boolean cancel() {
        boolean wasEventProcessed = false;

        State aState = state;
        switch (aState) {
            case booked:
                setState(State.available);
                wasEventProcessed = true;
                break;
            default:
                // Other states do respond to this event
        }

        return wasEventProcessed;
    }

    public boolean checkIn() {
        boolean wasEventProcessed = false;

        State aState = state;
        switch (aState) {
            case booked:
                setState(State.checkedIn);
                wasEventProcessed = true;
                break;
            default:
                // Other states do respond to this event
        }

        return wasEventProcessed;
    }

    public boolean checkOut() {
        boolean wasEventProcessed = false;

        State aState = state;
        switch (aState) {
            case checkedIn:
                setState(State.checkedOut);
                wasEventProcessed = true;
                break;
            default:
                // Other states do respond to this event
        }

        return wasEventProcessed;
    }

    public boolean cleaning() {
        boolean wasEventProcessed = false;

        State aState = state;
        switch (aState) {
            case checkedOut:
                setState(State.available);
                wasEventProcessed = true;
                break;
            default:
                // Other states do respond to this event
        }

        return wasEventProcessed;
    }

    private void setState(State aState) {
        state = aState;
    }
}