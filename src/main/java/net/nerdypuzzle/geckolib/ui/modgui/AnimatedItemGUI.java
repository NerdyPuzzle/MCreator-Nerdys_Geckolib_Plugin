package net.nerdypuzzle.geckolib.ui.modgui;

import net.mcreator.blockly.data.Dependency;
import net.mcreator.element.GeneratableElement;
import net.mcreator.element.ModElementType;
import net.mcreator.element.parts.TabEntry;
import net.mcreator.element.types.GUI;
import net.mcreator.minecraft.DataListEntry;
import net.mcreator.minecraft.ElementUtil;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.JEmptyBox;
import net.mcreator.ui.component.JStringListField;
import net.mcreator.ui.component.SearchableComboBox;
import net.mcreator.ui.component.util.ComboBoxUtil;
import net.mcreator.ui.component.util.ComponentUtils;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.dialogs.TypedTextureSelectorDialog;
import net.mcreator.ui.help.HelpUtils;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.minecraft.DataListComboBox;
import net.mcreator.ui.minecraft.MCItemHolder;
import net.mcreator.ui.minecraft.TextureHolder;
import net.mcreator.ui.modgui.ModElementGUI;
import net.mcreator.ui.procedure.AbstractProcedureSelector;
import net.mcreator.ui.procedure.ProcedureSelector;
import net.mcreator.ui.procedure.StringListProcedureSelector;
import net.mcreator.ui.validation.AggregatedValidationResult;
import net.mcreator.ui.validation.IValidable;
import net.mcreator.ui.validation.ValidationGroup;
import net.mcreator.ui.validation.Validator;
import net.mcreator.ui.validation.component.VComboBox;
import net.mcreator.ui.validation.component.VTextField;
import net.mcreator.ui.validation.validators.TextFieldValidator;
import net.mcreator.ui.validation.validators.TileHolderValidator;
import net.mcreator.ui.workspace.resources.TextureType;
import net.mcreator.util.ListUtils;
import net.mcreator.util.StringUtils;
import net.mcreator.workspace.elements.ModElement;
import net.mcreator.workspace.elements.VariableTypeLoader;
import net.nerdypuzzle.geckolib.element.types.AnimatedItem;
import net.nerdypuzzle.geckolib.element.types.GeckolibElement;
import net.nerdypuzzle.geckolib.parts.GeomodelRenderer;
import net.nerdypuzzle.geckolib.parts.PluginModelActions;
import net.nerdypuzzle.geckolib.parts.arm_pose_list.JArmPoseList;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

public class AnimatedItemGUI extends ModElementGUI<AnimatedItem> implements GeckolibElement {
    private TextureHolder texture;
    private StringListProcedureSelector specialInformation;
    private final VTextField idle = new VTextField(20);
    private final JSpinner stackSize = new JSpinner(new SpinnerNumberModel(64, 0, 64, 1));
    private final VTextField name = new VTextField(20);
    private final JComboBox<String> rarity = new JComboBox(new String[]{"COMMON", "UNCOMMON", "RARE", "EPIC"});
    private final JComboBox<String> perspective = new JComboBox(new String[]{"All Perspectives", "First Person", "Third/Second Person"});
    private final MCItemHolder recipeRemainder;
    private final JSpinner enchantability;
    private final JSpinner useDuration;
    private final JSpinner toolType;
    private final JSpinner damageCount;
    private final JCheckBox immuneToFire;
    private final JCheckBox destroyAnyBlock;
    private final JCheckBox stayInGridWhenCrafting;
    private final JCheckBox damageOnCrafting;
    private final JCheckBox hasGlow;
    private final JCheckBox enableArmPose;
    private ProcedureSelector glowCondition;
    private final DataListComboBox creativeTab;
    private ProcedureSelector onRightClickedInAir;
    private ProcedureSelector onCrafted;
    private ProcedureSelector onRightClickedOnBlock;
    private ProcedureSelector onEntityHitWith;
    private ProcedureSelector onItemInInventoryTick;
    private ProcedureSelector onItemInUseTick;
    private ProcedureSelector onStoppedUsing;
    private ProcedureSelector onEntitySwing;
    private ProcedureSelector onDroppedByPlayer;
    private ProcedureSelector onFinishUsingItem;
    private final ValidationGroup page1group;
    private final JSpinner damageVsEntity;
    private final JCheckBox enableMeleeDamage;
    private final JComboBox<String> guiBoundTo;
    private final JSpinner inventorySize;
    private final JSpinner inventoryStackSize;
    private final JCheckBox isFood;
    private final JSpinner nutritionalValue;
    private final JSpinner saturation;
    private final JCheckBox isMeat;
    private final JCheckBox isAlwaysEdible;
    private final JComboBox<String> animation;
    private final MCItemHolder eatResultItem;
    private final JCheckBox firstPersonArms = L10N.checkbox("elementgui.common.enable", new Object[0]);
    private final JTextField leftArm = new JTextField(20);
    private final JTextField rightArm = new JTextField(20);

