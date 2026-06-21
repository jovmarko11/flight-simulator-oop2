package airtraffic.service;

import airtraffic.model.Airport;
import airtraffic.utils.AirportUtils;
import airtraffic.utils.Validator;

import java.util.ArrayList;
import java.util.List;

public class AirportService {
    private List<Airport> airports;

    public AirportService() {
        airports = new ArrayList<>();
    }

    public List<Airport> getAirports() { return new ArrayList<>(airports); }

    public void addAirport(String code, String name, double x, double y){
        List<String> existingCodes = new ArrayList<>();
        for (Airport airport : airports) {
            existingCodes.add(airport.getCode());
        }

        // validacioni blok
        Validator.validateAirportCode(code);
        Validator.validateCodeUnique(code, existingCodes);
        Validator.validateAirportName(name);
        Validator.validateCoordinates(x, y);

        // dodavanje
        airports.add(new Airport(name, code, x, y));
    }


    public void removeAirport(Airport a){
        airports.remove(a);
    }

    public Airport searchAirportByCode(String code){
        return AirportUtils.findByCode(airports, code);
    }

    public boolean checkExistance(String code){
        for (Airport a : airports){
            if (a.getCode().equals(code)){
                return true;
            }
        }
        return false;
    }

    public void clearAll (){
        airports.clear();
    }

}
