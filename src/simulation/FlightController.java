package simulation;

import airtraffic.model.Airport;
import airtraffic.model.Flight;

import java.time.LocalTime;
import java.util.*;

public class FlightController {
    private final SimulationEngine engine;
    private final Map<Airport, Queue<Flight>> queues = new HashMap<>();

    public FlightController(SimulationEngine engine) { this.engine = engine; }


    public void schedule(List<Flight> flights) {
        queues.clear();

        List<Flight> sorted = new ArrayList<>(flights);
        sorted.sort(Comparator.comparingInt(this::departureMinutes));

        for (Flight f : sorted){
            Airport from = f.getFrom();
            if (!queues.containsKey(from)){
                queues.put(from, new LinkedList<>());
                from.setAvailable(true);
                from.setNextFreeMinute(0);
            }
            queues.get(from).add(f);
        }
    }

    public void update(double simMinutes){
        for (Airport a : queues.keySet()){
            Queue<Flight> queue = queues.get(a);
            if (queue.isEmpty()) continue;

            if (simMinutes >= a.getNextFreeMinute())
                a.setAvailable(true);

            Flight next = queue.peek();
            boolean timeHasCome = departureMinutes(next) <= simMinutes;

            if (a.isAvailable() && timeHasCome){
                queue.poll();
                engine.spawn(next, (int) simMinutes);
                a.setAvailable(false);
                a.setNextFreeMinute((int) simMinutes + 10);
            }
        }
    }

    public void reset(){
        for (Airport a : queues.keySet()){
            a.setAvailable(true);
            a.setNextFreeMinute(0);
        }
        queues.clear();
    }

    private int departureMinutes(Flight f){
        LocalTime t = f.getDepartureTime();
        return t.getHour() * 60 + t.getMinute();
    }
}
