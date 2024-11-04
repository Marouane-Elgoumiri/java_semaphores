package org.example.gui;

import org.example.model.Resource;
import javax.swing.*;
import java.awt.*;

public class ResourcePanel extends JPanel {
    private final Resource resource;

    public ResourcePanel(Resource resource) {
        this.resource = resource;
        setPreferredSize(new Dimension(100, 100));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(resource.isInUse() ? Color.RED : Color.GREEN);
        g.fillRect(10, 10, getWidth() - 20, getHeight() - 20);
        g.setColor(Color.BLACK);
        g.drawString("Resource " + resource.getId(), 20, 30);
        if (resource.isInUse()) {
            g.drawString("Used by: " + resource.getUsedByThread(), 20, 50);
        }
    }
}

