package airtraffic.utils;

import airtraffic.model.Airport;

import java.util.List;

public class AirportUtils {
    private AirportUtils() {}

    public static Airport findByCode(List<Airport> airports, String code) {
        for (Airport a : airports) {
            if (a.getCode().equals(code))
                return a;
        }
        return null;
    }
}
