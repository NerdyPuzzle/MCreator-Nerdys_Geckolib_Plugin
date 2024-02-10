package net.nerdypuzzle.geckolib.parts.arm_pose_list;

import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.entries.JSimpleEntriesList;
import net.mcreator.ui.help.IHelpContext;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.laf.themes.Theme;
import net.nerdypuzzle.geckolib.element.types.AnimatedItem;

import javax.swing.*;
import java.util.List;

public class JArmPoseList extends JSimpleEntriesList<JArmPoseEntry, AnimatedItem.ArmPoseEntry> {
    public JArmPoseList(MCreator mcreator, IHelpContext gui) {
        super(mcreator, gui);
        this.add.setText(L10N.t("elementgui.animateditem.add_arm_entry", new Object[0]));
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Theme.current().getForegroundColor(), 1), L10N.t("elementgui.animateditem.armpose_values", new Object[0]), 0, 0, this.getFont().deriveFont(12.0F), Theme.current().getForegroundColor()));
    }

    protected JArmPoseEntry newEntry(JPanel parent, List<JArmPoseEntry> entryList, boolean userAction) {
        return new JArmPoseEntry(this.mcreator, this.gui, parent, entryList);
    }
}
