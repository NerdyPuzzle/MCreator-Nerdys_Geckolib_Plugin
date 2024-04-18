package net.nerdypuzzle.geckolib.parts.blockstate_list;

import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.SearchableComboBox;
import net.mcreator.ui.component.entries.JSimpleListEntry;
import net.mcreator.ui.component.util.ComboBoxUtil;
import net.mcreator.ui.component.util.ComponentUtils;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.dialogs.TypedTextureSelectorDialog;
import net.mcreator.ui.help.HelpUtils;
import net.mcreator.ui.help.IHelpContext;
import net.mcreator.ui.init.L10N;;
import net.mcreator.ui.laf.themes.Theme;
import net.mcreator.ui.minecraft.TextureHolder;
import net.mcreator.ui.minecraft.boundingboxes.JBoundingBoxList;
import net.mcreator.ui.validation.IValidable;
import net.mcreator.ui.validation.ValidationGroup;
import net.mcreator.ui.validation.Validator;
import net.mcreator.ui.validation.validators.TileHolderValidator;
import net.mcreator.ui.workspace.resources.TextureType;
import net.mcreator.util.ListUtils;
import net.mcreator.workspace.Workspace;
import net.nerdypuzzle.geckolib.element.types.AnimatedBlock;
import net.nerdypuzzle.geckolib.parts.GeomodelRenderer;
import net.nerdypuzzle.geckolib.parts.PluginModelActions;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class JBlockstateListEntry extends JSimpleListEntry<AnimatedBlock.BlockstateListEntry> implements IValidable {
    private final Workspace workspace;
    private final ValidationGroup page1group = new ValidationGroup();
    private TextureHolder texture;
    private TextureHolder particleTexture;
    private final SearchableComboBox<String> geoModel;
    private Validator validator;
    private final JSpinner luminance;
    private JBoundingBoxList boundingBoxList;
    private final int index;

    public JBlockstateListEntry(MCreator mcreator, IHelpContext gui, JPanel parent, List<JBlockstateListEntry> entryList, int index) {
        super(parent, entryList);
        this.index = index;
        this.workspace = mcreator.getWorkspace();
        this.line.setOpaque(false);
        this.geoModel = new SearchableComboBox<>();
        this.geoModel.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXX");
        this.geoModel.setRenderer(new GeomodelRenderer());
        ComponentUtils.deriveFont(this.geoModel, 16.0F);
        this.geoModel.setPreferredSize(new Dimension(290, 39));
        this.luminance = new JSpinner(new SpinnerNumberModel(0, 0, 15, 1));
        this.boundingBoxList = new JBoundingBoxList(mcreator, gui, null);

        JPanel destal = new JPanel(new BorderLayout());
        destal.setOpaque(false);
        this.texture = (new TextureHolder(new TypedTextureSelectorDialog(mcreator, TextureType.BLOCK)));
        this.particleTexture = new TextureHolder(new TypedTextureSelectorDialog(mcreator, TextureType.BLOCK), 32);
        this.particleTexture.setOpaque(false);
        this.texture.setOpaque(false);
        destal.add(PanelUtils.totalCenterInPanel(ComponentUtils.squareAndBorder(this.texture, new Color(125, 255, 174), L10N.t("elementgui.block.texture_place_bottom_main", new Object[0]))));
        JPanel sbbp22 = PanelUtils.totalCenterInPanel(destal);
        sbbp22.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Theme.current().getForegroundColor(), 1), L10N.t("elementgui.block.block_textures", new Object[0]), 0, 0, this.getFont().deriveFont(12.0F), Theme.current().getForegroundColor()));
        JPanel topnbot = new JPanel(new BorderLayout());
        topnbot.setOpaque(false);
        topnbot.add("Center", sbbp22);
        JPanel bottomPanel = new JPanel(new GridLayout(3, 2, 0, 2));
        bottomPanel.setOpaque(false);
        bottomPanel.add(HelpUtils.wrapWithHelpButton(gui.withEntry("geckolib/name"), L10N.label("elementgui.animatedblock.model", new Object[0])));
        bottomPanel.add(this.geoModel);
        bottomPanel.add(HelpUtils.wrapWithHelpButton(gui.withEntry("block/particle_texture"), L10N.label("elementgui.block.particle_texture", new Object[0])));
        bottomPanel.add(this.particleTexture);
        bottomPanel.add(HelpUtils.wrapWithHelpButton(gui.withEntry("block/luminance"), L10N.label("elementgui.common.luminance", new Object[0])));
        bottomPanel.add(this.luminance);
        bottomPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Theme.current().getForegroundColor(), 1), L10N.t("elementgui.animatedblock.blockstate_properties", new Object[0]), 0, 0, this.getFont().deriveFont(12.0F), Theme.current().getForegroundColor()));
        this.boundingBoxList.setPreferredSize(new Dimension(600, 170));
        topnbot.add("East", PanelUtils.pullElementUp(PanelUtils.centerAndEastElement(bottomPanel, boundingBoxList)));
        topnbot.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Theme.current().getForegroundColor(), 1), L10N.t("elementgui.animatedblock.blockstate", new Object[0]) + " " + index, 0, 0, this.getFont().deriveFont(12.0F), Theme.current().getForegroundColor()));
        this.line.add(PanelUtils.totalCenterInPanel(topnbot));

        geoModel.setValidator(() -> {
            if (geoModel.getSelectedItem() == null || geoModel.getSelectedItem().equals(""))
                return new Validator.ValidationResult(Validator.ValidationResultType.ERROR,
                        L10N.t("elementgui.animatedentity.modelname"));
            return Validator.ValidationResult.PASSED;
        });
        this.page1group.addValidationElement(geoModel);

        this.texture.setValidator(new TileHolderValidator(this.texture));
        this.page1group.addValidationElement(this.texture);
    }

    public void reloadDataLists() {
        super.reloadDataLists();
        ComboBoxUtil.updateComboBoxContents(this.geoModel, ListUtils.merge(Collections.singleton(""), (Collection) PluginModelActions.getGeomodels(this.boundingBoxList.getMCreator()).stream().map(File::getName).filter((s) -> {
            return s.endsWith(".geo.json");
        }).collect(Collectors.toList())), "");
    }

    protected void setEntryEnabled(boolean enabled) {
    }

    @Override public Validator.ValidationResult getValidationStatus() {
        Validator.ValidationResult validationResult = Validator.ValidationResult.PASSED;
        if (!page1group.validateIsErrorFree()) {
            Validator.ValidationResult result = new Validator.ValidationResult(Validator.ValidationResultType.ERROR, page1group.getValidationProblemMessages().get(0));
            return result;
        }
        return validationResult;
    }

    @Override public void setValidator(Validator validator) {
        this.validator = validator;
    }

    @Override public Validator getValidator() {
        return validator;
    }

    public AnimatedBlock.BlockstateListEntry getEntry() {
        AnimatedBlock.BlockstateListEntry entry = new AnimatedBlock.BlockstateListEntry();
        entry.customModelName = this.geoModel.getSelectedItem();
        entry.particleTexture = this.particleTexture.getID();
        entry.texture = this.texture.getID();
        entry.luminance = (Integer)this.luminance.getValue();
        entry.boundingBoxes = this.boundingBoxList.getEntries();
        return entry;
    }

    public void setEntry(AnimatedBlock.BlockstateListEntry e) {
        this.geoModel.setSelectedItem(e.customModelName);
        this.particleTexture.setTextureFromTextureName(e.particleTexture);
        this.texture.setTextureFromTextureName(e.texture);
        this.luminance.setValue(e.luminance);
        this.boundingBoxList.setEntries(e.boundingBoxes);
    }
}
