// ExampleObjectOutlinePlugin.java
package com.example;

import com.google.common.collect.Sets;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
        name = "Example: Object Outline",
        description = "Outline specific game objects by ID",
        tags = {"object", "outline", "id", "overlay"}
)
public class ExampleObjectOutlinePlugin extends Plugin
{
    @Inject private Client client;
    @Inject private OverlayManager overlayManager;
    @Inject private ExampleObjectOutlineOverlay overlay;
    @Inject private ExampleObjectOutlineConfig config;

    // Keep live references to target objects currently in the scene.
    private final Set<GameObject> trackedObjects = Sets.newConcurrentHashSet();
    // Parsed IDs from config
    private Set<Integer> targetIds = new HashSet<>();

    @Provides
    ExampleObjectOutlineConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(ExampleObjectOutlineConfig.class);
    }

    Set<GameObject> getTrackedObjects()
    {
        return trackedObjects;
    }

    ExampleObjectOutlineConfig getConfig()
    {
        return config;
    }

    @Override
    protected void startUp()
    {
        parseIds();
        overlayManager.add(overlay);
        // Optionally: re-scan client scene here if you want immediate results on load.
    }

    @Override
    protected void shutDown()
    {
        overlayManager.remove(overlay);
        trackedObjects.clear();
    }

    private void parseIds()
    {
        String raw = config.objectIds();
        if (raw == null || raw.isBlank())
        {
            targetIds = Collections.emptySet();
            return;
        }
        targetIds = Arrays.stream(raw.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
        log.debug("Target object IDs: {}", targetIds);
    }

    // If you want live updates when the config changes, you can watch for it via ConfigChanged.

    @Subscribe
    public void onGameObjectSpawned(GameObjectSpawned e)
    {
        GameObject obj = e.getGameObject();
        if (targetIds.contains(obj.getId()))
        {
            trackedObjects.add(obj);
        }
    }

    @Subscribe
    public void onGameObjectDespawned(GameObjectDespawned e)
    {
        trackedObjects.remove(e.getGameObject());
    }
}
