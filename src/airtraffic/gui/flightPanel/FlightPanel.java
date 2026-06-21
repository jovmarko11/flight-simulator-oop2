package airtraffic.gui.flightPanel;

import airtraffic.exception.ValidationException;
import airtraffic.gui.dialogs.ErrorDialog;
import airtraffic.gui.MainFrame;
import airtraffic.gui.Theme;
import airtraffic.model.Airport;
import airtraffic.service.AirportService;
import airtraffic.service.FlightService;
import airtraffic.utils.Validator;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.util.ArrayList;

public class FlightPanel extends JPanel {
    private AirportService airportService;
    private FlightService flightService;
    private MainFrame mainFrame;
    private FlightTableModel flightTableModel;

    private JPanel formP;
    private JPanel flightP;
    private JPanel timeP;

    private JPanel tableP;
    private JTable table;
    private JScrollPane scrollPane;
    private JPanel buttonP;

    private JComboBox<Airport> departureCB;
    private JComboBox<Airport> arrivalCB;
    private JTextField departureTF = new JTextField();
    private JTextField durationTF = new JTextField();

    private JButton submitBtn;
    private JButton deleteBtn;

    public FlightPanel(FlightService flightService, AirportService airportService, MainFrame mainFrame) {
        this.flightService = flightService;
        this.airportService = airportService;
        this.mainFrame = mainFrame;

        initializeComponents();
        refreshTable();
        refreshAirports();
    }

    private void initializeFormPanel(){
        formP = new JPanel(new GridLayout(2, 1));
        initializeFlightPanel();
        initializeTimePanel();
        formP.add(flightP);
        formP.add(timeP);
    }

    private void initializeFlightPanel(){
        flightP = new JPanel(new GridLayout(2, 2));
        flightP.setBorder(BorderFactory.createEmptyBorder(Theme.PANEL_PADDING, Theme.PANEL_PADDING,Theme.LABEL_FIELD_GAP, Theme.PANEL_PADDING));
        flightP.setBackground(Theme.PANEL_BG);
        JLabel departureL = new JLabel("Polazak");
        JLabel arrivalL = new JLabel("Dolazak");

        departureL.setFont(Theme.UI_FONT);
        arrivalL.setFont(Theme.UI_FONT);

        departureCB = new JComboBox<>();
        arrivalCB = new JComboBox<>();

        flightP.add(departureL);
        flightP.add(departureCB);
        flightP.add(arrivalL);
        flightP.add(arrivalCB);
    }

    private void initializeTimePanel(){
        timeP = new JPanel(new GridLayout(2, 3));
        timeP.setBorder(BorderFactory.createEmptyBorder(Theme.LABEL_FIELD_GAP, Theme.PANEL_PADDING, Theme.PANEL_PADDING, Theme.PANEL_PADDING));
        timeP.setBackground(Theme.PANEL_BG);
        JLabel departureL = new JLabel("Polazak:");
        JLabel durationL = new JLabel("Trajanje");
        JLabel timeUnitL = new JLabel("(HH:MM)");
        JLabel minutesL = new JLabel("(minuti)");

        departureL.setFont(Theme.UI_FONT);
        durationL.setFont(Theme.UI_FONT);
        timeUnitL.setFont(Theme.SMALL_FONT);
        minutesL.setFont(Theme.SMALL_FONT);

        timeP.add(departureL);
        timeP.add(departureTF);
        timeP.add(timeUnitL);

        timeP.add(durationL);
        timeP.add(durationTF);
        timeP.add(minutesL);
    }

    private void initializeButtonPanel(){
        buttonP = new JPanel(new GridLayout(1, 2));
        buttonP.setBackground(Theme.PANEL_BG);
        buttonP.setBorder(BorderFactory.createEmptyBorder(8, Theme.PANEL_PADDING, 8, Theme.PANEL_PADDING));
        initializeSubmitBtn();
        initializeDeleteBtn();
        buttonP.add(deleteBtn);
        buttonP.add(submitBtn);
    }

    private void initializeSubmitBtn(){
        submitBtn = new JButton("Dodaj");
        Theme.styleButton(submitBtn, Theme.PRIMARY_BTN_BG);

        submitBtn.addActionListener((e)->{
            try{
                Airport from = (Airport) departureCB.getSelectedItem();
                Airport to = (Airport) arrivalCB.getSelectedItem();
                if (from == null || to == null){
                    throw new ValidationException("Nema dostupnih aerodroma. Dodajte aerodrome pre kreiranja leta.");
                }
                LocalTime departureTime = Validator.validateDeparture(departureTF.getText());
                int duration = Validator.parseInt(durationTF.getText(), "Trajanje");
                flightService.addFlight(from, to, departureTime, duration);
                mainFrame.refreshAll();
                durationTF.setText("");
                departureTF.setText("");
            }
            catch(Exception ex){
                ErrorDialog.showError(this, ex.getMessage());
            }
        });
    }

    private void initializeDeleteBtn(){
        deleteBtn = new JButton("Ukloni");
        Theme.styleButton(deleteBtn, Theme.ALERT_RED);

        deleteBtn.addActionListener((e)->{
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0){
                int modelRow = table.convertRowIndexToModel(selectedRow);
                flightService.removeFlightAt(modelRow);
                mainFrame.refreshAll();
            }
        });
    }

    private void initializeTablePanel(){
        flightTableModel = new FlightTableModel(new ArrayList<>());
        table = new JTable(flightTableModel);
        Theme.styleTable(table);

        scrollPane = new JScrollPane(table);
        tableP = new JPanel(new BorderLayout());
        tableP.setBackground(Theme.PANEL_BG);
        tableP.add(scrollPane, BorderLayout.CENTER);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        initializeFormPanel();
        initializeButtonPanel();
        initializeTablePanel();

        add(tableP, BorderLayout.CENTER);
        add(formP, BorderLayout.NORTH);
        add(buttonP, BorderLayout.SOUTH);
    }

    public void refreshTable() {    flightTableModel.refreshTable(flightService.getFlights());  }

    public void refreshAirports() {
        departureCB.removeAllItems();
        arrivalCB.removeAllItems();
        for (Airport a : airportService.getAirports()){
            departureCB.addItem(a);
            arrivalCB.addItem(a);
        }
    }

}
