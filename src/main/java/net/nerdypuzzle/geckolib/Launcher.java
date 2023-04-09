package net.nerdypuzzle.geckolib;

import net.mcreator.plugin.events.ui.ModElementGUIEvent;
import net.mcreator.plugin.events.workspace.MCreatorLoadedEvent;
import net.nerdypuzzle.geckolib.registry.PluginActions;
import net.nerdypuzzle.geckolib.registry.PluginElementTypes;
import net.mcreator.plugin.JavaPlugin;
import net.mcreator.plugin.Plugin;
import net.mcreator.plugin.events.PreGeneratorsLoadingEvent;
import net.nerdypuzzle.geckolib.registry.PluginEventTriggers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

public class Launcher extends JavaPlugin {

	public static final Logger LOG = LogManager.getLogger("GeckoLib Plugin");
	public static PluginActions ACTION_REGISTRY;

	public Launcher(Plugin plugin) {
		super(plugin);
		addListener(PreGeneratorsLoadingEvent.class, event -> PluginElementTypes.load());
		addListener(ModElementGUIEvent.BeforeLoading.class, event -> SwingUtilities.invokeLater(() -> PluginEventTriggers.dependencyWarning(event.getMCreator(), event.getModElementGUI())));
		addListener(MCreatorLoadedEvent.class, event -> {
			ACTION_REGISTRY = new PluginActions(event.getMCreator());
			SwingUtilities.invokeLater(() -> PluginEventTriggers.modifyMenus(event.getMCreator()));
		});

		LOG.info("Plugin was loaded");
	}

}