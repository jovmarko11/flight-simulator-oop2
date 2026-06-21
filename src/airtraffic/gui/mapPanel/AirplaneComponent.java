package airtraffic.gui.mapPanel;

import airtraffic.gui.Theme;
import simulation.Airplane;

import java.awt.*;

public class AirplaneComponent {
    private final Airplane airplane;

    public AirplaneComponent(Airplane airplane) {
        this.airplane = airplane;
    }

    public void draw(Graphics g,int sx, int sy){
        int r = Theme.AIRPLANE_RADIUS;
        g.setColor(Theme.AIRPLANE_BG);
        g.fillOval(sx - r, sy - r, 2 * r, 2 * r);
    }
}
