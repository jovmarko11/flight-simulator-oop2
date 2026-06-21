import airtraffic.gui.MainFrame;
import airtraffic.gui.dialogs.InactivityDialog;
import airtraffic.gui.mapPanel.MapPanel;
import airtraffic.service.AirportService;
import airtraffic.service.DataService;
import airtraffic.service.FlightService;
import simulation.SimulationEngine;
import time.Clock;
import time.InactivityTimer;

import javax.swing.*;
import java.awt.*;

/**
 * Ulazna tacka aplikacije (composition root).
 * Sastavlja servise, GUI i mehanizam neaktivnosti, i pokrece sat.
 */

public class Program {
    private AirportService airportService;
    private FlightService flightService;
    private DataService dataService;
    private MainFrame mainFrame;
    private SimulationEngine simulationEngine;
    private InactivityTimer timer;

    public Program() {
        airportService = new AirportService();
        flightService = new FlightService();
        dataService = new DataService(airportService, flightService);
    }

    public void run() {
        // Sav rad sa Swingom ide na EDT
        SwingUtilities.invokeLater(() -> {
            simulationEngine = new SimulationEngine(flightService);
            mainFrame = new MainFrame(airportService, flightService, dataService, simulationEngine);
            mainFrame.setVisible(true);

            setupInactivity();
            wireSimulationEngine();

            Clock.getInstance().activate();

        });
    }

    // Povezuje brojac neaktivnosti, dijalog upozorenja i globalno pracenje akcija
    private void setupInactivity() {
        InactivityDialog dialog = new InactivityDialog(mainFrame);
        timer = new InactivityTimer(dialog);

        // Nastavi rad i svaka akcija korisnika -> resetuj brojac i sakrij dijalog
        Runnable activity = () -> {
            timer.reset();
            dialog.hideDialog();
        };
        dialog.setOnContinue(activity);

        // Globalno hvatanje svake akcije korisnika (mis + tastatura)
        Toolkit.getDefaultToolkit().addAWTEventListener(
                e -> activity.run(),
                AWTEvent.MOUSE_EVENT_MASK
                        | AWTEvent.MOUSE_MOTION_EVENT_MASK
                        | AWTEvent.KEY_EVENT_MASK
        );
        MapPanel map = mainFrame.getMapPanel();
        map.setOnSelect(timer::pause);
        map.setOnDeselect(timer::resume);
    }

    private void wireSimulationEngine(){
        simulationEngine.setOnStart(timer::pause);
        simulationEngine.setOnStop(timer::resume);
        simulationEngine.setOnUpdate(()-> SwingUtilities.invokeLater(
                ()-> {
                    mainFrame.getMapPanel().repaint();
                    mainFrame.getControlPanel().refresh();
                }));
        mainFrame.getMapPanel().setEngine(simulationEngine);
    }

}
