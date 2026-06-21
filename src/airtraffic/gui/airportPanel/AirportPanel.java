package airtraffic.gui.airportPanel;

import airtraffic.gui.dialogs.ErrorDialog;
import airtraffic.gui.MainFrame;
import airtraffic.gui.Theme;
import airtraffic.model.Airport;
import airtraffic.service.AirportService;
import airtraffic.utils.Validator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AirportPanel extends JPanel {
    private AirportService airportService;
    private MainFrame mainFrame;
    private AirportTableModel airportTableModel;

    private JPanel formP;
    private JPanel btnP;
    private JPanel tableP;
    private JTable table;
    private JScrollPane scrollPane;

    private JTextField kodTF;
    private JTextField nameTF;
    private JTextField xTF;
    private JTextField yTF;

    private JButton submitBtn;
    private JButton deleteBtn;

    public AirportPanel(AirportService as, MainFrame mainFrame){
        this.airportService = as;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        initializeComponents();
    }

    private void initializeForm(){
        formP = new JPanel(new GridLayout(4, 2, 8, 8));

        // Labels
        JLabel kodL = new JLabel("Kod");
        JLabel nameL = new JLabel("Name");
        JLabel xL = new JLabel("X");
        JLabel yL = new JLabel("Y");

        // Labels font
        kodL.setFont(Theme.UI_FONT);
        nameL.setFont(Theme.UI_FONT);
        xL.setFont(Theme.UI_FONT);
        yL.setFont(Theme.UI_FONT);

        // TF
        kodTF = new JTextField();
        nameTF = new JTextField();
        xTF = new JTextField();
        yTF = new JTextField();

        formP.add(kodL);
        formP.add(kodTF);
        formP.add(nameL);
        formP.add(nameTF);
        formP.add(xL);
        formP.add(xTF);
        formP.add(yL);
        formP.add(yTF);
    }


    private void initializeButtonPanel(){
        btnP = new JPanel(new GridLayout(1, 2, 8, 8));
        initializeSubmitBtn();
        initializeDeleteBtn();
        btnP.add(deleteBtn);
        btnP.add(submitBtn);
    }

    private void initializeSubmitBtn(){
        submitBtn = new JButton("Dodaj");
        Theme.styleButton(submitBtn, Theme.PRIMARY_BTN_BG);

        submitBtn.addActionListener((e)->{
            try{
                String kod = kodTF.getText();
                String name = nameTF.getText();
                double x = Validator.parseDouble(xTF.getText(), "X koordinata");
                double y = Validator.parseDouble(yTF.getText(), "Y koordinata");
                airportService.addAirport(kod, name, x, y);
                mainFrame.refreshAll();
                kodTF.setText("");
                nameTF.setText("");
                xTF.setText("");
                yTF.setText("");
            } catch (Exception ex) {
                ErrorDialog.showError(this, ex.getMessage());
            }
        });
    }
    private void initializeDeleteBtn(){
        deleteBtn = new JButton("Ukloni");
        Theme.styleButton(deleteBtn, Theme.ALERT_RED);

        deleteBtn.addActionListener((e)->{
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0 && selectedRow < table.getRowCount()) {
                int modelRow = table.convertRowIndexToModel(selectedRow);
                Airport selected = airportService.getAirports().get(modelRow);
                airportService.removeAirport(selected);
                mainFrame.refreshAll();
            }
        });
    }

    private void initializeTablePanel(){
        airportTableModel = new AirportTableModel(new ArrayList<>());
        table = new JTable(airportTableModel);
        Theme.styleTable(table);

        scrollPane = new JScrollPane(table);
        tableP =  new JPanel(new BorderLayout());
        tableP.setBackground(Theme.PANEL_BG);
        tableP.add(scrollPane, BorderLayout.CENTER);
    }

    private void initializeComponents(){
        initializeForm();
        initializeButtonPanel();
        initializeTablePanel();
        this.add(formP, BorderLayout.NORTH);
        this.add(btnP, BorderLayout.SOUTH);
        this.add(tableP, BorderLayout.CENTER);
    }

    public void refreshTable(){airportTableModel.refreshTable(airportService.getAirports());}
}
