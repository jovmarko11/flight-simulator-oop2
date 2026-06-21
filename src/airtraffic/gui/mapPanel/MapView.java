package airtraffic.gui.mapPanel;

import airtraffic.model.Airport;
import simulation.SimulationEngine;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MapView extends JPanel {
    private SimulationEngine engine;
    private MapPanel mapP;
    private AirportFilterPanel airportFilterP;
    private SimulationControlPanel simulationControlP;

    public MapView(SimulationEngine engine){
        this.engine = engine;
        setLayout(new BorderLayout());

        initializeComponents();

        add(mapP, BorderLayout.CENTER);
        add(airportFilterP, BorderLayout.EAST);
        add(simulationControlP, BorderLayout.NORTH);
    }

    public MapPanel getMapPanel() { return mapP;}
    public SimulationControlPanel getControlPanel() { return simulationControlP;}

    private void initializeComponents(){
        mapP = new MapPanel();
        airportFilterP = new AirportFilterPanel(mapP);
        simulationControlP = new SimulationControlPanel(engine);
    }

    public void setAirports(List<Airport> airports){
        mapP.setAirports(airports);
        airportFilterP.refresh(mapP.getAirportComponents());
    }
}
