package org.example.gui;

import org.example.model.Resource;
import org.example.model.WorkerThread;
import org.example.service.SemaphoreManager;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SemaphoreGUI extends JFrame {
    private static final int MAX_RESOURCES = 3;
    private final List<Resource> resources;
    private final List<WorkerThread> workerThreads;
    private final List<ResourcePanel> resourcePanels;
    private final List<ThreadPanel> threadPanels;
    private final SemaphoreManager semaphoreManager;
    private final Timer updateTimer;
    private final JLabel statsLabel;
    private JButton startButton;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss.SSS");

    public SemaphoreGUI() {
        this.startButton = startButton;
        setTitle("Semaphore Visualization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize collections
        resources = new ArrayList<>();
        workerThreads = new ArrayList<>();
        resourcePanels = new ArrayList<>();
        threadPanels = new ArrayList<>();

        // Initialize resources
        for (int i = 0; i < MAX_RESOURCES; i++) {
            resources.add(new Resource(i));
        }

        semaphoreManager = new SemaphoreManager(MAX_RESOURCES, resources);

        // Create UI components
        JPanel resourceSection = createResourceSection();
        JPanel threadSection = new JPanel(new FlowLayout());
        JPanel controlSection = createControlSection();
        JPanel statsSection = new JPanel(new FlowLayout());

        // Statistics label
        statsLabel = new JLabel("Total Execution Time: 00:00.000");
        statsSection.add(statsLabel);

        // Layout
        add(new JLabel("Resources (Semaphore permits: " + MAX_RESOURCES + ")",
                SwingConstants.CENTER), BorderLayout.NORTH);
        add(resourceSection, BorderLayout.CENTER);
        add(threadSection, BorderLayout.SOUTH);
        add(controlSection, BorderLayout.EAST);
        add(statsSection, BorderLayout.WEST);

        // Start update timer
        updateTimer = new Timer(100, e -> updateStats());
        updateTimer.start();

        setSize(1000, 400);
        setLocationRelativeTo(null);
    }

    private JPanel createResourceSection() {
        JPanel panel = new JPanel(new FlowLayout());
        for (Resource resource : resources) {
            ResourcePanel resourcePanel = new ResourcePanel(resource);
            resourcePanels.add(resourcePanel);
            panel.add(resourcePanel);
        }
        return panel;
    }

    private JPanel createControlSection() {
        JPanel panel = new JPanel(new FlowLayout());

        JButton addThreadButton = new JButton("Add Thread");
        addThreadButton.addActionListener(e -> addNewThread());

        startButton = new JButton("Start All");
        startButton.addActionListener(e -> toggleExecution());

        panel.add(addThreadButton);
        panel.add(startButton);
        return panel;
    }

    private void addNewThread() {
        WorkerThread workerThread = new WorkerThread(workerThreads.size() + 1);
        workerThreads.add(workerThread);

        ThreadPanel threadPanel = new ThreadPanel(workerThread);
        threadPanels.add(threadPanel);
        ((JPanel)getContentPane().getComponent(2)).add(threadPanel);

        revalidate();
        repaint();
    }

    private void toggleExecution() {
        if ("Start All".equals(startButton.getText())) {
            startButton.setText("Stop All");
            semaphoreManager.setRunning(true);
            startAllThreads();
        } else {
            startButton.setText("Start All");
            semaphoreManager.setRunning(false);
            stopAllThreads();
        }
    }

    private void startAllThreads() {
        for (WorkerThread thread : workerThreads) {
            if (!thread.isRunning()) {
                semaphoreManager.startThread(thread, this::repaint);
            }
        }
    }

    private void stopAllThreads() {
        for (WorkerThread thread : workerThreads) {
            thread.stopExecution();
        }
        repaint();
    }

    private void updateStats() {
        long totalExecutionTime = workerThreads.stream()
                .mapToLong(WorkerThread::getExecutionTime)
                .sum();
        statsLabel.setText("Total Execution Time: " +
                timeFormat.format(totalExecutionTime));
        repaint();
    }

    @Override
    public void dispose() {
        updateTimer.stop();
        semaphoreManager.shutdown();
        super.dispose();
    }
}