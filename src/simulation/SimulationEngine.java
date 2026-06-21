package simulation;

import airtraffic.model.Airport;
import airtraffic.model.Flight;
import airtraffic.service.FlightService;
import time.Clock;
import time.ClockObservable;

import java.time.LocalTime;
import java.util.*;

public class SimulationEngine implements ClockObservable {

    public enum State { STOPPED,    RUNNING,    PAUSED }

    private static final int RATIO = 10;   // 1 realna sekunda = 10 sim-minuta

    private final FlightService flightService;
    private final FlightController flightController = new FlightController(this);
    private final List<Airplane> airplanes = new ArrayList<>();

    private double simMinutes = 0;
    private State state = State.STOPPED;

    private Runnable onUpdate;   // -> mapa.repaint() + panel.refresh()
    private Runnable onStart;    // -> inactivity.pause()
    private Runnable onStop;     // -> inactivity.resume()

    public SimulationEngine(FlightService flightService) {
        this.flightService = flightService;
        Clock.getInstance().addObserver(this);
    }

    public synchronized void start(){
        if (state == State.RUNNING) return;

        if (state == State.STOPPED){
            airplanes.clear();
            simMinutes = 0;
            flightController.schedule(flightService.getFlights());
        }

        state = State.RUNNING;
        if (onStart != null)
            onStart.run();
    }

    public synchronized void pause(){
        if (state != State.RUNNING) return;
        state = State.PAUSED;
        if (onStop != null)
            onStop.run();
    }

    public synchronized void reset(){
        state = State.STOPPED;
        simMinutes = 0;
        airplanes.clear();
        flightController.reset();
        if (onStop != null) onStop.run();
        if (onUpdate != null) onUpdate.run();
    }


    @Override
    public synchronized void onTick(long sec) {
        if (state != State.RUNNING) return;

        simMinutes += 0.2 * RATIO;
        flightController.update(simMinutes);

        for (Airplane a : airplanes){
            a.update(simMinutes);
        }
        if (onUpdate != null)
            onUpdate.run();
    }

    public void spawn(Flight f, int departMin){
        airplanes.add(new Airplane(f, departMin));
    }


    private static int toMinutes(LocalTime t) { return t.getHour() * 60 + t.getMinute(); }


    public synchronized List<Airplane> getAirplanes() { return new ArrayList<>(airplanes); }
    public double getSimMinutes() { return simMinutes; }
    public State getState() { return state; }

    public void setOnUpdate(Runnable r) { this.onUpdate = r; }
    public void setOnStart(Runnable r) { this.onStart = r; }
    public void setOnStop(Runnable r) { this.onStop = r; }
}
