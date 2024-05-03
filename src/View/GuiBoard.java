package View;

import Controller.ChessController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuiBoard {
    private final JFrame guiBoard;
    static int WINDOW_WIDTH = 600, WINDOW_HEIGHT = 600;

    public GuiBoard(ChessController.GuiBoardContains guiBoardContains) {
        this.guiBoard = new JFrame("Chess");
        this.guiBoard.setLayout(new BorderLayout());
        this.guiBoard.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        this.guiBoard.add(guiBoardContains, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton undoButton = new JButton("Undo");
        JButton redoButton = new JButton("Redo");

        buttonPanel.add(undoButton);
        buttonPanel.add(redoButton);

        this.guiBoard.add(buttonPanel, BorderLayout.SOUTH);


        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiBoardContains.undoMove();
            }
        });

        redoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiBoardContains.redoMove();
            }
        });

        this.guiBoard.setVisible(true);
    }
}