    private final VComboBox<String> geoModel;
    private final VComboBox<String> displaySettings;

    private JArmPoseList armPoseList;

    public AnimatedItemGUI(MCreator mcreator, ModElement modElement, boolean editingMode) {
        super(mcreator, modElement, editingMode);
        this.geoModel = new SearchableComboBox();
        this.displaySettings = new SearchableComboBox();
        this.recipeRemainder = new MCItemHolder(this.mcreator, ElementUtil::loadBlocksAndItems);
        this.enchantability = new JSpinner(new SpinnerNumberModel(0, -100, 128000, 1));
        this.useDuration = new JSpinner(new SpinnerNumberModel(0, -100, 128000, 1));
        this.toolType = new JSpinner(new SpinnerNumberModel(1.0, -100.0, 128000.0, 0.1));
        this.damageCount = new JSpinner(new SpinnerNumberModel(0, 0, 128000, 1));
        this.immuneToFire = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.destroyAnyBlock = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.enableArmPose = L10N.checkbox("elementgui.animateditem.enable_arm_pose", new Object[0]);
        this.stayInGridWhenCrafting = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.damageOnCrafting = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.hasGlow = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.creativeTab = new DataListComboBox(this.mcreator);
        this.page1group = new ValidationGroup();
        this.damageVsEntity = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 128000.0, 0.1));
        this.enableMeleeDamage = new JCheckBox();
        this.guiBoundTo = new JComboBox();
        this.inventorySize = new JSpinner(new SpinnerNumberModel(9, 0, 256, 1));
        this.inventoryStackSize = new JSpinner(new SpinnerNumberModel(64, 1, 1024, 1));
        this.isFood = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.nutritionalValue = new JSpinner(new SpinnerNumberModel(4, -1000, 1000, 1));
        this.saturation = new JSpinner(new SpinnerNumberModel(0.3, -1000.0, 1000.0, 0.1));
        this.isMeat = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.isAlwaysEdible = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.animation = new JComboBox(new String[]{"none", "eat", "block", "bow", "crossbow", "drink", "spear"});
        this.eatResultItem = new MCItemHolder(this.mcreator, ElementUtil::loadBlocksAndItems);
        this.initGUI();
        super.finalizeGUI();
    }

    protected void initGUI() {
        this.onRightClickedInAir = new ProcedureSelector(this.withEntry("item/when_right_clicked"), this.mcreator, L10N.t("elementgui.common.event_right_clicked_air", new Object[0]), Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/itemstack:itemstack"));
        this.onCrafted = new ProcedureSelector(this.withEntry("item/on_crafted"), this.mcreator, L10N.t("elementgui.common.event_on_crafted", new Object[0]), Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/itemstack:itemstack"));
        this.onRightClickedOnBlock = (new ProcedureSelector(this.withEntry("item/when_right_clicked_block"), this.mcreator, L10N.t("elementgui.common.event_right_clicked_block", new Object[0]), VariableTypeLoader.BuiltInTypes.ACTIONRESULTTYPE, Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/itemstack:itemstack/direction:direction/blockstate:blockstate"))).makeReturnValueOptional();
        this.onEntityHitWith = new ProcedureSelector(this.withEntry("item/when_entity_hit"), this.mcreator, L10N.t("elementgui.item.event_entity_hit", new Object[0]), Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/sourceentity:entity/itemstack:itemstack"));
        this.onItemInInventoryTick = new ProcedureSelector(this.withEntry("item/inventory_tick"), this.mcreator, L10N.t("elementgui.item.event_inventory_tick", new Object[0]), Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/itemstack:itemstack/slot:number"));
        this.onItemInUseTick = new ProcedureSelector(this.withEntry("item/hand_tick"), this.mcreator, L10N.t("elementgui.item.event_hand_tick", new Object[0]), Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/itemstack:itemstack/slot:number"));
        this.onStoppedUsing = new ProcedureSelector(this.withEntry("item/when_stopped_using"), this.mcreator, L10N.t("elementgui.item.event_stopped_using", new Object[0]), Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/itemstack:itemstack/time:number"));
        this.onEntitySwing = new ProcedureSelector(this.withEntry("item/when_entity_swings"), this.mcreator, L10N.t("elementgui.item.event_entity_swings", new Object[0]), Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/itemstack:itemstack"));
        this.onDroppedByPlayer = new ProcedureSelector(this.withEntry("item/on_dropped"), this.mcreator, L10N.t("elementgui.item.event_on_dropped", new Object[0]), Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/itemstack:itemstack"));
        this.onFinishUsingItem = new ProcedureSelector(this.withEntry("item/when_stopped_using"), this.mcreator, L10N.t("elementgui.item.player_useitem_finish", new Object[0]), Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/itemstack:itemstack"));
        this.glowCondition = (new ProcedureSelector(this.withEntry("item/condition_glow"), this.mcreator, L10N.t("elementgui.item.glowcondition", new Object[0]), AbstractProcedureSelector.Side.CLIENT, true, VariableTypeLoader.BuiltInTypes.LOGIC, Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/itemstack:itemstack"))).makeInline();
        this.specialInformation = new StringListProcedureSelector(this.withEntry("item/special_information"), mcreator, L10N.t("elementgui.common.special_information"), AbstractProcedureSelector.Side.CLIENT, new JStringListField(mcreator, null), 0, Dependency.fromString("x:number/y:number/z:number/entity:entity/world:world/itemstack:itemstack"));
        this.armPoseList = new JArmPoseList(this.mcreator, this);
        this.guiBoundTo.addActionListener((e) -> {
            if (!this.isEditingMode()) {
                String selected = (String)this.guiBoundTo.getSelectedItem();
                if (selected != null) {
                    ModElement element = this.mcreator.getWorkspace().getModElementByName(selected);
                    if (element != null) {
                        GeneratableElement generatableElement = element.getGeneratableElement();
                        if (generatableElement instanceof GUI) {
                            this.inventorySize.setValue(((GUI)generatableElement).getMaxSlotID() + 1);
                        }
                    }
                }
            }

        });
        JPanel pane2 = new JPanel(new BorderLayout(10, 10));
        JPanel pane3 = new JPanel(new BorderLayout(10, 10));
        JPanel foodProperties = new JPanel(new BorderLayout(10, 10));
        JPanel advancedProperties = new JPanel(new BorderLayout(10, 10));
        JPanel pane4 = new JPanel(new BorderLayout(10, 10));
        this.texture = new TextureHolder(new TypedTextureSelectorDialog(this.mcreator, TextureType.ITEM));
        this.texture.setOpaque(false);
        JPanel destal2 = new JPanel(new BorderLayout(0, 10));
        destal2.setOpaque(false);
        JPanel destal3 = new JPanel(new BorderLayout(15, 15));
        destal3.setOpaque(false);
        destal3.add("West", PanelUtils.totalCenterInPanel(ComponentUtils.squareAndBorder(this.texture, L10N.t("elementgui.item.texture", new Object[0]))));
        destal2.add("North", destal3);


        JPanel destal = new JPanel(new GridLayout(8, 2, 15, 5));
        destal.setOpaque(false);
        JComponent destal1 = PanelUtils.join(0, new Component[]{HelpUtils.wrapWithHelpButton(this.withEntry("item/glowing_effect"), L10N.label("elementgui.item.glowing_effect", new Object[0])), this.hasGlow, this.glowCondition});
        destal.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/special_information"), L10N.label("elementgui.item.tooltip_tip", new Object[0])));
        destal.add(this.specialInformation);
        destal.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/animation_name"), L10N.label("elementgui.animateditem.idle_animation", new Object[0])));
        destal.add(this.idle);
        destal.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/animation_perspective"), L10N.label("elementgui.animateditem.perspective", new Object[0])));
        destal.add(this.perspective);
        destal.add(new JEmptyBox()); destal.add(new JEmptyBox());
        destal.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/first_person"), L10N.label("elementgui.animateditem.first_person", new Object[0])));
        destal.add(this.firstPersonArms);
        destal.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/model_arm"), L10N.label("elementgui.animateditem.left_arm", new Object[0])));
        destal.add(this.leftArm);
        destal.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/model_arm"), L10N.label("elementgui.animateditem.right_arm", new Object[0])));
        destal.add(this.rightArm);

        this.leftArm.setEnabled(firstPersonArms.isSelected());
        this.rightArm.setEnabled(firstPersonArms.isSelected());

        this.firstPersonArms.addActionListener((e) -> {
            this.leftArm.setEnabled(firstPersonArms.isSelected());
            this.rightArm.setEnabled(firstPersonArms.isSelected());
        });

        this.firstPersonArms.setOpaque(false);
        this.hasGlow.setOpaque(false);
        this.hasGlow.setSelected(false);
        this.hasGlow.addActionListener((e) -> {
            this.updateGlowElements();
        });
        destal2.add("Center", PanelUtils.northAndCenterElement(destal, destal1, 10, 10));
        ComponentUtils.deriveFont(this.idle, 16.0F);
        ComponentUtils.deriveFont(this.leftArm, 16.0F);
        ComponentUtils.deriveFont(this.rightArm, 16.0F);
        JPanel rent = new JPanel(new GridLayout(2, 1, 3, 3));
        rent.setOpaque(false);
        this.geoModel.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXX");
        this.geoModel.setRenderer(new GeomodelRenderer());
        ComponentUtils.deriveFont(this.geoModel, 16.0F);
        rent.add(PanelUtils.join(
                HelpUtils.wrapWithHelpButton(this.withEntry("item/model"), L10N.label("elementgui.animateditem.geckolib_model")), this.geoModel));
        this.displaySettings.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXX");
        this.displaySettings.setRenderer(new GeomodelRenderer());
        ComponentUtils.deriveFont(this.displaySettings, 16.0F);
        rent.add(PanelUtils.join(
                HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/display_settings"), L10N.label("elementgui.aniblockitems.display_settings")), this.displaySettings));
        destal3.add("Center", rent);
        rent.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder((Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1), L10N.t("elementgui.animateditem.model", new Object[0]), 0, 0, this.getFont().deriveFont(12.0F), (Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR")));
        JPanel sbbp2 = new JPanel(new BorderLayout());
        sbbp2.setOpaque(false);
        sbbp2.add("West", destal2);
        pane2.add("Center", PanelUtils.totalCenterInPanel(PanelUtils.centerInPanel(sbbp2)));
        pane2.setOpaque(false);
        JPanel subpane2 = new JPanel(new GridLayout(15, 2, 2, 2));
        ComponentUtils.deriveFont(this.name, 16.0F);
        subpane2.add(HelpUtils.wrapWithHelpButton(this.withEntry("common/gui_name"), L10N.label("elementgui.common.name_in_gui", new Object[0])));
        subpane2.add(this.name);
        subpane2.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/rarity"), L10N.label("elementgui.common.rarity", new Object[0])));
        subpane2.add(this.rarity);
        subpane2.add(HelpUtils.wrapWithHelpButton(this.withEntry("common/creative_tab"), L10N.label("elementgui.common.creative_tab", new Object[0])));
        subpane2.add(this.creativeTab);
        subpane2.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/stack_size"), L10N.label("elementgui.common.max_stack_size", new Object[0])));
        subpane2.add(this.stackSize);
        subpane2.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/enchantability"), L10N.label("elementgui.common.enchantability", new Object[0])));
        subpane2.add(this.enchantability);
        subpane2.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/destroy_speed"), L10N.label("elementgui.item.destroy_speed", new Object[0])));
        subpane2.add(this.toolType);
        subpane2.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/damage_vs_entity"), L10N.label("elementgui.item.damage_vs_entity", new Object[0])));
        subpane2.add(PanelUtils.westAndCenterElement(this.enableMeleeDamage, this.damageVsEntity));
        subpane2.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/number_of_uses"), L10N.label("elementgui.item.number_of_uses", new Object[0])));
        subpane2.add(this.damageCount);
        subpane2.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/immune_to_fire"), L10N.label("elementgui.item.is_immune_to_fire", new Object[0])));
        subpane2.add(this.immuneToFire);
        subpane2.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/can_destroy_any_block"), L10N.label("elementgui.item.can_destroy_any_block", new Object[0])));
        subpane2.add(this.destroyAnyBlock);
        subpane2.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/container_item"), L10N.label("elementgui.item.container_item", new Object[0])));
        subpane2.add(this.stayInGridWhenCrafting);
        subpane2.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/container_item_damage"), L10N.label("elementgui.item.container_item_damage", new Object[0])));
        subpane2.add(this.damageOnCrafting);
        subpane2.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/recipe_remainder"), L10N.label("elementgui.item.recipe_remainder", new Object[0])));
        subpane2.add(PanelUtils.centerInPanel(this.recipeRemainder));
        subpane2.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/animation"), L10N.label("elementgui.item.item_animation", new Object[0])));
        subpane2.add(this.animation);
        subpane2.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/use_duration"), L10N.label("elementgui.item.use_duration", new Object[0])));
        subpane2.add(this.useDuration);
        this.enchantability.setOpaque(false);
        this.useDuration.setOpaque(false);
        this.toolType.setOpaque(false);
        this.damageCount.setOpaque(false);
        this.immuneToFire.setOpaque(false);
        this.destroyAnyBlock.setOpaque(false);
        this.stayInGridWhenCrafting.setOpaque(false);
        this.damageOnCrafting.setOpaque(false);
        subpane2.setOpaque(false);
        pane3.setOpaque(false);
        pane3.add("Center", PanelUtils.totalCenterInPanel(subpane2));
        JPanel foodSubpane = new JPanel(new GridLayout(6, 2, 2, 2));
        foodSubpane.setOpaque(false);
        this.isFood.setOpaque(false);
        this.isMeat.setOpaque(false);
        this.isAlwaysEdible.setOpaque(false);
        this.nutritionalValue.setOpaque(false);
        this.saturation.setOpaque(false);
        this.isFood.addActionListener((e) -> {
            this.updateFoodPanel();
            if (!this.isEditingMode()) {
                this.animation.setSelectedItem("eat");
                this.useDuration.setValue(32);
            }

        });
        this.updateFoodPanel();
        foodSubpane.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/is_food"), L10N.label("elementgui.item.is_food", new Object[0])));
        foodSubpane.add(this.isFood);
        foodSubpane.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/nutritional_value"), L10N.label("elementgui.item.nutritional_value", new Object[0])));
        foodSubpane.add(this.nutritionalValue);
        foodSubpane.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/saturation"), L10N.label("elementgui.item.saturation", new Object[0])));
        foodSubpane.add(this.saturation);
        foodSubpane.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/result_item"), L10N.label("elementgui.item.eating_result", new Object[0])));
        foodSubpane.add(PanelUtils.centerInPanel(this.eatResultItem));
        foodSubpane.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/is_meat"), L10N.label("elementgui.item.is_meat", new Object[0])));
        foodSubpane.add(this.isMeat);
        foodSubpane.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/always_edible"), L10N.label("elementgui.item.is_edible", new Object[0])));
        foodSubpane.add(this.isAlwaysEdible);
        foodProperties.add("Center", PanelUtils.totalCenterInPanel(foodSubpane));
        foodProperties.setOpaque(false);
        advancedProperties.setOpaque(false);
        JPanel events = new JPanel(new GridLayout(4, 3, 10, 10));
        events.setOpaque(false);
        events.add(this.onRightClickedInAir);
        events.add(this.onRightClickedOnBlock);
        events.add(this.onCrafted);
        events.add(this.onEntityHitWith);
        events.add(this.onItemInInventoryTick);
        events.add(this.onItemInUseTick);
        events.add(this.onStoppedUsing);
        events.add(this.onEntitySwing);
        events.add(this.onDroppedByPlayer);
        events.add(this.onFinishUsingItem);
        pane4.add("Center", PanelUtils.totalCenterInPanel(PanelUtils.maxMargin(events, 20, true, true, true, true)));
        pane4.setOpaque(false);
        JPanel inventoryProperties = new JPanel(new GridLayout(3, 2, 35, 2));
        inventoryProperties.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder((Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1), L10N.t("elementgui.common.page_inventory", new Object[0]), 4, 0, this.getFont(), (Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR")));
        inventoryProperties.setOpaque(false);
        inventoryProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/bind_gui"), L10N.label("elementgui.item.bind_gui", new Object[0])));
        inventoryProperties.add(this.guiBoundTo);
        inventoryProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/inventory_size"), L10N.label("elementgui.item.inventory_size", new Object[0])));
        inventoryProperties.add(this.inventorySize);
        inventoryProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("item/inventory_stack_size"), L10N.label("elementgui.common.max_stack_size", new Object[0])));
        inventoryProperties.add(this.inventoryStackSize);
        advancedProperties.add("Center", PanelUtils.totalCenterInPanel(inventoryProperties));
        this.texture.setValidator(new TileHolderValidator(this.texture));
        this.page1group.addValidationElement(this.texture);
        this.page1group.addValidationElement(this.idle);
        this.page1group.addValidationElement(this.geoModel);
        this.page1group.addValidationElement(this.displaySettings);
        this.name.setValidator(new TextFieldValidator(this.name, L10N.t("elementgui.item.error_item_needs_name", new Object[0])));
        this.name.enableRealtimeValidation();

        JPanel paneHands = new JPanel(new BorderLayout());
        paneHands.setOpaque(false);
        enableArmPose.setOpaque(false);
        armPoseList.setEnabled(enableArmPose.isSelected());
        enableArmPose.addActionListener((e) -> {
            armPoseList.setEnabled(enableArmPose.isSelected());
        });
        JComponent poseEditor = PanelUtils.northAndCenterElement(enableArmPose, armPoseList);
        paneHands.add(PanelUtils.northAndCenterElement(PanelUtils.join(0, new Component[]{new JEmptyBox()}), poseEditor));

        this.addPage(L10N.t("elementgui.common.page_visual", new Object[0]), pane2);
        this.addPage(L10N.t("elementgui.animateditem.page_hands", new Object[0]), paneHands);
        this.addPage(L10N.t("elementgui.common.page_properties", new Object[0]), pane3);
        this.addPage(L10N.t("elementgui.item.food_properties", new Object[0]), foodProperties);
        this.addPage(L10N.t("elementgui.common.page_advanced_properties", new Object[0]), advancedProperties);
        this.addPage(L10N.t("elementgui.common.page_triggers", new Object[0]), pane4);
        if (!this.isEditingMode()) {
            String readableNameFromModElement = StringUtils.machineToReadableName(this.modElement.getName());
            this.name.setText(readableNameFromModElement);
        }

        geoModel.setValidator(() -> {
            if (geoModel.getSelectedItem() == null || geoModel.getSelectedItem().equals(""))
                return new Validator.ValidationResult(Validator.ValidationResultType.ERROR,
                        L10N.t("elementgui.animatedentity.modelname"));
            return Validator.ValidationResult.PASSED;
        });

        displaySettings.setValidator(() -> {
            if (displaySettings.getSelectedItem() == null || displaySettings.getSelectedItem().equals(""))
                return new Validator.ValidationResult(Validator.ValidationResultType.ERROR,
                        L10N.t("elementgui.animatedentity.modelname"));
            return Validator.ValidationResult.PASSED;
        });

        this.idle.setValidator(new TextFieldValidator(this.idle, L10N.t("elementgui.animateditem.needs_idle", new Object[0])));
        this.idle.enableRealtimeValidation();

    }

    private void updateFoodPanel() {
        if (this.isFood.isSelected()) {
            this.nutritionalValue.setEnabled(true);
            this.saturation.setEnabled(true);
            this.isMeat.setEnabled(true);
            this.isAlwaysEdible.setEnabled(true);
            this.eatResultItem.setEnabled(true);
        } else {
            this.nutritionalValue.setEnabled(false);
            this.saturation.setEnabled(false);
            this.isMeat.setEnabled(false);
            this.isAlwaysEdible.setEnabled(false);
            this.eatResultItem.setEnabled(false);
        }

    }

    private void updateGlowElements() {
        this.glowCondition.setEnabled(this.hasGlow.isSelected());
    }

    public void reloadDataLists() {
        super.reloadDataLists();
        this.onRightClickedInAir.refreshListKeepSelected();
        this.onCrafted.refreshListKeepSelected();
        this.onRightClickedOnBlock.refreshListKeepSelected();
        this.onEntityHitWith.refreshListKeepSelected();
        this.specialInformation.refreshListKeepSelected();
        this.onItemInInventoryTick.refreshListKeepSelected();
        this.onItemInUseTick.refreshListKeepSelected();
        this.onStoppedUsing.refreshListKeepSelected();
        this.onEntitySwing.refreshListKeepSelected();
        this.onDroppedByPlayer.refreshListKeepSelected();
        this.onFinishUsingItem.refreshListKeepSelected();
        this.glowCondition.refreshListKeepSelected();
        ComboBoxUtil.updateComboBoxContents(this.creativeTab, ElementUtil.loadAllTabs(this.mcreator.getWorkspace()), new DataListEntry.Dummy("MISC"));
        ComboBoxUtil.updateComboBoxContents(this.guiBoundTo, ListUtils.merge(Collections.singleton("<NONE>"), (Collection)this.mcreator.getWorkspace().getModElements().stream().filter((var) -> {
            return var.getType() == ModElementType.GUI;
        }).map(ModElement::getName).collect(Collectors.toList())), "<NONE>");

        ComboBoxUtil.updateComboBoxContents(this.geoModel, ListUtils.merge(Collections.singleton(""), (Collection)PluginModelActions.getGeomodels(this.mcreator).stream().map(File::getName).filter((s) -> {
            return s.endsWith(".geo.json");
        }).collect(Collectors.toList())), "");

        ComboBoxUtil.updateComboBoxContents(this.displaySettings, ListUtils.merge(Collections.singleton(""), (Collection)PluginModelActions.getDisplaysettings(this.mcreator).stream().map(File::getName).filter((s) -> {
            return s.endsWith(".json");
        }).collect(Collectors.toList())), "");
    }

    protected AggregatedValidationResult validatePage(int page) {
        if (page == 1) {
            return new AggregatedValidationResult(new IValidable[]{this.name});
        } else {
            return (AggregatedValidationResult)(page == 0 ? new AggregatedValidationResult(new ValidationGroup[]{this.page1group}) : new AggregatedValidationResult.PASS());
        }
    }

    public void openInEditingMode(AnimatedItem item) {
        this.name.setText(item.name);
        this.idle.setText(item.idle);
        this.leftArm.setText(item.leftArm);
        this.rightArm.setText(item.rightArm);
        this.firstPersonArms.setSelected(item.firstPersonArms);
        this.rarity.setSelectedItem(item.rarity);
        this.perspective.setSelectedItem(item.perspective);
        this.texture.setTextureFromTextureName(item.texture);
        this.specialInformation.setSelectedProcedure(item.specialInformation);
        this.onRightClickedInAir.setSelectedProcedure(item.onRightClickedInAir);
        this.onRightClickedOnBlock.setSelectedProcedure(item.onRightClickedOnBlock);
        this.onCrafted.setSelectedProcedure(item.onCrafted);
        this.onEntityHitWith.setSelectedProcedure(item.onEntityHitWith);
        this.onItemInInventoryTick.setSelectedProcedure(item.onItemInInventoryTick);
        this.onItemInUseTick.setSelectedProcedure(item.onItemInUseTick);
        this.onStoppedUsing.setSelectedProcedure(item.onStoppedUsing);
        this.onEntitySwing.setSelectedProcedure(item.onEntitySwing);
        this.onDroppedByPlayer.setSelectedProcedure(item.onDroppedByPlayer);
        this.creativeTab.setSelectedItem(item.creativeTab);
        this.stackSize.setValue(item.stackSize);
        this.enchantability.setValue(item.enchantability);
        this.toolType.setValue(item.toolType);
        this.useDuration.setValue(item.useDuration);
        this.damageCount.setValue(item.damageCount);
        this.recipeRemainder.setBlock(item.recipeRemainder);
        this.immuneToFire.setSelected(item.immuneToFire);
        this.destroyAnyBlock.setSelected(item.destroyAnyBlock);
        this.stayInGridWhenCrafting.setSelected(item.stayInGridWhenCrafting);
        this.damageOnCrafting.setSelected(item.damageOnCrafting);
        this.hasGlow.setSelected(item.hasGlow);
        this.glowCondition.setSelectedProcedure(item.glowCondition);
        this.damageVsEntity.setValue(item.damageVsEntity);
        this.enableMeleeDamage.setSelected(item.enableMeleeDamage);
        this.guiBoundTo.setSelectedItem(item.guiBoundTo);
        this.inventorySize.setValue(item.inventorySize);
        this.inventoryStackSize.setValue(item.inventoryStackSize);
        this.isFood.setSelected(item.isFood);
        this.isMeat.setSelected(item.isMeat);
        this.isAlwaysEdible.setSelected(item.isAlwaysEdible);
        this.onFinishUsingItem.setSelectedProcedure(item.onFinishUsingItem);
        this.nutritionalValue.setValue(item.nutritionalValue);
        this.saturation.setValue(item.saturation);
        this.animation.setSelectedItem(item.animation);
        this.eatResultItem.setBlock(item.eatResultItem);
        this.updateGlowElements();
        this.updateFoodPanel();
        this.geoModel.setSelectedItem(item.normal);
        this.displaySettings.setSelectedItem(item.displaySettings);
        this.enableArmPose.setSelected(item.enableArmPose);
        this.armPoseList.setEntries(item.armPoseList);

        this.leftArm.setEnabled(firstPersonArms.isSelected());
        this.rightArm.setEnabled(firstPersonArms.isSelected());
        this.armPoseList.setEnabled(enableArmPose.isSelected());
    }

    public AnimatedItem getElementFromGUI() {
        AnimatedItem item = new AnimatedItem(this.modElement);
        item.name = this.name.getText();
        item.idle = this.idle.getText();
        item.leftArm = this.leftArm.getText();
        item.rightArm = this.rightArm.getText();
        item.firstPersonArms = this.firstPersonArms.isSelected();
        item.rarity = (String)this.rarity.getSelectedItem();
        item.perspective = (String)this.perspective.getSelectedItem();
        item.creativeTab = new TabEntry(this.mcreator.getWorkspace(), this.creativeTab.getSelectedItem());
        item.stackSize = (Integer)this.stackSize.getValue();
        item.enchantability = (Integer)this.enchantability.getValue();
        item.useDuration = (Integer)this.useDuration.getValue();
        item.toolType = (Double)this.toolType.getValue();
        item.damageCount = (Integer)this.damageCount.getValue();
        item.recipeRemainder = this.recipeRemainder.getBlock();
        item.immuneToFire = this.immuneToFire.isSelected();
        item.destroyAnyBlock = this.destroyAnyBlock.isSelected();
        item.enableArmPose = this.enableArmPose.isSelected();
        item.stayInGridWhenCrafting = this.stayInGridWhenCrafting.isSelected();
        item.damageOnCrafting = this.damageOnCrafting.isSelected();
        item.hasGlow = this.hasGlow.isSelected();
        item.glowCondition = this.glowCondition.getSelectedProcedure();
        item.onRightClickedInAir = this.onRightClickedInAir.getSelectedProcedure();
        item.onRightClickedOnBlock = this.onRightClickedOnBlock.getSelectedProcedure();
        item.onCrafted = this.onCrafted.getSelectedProcedure();
        item.onEntityHitWith = this.onEntityHitWith.getSelectedProcedure();
        item.onItemInInventoryTick = this.onItemInInventoryTick.getSelectedProcedure();
        item.onItemInUseTick = this.onItemInUseTick.getSelectedProcedure();
        item.onStoppedUsing = this.onStoppedUsing.getSelectedProcedure();
        item.onEntitySwing = this.onEntitySwing.getSelectedProcedure();
        item.onDroppedByPlayer = this.onDroppedByPlayer.getSelectedProcedure();
        item.damageVsEntity = (Double)this.damageVsEntity.getValue();
        item.enableMeleeDamage = this.enableMeleeDamage.isSelected();
        item.inventorySize = (Integer)this.inventorySize.getValue();
        item.inventoryStackSize = (Integer)this.inventoryStackSize.getValue();
        item.guiBoundTo = (String)this.guiBoundTo.getSelectedItem();
        item.isFood = this.isFood.isSelected();
        item.nutritionalValue = (Integer)this.nutritionalValue.getValue();
        item.saturation = (Double)this.saturation.getValue();
        item.isMeat = this.isMeat.isSelected();
        item.isAlwaysEdible = this.isAlwaysEdible.isSelected();
        item.animation = (String)this.animation.getSelectedItem();
        item.onFinishUsingItem = this.onFinishUsingItem.getSelectedProcedure();
        item.eatResultItem = this.eatResultItem.getBlock();
        item.specialInformation = this.specialInformation.getSelectedProcedure();
        item.texture = this.texture.getID();
        item.renderType = 0;
        item.normal = (String)this.geoModel.getSelectedItem();
        item.displaySettings = (String)this.displaySettings.getSelectedItem();
        item.armPoseList = this.armPoseList.getEntries();

        return item;
    }

}

