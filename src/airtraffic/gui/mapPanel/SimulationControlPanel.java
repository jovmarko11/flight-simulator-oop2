package airtraffic.gui.mapPanel;

import airtraffic.gui.Theme;
import airtraffic.gui.dialogs.ErrorDialog;
import simulation.SimulationEngine;

import javax.swing.*;
import java.awt.*;

public class SimulationControlPanel extends JPanel{

    private SimulationEngine engine;

    int t, hh, mm;

    private JPanel btnPanel;
    private JPanel infoP;
    private JPanel timer;
    private JLabel timeLabel;
    private JLabel statusLabel;

    private JButton startBtn;
    private JButton pauseBtn;
    private JButton resetBtn;

    public SimulationControlPanel(SimulationEngine engine) {
        this.engine = engine;
        setLayout(new GridLayout(1, 2));
        setBackground(Theme.PANEL_BG);
        setBorder(BorderFactory.createEmptyBorder(10, Theme.PANEL_PADDING, 5, Theme.PANEL_PADDING));


        initializeButtons();
        initializeTimer();

        this.add(infoP);
        this.add(btnPanel);
        refresh();
    }

    private void initializeButtons(){
        btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, Theme.BUTTON_GAP, 0));
        btnPanel.setBackground(Theme.PANEL_BG);

        initializeStartBtn();
        initializePauseBtn();
        initializeResetBtn();

        btnPanel.add(startBtn);
        btnPanel.add(pauseBtn);
        btnPanel.add(resetBtn);
    }
    private void initializeStartBtn(){
        startBtn = new JButton("Pokreni");
        Theme.styleControlButton(startBtn, Theme.SIM_START_BG);

        startBtn.addActionListener(e ->{
            try{
                engine.start();
                refresh();
            }
            catch (Exception ex){
                ErrorDialog.showError(this, ex.getMessage());
            }
        });
    }
    private void initializePauseBtn(){
        pauseBtn = new JButton("Pauza");
        Theme.styleControlButton(pauseBtn, Theme.SIM_PAUSE_BG);

        pauseBtn.addActionListener(e ->{
            try{
                engine.pause();
                refresh();
            }
            catch (Exception ex){
                ErrorDialog.showError(this, ex.getMessage());
            }
        });
    }
    private void initializeResetBtn(){
        resetBtn = new JButton("Reset");
        Theme.styleControlButton(resetBtn, Theme.SIM_RESET_BG);

        resetBtn.addActionListener(e ->{
            try{
                engine.reset();
                refresh();
            }
            catch (Exception ex){
                ErrorDialog.showError(this, ex.getMessage());
            }
        });
    }

    private void initializeTimer(){
        timer = new JPanel(new BorderLayout());
        timer.setBackground(Theme.PANEL_BG);

        JLabel headerL = new JLabel("VREME SIMULACIJE");
        headerL.setForeground(Theme.SIM_HEADER_FG);
        headerL.setFont(Theme.SIM_HEADER_FONT);

        timeLabel = new JLabel(hh + ":" + mm);
        timeLabel.setForeground(Theme.SIM_TIMER_FG);
        timeLabel.setFont(Theme.SIM_TIMER_FONT);

        statusLabel = new JLabel("Zaustavljeno");
        statusLabel.setFont(Theme.SIM_STATUS_FONT);

        timer.add(headerL, BorderLayout.NORTH);
        timer.add(timeLabel, BorderLayout.CENTER);

        infoP = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 0));
        infoP.setBackground(Theme.PANEL_BG);
        infoP.add(timer);
        infoP.add(statusLabel);
    }

    public void refresh(){
        SimulationEngine.State state = engine.getState();

        t = (int) engine.getSimMinutes();
        hh = (t / 60) % 24;
        mm = t % 60;

        timeLabel.setText(String.format("%02d:%02d", hh, mm));

        switch(state){
            case STOPPED -> {
                statusLabel.setText("Zaustavljeno");
                statusLabel.setForeground(Theme.STATUS_STOPPED_COLOR);
                statusLabel.setIcon(Theme.statusDot(Theme.STATUS_STOPPED_COLOR));
                startBtn.setText("Pokreni");
                Theme.setControlButtonEnabled(startBtn, true, Theme.SIM_START_BG);
                Theme.setControlButtonEnabled(pauseBtn, false, Theme.SIM_PAUSE_BG);
                Theme.setControlButtonEnabled(resetBtn, false, Theme.SIM_RESET_BG);
            }
            case RUNNING -> {
                statusLabel.setText("U toku");
                statusLabel.setForeground(Theme.STATUS_RUNNING_COLOR);
                statusLabel.setIcon(Theme.statusDot(Theme.STATUS_RUNNING_COLOR));
                startBtn.setText("Pokreni");
                Theme.setControlButtonEnabled(startBtn, false, Theme.SIM_START_BG);
                Theme.setControlButtonEnabled(pauseBtn, true, Theme.SIM_PAUSE_BG);
                Theme.setControlButtonEnabled(resetBtn, true, Theme.SIM_RESET_BG);
            }

            case PAUSED -> {
                statusLabel.setText("Pauzirano");
                statusLabel.setForeground(Theme.STATUS_PAUSED_COLOR);
                statusLabel.setIcon(Theme.statusDot(Theme.STATUS_PAUSED_COLOR));
                startBtn.setText("Nastavi");
                Theme.setControlButtonEnabled(startBtn, true, Theme.SIM_START_BG);
                Theme.setControlButtonEnabled(pauseBtn, false, Theme.SIM_PAUSE_BG);
                Theme.setControlButtonEnabled(resetBtn, true, Theme.SIM_RESET_BG);
            }
        }

    }

}
