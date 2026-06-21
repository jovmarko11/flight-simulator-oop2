package airtraffic.gui.mapPanel;

import airtraffic.gui.Theme;
import airtraffic.gui.dialogs.ErrorDialog;
import airtraffic.model.Airport;
import simulation.Airplane;
import simulation.SimulationEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MapPanel extends JPanel {
    private List<AirportComponent> components;
    private Timer blinkTimer;
    private AirportComponent selected;
    private boolean blinkState = false;

    private SimulationEngine engine;

    private Runnable onSelect;
    private Runnable onDeselect;

    private void fireSelect() { if (onSelect != null) onSelect.run(); }
    private void fireDeselect() { if (onDeselect != null) onDeselect.run(); }

    public void setOnSelect(Runnable r) { this.onSelect = r; }
    public void setOnDeselect(Runnable r) { this.onDeselect = r; }

    public MapPanel() {
        components = new ArrayList<AirportComponent>();
        setBackground(Theme.WINDOW_BG);

        blinkTimer = new Timer(500, e ->{
            blinkState = !blinkState;
            if (selected != null) selected.setBlinkState(blinkState);
            repaint();
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    AirportComponent clicked = getAirportAt(e.getX(), e.getY());

                    if (selected != null && clicked == selected) {
                        selected.setSelected(false);
                        selected.setBlinkState(false);
                        selected = null;
                        blinkTimer.stop();
                        fireDeselect();
                    } else if (clicked != null) {
                        if (selected != null) {
                            selected.setSelected(false);
                            selected.setBlinkState(false);
                        }
                        selected = clicked;
                        selected.setSelected(true);
                        blinkTimer.start();
                        fireSelect();
                    } else {
                        if (selected != null) {
                            selected.setBlinkState(false);
                            selected.setSelected(false);
                            selected = null;
                            blinkTimer.stop();
                            fireDeselect();
                        }
                    }
                    repaint();
                }
                catch (Exception ex) {
                    String msg = ex.getMessage() != null ? ex.getMessage() : "Došlo je do greške pri interakciji sa mapom.";
                    ErrorDialog.showError(MapPanel.this, msg);
                }
            }
        });
    }

    public void setAirports(List<Airport> airports){
        if (selected != null) {
            selected = null;
            blinkTimer.stop();
            blinkState = false;
            fireDeselect();
        }
        components.clear();
        for (Airport a : airports){
            AirportComponent airportComponent = new AirportComponent(a);
            components.add(airportComponent);
        }
        repaint();
    }

    // crta sve vidljive aerodrome pozivajuci draw metodu AirportComponent klase uz afinu transformaciju koordinata na koordinatni sistem mape
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        for (AirportComponent component : components) {
            if (!component.isVisible()) continue;
            int sx = (int) toScreenX(component.getAirport().getX());
            int sy = (int) toScreenY(component.getAirport().getY());
            component.draw(g, sx, sy);
        }

        if (engine != null){
            for (Airplane a : engine.getAirplanes()) {
                if (a.getState() != Airplane.State.FLYING) continue;
                int sx = (int) toScreenX(a.getX());
                int sy = (int) toScreenY(a.getY());
                new AirplaneComponent(a).draw(g, sx, sy);
            }
        }
    }
    // afine transformacije koordinata
    private double toScreenX(double x){     return ((x + 180) / 360) * getWidth();     }
    private double toScreenY(double y){     return (1 -(y + 90) / 180) * getHeight();     }

    public List<AirportComponent> getAirportComponents() { return components; }

    private AirportComponent getAirportAt(int px, int py){
        int half = Theme.AIRPORTCOMPONENT_SIZE / 2;
        for (AirportComponent ac : components) {
            if (!ac.isVisible()) continue;
            int sx = (int) toScreenX(ac.getAirport().getX());
            int sy = (int) toScreenY(ac.getAirport().getY());
            if (Math.abs(px - sx) <= half && Math.abs(py - sy) <= half)
                return ac;
        }
        return null;
    }

    public void setEngine(SimulationEngine engine) {    this.engine = engine; }
}
