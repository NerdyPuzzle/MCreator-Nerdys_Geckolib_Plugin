package net.nerdypuzzle.geckolib.registry;

import net.mcreator.ui.MCreator;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.init.UIRES;
import net.mcreator.ui.modgui.ModElementGUI;
import net.mcreator.util.image.ImageUtils;
import net.nerdypuzzle.geckolib.Launcher;
import net.nerdypuzzle.geckolib.element.types.GeckolibElement;

import javax.swing.*;

public class PluginEventTriggers {
    public static void dependencyWarning(MCreator mcreator, ModElementGUI modElement) {
        if (!mcreator.getWorkspaceSettings().getDependencies().contains("geckolib") && modElement instanceof GeckolibElement) {
            StringBuilder stringBuilder = new StringBuilder(L10N.t("dialog.geckolib.enable_geckolib"));
            JOptionPane.showMessageDialog(mcreator, stringBuilder.toString(),
                    L10N.t("dialog.geckolib.error_no_dependency"), JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void modifyMenus(MCreator mcreator) {
        JMenu geckolib = L10N.menu("menubar.geckolib");
        geckolib.setMnemonic('R');
        geckolib.setIcon(new ImageIcon(ImageUtils.resizeAA(UIRES.get("16px.geckolibicon").getImage(), 17, 17)));
        geckolib.add(Launcher.ACTION_REGISTRY.importGeckoLibModel);
        geckolib.add(Launcher.ACTION_REGISTRY.importDisplaySettings);
        geckolib.addSeparator();
        geckolib.add(Launcher.ACTION_REGISTRY.tutorial);
        mcreator.getMainMenuBar().add(geckolib);
    }
}
