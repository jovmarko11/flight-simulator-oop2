package airtraffic.gui.dialogs;

import time.InactivityListener;

import javax.swing.*;
import java.awt.*;

public class InactivityDialog implements InactivityListener {

    private final JDialog dialog;
    private final JLabel msgLabel;
    private Runnable onContinue;   // sta da uradi kad korisnik klikne "Nastavi"

    public InactivityDialog(Frame owner) {
        dialog = new JDialog(owner, "Neaktivnost", false);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        msgLabel = new JLabel("", SwingConstants.CENTER);

        JButton continueButton = new JButton("Nastavi rad");
        continueButton.addActionListener(e -> {
            dialog.setVisible(false);
            if (onContinue != null) onContinue.run();   // npr. timer.reset()
        });

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.add(msgLabel, BorderLayout.CENTER);
        panel.add(continueButton, BorderLayout.SOUTH);

        dialog.setContentPane(panel);
        dialog.setSize(320, 130);
        dialog.setLocationRelativeTo(owner);
    }

    // Postavlja akciju za Nastavi dugme naknadno
    public void setOnContinue(Runnable onContinue) {
        this.onContinue = onContinue;
    }

    // Sakrij prozor (kad korisnik postane aktivan na neki drugi nacin)
    public void hideDialog() {
        SwingUtilities.invokeLater(() -> dialog.setVisible(false));
    }

    @Override
    public void onWarning(long secondsLeft) {
        // onTick dolazi iz Clock niti -> sve sto dira Swing ide na EDT
        SwingUtilities.invokeLater(() -> {
            msgLabel.setText("Program se zatvara za " + secondsLeft + " s.");
            if (!dialog.isVisible()) dialog.setVisible(true);
        });
    }

    @Override
    public void onLimitReached() {
        SwingUtilities.invokeLater(() -> System.exit(0));
    }
}
