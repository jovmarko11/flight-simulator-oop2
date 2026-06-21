package airtraffic.gui;

import airtraffic.gui.dialogs.ErrorDialog;
import airtraffic.service.DataService;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileMenu extends JMenuBar {
    private DataService dataService;
    private MainFrame owner;
    private JMenu fileMenu;
    private JMenuItem importCsv;
    private JMenuItem exportCsv;
    private JMenuItem importJson;
    private JMenuItem exportJson;

    public FileMenu(DataService dataService, MainFrame owner){
        this.owner = owner;
        this.dataService = dataService;
        initializeMenu();
        addActionListeners();
        add(fileMenu);
    }

    private void initializeMenu(){
        fileMenu = new JMenu("Fajl");
        fileMenu.setFont(Theme.UI_FONT);
        fileMenu.setBackground(Theme.TABLE_HEADER_BG);
        fileMenu.setForeground(Theme.TABLE_HEADER_FG);

        importCsv = new JMenuItem("Uvezi CSV");
        exportCsv = new JMenuItem("Izvezi CSV");
        importJson = new JMenuItem("Uvezi JSON");
        exportJson = new JMenuItem("Izvezi JSON");

        importCsv.setFont(Theme.UI_FONT);
        exportCsv.setFont(Theme.UI_FONT);
        importJson.setFont(Theme.UI_FONT);
        exportJson.setFont(Theme.UI_FONT);

        fileMenu.add(importCsv);
        fileMenu.add(importJson);
        fileMenu.add(exportCsv);
        fileMenu.add(exportJson);
    }

    private void addActionListeners(){
        importCsv.addActionListener(e ->{
           JFileChooser chooser = new JFileChooser();
           int result = chooser.showOpenDialog(this);
           if (result == JFileChooser.APPROVE_OPTION){
               File f = chooser.getSelectedFile();
               try{
                   dataService.importFromCsv(f);
                   owner.refreshAll();
               }
               catch(Exception ex){
                   ErrorDialog.showError(this, ex.getMessage());
               }
           }
        });

        importJson.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                try{
                    dataService.importFromJson(f);
                    owner.refreshAll();
                }
                catch(Exception ex){
                    ErrorDialog.showError(this, ex.getMessage());
                }
            }
        });

        exportCsv.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                try{
                    File f = chooser.getSelectedFile();
                    dataService.exportToCsv(f);
                }
                catch(Exception ex){
                    ErrorDialog.showError(this, ex.getMessage());
                }
            }
        });

        exportJson.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                try{
                    File f = chooser.getSelectedFile();
                    dataService.exportToJson(f);
                }
                catch(Exception ex){
                    ErrorDialog.showError(this, ex.getMessage());
                }
            }
        });
    }

}
