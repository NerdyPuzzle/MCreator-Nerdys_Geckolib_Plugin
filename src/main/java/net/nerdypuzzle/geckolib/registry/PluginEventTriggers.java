package net.nerdypuzzle.geckolib.registry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Worker;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import net.mcreator.io.FileIO;
import net.mcreator.io.OS;
import net.mcreator.io.net.WebIO;
import net.mcreator.plugin.MCREvent;
import net.mcreator.plugin.PluginLoader;
import net.mcreator.plugin.PluginUpdateInfo;
import net.mcreator.plugin.events.ui.BlocklyPanelRegisterJSObjects;
import net.mcreator.preferences.PreferencesManager;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.MCreatorApplication;
import net.mcreator.ui.blockly.BlocklyEditorType;
import net.mcreator.ui.blockly.BlocklyPanel;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.component.util.ThreadUtil;
import net.mcreator.ui.dialogs.MCreatorDialog;
import net.mcreator.ui.init.BlocklyJavaScriptsLoader;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.init.UIRES;
import net.mcreator.ui.laf.themes.Theme;
import net.mcreator.ui.modgui.ModElementGUI;
import net.mcreator.ui.modgui.ProcedureGUI;
import net.mcreator.util.DesktopUtils;
import net.mcreator.util.image.ImageUtils;
import net.mcreator.workspace.elements.VariableTypeLoader;
import net.nerdypuzzle.geckolib.Launcher;
import net.nerdypuzzle.geckolib.element.types.GeckolibElement;
import net.nerdypuzzle.geckolib.parts.JavabridgeReplacement;
import net.nerdypuzzle.geckolib.parts.PluginPanelGeckolib;
import netscape.javascript.JSObject;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;

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

    public static void interceptProcedurePanel(MCreator mcreator, ModElementGUI modElement) {
        if (modElement instanceof ProcedureGUI procedure) {
            ThreadUtil.runOnFxThread(() -> { // Run on FX thread to prevent issues
                try {
                    Field panel = ProcedureGUI.class.getDeclaredField("blocklyPanel");
                    panel.setAccessible(true);
                    BlocklyPanel blocklyPanel = (BlocklyPanel) panel.get(procedure); // Get the blockly panel from the procedure GUI

                    Field loaded = BlocklyPanel.class.getDeclaredField("loaded");
                    loaded.setAccessible(true);
                    loaded.set(blocklyPanel, true); // Prevent the constructor from undoing our changes

                    Field engine = BlocklyPanel.class.getDeclaredField("webEngine"); // Get the webengine field to set it after making the changes
                    engine.setAccessible(true);

                    Field listeners = BlocklyPanel.class.getDeclaredField("changeListeners");
                    listeners.setAccessible(true);
                    List<ChangeListener> listenerslist = (List<ChangeListener>) listeners.get(blocklyPanel);

                    JavabridgeReplacement javabridge = new JavabridgeReplacement(mcreator, () -> ThreadUtil.runOnSwingThread(
                            () -> listenerslist.forEach(listener -> listener.stateChanged(new ChangeEvent(blocklyPanel)))));

                    WebView browser = new WebView();
                    browser.setContextMenuEnabled(false);
                    Scene scene = new Scene(browser);
                    java.awt.Color bg = Theme.current().getSecondAltBackgroundColor();
                    scene.setFill(javafx.scene.paint.Color.rgb(bg.getRed(), bg.getGreen(), bg.getBlue()));
                    blocklyPanel.setScene(scene);

                    browser.getChildrenUnmodifiable().addListener(
                            (ListChangeListener<Node>) change -> browser.lookupAll(".scroll-bar")
                                    .forEach(bar -> bar.setVisible(false)));
                    WebEngine webEngine = browser.getEngine();
                    webEngine.load(blocklyPanel.getClass().getResource("/blockly/blockly.html").toExternalForm());
                    webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
                        if (newState == Worker.State.SUCCEEDED && webEngine.getDocument() != null) {
                            // load CSS from file to select proper style for OS
                            Element styleNode = webEngine.getDocument().createElement("style");
                            String css = FileIO.readResourceToString("/blockly/css/mcreator_blockly.css");
                            if (PluginLoader.INSTANCE.getResourceAsStream(
                                    "themes/" + Theme.current().getID() + "/styles/blockly.css") != null) {
                                css += FileIO.readResourceToString(PluginLoader.INSTANCE,
                                        "/themes/" + Theme.current().getID() + "/styles/blockly.css");
                            } else {
                                css += FileIO.readResourceToString(PluginLoader.INSTANCE,
                                        "/themes/default_dark/styles/blockly.css");
                            }
                            if (PreferencesManager.PREFERENCES.blockly.transparentBackground.get()
                                    && OS.getOS() == OS.WINDOWS) {

                                try {
                                    Method comps = BlocklyPanel.class.getDeclaredMethod("makeComponentsTransparent", Scene.class);
                                    comps.setAccessible(true);
                                    comps.invoke(blocklyPanel, scene);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                                css += FileIO.readResourceToString("/blockly/css/mcreator_blockly_transparent.css");
                            }
                            //remove font declaration if property set so
                            if (PreferencesManager.PREFERENCES.blockly.legacyFont.get()) {
                                css = css.replace("font-family: sans-serif;", "");
                            }
                            Text styleContent = webEngine.getDocument().createTextNode(css);
                            styleNode.appendChild(styleContent);
                            webEngine.getDocument().getDocumentElement().getElementsByTagName("head").item(0)
                                    .appendChild(styleNode);
                            // register JS bridge
                            JSObject window = (JSObject) webEngine.executeScript("window");
                            window.setMember("javabridge", javabridge);
                            window.setMember("editorType", BlocklyEditorType.PROCEDURE.registryName());
                            // allow plugins to register additional JS objects
                            Map<String, Object> domWindowMembers = new HashMap<>();
                            MCREvent.event(new BlocklyPanelRegisterJSObjects(blocklyPanel, domWindowMembers));
                            domWindowMembers.forEach(window::setMember);
                            // @formatter:off
                            webEngine.executeScript("var MCR_BLOCKLY_PREF = { "
                                    + "'comments' : " + PreferencesManager.PREFERENCES.blockly.enableComments.get() + ","
                                    + "'renderer' : '" + PreferencesManager.PREFERENCES.blockly.blockRenderer.get().toLowerCase(Locale.ENGLISH) + "',"
                                    + "'collapse' : " + PreferencesManager.PREFERENCES.blockly.enableCollapse.get() + ","
                                    + "'trashcan' : " + PreferencesManager.PREFERENCES.blockly.enableTrashcan.get() + ","
                                    + "'maxScale' : " + PreferencesManager.PREFERENCES.blockly.maxScale.get() / 100.0 + ","
                                    + "'minScale' : " + PreferencesManager.PREFERENCES.blockly.minScale.get() / 100.0 + ","
                                    + "'scaleSpeed' : " + PreferencesManager.PREFERENCES.blockly.scaleSpeed.get() / 100.0 + ","
                                    + "'saturation' :" + PreferencesManager.PREFERENCES.blockly.colorSaturation.get() / 100.0 + ","
                                    + "'value' :" + PreferencesManager.PREFERENCES.blockly.colorValue.get() / 100.0
                                    + " };");
                            // @formatter:on
                            // Blockly core
                            webEngine.executeScript(FileIO.readResourceToString("/jsdist/blockly_compressed.js"));
                            webEngine.executeScript(
                                    FileIO.readResourceToString("/jsdist/msg/" + L10N.getBlocklyLangName() + ".js"));
                            webEngine.executeScript(FileIO.readResourceToString("/jsdist/blocks_compressed.js"));
                            // Blockly MCreator definitions
                            webEngine.executeScript(FileIO.readResourceToString("/blockly/js/mcreator_blockly.js"));
                            // Load JavaScript files from plugins
                            for (String script : BlocklyJavaScriptsLoader.INSTANCE.getScripts())
                                webEngine.executeScript(script);
                            //JS code generation for custom variables
                            webEngine.executeScript(VariableTypeLoader.INSTANCE.getVariableBlocklyJS());

                            try {
                                Field tasks = BlocklyPanel.class.getDeclaredField("runAfterLoaded");
                                tasks.setAccessible(true);
                                List<Runnable> tasklist = (List<Runnable>) tasks.get(blocklyPanel);
                                tasklist.forEach(ThreadUtil::runOnFxThread);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    engine.set(blocklyPanel, webEngine); // Replace the webengine with the one initialized here

                    panel.set(procedure, blocklyPanel); // Replace the blocklypanel with the one initialized here
                } catch (Exception e) {
                    e.printStackTrace();
                }


            });
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
            dialog.setSize(700, 200);
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
