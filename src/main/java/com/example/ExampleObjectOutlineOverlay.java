// ExampleObjectOutlineOverlay.java
package com.example;

import javax.inject.Inject;
import java.awt.*;
import java.util.Set;

import net.runelite.api.GameObject;
import net.runelite.client.ui.overlay.*;

public class ExampleObjectOutlineOverlay extends Overlay
{
    private final ExampleObjectOutlinePlugin plugin;

    @Inject
    private ExampleObjectOutlineOverlay(ExampleObjectOutlinePlugin plugin)
    {
        this.plugin = plugin;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
        setPriority(OverlayPriority.HIGH);
    }

    @Override
    public Dimension render(Graphics2D g)
    {
        ExampleObjectOutlineConfig cfg = plugin.getConfig();
        Set<GameObject> objects = plugin.getTrackedObjects();

        if (objects.isEmpty())
        {
            return null;
        }

        // Nice crisp lines:
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        BasicStroke stroke = new BasicStroke(cfg.strokeWidth());
        g.setStroke(stroke);

        for (GameObject obj : objects)
        {
            if (obj == null || obj.getConvexHull() == null)
                continue;

            Shape hull = obj.getConvexHull();

            // our test using obj.getClickbox();
            Shape clickbox = obj.getClickbox();

            // Optional fill (translucent)
            if (cfg.fillColor().getAlpha() > 0)
            {
                g.setColor(cfg.fillColor());
                g.fill(hull);
            }

            // Outline
            g.setColor(cfg.outlineColor());
            g.draw(hull);
        }
        return null;
    }
}
