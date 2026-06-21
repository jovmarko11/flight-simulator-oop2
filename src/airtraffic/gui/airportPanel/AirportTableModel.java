package airtraffic.gui.airportPanel;

import airtraffic.model.Airport;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class AirportTableModel extends AbstractTableModel {
    private List<Airport> airports;
    private final String[] columnNames = {"Kod", "Naziv", "X", "Y"};

    public AirportTableModel(List<Airport> airports ) { this.airports = new ArrayList<>(airports); }

    public void refreshTable(List<Airport> newData) {
        this.airports = new ArrayList<>(newData);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return airports.size();
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
            case 0 -> airports.get(rowIndex).getCode();
            case 1 -> airports.get(rowIndex).getName();
            case 2 -> airports.get(rowIndex).getX();
            case 3 -> airports.get(rowIndex).getY();
            default -> throw new IndexOutOfBoundsException("AirportTableModel.getValueAt() - index out of bounds");
        };
    }
}
