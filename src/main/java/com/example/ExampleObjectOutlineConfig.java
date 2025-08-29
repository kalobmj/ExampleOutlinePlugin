// ExampleObjectOutlineConfig.java
package com.example;

import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup("exampleObjectOutline")
public interface ExampleObjectOutlineConfig extends Config
{
    @ConfigItem(
            keyName = "objectIds",
            name = "Object IDs (comma-separated)",
            description = "Game Object IDs to highlight, e.g. 10060, 10061",
            position = 1
    )
    default String objectIds()
    {
        return "";
    }

    @Alpha
    @ConfigItem(
            keyName = "outlineColor",
            name = "Outline color",
            description = "Color of the outline",
            position = 2
    )
    default Color outlineColor()
    {
        return new Color(0, 255, 255, 200);
    }

    @Range(min = 1, max = 6)
    @ConfigItem(
            keyName = "strokeWidth",
            name = "Outline width",
            description = "Line width of outline",
            position = 3
    )
    default int strokeWidth()
    {
        return 2;
    }

    @Alpha
    @ConfigItem(
            keyName = "fillColor",
            name = "Fill color (optional)",
            description = "Translucent fill inside hull (set alpha low)",
            position = 4
    )
    default Color fillColor()
    {
        return new Color(0, 255, 255, 40);
    }
}
