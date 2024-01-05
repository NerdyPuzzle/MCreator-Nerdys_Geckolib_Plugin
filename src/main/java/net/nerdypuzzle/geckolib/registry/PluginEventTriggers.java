package net.nerdypuzzle.geckolib.registry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.mcreator.io.net.WebIO;
import net.mcreator.plugin.PluginLoader;
import net.mcreator.plugin.PluginUpdateInfo;
import net.mcreator.preferences.PreferencesManager;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.MCreatorApplication;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.dialogs.MCreatorDialog;
import net.mcreator.ui.dialogs.UpdatePluginDialog;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.init.UIRES;
import net.mcreator.ui.modgui.ModElementGUI;
import net.mcreator.ui.notifications.INotificationConsumer;
import net.mcreator.ui.notifications.NotificationsRenderer;
import net.mcreator.util.DesktopUtils;
import net.mcreator.util.image.ImageUtils;
import net.nerdypuzzle.geckolib.Launcher;
import net.nerdypuzzle.geckolib.element.types.GeckolibElement;
import net.nerdypuzzle.geckolib.parts.PluginPanelGeckolib;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class PluginEventTriggers {

    private static final Set<PluginUpdateInfo> pluginUpdates = new HashSet();

    private static void checkForPluginUpdates() {
        if (MCreatorApplication.isInternet) {
            pluginUpdates.addAll(Launcher.PLUGIN_INSTANCE.parallelStream().map((plugin) -> {
                try {
                    String updateJSON = WebIO.readURLToString(plugin.getInfo().getUpdateJSONURL());
                    JsonObject updateData = JsonParser.parseString(updateJSON).getAsJsonObject().get(plugin.getID()).getAsJsonObject();
                    String version = updateData.get("latest").getAsString();
                    if (!version.equals(plugin.getPluginVersion())) {
                        return new PluginUpdateInfo(plugin, version, updateData.has("changes") ? updateData.get("changes").getAsJsonArray().asList().stream().map(JsonElement::getAsString).toList() : null);
                    }
                } catch (Exception var4) {
                    var4.printStackTrace();
                }

                return null;
            }).filter(Objects::nonNull).toList());
        }

    }

    public static void dependencyWarning(MCreator mcreator, ModElementGUI modElement) {
        if (!mcreator.getWorkspaceSettings().getDependencies().contains("geckolib") && modElement instanceof GeckolibElement) {
            StringBuilder stringBuilder = new StringBuilder(L10N.t("dialog.geckolib.enable_geckolib"));
            JOptionPane.showMessageDialog(mcreator, stringBuilder.toString(),
                    L10N.t("dialog.geckolib.error_no_dependency"), JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void forceCheckUpdates(MCreator mcreator) {
        checkForPluginUpdates();
        Collection<PluginUpdateInfo> pluginUpdateInfos = pluginUpdates;
        if (!pluginUpdateInfos.isEmpty()) {
            JPanel pan = new JPanel(new BorderLayout(10, 15));
            JPanel plugins = new JPanel(new GridLayout(0, 1, 10, 10));
            pan.add("North", new JScrollPane(PanelUtils.pullElementUp(plugins)));
            pan.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            pan.setPreferredSize(new Dimension(560, 250));
            Iterator var5 = pluginUpdateInfos.iterator();

            while(var5.hasNext()) {
                PluginUpdateInfo pluginUpdateInfo = (PluginUpdateInfo)var5.next();
                StringBuilder usb = new StringBuilder(L10N.t("dialog.plugin_update_notify.version_message", new Object[]{pluginUpdateInfo.plugin().getInfo().getName(), pluginUpdateInfo.plugin().getInfo().getVersion(), pluginUpdateInfo.newVersion()}));
                if (pluginUpdateInfo.recentChanges() != null) {
                    usb.append("<br>").append(L10N.t("dialog.plugin_update_notify.changes_message", new Object[0])).append("<ul>");
                    Iterator var8 = pluginUpdateInfo.recentChanges().iterator();

                    while(var8.hasNext()) {
                        String change = (String)var8.next();
                        usb.append("<li>").append(change).append("</li>");
                    }
                }

                JLabel label = new JLabel(usb.toString());
                JButton update = L10N.button("dialog.plugin_update_notify.update", new Object[0]);
                update.addActionListener((e) -> {
                    DesktopUtils.browseSafe("https://mcreator.net/node/" + pluginUpdateInfo.plugin().getInfo().getPluginPageID());
                });
                plugins.add(PanelUtils.westAndEastElement(label, PanelUtils.join(new Component[]{update})));
            }

            MCreatorDialog dialog = new MCreatorDialog(mcreator, L10N.t("dialog.plugin_update_notify.update_title", new Object[0]));
            dialog.setSize(700, 300);
            dialog.setLocationRelativeTo(mcreator);
            dialog.setModal(true);
            JButton close = L10N.button("dialog.plugin_update_notify.close", new Object[0]);
            close.addActionListener((e) -> {
                dialog.setVisible(false);
            });
            dialog.add("Center", PanelUtils.centerAndSouthElement(pan, PanelUtils.join(new Component[]{close})));
            dialog.setVisible(true);
        }
    }

    public static void modifyMenus(MCreator mcreator) {
        JMenu geckolib = L10N.menu("menubar.geckolib");
        geckolib.setMnemonic('R');
        geckolib.setIcon(new ImageIcon(ImageUtils.resizeAA(UIRES.get("16px.geckolibicon").getImage(), 17, 17)));
        geckolib.add(Launcher.ACTION_REGISTRY.importGeckoLibModel);
        geckolib.add(Launcher.ACTION_REGISTRY.importDisplaySettings);
        geckolib.addSeparator();
        geckolib.add(Launcher.ACTION_REGISTRY.convertion_to_geckolib);
        geckolib.add(Launcher.ACTION_REGISTRY.convertion_from_geckolib);
        geckolib.addSeparator();
        geckolib.add(Launcher.ACTION_REGISTRY.tutorial);

        PluginPanelGeckolib panel = new PluginPanelGeckolib(mcreator.mv);
        panel.setOpaque(false);

        mcreator.mv.resourcesPan.addResourcesTab(L10N.t("menubar.geckolib", new Object[0]), panel);
        mcreator.getMainMenuBar().add(geckolib);

        forceCheckUpdates(mcreator);
    }

}
