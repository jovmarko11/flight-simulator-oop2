package airtraffic.service;

import airtraffic.exception.FileParseException;
import airtraffic.exception.ValidationException;
import airtraffic.files.CsvHandler;
import airtraffic.files.FileHandler;
import airtraffic.files.JsonHandler;
import airtraffic.model.Airport;
import airtraffic.model.Flight;
import airtraffic.utils.DataBundle;

import java.io.File;

public class DataService {
    private AirportService airportService;
    private FlightService flightService;

    private FileHandler csvHandler = new CsvHandler();
    private FileHandler jsonHandler = new JsonHandler();

    public DataService(AirportService airportService, FlightService flightService) {
        this.airportService = airportService;
        this.flightService = flightService;
    }

    private void importFrom(FileHandler handler, File file, String formatName) {
        DataBundle bundle = handler.read(file);
        airportService.clearAll();
        flightService.clearAll();
        for (Airport a : bundle.getAirports()) {
            try{
                airportService.addAirport(a.getCode(), a.getName(), a.getX(), a.getY());
            }
            catch(ValidationException e){
                throw new FileParseException("Invalid airport data in: " + formatName + ": " + e.getMessage());
            }
        }

        for (Flight f : bundle.getFlights()) {
            try{
                flightService.addFlight(f.getFrom(), f.getTo(), f.getDepartureTime(), f.getDuration());
            }
            catch(ValidationException e){
                throw new FileParseException("Invalid flight data in " + formatName + ": " + e.getMessage());
            }
        }
    }

    private void exportTo(FileHandler handler, File file, String formatName) throws FileParseException {
        DataBundle bundle = new DataBundle(flightService.getFlights(), airportService.getAirports());
        handler.write(file, bundle);
    }

    public void importFromCsv(File f) {
        importFrom(csvHandler, f, "csv");
    }

    public void importFromJson(File f) {
        importFrom(jsonHandler, f, "json");
    }

    public void exportToCsv(File f) throws FileParseException {
        exportTo(csvHandler, f, "csv");
    }

    public void exportToJson(File f) throws FileParseException {
        exportTo(jsonHandler, f, "json");
    }
    //
}
