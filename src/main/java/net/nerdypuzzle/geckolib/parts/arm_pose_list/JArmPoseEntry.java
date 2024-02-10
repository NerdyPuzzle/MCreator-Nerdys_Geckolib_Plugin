package net.nerdypuzzle.geckolib.parts.arm_pose_list;

import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.entries.JSimpleListEntry;
import net.mcreator.ui.help.IHelpContext;
import net.mcreator.ui.init.L10N;
import net.mcreator.workspace.Workspace;
import net.nerdypuzzle.geckolib.element.types.AnimatedItem;

import javax.swing.*;
import java.util.List;

public class JArmPoseEntry extends JSimpleListEntry<AnimatedItem.ArmPoseEntry> {
    private final Workspace workspace;
    private final JComboBox<String> armHeld = new JComboBox<>(new String[]{"LEFT", "RIGHT"});
    private final JComboBox<String> arm = new JComboBox<>(new String[]{"LEFT", "RIGHT"});
    private final JComboBox<String> angle = new JComboBox<>(new String[]{"X", "Y", "Z"});
    private final JSpinner rotation = new JSpinner(new SpinnerNumberModel(1, -10000, 10000, 0.1));
    private final JCheckBox swings = L10N.checkbox("elementgui.animateditem.swings", new Object[0]);
    private final JCheckBox followsHead = L10N.checkbox("elementgui.animateditem.follows_head", new Object[0]);

    public JArmPoseEntry(MCreator mcreator, IHelpContext gui, JPanel parent, List<JArmPoseEntry> entryList) {
        super(parent, entryList);
        this.workspace = mcreator.getWorkspace();
        swings.setOpaque(false);
        followsHead.setOpaque(false);
        this.line.add(L10N.label("elementgui.animateditem.arm_held", new Object[0]));
        this.line.add(armHeld);
        this.line.add(L10N.label("elementgui.animateditem.arm", new Object[0]));
        this.line.add(arm);
        this.line.add(L10N.label("elementgui.animateditem.rotation_angle", new Object[0]));
        this.line.add(angle);
        this.line.add(L10N.label("elementgui.animateditem.rotation", new Object[0]));
        this.line.add(rotation);
        this.line.add(swings);
        this.line.add(followsHead);
    }

    protected void setEntryEnabled(boolean enabled) {
        armHeld.setEnabled(enabled);
        arm.setEnabled(enabled);
        angle.setEnabled(enabled);
        rotation.setEnabled(enabled);
        swings.setEnabled(enabled);
        followsHead.setEnabled(enabled);
    }

    public AnimatedItem.ArmPoseEntry getEntry() {
        AnimatedItem.ArmPoseEntry entry = new AnimatedItem.ArmPoseEntry();
        entry.armHeld = (String) armHeld.getSelectedItem();
        entry.arm = (String) arm.getSelectedItem();
        entry.angle = (String) angle.getSelectedItem();
        entry.rotation = (Number) rotation.getValue();
        entry.swings = swings.isSelected();
        entry.followsHead = followsHead.isSelected();
        return entry;
    }

    public void setEntry(AnimatedItem.ArmPoseEntry e) {
        armHeld.setSelectedItem(e.armHeld);
        arm.setSelectedItem(e.arm);
        angle.setSelectedItem(e.angle);
        rotation.setValue(e.rotation);
        swings.setSelected(e.swings);
        followsHead.setSelected(e.followsHead);
    }
}
