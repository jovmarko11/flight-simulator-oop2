package airtraffic.gui.mapPanel;

import airtraffic.gui.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AirportFilterPanel extends JPanel {
    private MapPanel mapP;
    private JScrollPane scrollPane;
    private JPanel checkBoxP;

    public void refresh(List<AirportComponent> components){
        checkBoxP.removeAll();
        for (AirportComponent ac : components) {
            checkBoxP.add(createCheckBox(ac));
        }
        checkBoxP.revalidate();
        checkBoxP.repaint();
    }

    public AirportFilterPanel(MapPanel mapP) {
        this.mapP = mapP;
        setLayout(new BorderLayout());
        setBackground(Theme.MIDNIGHT_NAVY);
        setPreferredSize(new Dimension(150, 0));
        initializeComponents();
    }

    private void initializeComponents() {
        initializeHeader();
        initializeScrollPane();
    }

    private void initializeHeader(){
        JLabel title = new JLabel ("Filter");
        title.setFont(Theme.UI_FONT);
        title.setForeground(Theme.TABLE_HEADER_FG);
        title.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 0));
        add(title, BorderLayout.NORTH);
    }

    private void initializeScrollPane(){
        checkBoxP = new JPanel();
        checkBoxP.setBackground(Theme.MIDNIGHT_NAVY);
        checkBoxP.setLayout(new BoxLayout(checkBoxP, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(checkBoxP);
        scrollPane.getViewport().setBackground(Theme.MIDNIGHT_NAVY);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JCheckBox createCheckBox(AirportComponent ac){
        JCheckBox cb = new JCheckBox(ac.getAirport().toString(), ac.isVisible());
        cb.setBackground(Theme.MIDNIGHT_NAVY);
        cb.setForeground(Theme.TABLE_HEADER_FG);

        cb.addItemListener(e -> {
            ac.setVisible(cb.isSelected());
            mapP.repaint();
        });
        return cb;
    }
}
