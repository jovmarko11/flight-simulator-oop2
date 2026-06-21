package airtraffic.gui.flightPanel;

import airtraffic.model.Flight;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class FlightTableModel extends AbstractTableModel {
    private List<Flight> flights;
    private String[] columnNames = {"From", "To", "Departure Time", "Arrival Time", "Duration"};

    public FlightTableModel(List<Flight> flights) { this.flights = new ArrayList<>(flights); }

    public void refreshTable(List<Flight> flights) {
        this.flights = new  ArrayList<>(flights);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return flights.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return switch (columnIndex) {
            case 0 -> flights.get(rowIndex).getFrom();
            case 1 -> flights.get(rowIndex).getTo();
            case 2 -> flights.get(rowIndex).getDepartureTime();
            case 3 -> flights.get(rowIndex).getArrivalTime();
            case 4 -> flights.get(rowIndex).getDuration();
            default -> throw new IndexOutOfBoundsException("FlightTableModel.getValueAt() - index out of bounds");
        };
    }
}
