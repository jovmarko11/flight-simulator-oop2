package airtraffic.service;

import airtraffic.exception.ValidationException;
import airtraffic.model.Airport;
import airtraffic.model.Flight;
import airtraffic.utils.Validator;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FlightService {

    private List<Flight> flights = new ArrayList<Flight>();

    public void addFlight(Airport from, Airport to, LocalTime departureTime, int duration) throws ValidationException {
        Validator.validateFlightRoute(from.getCode(), to.getCode());
        Validator.validateDuration(duration);

        Flight f = new Flight(from, to, departureTime, duration);
        flights.add(f);
    }

    public List<Flight> getFlights() { return new ArrayList<>(flights); }

    public void removeFlightAt(int index){
        if (index < 0 || index >= flights.size())
            throw new IndexOutOfBoundsException("index out of bounds");
        flights.remove(index);
    }

    public void clearAll() { flights.clear(); }
}
