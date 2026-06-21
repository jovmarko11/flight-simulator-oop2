package airtraffic.gui;

import airtraffic.gui.airportPanel.AirportPanel;
import airtraffic.gui.flightPanel.FlightPanel;
import airtraffic.gui.mapPanel.MapPanel;
import airtraffic.gui.mapPanel.MapView;
import airtraffic.gui.mapPanel.SimulationControlPanel;
import airtraffic.service.*;
import simulation.SimulationEngine;

import javax.swing.*;

public class MainFrame extends JFrame {
    private SimulationEngine engine;
    private AirportService airportService;
    private FlightService flightService;
    private DataService dataService;

    private AirportPanel airportPanel;
    private FlightPanel flightPanel;
    private MapView mapViewPanel;

    private JTabbedPane tabs;
    private JMenuBar menuBar;

    public MainFrame(AirportService as, FlightService fs,DataService ds, SimulationEngine eng){
        engine = eng;
        airportService = as;
        flightService = fs;
        dataService = ds;

        initializeComponents();
        configureMainFrame();
    }

    public MapPanel getMapPanel(){ return mapViewPanel.getMapPanel(); }
    public SimulationControlPanel getControlPanel() {return mapViewPanel.getControlPanel(); }

    private void configureMainFrame(){
        setTitle("Air Traffic Simulator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(tabs);
    }

    private void initializeComponents(){
        tabs = new JTabbedPane();
        tabs.setBackground(Theme.PANEL_BG);
        tabs.setForeground(Theme.MIDNIGHT_NAVY);
        tabs.setFont(Theme.UI_FONT);

        airportPanel = new AirportPanel(airportService, this);
        flightPanel = new FlightPanel(flightService, airportService, this);
        mapViewPanel = new MapView(engine);
        mapViewPanel.setAirports(airportService.getAirports());

        tabs.add("Aerodromi", airportPanel);
        tabs.add("Letovi", flightPanel);
        tabs.add("Mapa", mapViewPanel);

        menuBar = new FileMenu(dataService, this);
        setJMenuBar(menuBar);
    }

    public void refreshAll(){
        airportPanel.refreshTable();
        flightPanel.refreshTable();
        flightPanel.refreshAirports();
        mapViewPanel.setAirports(airportService.getAirports());
    }


}
