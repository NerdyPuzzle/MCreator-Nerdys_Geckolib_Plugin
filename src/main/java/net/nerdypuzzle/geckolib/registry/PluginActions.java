package net.nerdypuzzle.geckolib.registry;

import net.mcreator.ui.MCreator;
import net.mcreator.ui.action.ActionRegistry;
import net.mcreator.ui.action.BasicAction;
import net.mcreator.ui.action.VisitURIAction;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.init.UIRES;
import net.mcreator.util.image.ImageUtils;
import net.nerdypuzzle.geckolib.parts.PluginDialogs;
import net.nerdypuzzle.geckolib.parts.PluginModelActions;

import javax.swing.*;

public class PluginActions extends ActionRegistry {
    public final BasicAction importGeckoLibModel;
    public final BasicAction importDisplaySettings;
    public final BasicAction tutorial;
    public final BasicAction convertion_to_geckolib;
    public final BasicAction convertion_from_geckolib;
    public PluginActions(MCreator mcreator) {
        super(mcreator);
        this.importGeckoLibModel = new PluginModelActions.GECKOLIB(this).setIcon(UIRES.get("16px.importgeckolibmodel"));
        this.importDisplaySettings = new PluginModelActions.DISPLAYSETTINGS(this).setIcon(UIRES.get("16px.importgeckolibmodel"));
        this.tutorial = (new VisitURIAction(this, L10N.t("action.tutorial", new Object[0]), "https://mcreator.net/forum/93274/tutorial-how-use-nerdys-geckolib-plugin-40-20224")).setIcon(new ImageIcon(ImageUtils.resizeAA(UIRES.get("16px.questionmark").getImage(), 14, 14)));
        this.convertion_to_geckolib = PluginDialogs.Entity2GeckoLib.getAction(this);
        this.convertion_from_geckolib = PluginDialogs.GeckoLib2Entity.getAction(this);
    }
}
