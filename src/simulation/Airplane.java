package simulation;

import airtraffic.model.Flight;

public class Airplane {
    private static int nextId = 1;
    private final int id;
    private final Flight flight;
    private final int actualDepartureMin;

    public enum State { WAITING, FLYING, LANDED }
    private State state;
    private double x, y;

    public Airplane(Flight flight, int actualDepartureMin) {
        this.id = nextId++;
        this.flight = flight;
        this.actualDepartureMin = actualDepartureMin;
        this.x = flight.getFrom().getX();
        this.y = flight.getFrom().getY();
        this.state = State.WAITING;
    }

    public void update(double simMinutes) {
        double dep = actualDepartureMin;
        double arr = dep + flight.getDuration();

        if (simMinutes < dep) {
            state = State.WAITING;
            x = flight.getFrom().getX();
            y = flight.getFrom().getY();
            return;
        }
        if (simMinutes >= arr) {
            state = State.LANDED;
            x = flight.getTo().getX();
            y = flight.getTo().getY();
            return;
        }
        state = State.FLYING;
        double p = (simMinutes - dep) / flight.getDuration();
        x = flight.getFrom().getX() + p * (flight.getTo().getX() - flight.getFrom().getX());
        y = flight.getFrom().getY() + p * (flight.getTo().getY() - flight.getFrom().getY());
    }

    public Flight getFlight() { return flight; }
    public int getId() { return id; }
    public State getState() { return state; }
    public double getX() { return x; }
    public double getY() { return y; }
}