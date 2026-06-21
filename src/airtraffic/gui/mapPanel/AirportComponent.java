package airtraffic.gui.mapPanel;

import airtraffic.gui.Theme;
import airtraffic.model.Airport;

import javax.swing.*;
import java.awt.*;

public class AirportComponent {
    private Airport airport;
    private boolean selected;
    private boolean blinkState;
    private Color primaryC = Theme.AIRPORTCOMPONENT_BG;
    private Color selectedC = Theme.AIRPORTCOMPONENT_SELECTED;
    private boolean visible;

    public Color getSelectedC() {return selectedC;}
    public Color getPrimaryC() {return primaryC;}
    public boolean isBlinkState() {return blinkState;}
    public AirportComponent(Airport airport) {
        this.airport = airport; visible = true;
    }

    // prima vec izracunate piksele na mapi
    public void draw(Graphics g, int sx, int sy) {
        if (selected && blinkState){
            g.setColor(selectedC);
        }
        else{
            g.setColor(primaryC);
        }

        g.fillRect(sx - Theme.AIRPORTCOMPONENT_SIZE / 2,  sy - Theme.AIRPORTCOMPONENT_SIZE / 2, Theme.AIRPORTCOMPONENT_SIZE, Theme.AIRPORTCOMPONENT_SIZE);

        g.setColor(Theme.AIRPORTCOMPONENT_BG);
        g.drawString(airport.getCode(), (sx + Theme.AIRPORTCOMPONENT_SIZE / 2 + 4), (sy + 4));
    }

    public void setSelected(boolean selected) { this.selected = selected; }
    public boolean isSelected() { return selected; }
    public void setBlinkState(boolean blinkState) { this.blinkState = blinkState; }
    public Airport getAirport() { return airport; }

    public void setVisible(boolean visible) { this.visible = visible; }
    public boolean isVisible() { return visible; }

}
