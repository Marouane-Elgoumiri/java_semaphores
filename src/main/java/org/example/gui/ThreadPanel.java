package org.example.gui;

import org.example.model.WorkerThread;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class ThreadPanel extends JPanel {
    private final WorkerThread workerThread;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss.SSS");

    public ThreadPanel(WorkerThread workerThread) {
        this.workerThread = workerThread;
        setPreferredSize(new Dimension(150, 70));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(workerThread.isRunning() ? Color.BLUE : Color.GRAY);
        g.fillRect(10, 10, getWidth() - 20, getHeight() - 20);
        g.setColor(Color.WHITE);
        g.drawString("Thread " + workerThread.getId(), 20, 25);
        g.drawString(workerThread.getStatus(), 20, 40);
        g.drawString("Time: " + timeFormat.format(workerThread.getExecutionTime()), 20, 55);
    }
}
