package net.nerdypuzzle.geckolib.ui.modgui;

import net.mcreator.blockly.data.Dependency;
import net.mcreator.element.parts.TabEntry;
import net.mcreator.minecraft.DataListEntry;
import net.mcreator.minecraft.ElementUtil;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.CollapsiblePanel;
import net.mcreator.ui.component.JEmptyBox;
import net.mcreator.ui.component.SearchableComboBox;
import net.mcreator.ui.component.util.ComboBoxUtil;
import net.mcreator.ui.component.util.ComponentUtils;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.dialogs.TextureImportDialogs;
import net.mcreator.ui.dialogs.TypedTextureSelectorDialog;
import net.mcreator.ui.help.HelpUtils;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.init.UIRES;
import net.mcreator.ui.laf.renderer.ModelComboBoxRenderer;
import net.mcreator.ui.laf.renderer.WTextureComboBoxRenderer;
import net.mcreator.ui.minecraft.DataListComboBox;
import net.mcreator.ui.minecraft.MCItemListField;
import net.mcreator.ui.minecraft.SoundSelector;
import net.mcreator.ui.minecraft.TextureHolder;
import net.mcreator.ui.modgui.ModElementGUI;
import net.mcreator.ui.procedure.ProcedureSelector;
import net.mcreator.ui.validation.AggregatedValidationResult;
import net.mcreator.ui.validation.ValidationGroup;
import net.mcreator.ui.validation.Validator;
import net.mcreator.ui.validation.component.VComboBox;
import net.mcreator.ui.validation.component.VTextField;
import net.mcreator.ui.validation.validators.ConditionalTextFieldValidator;
import net.mcreator.ui.validation.validators.TextFieldValidator;
import net.mcreator.ui.workspace.resources.TextureType;
import net.mcreator.util.ListUtils;
import net.mcreator.util.StringUtils;
import net.mcreator.workspace.elements.ModElement;
import net.mcreator.workspace.resources.Model;
import net.nerdypuzzle.geckolib.element.types.AnimatedArmor;
import net.nerdypuzzle.geckolib.element.types.GeckolibElement;
import net.nerdypuzzle.geckolib.parts.GeomodelRenderer;
import net.nerdypuzzle.geckolib.parts.PluginModelActions;
import net.nerdypuzzle.geckolib.parts.RefactoredSystemsICopyPastedBecauseIWasTooBloodyLazyToActuallyProperlyReplace;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class AnimatedArmorGUI extends ModElementGUI<AnimatedArmor> implements GeckolibElement {

    private static final Logger LOG = LogManager.getLogger("Animated Armor UI");

    private TextureHolder textureHelmet;
    private TextureHolder textureBody;
    private TextureHolder textureLeggings;
    private TextureHolder textureBoots;

    private final VTextField helmetName = new VTextField();
    private final VTextField bodyName = new VTextField();
    private final VTextField leggingsName = new VTextField();
    private final VTextField bootsName = new VTextField();
    private final JTextField helmetSpecialInfo = new JTextField(20);
    private final JTextField bodySpecialInfo = new JTextField(20);
    private final JTextField leggingsSpecialInfo = new JTextField(20);
    private final JTextField bootsSpecialInfo = new JTextField(20);

    private final VComboBox<String> armorTextureFile;

    private final JCheckBox enableHelmet = L10N.checkbox("elementgui.armor.armor_helmet");
    private final JCheckBox enableBody = L10N.checkbox("elementgui.armor.armor_chestplate");
    private final JCheckBox enableLeggings = L10N.checkbox("elementgui.armor.armor_leggings");
    private final JCheckBox enableBoots = L10N.checkbox("elementgui.armor.armor_boots");

    private final Model normal = new Model.BuiltInModel("Normal");
    private final Model tool = new Model.BuiltInModel("Tool");
    private final SearchableComboBox<Model> helmetItemRenderType = new SearchableComboBox<>(
            new Model[] { normal, tool });
    private final SearchableComboBox<Model> bodyItemRenderType = new SearchableComboBox<>(new Model[] { normal, tool });
    private final SearchableComboBox<Model> leggingsItemRenderType = new SearchableComboBox<>(
            new Model[] { normal, tool });
    private final SearchableComboBox<Model> bootsItemRenderType = new SearchableComboBox<>(
            new Model[] { normal, tool });

    private final JCheckBox helmetImmuneToFire = L10N.checkbox("elementgui.common.enable");
    private final JCheckBox bodyImmuneToFire = L10N.checkbox("elementgui.common.enable");
    private final JCheckBox leggingsImmuneToFire = L10N.checkbox("elementgui.common.enable");
    private final JCheckBox bootsImmuneToFire = L10N.checkbox("elementgui.common.enable");
    private final JCheckBox fullyEquipped = L10N.checkbox("elementgui.common.enable");

    private final SoundSelector equipSound = new SoundSelector(mcreator);

    private final int fact = 5;

    private final JSpinner maxDamage = new JSpinner(new SpinnerNumberModel(25, 0, 1024, 1));
    private final JSpinner damageValueBoots = new JSpinner(new SpinnerNumberModel(2, 0, 1024, 1));
    private final JSpinner damageValueLeggings = new JSpinner(new SpinnerNumberModel(5, 0, 1024, 1));
    private final JSpinner damageValueBody = new JSpinner(new SpinnerNumberModel(6, 0, 1024, 1));
    private final JSpinner damageValueHelmet = new JSpinner(new SpinnerNumberModel(2, 0, 1024, 1));
    private final JSpinner enchantability = new JSpinner(new SpinnerNumberModel(9, 0, 100, 1));
    private final JSpinner toughness = new JSpinner(new SpinnerNumberModel(0.0, 0, 10, 0.1));
    private final JSpinner knockbackResistance = new JSpinner(new SpinnerNumberModel(0.0, 0, 5.0, 0.1));

    private ProcedureSelector onHelmetTick;
    private ProcedureSelector onBodyTick;
    private ProcedureSelector onLeggingsTick;
    private ProcedureSelector onBootsTick;

    private final DataListComboBox creativeTab = new DataListComboBox(mcreator);

    private final ValidationGroup group1page = new ValidationGroup();
    private final ValidationGroup group2page = new ValidationGroup();

    private CollapsiblePanel helmetCollapsiblePanel;
    private CollapsiblePanel bodyCollapsiblePanel;
    private CollapsiblePanel leggingsCollapsiblePanel;
    private CollapsiblePanel bootsCollapsiblePanel;

    private MCItemListField repairItems;

    private VTextField idle = new VTextField(12);

    private JTextField head = new JTextField(12);
    private JTextField chest = new JTextField(12);
    private JTextField rightArm = new JTextField(12);
    private JTextField leftArm = new JTextField(12);
    private JTextField rightLeg = new JTextField(12);
    private JTextField leftLeg = new JTextField(12);
    private JTextField rightBoot = new JTextField(12);
    private JTextField leftBoot = new JTextField(12);

    private final VComboBox<String> geoModel;

    public AnimatedArmorGUI(MCreator mcreator, ModElement modElement, boolean editingMode) {
        super(mcreator, modElement, editingMode);
        this.armorTextureFile = new SearchableComboBox();
        this.geoModel = new SearchableComboBox();
        this.initGUI();
        super.finalizeGUI();
    }

    @Override protected void initGUI() {
        onHelmetTick = new ProcedureSelector(this.withEntry("armor/helmet_tick"), mcreator,
                L10N.t("elementgui.armor.helmet_tick_event"),
                Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/itemstack:itemstack"));
        onBodyTick = new ProcedureSelector(this.withEntry("armor/chestplate_tick"), mcreator,
                L10N.t("elementgui.armor.chestplate_tick_event"),
                Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/itemstack:itemstack"));
        onLeggingsTick = new ProcedureSelector(this.withEntry("armor/leggings_tick"), mcreator,
                L10N.t("elementgui.armor.leggings_tick_event"),
                Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/itemstack:itemstack"));
        onBootsTick = new ProcedureSelector(this.withEntry("armor/boots_tick"), mcreator,
                L10N.t("elementgui.armor.boots_tick_event"),
                Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/itemstack:itemstack"));

        repairItems = new MCItemListField(mcreator, ElementUtil::loadBlocksAndItems);

        JPanel pane2 = new JPanel(new BorderLayout(10, 10));
        JPanel pane5 = new JPanel(new BorderLayout(10, 10));
        JPanel pane6 = new JPanel(new BorderLayout(10, 10));

        helmetName.setPreferredSize(new Dimension(350, 36));
        bodyName.setPreferredSize(new Dimension(350, 36));
        leggingsName.setPreferredSize(new Dimension(350, 36));
        bootsName.setPreferredSize(new Dimension(350, 36));

        ComponentUtils.deriveFont(helmetItemRenderType, 16);
        helmetItemRenderType.setPreferredSize(new Dimension(350, 42));
        helmetItemRenderType.setRenderer(new ModelComboBoxRenderer());

        ComponentUtils.deriveFont(bodyItemRenderType, 16);
        bodyItemRenderType.setPreferredSize(new Dimension(350, 42));
        bodyItemRenderType.setRenderer(new ModelComboBoxRenderer());

        ComponentUtils.deriveFont(leggingsItemRenderType, 16);
        leggingsItemRenderType.setPreferredSize(new Dimension(350, 42));
        leggingsItemRenderType.setRenderer(new ModelComboBoxRenderer());

        ComponentUtils.deriveFont(bootsItemRenderType, 16);
        bootsItemRenderType.setPreferredSize(new Dimension(350, 42));
        bootsItemRenderType.setRenderer(new ModelComboBoxRenderer());

        ComponentUtils.deriveFont(helmetName, 16);
        ComponentUtils.deriveFont(bodyName, 16);
        ComponentUtils.deriveFont(leggingsName, 16);
        ComponentUtils.deriveFont(bootsName, 16);
        ComponentUtils.deriveFont(head, 16);
        ComponentUtils.deriveFont(chest, 16);
        ComponentUtils.deriveFont(rightArm, 16);
        ComponentUtils.deriveFont(leftArm, 16);
        ComponentUtils.deriveFont(rightLeg, 16);
        ComponentUtils.deriveFont(leftLeg, 16);
        ComponentUtils.deriveFont(rightBoot, 16);
        ComponentUtils.deriveFont(leftBoot, 16);

        ComponentUtils.deriveFont(helmetSpecialInfo, 16);
        ComponentUtils.deriveFont(bodySpecialInfo, 16);
        ComponentUtils.deriveFont(leggingsSpecialInfo, 16);
        ComponentUtils.deriveFont(bootsSpecialInfo, 16);
        ComponentUtils.deriveFont(idle, 16);

        ComponentUtils.deriveFont(armorTextureFile, 16);

        JPanel destal = new JPanel();
        destal.setLayout(new BoxLayout(destal, BoxLayout.Y_AXIS));
        destal.setOpaque(false);

        textureHelmet = new TextureHolder(new TypedTextureSelectorDialog(mcreator, TextureType.ITEM));
        textureBody = new TextureHolder(new TypedTextureSelectorDialog(mcreator, TextureType.ITEM));
        textureLeggings = new TextureHolder(new TypedTextureSelectorDialog(mcreator, TextureType.ITEM));
        textureBoots = new TextureHolder(new TypedTextureSelectorDialog(mcreator, TextureType.ITEM));

        textureHelmet.setOpaque(false);
        textureBody.setOpaque(false);
        textureLeggings.setOpaque(false);
        textureBoots.setOpaque(false);

        enableHelmet.setSelected(true);
        enableBody.setSelected(true);
        enableLeggings.setSelected(true);
        enableBoots.setSelected(true);

        enableHelmet.setOpaque(false);
        enableBody.setOpaque(false);
        enableLeggings.setOpaque(false);
        enableBoots.setOpaque(false);

        helmetImmuneToFire.setOpaque(false);
        bodyImmuneToFire.setOpaque(false);
        leggingsImmuneToFire.setOpaque(false);
        bootsImmuneToFire.setOpaque(false);

        JPanel helmetSubPanel = new JPanel(new GridLayout(3, 2, 4, 4));
        helmetSubPanel.setOpaque(false);

        helmetSubPanel.add(
                HelpUtils.wrapWithHelpButton(this.withEntry("item/model"), L10N.label("elementgui.common.item_model")));
        helmetSubPanel.add(helmetItemRenderType);

        helmetSubPanel.add(PanelUtils.join(FlowLayout.LEFT, L10N.label("elementgui.item.tooltip_tip")));
        helmetSubPanel.add(helmetSpecialInfo);

        helmetSubPanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/immune_to_fire"),
                L10N.label("elementgui.item.is_immune_to_fire")));
        helmetSubPanel.add(helmetImmuneToFire);

        helmetCollapsiblePanel = new CollapsiblePanel(L10N.t("elementgui.armor.advanced_helmet"), helmetSubPanel);

        JComponent helText = PanelUtils.centerAndSouthElement(PanelUtils.centerInPanelPadding(textureHelmet, 0, 0),
                enableHelmet);
        helText.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder((Color) UIManager.get("MCreatorLAF.GRAY_COLOR")),
                BorderFactory.createEmptyBorder(15, 12, 0, 12)));

        destal.add(PanelUtils.westAndCenterElement(PanelUtils.pullElementUp(helText), PanelUtils.centerAndSouthElement(
                PanelUtils.join(FlowLayout.LEFT, L10N.label("elementgui.armor.helmet_name"), helmetName),
                helmetCollapsiblePanel), 5, 0));

        destal.add(new JEmptyBox(10, 10));

        JComponent bodText = PanelUtils.centerAndSouthElement(PanelUtils.centerInPanelPadding(textureBody, 0, 0),
                enableBody);
        bodText.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder((Color) UIManager.get("MCreatorLAF.GRAY_COLOR")),
                BorderFactory.createEmptyBorder(15, 17, 0, 17)));

        JPanel bodySubPanel = new JPanel(new GridLayout(3, 2, 4, 4));
        bodySubPanel.setOpaque(false);

        bodySubPanel.add(
                HelpUtils.wrapWithHelpButton(this.withEntry("item/model"), L10N.label("elementgui.common.item_model")));
        bodySubPanel.add(bodyItemRenderType);

        bodySubPanel.add(PanelUtils.join(FlowLayout.LEFT, L10N.label("elementgui.item.tooltip_tip")));
        bodySubPanel.add(bodySpecialInfo);

        bodySubPanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/immune_to_fire"),
                L10N.label("elementgui.item.is_immune_to_fire")));
        bodySubPanel.add(bodyImmuneToFire);

        bodyCollapsiblePanel = new CollapsiblePanel(L10N.t("elementgui.armor.advanced_chestplate"), bodySubPanel);

        destal.add(PanelUtils.westAndCenterElement(PanelUtils.pullElementUp(bodText), PanelUtils.centerAndSouthElement(
                PanelUtils.join(FlowLayout.LEFT, L10N.label("elementgui.armor.chestplate_name"), bodyName),
                bodyCollapsiblePanel), 5, 0));

        destal.add(new JEmptyBox(10, 10));

        JComponent legText = PanelUtils.centerAndSouthElement(PanelUtils.centerInPanelPadding(textureLeggings, 0, 0),
                enableLeggings);
        legText.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder((Color) UIManager.get("MCreatorLAF.GRAY_COLOR")),
                BorderFactory.createEmptyBorder(15, 8, 0, 8)));

        JPanel leggingsSubPanel = new JPanel(new GridLayout(3, 2, 4, 4));
        leggingsSubPanel.setOpaque(false);

        leggingsSubPanel.add(
                HelpUtils.wrapWithHelpButton(this.withEntry("item/model"), L10N.label("elementgui.common.item_model")));
        leggingsSubPanel.add(leggingsItemRenderType);

        leggingsSubPanel.add(PanelUtils.join(FlowLayout.LEFT, L10N.label("elementgui.item.tooltip_tip")));
        leggingsSubPanel.add(leggingsSpecialInfo);

        leggingsSubPanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/immune_to_fire"),
                L10N.label("elementgui.item.is_immune_to_fire")));
        leggingsSubPanel.add(leggingsImmuneToFire);

        leggingsCollapsiblePanel = new CollapsiblePanel(L10N.t("elementgui.armor.advanced_leggings"), leggingsSubPanel);

        destal.add(PanelUtils.westAndCenterElement(PanelUtils.pullElementUp(legText), PanelUtils.centerAndSouthElement(
                PanelUtils.join(FlowLayout.LEFT, L10N.label("elementgui.armor.leggings_name"), leggingsName),
                leggingsCollapsiblePanel), 5, 0));

        destal.add(new JEmptyBox(10, 10));

        JComponent bootText = PanelUtils.centerAndSouthElement(PanelUtils.centerInPanelPadding(textureBoots, 0, 0),
                enableBoots);
        bootText.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder((Color) UIManager.get("MCreatorLAF.GRAY_COLOR")),
                BorderFactory.createEmptyBorder(15, 16, 0, 15)));

        JPanel bootsSubPanel = new JPanel(new GridLayout(3, 2, 4, 4));
        bootsSubPanel.setOpaque(false);

        bootsSubPanel.add(
                HelpUtils.wrapWithHelpButton(this.withEntry("item/model"), L10N.label("elementgui.common.item_model")));
        bootsSubPanel.add(bootsItemRenderType);

        bootsSubPanel.add(PanelUtils.join(FlowLayout.LEFT, L10N.label("elementgui.item.tooltip_tip")));
        bootsSubPanel.add(bootsSpecialInfo);

        bootsSubPanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/immune_to_fire"),
                L10N.label("elementgui.item.is_immune_to_fire")));
        bootsSubPanel.add(bootsImmuneToFire);

        bootsCollapsiblePanel = new CollapsiblePanel(L10N.t("elementgui.armor.advanced_boots"), bootsSubPanel);

        destal.add(PanelUtils.westAndCenterElement(PanelUtils.pullElementUp(bootText), PanelUtils.centerAndSouthElement(
                PanelUtils.join(FlowLayout.LEFT, L10N.label("elementgui.armor.boots_name"), bootsName),
                bootsCollapsiblePanel), 5, 0));

        enableHelmet.addActionListener(event -> {
            textureHelmet.setEnabled(enableHelmet.isSelected());
            helmetName.setEnabled(enableHelmet.isSelected());
        });

        enableBody.addActionListener(event -> {
            textureBody.setEnabled(enableBody.isSelected());
            bodyName.setEnabled(enableBody.isSelected());
        });

        enableLeggings.addActionListener(event -> {
            textureLeggings.setEnabled(enableLeggings.isSelected());
            leggingsName.setEnabled(enableLeggings.isSelected());
        });

        enableBoots.addActionListener(event -> {
            textureBoots.setEnabled(enableBoots.isSelected());
            bootsName.setEnabled(enableBoots.isSelected());
        });

        JPanel sbbp22 = new JPanel();

        sbbp22.setOpaque(false);

        sbbp22.add(destal);

        GridLayout klo = new GridLayout(2, 2);

        klo.setHgap(20);
        klo.setVgap(20);

        JPanel events = new JPanel();
        events.setLayout(new BoxLayout(events, BoxLayout.PAGE_AXIS));
        JPanel events2 = new JPanel(new GridLayout(1, 4, 8, 8));
        events2.setOpaque(false);
        events2.add(onHelmetTick);
        events2.add(onBodyTick);
        events2.add(onLeggingsTick);
        events2.add(onBootsTick);
        events.add(PanelUtils.join(events2));
        events.setOpaque(false);
        pane6.add("Center", PanelUtils.totalCenterInPanel(events));

        pane2.setOpaque(false);
        pane2.add("Center", PanelUtils.totalCenterInPanel(sbbp22));

        JPanel enderpanel = new JPanel(new GridLayout(8, 2, 20, 10));

        enderpanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("common/creative_tab"),
                L10N.label("elementgui.common.creative_tab")));
        enderpanel.add(creativeTab);

        enderpanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("armor/equip_sound"),
                L10N.label("elementgui.armor.equip_sound")));
        enderpanel.add(equipSound);

        enderpanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("armor/max_damage_absorbed"),
                L10N.label("elementgui.armor.max_damage_absorption")));
        enderpanel.add(maxDamage);

        enderpanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("armor/damage_values"),
                L10N.label("elementgui.armor.damage_values")));
        enderpanel.add(PanelUtils.gridElements(1, 4, damageValueHelmet, damageValueBody, damageValueLeggings,
                damageValueBoots));

        enderpanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("armor/enchantability"),
                L10N.label("elementgui.common.enchantability")));
        enderpanel.add(enchantability);

        enderpanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("armor/toughness"),
                L10N.label("elementgui.armor.toughness")));
        enderpanel.add(toughness);

        enderpanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("armor/knockback_resistance"),
                L10N.label("elementgui.armor.knockback_resistance")));
        enderpanel.add(knockbackResistance);

        enderpanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("armor/repair_items"),
                L10N.label("elementgui.common.repair_items")));
        enderpanel.add(repairItems);

        enderpanel.setOpaque(false);

        pane5.setOpaque(false);
        pane6.setOpaque(false);

        JPanel clopa = new JPanel(new BorderLayout(35, 35));
        clopa.add("Center", enderpanel);
        clopa.setOpaque(false);

        pane5.add("Center", PanelUtils.totalCenterInPanel(clopa));

        textureHelmet.setValidator(() -> {
            if (enableHelmet.isSelected() && !textureHelmet.hasTexture())
                return new Validator.ValidationResult(Validator.ValidationResultType.ERROR,
                        L10N.t("elementgui.armor.need_texture"));
            return Validator.ValidationResult.PASSED;
        });

        textureBody.setValidator(() -> {
            if (enableBody.isSelected() && !textureBody.hasTexture())
                return new Validator.ValidationResult(Validator.ValidationResultType.ERROR,
                        L10N.t("elementgui.armor.need_texture"));
            return Validator.ValidationResult.PASSED;
        });

        textureLeggings.setValidator(() -> {
            if (enableLeggings.isSelected() && !textureLeggings.hasTexture())
                return new Validator.ValidationResult(Validator.ValidationResultType.ERROR,
                        L10N.t("elementgui.armor.need_texture"));
            return Validator.ValidationResult.PASSED;
        });

        textureBoots.setValidator(() -> {
            if (enableBoots.isSelected() && !textureBoots.hasTexture())
                return new Validator.ValidationResult(Validator.ValidationResultType.ERROR,
                        L10N.t("elementgui.armor.need_texture"));
            return Validator.ValidationResult.PASSED;
        });

        bootsName.setValidator(
                new ConditionalTextFieldValidator(bootsName, L10N.t("elementgui.armor.boots_need_name"), enableBoots,
                        true));
        bodyName.setValidator(
                new ConditionalTextFieldValidator(bodyName, L10N.t("elementgui.armor.chestplate_needs_name"), enableBody,
                        true));
        leggingsName.setValidator(
                new ConditionalTextFieldValidator(leggingsName, L10N.t("elementgui.armor.leggings_need_name"),
                        enableLeggings, true));
        helmetName.setValidator(
                new ConditionalTextFieldValidator(helmetName, L10N.t("elementgui.armor.helmet_needs_name"),
                        enableHelmet, true));

        bootsName.enableRealtimeValidation();
        bodyName.enableRealtimeValidation();
        leggingsName.enableRealtimeValidation();
        helmetName.enableRealtimeValidation();

        group1page.addValidationElement(textureHelmet);
        group1page.addValidationElement(textureBody);
        group1page.addValidationElement(textureLeggings);
        group1page.addValidationElement(textureBoots);

        group1page.addValidationElement(bootsName);
        group1page.addValidationElement(bodyName);
        group1page.addValidationElement(leggingsName);
        group1page.addValidationElement(helmetName);

        this.armorTextureFile.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXX");
        this.armorTextureFile.setRenderer(new WTextureComboBoxRenderer.TypeTextures(this.mcreator.getWorkspace(), TextureType.ITEM));
        ComponentUtils.deriveFont(this.armorTextureFile, 16.0F);

        JButton importmobtexture = new JButton(UIRES.get("18px.add"));
        importmobtexture.setToolTipText(L10N.t("elementgui.ranged_item.bullet_model_tooltip", new Object[0]));
        importmobtexture.setOpaque(false);
        importmobtexture.addActionListener((e) -> {
            TextureImportDialogs.importMultipleTextures(this.mcreator, TextureType.ITEM);
            this.armorTextureFile.removeAllItems();
            this.armorTextureFile.addItem("");
            List<File> textures = this.mcreator.getFolderManager().getTexturesList(TextureType.ITEM);
            Iterator var3 = textures.iterator();

            while(var3.hasNext()) {
                File element = (File)var3.next();
                if (element.getName().endsWith(".png")) {
                    this.armorTextureFile.addItem(element.getName());
                }
            }

        });

        armorTextureFile.setValidator(() -> {
            if (armorTextureFile.getSelectedItem() == null || armorTextureFile.getSelectedItem().equals(""))
                return new Validator.ValidationResult(Validator.ValidationResultType.ERROR,
                        L10N.t("elementgui.animatedarmor.texture_invalid"));
            return Validator.ValidationResult.PASSED;
        });

        group1page.addValidationElement(armorTextureFile);


        JPanel gprops = new JPanel(new GridLayout(13, 2, 4, 4));
        JPanel pane3 = new JPanel(new GridLayout(1, 1, 10, 10));

        gprops.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/armor_model"),
                L10N.label("elementgui.animatedarmor.model_name")));
        this.geoModel.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXX");
        this.geoModel.setRenderer(new GeomodelRenderer());
        ComponentUtils.deriveFont(this.geoModel, 16.0F);
        gprops.add(this.geoModel);
        gprops.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/armor_texture"),
        L10N.label("elementgui.animatedarmor.texture")));
        gprops.add(armorTextureFile, importmobtexture);
        gprops.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/animation_name"),
                L10N.label("elementgui.animatedarmor.idle_animation")));
        gprops.add(idle);
        gprops.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/fully_equipped"),
                L10N.label("elementgui.animatedarmor.fully_equipped")));
        gprops.add(fullyEquipped);
        gprops.add(new JEmptyBox()); gprops.add(new JEmptyBox());
        gprops.add(L10N.label("elementgui.animatedarmor.head"));
        gprops.add(head);
        gprops.add(L10N.label("elementgui.animatedarmor.chest"));
        gprops.add(chest);
        gprops.add(L10N.label("elementgui.animatedarmor.right_arm"));
        gprops.add(rightArm);
        gprops.add(L10N.label("elementgui.animatedarmor.left_arm"));
        gprops.add(leftArm);
        gprops.add(L10N.label("elementgui.animatedarmor.right_leg"));
        gprops.add(rightLeg);
        gprops.add(L10N.label("elementgui.animatedarmor.left_leg"));
        gprops.add(leftLeg);
        gprops.add(L10N.label("elementgui.animatedarmor.right_boot"));
        gprops.add(rightBoot);
        gprops.add(L10N.label("elementgui.animatedarmor.left_boot"));
        gprops.add(leftBoot);


        pane3.add("Center", PanelUtils.totalCenterInPanel(gprops));

        gprops.setOpaque(false);
        pane3.setOpaque(false);
        this.fullyEquipped.setOpaque(false);

        geoModel.setValidator(() -> {
            if (geoModel.getSelectedItem() == null || geoModel.getSelectedItem().equals(""))
                return new Validator.ValidationResult(Validator.ValidationResultType.ERROR,
                        L10N.t("elementgui.animatedentity.modelname"));
            return Validator.ValidationResult.PASSED;
        });

        this.idle.setValidator(
                new TextFieldValidator(this.idle, L10N.t("elementgui.animatedarmor.idle_required")));
        this.idle.enableRealtimeValidation();

        group2page.addValidationElement(geoModel);
        group2page.addValidationElement(idle);

        addPage(L10N.t("elementgui.common.page_visual"), pane2);
        addPage(L10N.t("elementgui.common.page_properties"), pane5);
        addPage(L10N.t("elementgui.animatedarmor.geckolib_properties"), pane3);
        addPage(L10N.t("elementgui.common.page_triggers"), pane6);

        if (!isEditingMode()) {
            String readableNameFromModElement = StringUtils.machineToReadableName(modElement.getName());
            helmetName.setText(L10N.t("elementgui.armor.helmet", readableNameFromModElement));
            bodyName.setText(L10N.t("elementgui.armor.chestplate", readableNameFromModElement));
            leggingsName.setText(L10N.t("elementgui.armor.leggings", readableNameFromModElement));
            bootsName.setText(L10N.t("elementgui.armor.boots", readableNameFromModElement));
            head.setText("armorHead");
            chest.setText("armorBody");
            rightArm.setText("armorRightArm");
            leftArm.setText("armorLeftArm");
            rightLeg.setText("armorRightLeg");
            leftLeg.setText("armorLeftLeg");
            rightBoot.setText("armorRightBoot");
            leftBoot.setText("armorLeftBoot");
        }
    }

    @Override public void reloadDataLists() {
        super.reloadDataLists();

        ComboBoxUtil.updateComboBoxContents(this.armorTextureFile, ListUtils.merge(Collections.singleton(""), (Collection)this.mcreator.getFolderManager().getTexturesList(TextureType.ITEM).stream().map(File::getName).filter((s) -> {
            return s.endsWith(".png");
        }).collect(Collectors.toList())), "");

        onHelmetTick.refreshListKeepSelected();
        onBodyTick.refreshListKeepSelected();
        onLeggingsTick.refreshListKeepSelected();
        onBootsTick.refreshListKeepSelected();
        ComboBoxUtil.updateComboBoxContents(creativeTab, ElementUtil.loadAllTabs(mcreator.getWorkspace()),
                new DataListEntry.Dummy("COMBAT"));

        ComboBoxUtil.updateComboBoxContents(helmetItemRenderType, ListUtils.merge(Arrays.asList(normal, tool),
                Model.getModelsWithTextureMaps(mcreator.getWorkspace()).stream()
                        .filter(el -> el.getType() == Model.Type.JSON || el.getType() == Model.Type.OBJ)
                        .collect(Collectors.toList())));

        ComboBoxUtil.updateComboBoxContents(bodyItemRenderType, ListUtils.merge(Arrays.asList(normal, tool),
                Model.getModelsWithTextureMaps(mcreator.getWorkspace()).stream()
                        .filter(el -> el.getType() == Model.Type.JSON || el.getType() == Model.Type.OBJ)
                        .collect(Collectors.toList())));

        ComboBoxUtil.updateComboBoxContents(leggingsItemRenderType, ListUtils.merge(Arrays.asList(normal, tool),
                Model.getModelsWithTextureMaps(mcreator.getWorkspace()).stream()
                        .filter(el -> el.getType() == Model.Type.JSON || el.getType() == Model.Type.OBJ)
                        .collect(Collectors.toList())));

        ComboBoxUtil.updateComboBoxContents(bootsItemRenderType, ListUtils.merge(Arrays.asList(normal, tool),
                Model.getModelsWithTextureMaps(mcreator.getWorkspace()).stream()
                        .filter(el -> el.getType() == Model.Type.JSON || el.getType() == Model.Type.OBJ)
                        .collect(Collectors.toList())));

        ComboBoxUtil.updateComboBoxContents(this.geoModel, ListUtils.merge(Collections.singleton(""),
                (Collection)PluginModelActions.getGeomodels(this.mcreator).stream().map(File::getName).filter((s) -> {
            return s.endsWith(".geo.json");
        }).collect(Collectors.toList())), "");

    }

    @Override protected AggregatedValidationResult validatePage(int page) {
        if (page == 2)
            return new AggregatedValidationResult(group2page);
        else if (page == 0)
            return new AggregatedValidationResult(group1page);
        return new AggregatedValidationResult.PASS();
    }

    @Override public void openInEditingMode(AnimatedArmor armor) {
        helmetCollapsiblePanel.toggleVisibility(
                !helmetSpecialInfo.getText().isEmpty());
        bodyCollapsiblePanel.toggleVisibility(
                !bodySpecialInfo.getText().isEmpty());
        leggingsCollapsiblePanel.toggleVisibility(
                !leggingsSpecialInfo.getText().isEmpty());
        bootsCollapsiblePanel.toggleVisibility(
                !bootsSpecialInfo.getText().isEmpty());
        textureHelmet.setTextureFromTextureName(armor.textureHelmet);
        textureBody.setTextureFromTextureName(armor.textureBody);
        this.geoModel.setSelectedItem(armor.model);
        idle.setText(armor.idle);
        head.setText(armor.head);
        chest.setText(armor.chest);
        rightArm.setText(armor.rightArm);
        leftArm.setText(armor.leftArm);
        rightLeg.setText(armor.rightLeg);
        leftLeg.setText(armor.leftLeg);
        rightBoot.setText(armor.rightBoot);
        leftBoot.setText(armor.leftBoot);
        fullyEquipped.setSelected(armor.fullyEquipped);
        textureLeggings.setTextureFromTextureName(armor.textureLeggings);
        textureBoots.setTextureFromTextureName(armor.textureBoots);
        armorTextureFile.setSelectedItem(armor.armorTextureFile);
        maxDamage.setValue(armor.maxDamage);
        damageValueBoots.setValue(armor.damageValueBoots);
        damageValueLeggings.setValue(armor.damageValueLeggings);
        damageValueBody.setValue(armor.damageValueBody);
        damageValueHelmet.setValue(armor.damageValueHelmet);
        enchantability.setValue(armor.enchantability);
        toughness.setValue(armor.toughness);
        knockbackResistance.setValue(armor.knockbackResistance);
        onHelmetTick.setSelectedProcedure(armor.onHelmetTick);
        onBodyTick.setSelectedProcedure(armor.onBodyTick);
        onLeggingsTick.setSelectedProcedure(armor.onLeggingsTick);
        onBootsTick.setSelectedProcedure(armor.onBootsTick);
        enableHelmet.setSelected(armor.enableHelmet);
        enableBody.setSelected(armor.enableBody);
        enableLeggings.setSelected(armor.enableLeggings);
        enableBoots.setSelected(armor.enableBoots);
        creativeTab.setSelectedItem(armor.creativeTab);
        textureHelmet.setEnabled(enableHelmet.isSelected());
        textureBody.setEnabled(enableBody.isSelected());
        textureLeggings.setEnabled(enableLeggings.isSelected());
        textureBoots.setEnabled(enableBoots.isSelected());
        helmetName.setText(armor.helmetName);
        bodyName.setText(armor.bodyName);
        leggingsName.setText(armor.leggingsName);
        bootsName.setText(armor.bootsName);
        repairItems.setListElements(armor.repairItems);
        equipSound.setSound(armor.equipSound);

        helmetSpecialInfo.setText(armor.helmetSpecialInfo.stream().map(info -> info.replace(",", "\\,"))
                .collect(Collectors.joining(",")));
        bodySpecialInfo.setText(
                armor.bodySpecialInfo.stream().map(info -> info.replace(",", "\\,")).collect(Collectors.joining(",")));
        leggingsSpecialInfo.setText(armor.leggingsSpecialInfo.stream().map(info -> info.replace(",", "\\,"))
                .collect(Collectors.joining(",")));
        bootsSpecialInfo.setText(
                armor.bootsSpecialInfo.stream().map(info -> info.replace(",", "\\,")).collect(Collectors.joining(",")));

        helmetImmuneToFire.setSelected(armor.helmetImmuneToFire);
        bodyImmuneToFire.setSelected(armor.bodyImmuneToFire);
        leggingsImmuneToFire.setSelected(armor.leggingsImmuneToFire);
        bootsImmuneToFire.setSelected(armor.bootsImmuneToFire);

        Model helmetItemModel = armor.getHelmetItemModel();
        if (helmetItemModel != null)
            helmetItemRenderType.setSelectedItem(helmetItemModel);
        Model bodyItemModel = armor.getBodyItemModel();
        if (bodyItemModel != null)
            bodyItemRenderType.setSelectedItem(bodyItemModel);
        Model leggingsItemModel = armor.getLeggingsItemModel();
        if (leggingsItemModel != null)
            leggingsItemRenderType.setSelectedItem(leggingsItemModel);
        Model bootsItemModel = armor.getBootsItemModel();
        if (bootsItemModel != null)
            bootsItemRenderType.setSelectedItem(bootsItemModel);

    }

    @Override public AnimatedArmor getElementFromGUI() {
        AnimatedArmor armor = new AnimatedArmor(modElement);
        armor.enableHelmet = enableHelmet.isSelected();
        armor.textureHelmet = textureHelmet.getID();
        armor.enableBody = enableBody.isSelected();
        armor.model = (String)this.geoModel.getSelectedItem();
        armor.idle = idle.getText();
        armor.head = head.getText();
        armor.chest = chest.getText();
        armor.rightArm = rightArm.getText();
        armor.leftArm = leftArm.getText();
        armor.rightLeg = rightLeg.getText();
        armor.leftLeg = leftLeg.getText();
        armor.rightBoot = rightBoot.getText();
        armor.leftBoot = leftBoot.getText();
        armor.fullyEquipped = fullyEquipped.isSelected();
        armor.textureBody = textureBody.getID();
        armor.enableLeggings = enableLeggings.isSelected();
        armor.textureLeggings = textureLeggings.getID();
        armor.enableBoots = enableBoots.isSelected();
        armor.textureBoots = textureBoots.getID();
        armor.onHelmetTick = onHelmetTick.getSelectedProcedure();
        armor.onBodyTick = onBodyTick.getSelectedProcedure();
        armor.onLeggingsTick = onLeggingsTick.getSelectedProcedure();
        armor.onBootsTick = onBootsTick.getSelectedProcedure();
        armor.creativeTab = new TabEntry(mcreator.getWorkspace(), creativeTab.getSelectedItem());
        armor.armorTextureFile = (String)this.armorTextureFile.getSelectedItem();
        armor.maxDamage = (int) maxDamage.getValue();
        armor.damageValueHelmet = (int) damageValueHelmet.getValue();
        armor.damageValueBody = (int) damageValueBody.getValue();
        armor.damageValueLeggings = (int) damageValueLeggings.getValue();
        armor.damageValueBoots = (int) damageValueBoots.getValue();
        armor.enchantability = (int) enchantability.getValue();
        armor.toughness = (double) toughness.getValue();
        armor.knockbackResistance = (double) knockbackResistance.getValue();
        armor.helmetName = helmetName.getText();
        armor.bodyName = bodyName.getText();
        armor.leggingsName = leggingsName.getText();
        armor.bootsName = bootsName.getText();
        armor.equipSound = equipSound.getSound();
        armor.repairItems = repairItems.getListElements();
        armor.helmetSpecialInfo = RefactoredSystemsICopyPastedBecauseIWasTooBloodyLazyToActuallyProperlyReplace.splitCommaSeparatedStringListWithEscapes(helmetSpecialInfo.getText());
        armor.bodySpecialInfo = RefactoredSystemsICopyPastedBecauseIWasTooBloodyLazyToActuallyProperlyReplace.splitCommaSeparatedStringListWithEscapes(bodySpecialInfo.getText());
        armor.leggingsSpecialInfo = RefactoredSystemsICopyPastedBecauseIWasTooBloodyLazyToActuallyProperlyReplace.splitCommaSeparatedStringListWithEscapes(leggingsSpecialInfo.getText());
        armor.bootsSpecialInfo = RefactoredSystemsICopyPastedBecauseIWasTooBloodyLazyToActuallyProperlyReplace.splitCommaSeparatedStringListWithEscapes(bootsSpecialInfo.getText());
        armor.helmetImmuneToFire = helmetImmuneToFire.isSelected();
        armor.bodyImmuneToFire = bodyImmuneToFire.isSelected();
        armor.leggingsImmuneToFire = leggingsImmuneToFire.isSelected();
        armor.bootsImmuneToFire = bootsImmuneToFire.isSelected();

        Model.Type helmetModelType = Objects.requireNonNull(helmetItemRenderType.getSelectedItem()).getType();
        armor.helmetItemRenderType = 0;
        if (helmetModelType == Model.Type.JSON)
            armor.helmetItemRenderType = 1;
        else if (helmetModelType == Model.Type.OBJ)
            armor.helmetItemRenderType = 2;
        armor.helmetItemCustomModelName = Objects.requireNonNull(helmetItemRenderType.getSelectedItem())
                .getReadableName();

        Model.Type bodyModelType = Objects.requireNonNull(bodyItemRenderType.getSelectedItem()).getType();
        armor.bodyItemRenderType = 0;
        if (bodyModelType == Model.Type.JSON)
            armor.bodyItemRenderType = 1;
        else if (bodyModelType == Model.Type.OBJ)
            armor.bodyItemRenderType = 2;
        armor.bodyItemCustomModelName = Objects.requireNonNull(bodyItemRenderType.getSelectedItem()).getReadableName();

        Model.Type leggingsModelType = Objects.requireNonNull(leggingsItemRenderType.getSelectedItem()).getType();
        armor.leggingsItemRenderType = 0;
        if (leggingsModelType == Model.Type.JSON)
            armor.leggingsItemRenderType = 1;
        else if (leggingsModelType == Model.Type.OBJ)
            armor.leggingsItemRenderType = 2;
        armor.leggingsItemCustomModelName = Objects.requireNonNull(leggingsItemRenderType.getSelectedItem())
                .getReadableName();

        Model.Type bootsModelType = Objects.requireNonNull(bootsItemRenderType.getSelectedItem()).getType();
        armor.bootsItemRenderType = 0;
        if (bootsModelType == Model.Type.JSON)
            armor.bootsItemRenderType = 1;
        else if (bootsModelType == Model.Type.OBJ)
            armor.bootsItemRenderType = 2;
        armor.bootsItemCustomModelName = Objects.requireNonNull(bootsItemRenderType.getSelectedItem())
                .getReadableName();

        return armor;
    }

}