package net.nerdypuzzle.geckolib.ui.modgui;

import net.mcreator.blockly.data.Dependency;
import net.mcreator.element.GeneratableElement;
import net.mcreator.element.ModElementType;
import net.mcreator.element.parts.*;
import net.mcreator.element.parts.gui.GUIComponent;
import net.mcreator.element.parts.gui.InputSlot;
import net.mcreator.element.parts.gui.OutputSlot;
import net.mcreator.element.parts.gui.Slot;
import net.mcreator.element.types.GUI;
import net.mcreator.element.types.interfaces.IBlockWithBoundingBox;
import net.mcreator.minecraft.DataListEntry;
import net.mcreator.minecraft.ElementUtil;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.JColor;
import net.mcreator.ui.component.JEmptyBox;
import net.mcreator.ui.component.JStringListField;
import net.mcreator.ui.component.SearchableComboBox;
import net.mcreator.ui.component.util.ComboBoxFullWidthPopup;
import net.mcreator.ui.component.util.ComboBoxUtil;
import net.mcreator.ui.component.util.ComponentUtils;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.dialogs.TypedTextureSelectorDialog;
import net.mcreator.ui.help.HelpUtils;
import net.mcreator.ui.help.IHelpContext;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.init.UIRES;
import net.mcreator.ui.laf.renderer.ItemTexturesComboBoxRenderer;
import net.mcreator.ui.minecraft.*;
import net.mcreator.ui.minecraft.boundingboxes.JBoundingBoxList;
import net.mcreator.ui.modgui.ModElementGUI;
import net.mcreator.ui.procedure.AbstractProcedureSelector;
import net.mcreator.ui.procedure.NumberProcedureSelector;
import net.mcreator.ui.procedure.ProcedureSelector;
import net.mcreator.ui.procedure.StringListProcedureSelector;
import net.mcreator.ui.validation.AggregatedValidationResult;
import net.mcreator.ui.validation.IValidable;
import net.mcreator.ui.validation.ValidationGroup;
import net.mcreator.ui.validation.Validator;
import net.mcreator.ui.validation.component.VComboBox;
import net.mcreator.ui.validation.component.VTextField;
import net.mcreator.ui.validation.validators.*;
import net.mcreator.ui.workspace.resources.TextureType;
import net.mcreator.util.ListUtils;
import net.mcreator.util.StringUtils;
import net.mcreator.workspace.elements.ModElement;
import net.mcreator.workspace.elements.VariableTypeLoader;
import net.nerdypuzzle.geckolib.element.types.AnimatedBlock;
import net.nerdypuzzle.geckolib.element.types.GeckolibElement;
import net.nerdypuzzle.geckolib.parts.GeomodelRenderer;
import net.nerdypuzzle.geckolib.parts.PluginModelActions;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnimatedBlockGUI extends ModElementGUI<AnimatedBlock> implements GeckolibElement {
    private final DataListComboBox material;
    private TextureHolder texture;
    private TextureHolder textureTop;
    private TextureHolder textureLeft;
    private TextureHolder textureFront;
    private TextureHolder textureRight;
    private TextureHolder textureBack;
    private TextureHolder itemTexture;
    private TextureHolder particleTexture;
    private final JCheckBox disableOffset;
    private JBoundingBoxList boundingBoxList;
    private ProcedureSelector onBlockAdded;
    private ProcedureSelector onNeighbourBlockChanges;
    private ProcedureSelector onTickUpdate;
    private ProcedureSelector onRandomUpdateEvent;
    private ProcedureSelector onDestroyedByPlayer;
    private ProcedureSelector onDestroyedByExplosion;
    private ProcedureSelector onStartToDestroy;
    private ProcedureSelector onEntityCollides;
    private ProcedureSelector onEntityWalksOn;
    private ProcedureSelector onBlockPlayedBy;
    private ProcedureSelector onRightClicked;
    private ProcedureSelector onRedstoneOn;
    private ProcedureSelector onRedstoneOff;
    private ProcedureSelector onHitByProjectile;
    private ProcedureSelector particleCondition;
    private NumberProcedureSelector emittedRedstonePower;
    private ProcedureSelector placingCondition;
    private ProcedureSelector generateCondition;
    private final JSpinner hardness;
    private final JSpinner resistance;
    private final VTextField name;
    private final JSpinner luminance;
    private final JSpinner dropAmount;
    private final JSpinner lightOpacity;
    private final JSpinner tickRate;
    private final JSpinner enchantPowerBonus;
    private final JColor beaconColorModifier;
    private final JCheckBox hasGravity;
    private final JCheckBox isWaterloggable;
    private final JCheckBox tickRandomly;
    private final JCheckBox unbreakable;
    private final JCheckBox isNotColidable;
    private final JCheckBox canRedstoneConnect;
    private final JComboBox<String> tintType;
    private final JCheckBox isItemTinted;
    private final JCheckBox hasTransparency;
    private final JCheckBox connectedSides;
    private final JCheckBox emissiveRendering;
    private final JCheckBox displayFluidOverlay;
    private final JCheckBox hasEnergyStorage;
    private final JCheckBox isFluidTank;
    private final JSpinner energyInitial;
    private final JSpinner energyCapacity;
    private final JSpinner energyMaxReceive;
    private final JSpinner energyMaxExtract;
    private final JSpinner fluidCapacity;
    private final JSpinner animationCount;
    private FluidListField fluidRestrictions;
    private final DataListComboBox soundOnStep;
    private final JRadioButton defaultSoundType;
    private final JRadioButton customSoundType;
    private final SoundSelector breakSound;
    private final SoundSelector fallSound;
    private final SoundSelector hitSound;
    private final SoundSelector placeSound;
    private final SoundSelector stepSound;
    private final JCheckBox isReplaceable;
    private final JCheckBox canProvidePower;
    private final JComboBox<String> colorOnMap;
    private final MCItemHolder creativePickItem;
    private final MCItemHolder customDrop;
    private final JComboBox<String> generationShape;
    private final JSpinner minGenerateHeight;
    private final JSpinner maxGenerateHeight;
    private final JSpinner frequencyPerChunks;
    private final JSpinner frequencyOnChunk;
    private BiomeListField restrictionBiomes;
    private MCItemListField blocksToReplace;
    private final JCheckBox plantsGrowOn;

    private final JCheckBox generateFeature = L10N.checkbox("elementgui.common.enable");
    private final JCheckBox isLadder;
    private final JComboBox<String> reactionToPushing;
    private final JComboBox<String> offsetType;
    private final JComboBox<String> aiPathNodeType;
    private final DataListComboBox creativeTab;
    private final DataListComboBox particleToSpawn;
    private final JComboBox<String> particleSpawningShape;
    private final JSpinner particleSpawningRadious;
    private final JSpinner particleAmount;
    private final JSpinner slipperiness;
    private final JSpinner speedFactor;
    private final JSpinner jumpFactor;
    private final JComboBox<String> rotationMode;
    private final JCheckBox enablePitch;
    private final JComboBox<String> destroyTool;
    private final JSpinner breakHarvestLevel;
    private final JCheckBox requiresCorrectTool;
    private final JCheckBox spawnParticles;
    private final JComboBox<String> transparencyType;
    private final JCheckBox hasInventory;
    private final JCheckBox openGUIOnRightClick;
    private final JComboBox<String> guiBoundTo;
    private final JSpinner inventorySize;
    private final JSpinner inventoryStackSize;
    private final JCheckBox inventoryDropWhenDestroyed;
    private final JCheckBox inventoryComparatorPower;
    private final VTextField outSlotIDs;
    private final VTextField inSlotIDs;
    private StringListProcedureSelector specialInformation;
    private final ValidationGroup page1group;
    private final ValidationGroup page3group;
    private final JComboBox<String> blockBase;
    private final JSpinner flammability;
    private final JSpinner fireSpreadSpeed;
    private final JCheckBox useLootTableForDrops;
    private final VComboBox<String> geoModel;
    private final VComboBox<String> displaySettings;

    public AnimatedBlockGUI(MCreator mcreator, ModElement modElement, boolean editingMode) {
        super(mcreator, modElement, editingMode);
        this.geoModel = new SearchableComboBox();
        this.displaySettings = new SearchableComboBox();
        this.material = new DataListComboBox(this.mcreator, ElementUtil.loadMaterials());
        this.disableOffset = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.hardness = new JSpinner(new SpinnerNumberModel(1.0, -1.0, 64000.0, 0.05));
        this.resistance = new JSpinner(new SpinnerNumberModel(10.0, 0.0, 2.147483647E9, 0.5));
        this.name = new VTextField(19);
        this.luminance = new JSpinner(new SpinnerNumberModel(0, 0, 15, 1));
        this.dropAmount = new JSpinner(new SpinnerNumberModel(1, 0, 64, 1));
        this.lightOpacity = new JSpinner(new SpinnerNumberModel(15, 0, 15, 1));
        this.tickRate = new JSpinner(new SpinnerNumberModel(0, 0, 9999999, 1));
        this.enchantPowerBonus = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1024.0, 0.1));
        this.beaconColorModifier = new JColor(this.mcreator, true, false);
        this.hasGravity = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.isWaterloggable = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.tickRandomly = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.unbreakable = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.isNotColidable = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.canRedstoneConnect = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.tintType = new JComboBox(new String[]{"No tint", "Grass", "Foliage", "Birch foliage", "Spruce foliage", "Default foliage", "Water", "Sky", "Fog", "Water fog"});
        this.isItemTinted = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.hasTransparency = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.connectedSides = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.emissiveRendering = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.displayFluidOverlay = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.hasEnergyStorage = L10N.checkbox("elementgui.block.enable_energy_storage", new Object[0]);
        this.isFluidTank = L10N.checkbox("elementgui.block.enable_fluid_storage", new Object[0]);
        this.energyInitial = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        this.energyCapacity = new JSpinner(new SpinnerNumberModel(400000, 0, Integer.MAX_VALUE, 1));
        this.energyMaxReceive = new JSpinner(new SpinnerNumberModel(200, 0, Integer.MAX_VALUE, 1));
        this.energyMaxExtract = new JSpinner(new SpinnerNumberModel(200, 0, Integer.MAX_VALUE, 1));
        this.fluidCapacity = new JSpinner(new SpinnerNumberModel(8000, 0, Integer.MAX_VALUE, 1));
        this.soundOnStep = new DataListComboBox(this.mcreator, ElementUtil.loadStepSounds());
        this.defaultSoundType = L10N.radiobutton("elementgui.common.default_sound_type", new Object[0]);
        this.customSoundType = L10N.radiobutton("elementgui.common.custom_sound_type", new Object[0]);
        this.breakSound = new SoundSelector(this.mcreator);
        this.fallSound = new SoundSelector(this.mcreator);
        this.hitSound = new SoundSelector(this.mcreator);
        this.placeSound = new SoundSelector(this.mcreator);
        this.stepSound = new SoundSelector(this.mcreator);
        this.isReplaceable = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.canProvidePower = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.colorOnMap = new JComboBox();
        this.creativePickItem = new MCItemHolder(this.mcreator, ElementUtil::loadBlocksAndItems);
        this.customDrop = new MCItemHolder(this.mcreator, ElementUtil::loadBlocksAndItems);
        this.generationShape = new JComboBox(new String[]{"UNIFORM", "TRIANGLE"});
        this.minGenerateHeight = new JSpinner(new SpinnerNumberModel(0, -2032, 2016, 1));
        this.maxGenerateHeight = new JSpinner(new SpinnerNumberModel(64, -2032, 2016, 1));
        this.frequencyPerChunks = new JSpinner(new SpinnerNumberModel(10, 1, 64, 1));
        this.frequencyOnChunk = new JSpinner(new SpinnerNumberModel(16, 1, 64, 1));
        this.plantsGrowOn = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.isLadder = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.reactionToPushing = new JComboBox(new String[]{"NORMAL", "DESTROY", "BLOCK", "PUSH_ONLY", "IGNORE"});
        this.offsetType = new JComboBox(new String[]{"NONE", "XZ", "XYZ"});
        this.aiPathNodeType = new JComboBox();
        this.creativeTab = new DataListComboBox(this.mcreator);
        this.particleToSpawn = new DataListComboBox(this.mcreator);
        this.particleSpawningShape = new JComboBox(new String[]{"Spread", "Top", "Tube", "Plane"});
        this.particleSpawningRadious = new JSpinner(new SpinnerNumberModel(0.5, 0.0, 100.0, 0.1));
        this.particleAmount = new JSpinner(new SpinnerNumberModel(4, 0, 1000, 1));
        this.slipperiness = new JSpinner(new SpinnerNumberModel(0.6, 0.01, 5.0, 0.1));
        this.speedFactor = new JSpinner(new SpinnerNumberModel(1.0, -1000.0, 1000.0, 0.1));
        this.jumpFactor = new JSpinner(new SpinnerNumberModel(1.0, -1000.0, 1000.0, 0.1));
        this.animationCount = new JSpinner(new SpinnerNumberModel(1.0, 1.0, 100, 1));
        this.rotationMode = new JComboBox(new String[]{"<html>No rotation<br><small>Fixed block orientation", "<html>Y axis rotation (S/W/N/E)<br><small>Rotation from player side", "<html>D/U/N/S/W/E rotation<br><small>Rotation from player side", "<html>Y axis rotation (S/W/N/E)<br><small>Rotation from block face", "<html>D/U/N/S/W/E rotation<br><small>Rotation from block face", "<html>Log rotation (X/Y/Z)<br><small>Imitates vanilla log rotation"});
        this.enablePitch = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.destroyTool = new JComboBox(new String[]{"Not specified", "pickaxe", "axe", "shovel", "hoe"});
        this.breakHarvestLevel = new JSpinner(new SpinnerNumberModel(1, -1, 100, 1));
        this.requiresCorrectTool = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.spawnParticles = L10N.checkbox("elementgui.block.spawn_particles", new Object[0]);
        this.transparencyType = new JComboBox(new String[]{"SOLID", "CUTOUT", "CUTOUT_MIPPED", "TRANSLUCENT"});
        this.hasInventory = L10N.checkbox("elementgui.block.has_inventory", new Object[0]);
        this.openGUIOnRightClick = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.guiBoundTo = new JComboBox();
        this.inventorySize = new JSpinner(new SpinnerNumberModel(9, 0, 256, 1));
        this.inventoryStackSize = new JSpinner(new SpinnerNumberModel(64, 1, 1024, 1));
        this.inventoryDropWhenDestroyed = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.inventoryComparatorPower = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.outSlotIDs = new VTextField(18);
        this.inSlotIDs = new VTextField(18);
        this.page1group = new ValidationGroup();
        this.page3group = new ValidationGroup();
        this.blockBase = new JComboBox(new String[]{"Default basic block"});
        this.flammability = new JSpinner(new SpinnerNumberModel(0, 0, 1024, 1));
        this.fireSpreadSpeed = new JSpinner(new SpinnerNumberModel(0, 0, 1024, 1));
        this.useLootTableForDrops = L10N.checkbox("elementgui.common.use_table_loot_drops", new Object[0]);
        this.initGUI();
        super.finalizeGUI();
    }

    protected void initGUI() {
        this.destroyTool.setRenderer(new ItemTexturesComboBoxRenderer());
        this.blocksToReplace = new MCItemListField(this.mcreator, ElementUtil::loadBlocksAndTags, false, true);
        this.restrictionBiomes = new BiomeListField(this.mcreator);
        restrictionBiomes = new BiomeListField(mcreator, true);
        restrictionBiomes.setValidator(new ItemListFieldSingleTagValidator(restrictionBiomes));
        this.fluidRestrictions = new FluidListField(this.mcreator);
        boundingBoxList = new JBoundingBoxList(mcreator, this, null);
        this.blocksToReplace.setListElements(List.of(new MItemBlock(this.mcreator.getWorkspace(), "TAG:stone_ore_replaceables")));
        this.onBlockAdded = new ProcedureSelector(this.withEntry("block/when_added"), this.mcreator, L10N.t("elementgui.block.event_on_block_added", new Object[0]), Dependency.fromString("x:number/y:number/z:number/world:world/blockstate:blockstate/oldState:blockstate/moving:logic"));
        this.onNeighbourBlockChanges = new ProcedureSelector(this.withEntry("block/when_neighbour_changes"), this.mcreator, L10N.t("elementgui.common.event_on_neighbour_block_changes", new Object[0]), Dependency.fromString("x:number/y:number/z:number/world:world/blockstate:blockstate"));
        this.onTickUpdate = new ProcedureSelector(this.withEntry("block/update_tick"), this.mcreator, L10N.t("elementgui.common.event_on_update_tick", new Object[0]), Dependency.fromString("x:number/y:number/z:number/world:world/blockstate:blockstate"));
        this.onRandomUpdateEvent = new ProcedureSelector(this.withEntry("block/display_tick_update"), this.mcreator, L10N.t("elementgui.common.event_on_random_update", new Object[0]), AbstractProcedureSelector.Side.CLIENT, Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/blockstate:blockstate"));
        this.onDestroyedByPlayer = new ProcedureSelector(this.withEntry("block/when_destroyed_player"), this.mcreator, L10N.t("elementgui.block.event_on_block_destroyed_by_player", new Object[0]), Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/blockstate:blockstate"));
        this.onDestroyedByExplosion = new ProcedureSelector(this.withEntry("block/when_destroyed_explosion"), this.mcreator, L10N.t("elementgui.block.event_on_block_destroyed_by_explosion", new Object[0]), Dependency.fromString("x:number/y:number/z:number/world:world"));
        this.onStartToDestroy = new ProcedureSelector(this.withEntry("block/when_destroy_start"), this.mcreator, L10N.t("elementgui.block.event_on_player_starts_destroy", new Object[0]), Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/blockstate:blockstate"));
        this.onEntityCollides = new ProcedureSelector(this.withEntry("block/when_entity_collides"), this.mcreator, L10N.t("elementgui.block.event_on_entity_collides", new Object[0]), Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/blockstate:blockstate"));
        this.onEntityWalksOn = new ProcedureSelector(this.withEntry("block/when_entity_walks_on"), this.mcreator, L10N.t("elementgui.block.event_on_entity_walks_on", new Object[0]), Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/blockstate:blockstate"));
        this.onBlockPlayedBy = new ProcedureSelector(this.withEntry("block/when_block_placed_by"), this.mcreator, L10N.t("elementgui.common.event_on_block_placed_by", new Object[0]), Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/itemstack:itemstack/blockstate:blockstate"));
        this.onRightClicked = (new ProcedureSelector(this.withEntry("block/when_right_clicked"), this.mcreator, L10N.t("elementgui.block.event_on_right_clicked", new Object[0]), VariableTypeLoader.BuiltInTypes.ACTIONRESULTTYPE, Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/direction:direction/blockstate:blockstate/hitX:number/hitY:number/hitZ:number"))).makeReturnValueOptional();
        this.onRedstoneOn = new ProcedureSelector(this.withEntry("block/on_redstone_on"), this.mcreator, L10N.t("elementgui.block.event_on_redstone_on", new Object[0]), Dependency.fromString("x:number/y:number/z:number/world:world/blockstate:blockstate"));
        this.onRedstoneOff = new ProcedureSelector(this.withEntry("block/on_redstone_off"), this.mcreator, L10N.t("elementgui.block.event_on_redstone_off", new Object[0]), Dependency.fromString("x:number/y:number/z:number/world:world/blockstate:blockstate"));
        this.onHitByProjectile = new ProcedureSelector(this.withEntry("block/on_hit_by_projectile"), this.mcreator, L10N.t("elementgui.common.event_on_block_hit_by_projectile", new Object[0]), Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/direction:direction/blockstate:blockstate/hitX:number/hitY:number/hitZ:number"));
        this.particleCondition = (new ProcedureSelector(this.withEntry("block/particle_condition"), this.mcreator, L10N.t("elementgui.block.event_particle_condition", new Object[0]), AbstractProcedureSelector.Side.CLIENT, true, VariableTypeLoader.BuiltInTypes.LOGIC, Dependency.fromString("x:number/y:number/z:number/world:world/blockstate:blockstate"))).makeInline();
        this.emittedRedstonePower = new NumberProcedureSelector((IHelpContext)null, this.mcreator, new JSpinner(new SpinnerNumberModel(15, 0, 15, 1)), Dependency.fromString("x:number/y:number/z:number/world:world/direction:direction/blockstate:blockstate"));
        this.placingCondition = (new ProcedureSelector(this.withEntry("block/placing_condition"), this.mcreator, L10N.t("elementgui.block.event_placing_condition", new Object[0]), VariableTypeLoader.BuiltInTypes.LOGIC, Dependency.fromString("x:number/y:number/z:number/world:world/blockstate:blockstate"))).setDefaultName(L10N.t("condition.common.no_additional", new Object[0])).makeInline();
        this.generateCondition = (new ProcedureSelector(this.withEntry("block/generation_condition"), this.mcreator, L10N.t("elementgui.block.event_generate_condition", new Object[0]), VariableTypeLoader.BuiltInTypes.LOGIC, Dependency.fromString("x:number/y:number/z:number/world:world"))).setDefaultName(L10N.t("condition.common.no_additional", new Object[0])).makeInline();
        specialInformation = new StringListProcedureSelector(this.withEntry("block/special_information"), mcreator, L10N.t("elementgui.common.special_information"), AbstractProcedureSelector.Side.CLIENT, new JStringListField(mcreator, null), 0, Dependency.fromString("x:number/y:number/z:number/entity:entity/world:world/itemstack:itemstack"));
        this.blockBase.addActionListener((e) -> {
            this.disableOffset.setEnabled(true);
            this.boundingBoxList.setEnabled(true);
            this.rotationMode.setEnabled(true);
            this.hasGravity.setEnabled(true);
            this.transparencyType.setEnabled(true);
            this.hasTransparency.setEnabled(true);
            this.material.setEnabled(true);
            this.connectedSides.setEnabled(true);
            this.isWaterloggable.setEnabled(true);
            if (this.blockBase.getSelectedItem() != null && this.blockBase.getSelectedItem().equals("Pane")) {
                this.connectedSides.setEnabled(false);
                this.isWaterloggable.setEnabled(false);
                this.rotationMode.setEnabled(false);
                this.disableOffset.setEnabled(false);
                this.boundingBoxList.setEnabled(false);
                this.connectedSides.setSelected(false);
                this.isWaterloggable.setSelected(false);
                this.rotationMode.setSelectedIndex(0);
                if (!this.isEditingMode()) {
                    this.transparencyType.setSelectedItem("CUTOUT_MIPPED");
                    this.lightOpacity.setValue(0);
                }
            } else if (this.blockBase.getSelectedItem() != null && this.blockBase.getSelectedItem().equals("Leaves")) {
                this.material.setEnabled(false);
                this.rotationMode.setEnabled(false);
                this.hasTransparency.setEnabled(false);
                this.transparencyType.setEnabled(false);
                this.isWaterloggable.setSelected(false);
                this.disableOffset.setEnabled(false);
                this.boundingBoxList.setEnabled(false);
                this.material.setSelectedItem("LEAVES");
                this.rotationMode.setSelectedIndex(0);
                this.hasTransparency.setSelected(false);
                this.transparencyType.setSelectedItem("SOLID");
                this.isWaterloggable.setEnabled(false);
                if (!this.isEditingMode()) {
                    this.lightOpacity.setValue(0);
                }
            } else if (this.blockBase.getSelectedItem() != null && this.blockBase.getSelectedIndex() != 0) {
                this.disableOffset.setEnabled(false);
                this.boundingBoxList.setEnabled(false);
                this.hasGravity.setEnabled(false);
                this.rotationMode.setEnabled(false);
                this.isWaterloggable.setEnabled(false);
                this.hasGravity.setSelected(false);
                this.rotationMode.setSelectedIndex(0);
                if (!this.isEditingMode()) {
                    this.lightOpacity.setValue(0);
                    if (this.blockBase.getSelectedItem().equals("Wall") || this.blockBase.getSelectedItem().equals("Fence") || this.blockBase.getSelectedItem().equals("TrapDoor") || this.blockBase.getSelectedItem().equals("Door") || this.blockBase.getSelectedItem().equals("FenceGate") || this.blockBase.getSelectedItem().equals("EndRod") || this.blockBase.getSelectedItem().equals("PressurePlate") || this.blockBase.getSelectedItem().equals("Button")) {
                        this.hasTransparency.setSelected(true);
                    }
                }
            }

            this.updateTextureOptions();
        });
        JPanel pane2 = new JPanel(new BorderLayout(10, 10));
        JPanel pane3 = new JPanel(new BorderLayout(10, 10));
        JPanel pane4 = new JPanel(new BorderLayout(10, 10));
        JPanel pane7 = new JPanel(new BorderLayout(10, 10));
        JPanel pane8 = new JPanel(new BorderLayout(10, 10));
        JPanel pane9 = new JPanel(new BorderLayout(10, 10));
        JPanel bbPane = new JPanel(new BorderLayout(10, 10));
        pane8.setOpaque(false);
        JPanel destal = new JPanel(new GridLayout(3, 4));
        destal.setOpaque(false);
        this.texture = (new TextureHolder(new TypedTextureSelectorDialog(this.mcreator, TextureType.BLOCK))).setFlipUV(true);
        this.textureTop = (new TextureHolder(new TypedTextureSelectorDialog(this.mcreator, TextureType.BLOCK))).setFlipUV(true);
        this.textureLeft = new TextureHolder(new TypedTextureSelectorDialog(this.mcreator, TextureType.BLOCK));
        this.textureFront = new TextureHolder(new TypedTextureSelectorDialog(this.mcreator, TextureType.BLOCK));
        this.textureRight = new TextureHolder(new TypedTextureSelectorDialog(this.mcreator, TextureType.BLOCK));
        this.textureBack = new TextureHolder(new TypedTextureSelectorDialog(this.mcreator, TextureType.BLOCK));
        this.itemTexture = new TextureHolder(new TypedTextureSelectorDialog(this.mcreator, TextureType.ITEM), 32);
        this.particleTexture = new TextureHolder(new TypedTextureSelectorDialog(this.mcreator, TextureType.BLOCK), 32);
        this.itemTexture.setOpaque(false);
        this.particleTexture.setOpaque(false);
        this.texture.setOpaque(false);
        this.textureTop.setOpaque(false);
        this.textureLeft.setOpaque(false);
        this.textureFront.setOpaque(false);
        this.textureRight.setOpaque(false);
        this.textureBack.setOpaque(false);
        this.isReplaceable.setOpaque(false);
        this.canProvidePower.setOpaque(false);
        generateFeature.setOpaque(false);
        destal.add(new JLabel());
        destal.add(ComponentUtils.squareAndBorder(this.textureTop, L10N.t("elementgui.block.texture_place_top", new Object[0])));
        destal.add(new JLabel());
        destal.add(new JLabel());
        destal.add(ComponentUtils.squareAndBorder(this.textureLeft, new Color(126, 196, 255), L10N.t("elementgui.block.texture_place_left_overlay", new Object[0])));
        destal.add(ComponentUtils.squareAndBorder(this.textureFront, L10N.t("elementgui.block.texture_place_front_side", new Object[0])));
        destal.add(ComponentUtils.squareAndBorder(this.textureRight, L10N.t("elementgui.block.texture_place_right", new Object[0])));
        destal.add(ComponentUtils.squareAndBorder(this.textureBack, L10N.t("elementgui.block.texture_place_back", new Object[0])));
        this.textureLeft.setActionListener((event) -> {
            if (!this.texture.hasTexture() && !this.textureTop.hasTexture() && !this.textureBack.hasTexture() && !this.textureFront.hasTexture() && !this.textureRight.hasTexture()) {
                this.texture.setTextureFromTextureName(this.textureLeft.getID());
                this.textureTop.setTextureFromTextureName(this.textureLeft.getID());
                this.textureBack.setTextureFromTextureName(this.textureLeft.getID());
                this.textureFront.setTextureFromTextureName(this.textureLeft.getID());
                this.textureRight.setTextureFromTextureName(this.textureLeft.getID());
            }

        });
        destal.add(new JLabel());
        destal.add(ComponentUtils.squareAndBorder(this.texture, new Color(125, 255, 174), L10N.t("elementgui.block.texture_place_bottom_main", new Object[0])));
        destal.add(new JLabel());
        destal.add(new JLabel());
        JPanel txblock4 = new JPanel(new BorderLayout());
        txblock4.setOpaque(false);
        txblock4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder((Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1), L10N.t("elementgui.block.block_base_item_texture", new Object[0]), 0, 0, this.getFont().deriveFont(12.0F), (Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR")));
        txblock4.add("Center", PanelUtils.gridElements(3, 2, new Component[]{HelpUtils.wrapWithHelpButton(this.withEntry("block/base"), L10N.label("elementgui.block.block_base", new Object[0])), this.blockBase, HelpUtils.wrapWithHelpButton(this.withEntry("block/item_texture"), L10N.label("elementgui.block.item_texture", new Object[0])), PanelUtils.centerInPanel(this.itemTexture), HelpUtils.wrapWithHelpButton(this.withEntry("block/particle_texture"), L10N.label("elementgui.block.particle_texture", new Object[0])), PanelUtils.centerInPanel(this.particleTexture)}));
        JPanel sbbp2 = new JPanel(new BorderLayout(1, 5));
        JPanel sbbp22 = PanelUtils.totalCenterInPanel(destal);
        sbbp2.setOpaque(false);
        this.plantsGrowOn.setOpaque(false);
        sbbp22.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder((Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1), L10N.t("elementgui.block.block_textures", new Object[0]), 0, 0, this.getFont().deriveFont(12.0F), (Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR")));
        JPanel topnbot = new JPanel(new BorderLayout());
        topnbot.setOpaque(false);
        topnbot.add("Center", sbbp22);
        JComponent txblock3 = PanelUtils.gridElements(1, 1, specialInformation);
        txblock3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder((Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1), L10N.t("elementgui.common.special_information", new Object[0]), 0, 0, this.getFont().deriveFont(12.0F), (Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR")));
        sbbp2.add("Center", topnbot);
        JPanel render = new JPanel();
        render.setLayout(new BoxLayout(render, 3));
        ComponentUtils.deriveFont(this.transparencyType, 16.0F);
        ComponentUtils.deriveFont(this.blockBase, 16.0F);
        ComponentUtils.deriveFont(this.tintType, 16.0F);
        JPanel transparencySettings = new JPanel(new GridLayout(4, 2, 0, 2));
        transparencySettings.setOpaque(false);
        transparencySettings.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/has_transparency"), L10N.label("elementgui.block.has_trasparency", new Object[0])));
        transparencySettings.add(this.hasTransparency);
        transparencySettings.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/transparency_type"), L10N.label("elementgui.block.transparency_type", new Object[0])));
        transparencySettings.add(this.transparencyType);
        transparencySettings.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/connected_sides"), L10N.label("elementgui.block.connected_sides", new Object[0])));
        transparencySettings.add(this.connectedSides);
        transparencySettings.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/fluid_overlay"), L10N.label("elementgui.block.fluid_overlay", new Object[0])));
        transparencySettings.add(this.displayFluidOverlay);
        ComponentUtils.deriveFont(this.rotationMode, 16.0F);
        JPanel rent = new JPanel(new GridLayout(6, 2, 0, 2));
        rent.setOpaque(false);
        rent.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/name"), L10N.label("elementgui.animatedblock.model", new Object[0])));
        this.geoModel.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXX");
        this.geoModel.setRenderer(new GeomodelRenderer());
        ComponentUtils.deriveFont(this.geoModel, 16.0F);
        rent.add(this.geoModel);
        rent.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/display_settings"), L10N.label("elementgui.aniblockitems.display_settings", new Object[0])));
        this.displaySettings.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXX");
        this.displaySettings.setRenderer(new GeomodelRenderer());
        ComponentUtils.deriveFont(this.displaySettings, 16.0F);
        rent.add(this.displaySettings);
        rent.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/block_animations"), L10N.label("elementgui.aniblockitems.animation_count", new Object[0])));
        rent.add(animationCount);
        rent.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/rotation_mode"), L10N.label("elementgui.block.rotation_mode", new Object[0])));
        rent.add(this.rotationMode);
        rent.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/enable_pitch"), L10N.label("elementgui.block.enable_pitch", new Object[0])));
        rent.add(this.enablePitch);
        rent.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/is_waterloggable"), L10N.label("elementgui.block.is_waterloggable", new Object[0])));
        rent.add(this.isWaterloggable);
        this.rotationMode.setPreferredSize(new Dimension(320, 42));
        this.enablePitch.setOpaque(false);
        this.enablePitch.setEnabled(false);
        this.rotationMode.addActionListener((e) -> {
            this.enablePitch.setEnabled(this.rotationMode.getSelectedIndex() == 1 || this.rotationMode.getSelectedIndex() == 3);
            if (!this.enablePitch.isEnabled()) {
                this.enablePitch.setSelected(false);
            }

        });
        JPanel tintPanel = new JPanel(new GridLayout(2, 2, 0, 2));
        tintPanel.setOpaque(false);
        this.isItemTinted.setOpaque(false);
        tintPanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/tint_type"), L10N.label("elementgui.common.tint_type", new Object[0])));
        tintPanel.add(this.tintType);
        tintPanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/is_item_tinted"), L10N.label("elementgui.block.is_item_tinted", new Object[0])));
        tintPanel.add(this.isItemTinted);
        topnbot.add("South", PanelUtils.northAndCenterElement(tintPanel, txblock4));
        rent.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder((Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1), L10N.t("elementgui.block.render_type", new Object[0]), 0, 0, this.getFont().deriveFont(12.0F), (Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR")));
        transparencySettings.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder((Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1), L10N.t("elementgui.block.transparency", new Object[0]), 0, 0, this.getFont().deriveFont(12.0F), (Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR")));
        tintPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder((Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1), L10N.t("elementgui.block.block_tint", new Object[0]), 0, 0, this.getFont().deriveFont(12.0F), (Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR")));
        render.add(rent);
        render.add(transparencySettings);
        render.add(txblock3);
        render.setOpaque(false);
        this.hasTransparency.setOpaque(false);
        this.connectedSides.setOpaque(false);
        this.emissiveRendering.setOpaque(false);
        this.displayFluidOverlay.setOpaque(false);
        sbbp2.add("East", PanelUtils.pullElementUp(render));
        pane2.setOpaque(false);
        pane2.add("Center", PanelUtils.totalCenterInPanel(sbbp2));
        JPanel northPanel = new JPanel(new GridLayout(1, 2, 10, 2));
        northPanel.setOpaque(false);
        northPanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/disable_offset"), L10N.label("elementgui.common.disable_offset", new Object[0])));
        northPanel.add(this.disableOffset);
        this.disableOffset.setOpaque(false);
        bbPane.add(PanelUtils.northAndCenterElement(PanelUtils.join(0, new Component[]{northPanel}), this.boundingBoxList));
        bbPane.setOpaque(false);
        bbPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        if (!this.isEditingMode()) {
            this.boundingBoxList.setEntries(Collections.singletonList(new IBlockWithBoundingBox.BoxEntry()));
            this.animationCount.setValue(1);
        }

        this.boundingBoxList.addPropertyChangeListener("boundingBoxChanged", (e) -> {
            this.updateParametersBasedOnBoundingBoxSize();
        });
        JPanel selp = new JPanel(new GridLayout(14, 2, 0, 2));
        JPanel selp3 = new JPanel(new GridLayout(8, 2, 0, 2));
        JPanel soundProperties = new JPanel(new GridLayout(7, 2, 0, 2));
        JPanel advancedProperties = new JPanel(new GridLayout(12, 2, 0, 2));
        this.hasGravity.setOpaque(false);
        this.tickRandomly.setOpaque(false);
        this.unbreakable.setOpaque(false);
        this.useLootTableForDrops.setOpaque(false);
        this.requiresCorrectTool.setOpaque(false);
        this.destroyTool.addActionListener((e) -> {
            this.updateRequiresCorrectTool();
        });
        selp3.setOpaque(false);
        advancedProperties.setOpaque(false);
        ComponentUtils.deriveFont(this.name, 16.0F);
        this.hardness.setOpaque(false);
        this.resistance.setOpaque(false);
        this.lightOpacity.setOpaque(false);
        this.isNotColidable.setOpaque(false);
        selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("common/gui_name"), L10N.label("elementgui.common.name_in_gui", new Object[0])));
        selp.add(this.name);
        selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/material"), L10N.label("elementgui.block.material", new Object[0])));
        selp.add(this.material);
        selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("common/creative_tab"), L10N.label("elementgui.common.creative_tab", new Object[0])));
        selp.add(this.creativeTab);
        selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/hardness"), L10N.label("elementgui.common.hardness", new Object[0])));
        selp.add(this.hardness);
        selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/resistance"), L10N.label("elementgui.common.resistance", new Object[0])));
        selp.add(this.resistance);
        selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/slipperiness"), L10N.label("elementgui.block.slipperiness", new Object[0])));
        selp.add(this.slipperiness);
        selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/jump_factor"), L10N.label("elementgui.block.jump_factor", new Object[0])));
        selp.add(this.jumpFactor);
        selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/speed_factor"), L10N.label("elementgui.block.speed_factor", new Object[0])));
        selp.add(this.speedFactor);
        selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/luminance"), L10N.label("elementgui.common.luminance", new Object[0])));
        selp.add(this.luminance);
        selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/light_opacity"), L10N.label("elementgui.common.light_opacity", new Object[0])));
        selp.add(this.lightOpacity);
        selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/has_gravity"), L10N.label("elementgui.block.has_gravity", new Object[0])));
        selp.add(this.hasGravity);
        selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/can_walk_through"), L10N.label("elementgui.block.can_walk_through", new Object[0])));
        selp.add(this.isNotColidable);
        selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/emissive_rendering"), L10N.label("elementgui.common.emissive_rendering", new Object[0])));
        selp.add(this.emissiveRendering);
        selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/replaceable"), L10N.label("elementgui.block.is_replaceable", new Object[0])));
        selp.add(this.isReplaceable);
        this.creativeTab.setPrototypeDisplayValue(new DataListEntry.Dummy("BUILDING_BLOCKS"));
        this.creativeTab.addPopupMenuListener(new ComboBoxFullWidthPopup());
        selp3.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/custom_drop"), L10N.label("elementgui.common.custom_drop", new Object[0])));
        selp3.add(PanelUtils.centerInPanel(this.customDrop));
        selp3.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/drop_amount"), L10N.label("elementgui.common.drop_amount", new Object[0])));
        selp3.add(this.dropAmount);
        selp3.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/use_loot_table_for_drops"), L10N.label("elementgui.common.use_loot_table_for_drop", new Object[0])));
        selp3.add(PanelUtils.centerInPanel(this.useLootTableForDrops));
        selp3.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/creative_pick_item"), L10N.label("elementgui.common.creative_pick_item", new Object[0])));
        selp3.add(PanelUtils.centerInPanel(this.creativePickItem));
        selp3.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/harvest_tool"), L10N.label("elementgui.block.harvest_tool", new Object[0])));
        selp3.add(this.destroyTool);
        selp3.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/harvest_level"), L10N.label("elementgui.block.harvest_level", new Object[0])));
        selp3.add(this.breakHarvestLevel);
        selp3.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/requires_correct_tool"), L10N.label("elementgui.block.requires_correct_tool", new Object[0])));
        selp3.add(this.requiresCorrectTool);
        selp3.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/unbreakable"), L10N.label("elementgui.block.is_unbreakable", new Object[0])));
        selp3.add(this.unbreakable);
        ButtonGroup bg = new ButtonGroup();
        bg.add(this.defaultSoundType);
        bg.add(this.customSoundType);
        this.defaultSoundType.setSelected(true);
        this.defaultSoundType.setOpaque(false);
        this.customSoundType.setOpaque(false);
        this.defaultSoundType.addActionListener((event) -> {
            this.updateSoundType();
        });
        this.customSoundType.addActionListener((event) -> {
            this.updateSoundType();
        });
        soundProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/block_sound"), this.defaultSoundType));
        soundProperties.add(this.soundOnStep);
        soundProperties.add(PanelUtils.join(0, new Component[]{this.customSoundType}));
        soundProperties.add(new JEmptyBox());
        soundProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/break_sound"), L10N.label("elementgui.common.soundtypes.break_sound", new Object[0])));
        soundProperties.add(this.breakSound);
        soundProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/fall_sound"), L10N.label("elementgui.common.soundtypes.fall_sound", new Object[0])));
        soundProperties.add(this.fallSound);
        soundProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/hit_sound"), L10N.label("elementgui.common.soundtypes.hit_sound", new Object[0])));
        soundProperties.add(this.hitSound);
        soundProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/place_sound"), L10N.label("elementgui.common.soundtypes.place_sound", new Object[0])));
        soundProperties.add(this.placeSound);
        soundProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/step_sound"), L10N.label("elementgui.common.soundtypes.step_sound", new Object[0])));
        soundProperties.add(this.stepSound);
        advancedProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/tick_rate"), L10N.label("elementgui.common.tick_rate", new Object[0])));
        advancedProperties.add(this.tickRate);
        advancedProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/tick_randomly"), L10N.label("elementgui.block.tick_randomly", new Object[0])));
        advancedProperties.add(this.tickRandomly);
        this.tickRandomly.addActionListener((e) -> {
            this.tickRate.setEnabled(!this.tickRandomly.isSelected());
        });
        advancedProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/color_on_map"), L10N.label("elementgui.block.color_on_map", new Object[0])));
        advancedProperties.add(this.colorOnMap);
        advancedProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/can_plants_grow"), L10N.label("elementgui.block.can_plants_grow", new Object[0])));
        advancedProperties.add(this.plantsGrowOn);
        advancedProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/beacon_color_modifier"), L10N.label("elementgui.block.beacon_color_modifier", new Object[0])));
        advancedProperties.add(this.beaconColorModifier);
        advancedProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/is_ladder"), L10N.label("elementgui.block.is_ladder", new Object[0])));
        advancedProperties.add(this.isLadder);
        advancedProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/enchantments_bonus"), L10N.label("elementgui.block.enchantments_bonus", new Object[0])));
        advancedProperties.add(this.enchantPowerBonus);
        advancedProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/flammability"), L10N.label("elementgui.block.flammability", new Object[0])));
        advancedProperties.add(this.flammability);
        advancedProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/fire_spread_speed"), L10N.label("elementgui.common.fire_spread_speed", new Object[0])));
        advancedProperties.add(this.fireSpreadSpeed);
        advancedProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/push_reaction"), L10N.label("elementgui.block.push_reaction", new Object[0])));
        advancedProperties.add(this.reactionToPushing);
        advancedProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/ai_path_node_type"), L10N.label("elementgui.common.ai_path_node_type", new Object[0])));
        advancedProperties.add(this.aiPathNodeType);
        advancedProperties.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/offset_type"), L10N.label("elementgui.common.offset_type", new Object[0])));
        advancedProperties.add(this.offsetType);
        JComponent advancedWithCondition = PanelUtils.northAndCenterElement(advancedProperties, this.placingCondition, 5, 5);
        this.isWaterloggable.setOpaque(false);
        this.canRedstoneConnect.setOpaque(false);
        this.isLadder.setOpaque(false);
        this.useLootTableForDrops.addActionListener((e) -> {
            this.customDrop.setEnabled(!this.useLootTableForDrops.isSelected());
            this.dropAmount.setEnabled(!this.useLootTableForDrops.isSelected());
        });
        this.isWaterloggable.addActionListener((e) -> {
            this.hasGravity.setEnabled(!this.isWaterloggable.isSelected());
            if (this.isWaterloggable.isSelected()) {
                this.hasGravity.setSelected(false);
            }

        });
        selp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder((Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1), L10N.t("elementgui.common.properties_general", new Object[0]), 4, 0, this.getFont(), (Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR")));
        selp3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder((Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1), L10N.t("elementgui.common.properties_dropping", new Object[0]), 4, 0, this.getFont(), (Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR")));
        soundProperties.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder((Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1), L10N.t("elementgui.common.properties_sound", new Object[0]), 4, 0, this.getFont(), (Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR")));
        advancedWithCondition.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder((Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1), L10N.t("elementgui.block.properties_advanced_block", new Object[0]), 4, 0, this.getFont(), (Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR")));
        selp.setOpaque(false);
        soundProperties.setOpaque(false);
        pane3.add("Center", PanelUtils.totalCenterInPanel(PanelUtils.westAndEastElement(selp, PanelUtils.centerAndSouthElement(selp3, soundProperties))));
        pane3.setOpaque(false);
        JPanel events2 = new JPanel(new GridLayout(4, 5, 5, 5));
        events2.setOpaque(false);
        events2.add(this.onRightClicked);
        events2.add(this.onBlockAdded);
        events2.add(this.onNeighbourBlockChanges);
        events2.add(this.onTickUpdate);
        events2.add(this.onDestroyedByPlayer);
        events2.add(this.onDestroyedByExplosion);
        events2.add(this.onStartToDestroy);
        events2.add(this.onEntityCollides);
        events2.add(this.onEntityWalksOn);
        events2.add(this.onHitByProjectile);
        events2.add(this.onBlockPlayedBy);
        events2.add(this.onRedstoneOn);
        events2.add(this.onRedstoneOff);
        events2.add(this.onRandomUpdateEvent);
        pane4.add("Center", PanelUtils.totalCenterInPanel(events2));
        pane4.setOpaque(false);
        JPanel invblock = new JPanel(new BorderLayout(10, 40));
        invblock.setOpaque(false);
        this.hasInventory.setOpaque(false);
        this.openGUIOnRightClick.setOpaque(false);
        this.inventorySize.setOpaque(false);
        this.inventoryStackSize.setOpaque(false);
        this.inventoryDropWhenDestroyed.setOpaque(false);
        this.inventoryComparatorPower.setOpaque(false);
        this.inventoryDropWhenDestroyed.setSelected(true);
        this.inventoryComparatorPower.setSelected(true);
        JPanel props = new JPanel(new GridLayout(8, 2, 25, 2));
        props.setOpaque(false);
        props.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/bind_gui"), L10N.label("elementgui.block.bind_gui", new Object[0])));
        props.add(this.guiBoundTo);
        props.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/bind_gui_open"), L10N.label("elementgui.block.bind_gui_open", new Object[0])));
        props.add(this.openGUIOnRightClick);
        props.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/inventory_size"), L10N.label("elementgui.block.inventory_size", new Object[0])));
        props.add(this.inventorySize);
        props.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/inventory_stack_size"), L10N.label("elementgui.common.max_stack_size", new Object[0])));
        props.add(this.inventoryStackSize);
        props.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/drop_inventory_items"), L10N.label("elementgui.block.drop_inventory_items", new Object[0])));
        props.add(this.inventoryDropWhenDestroyed);
        props.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/comparator_data"), L10N.label("elementgui.block.comparator_data", new Object[0])));
        props.add(this.inventoryComparatorPower);
        props.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/input_slots"), L10N.label("elementgui.block.input_slots", new Object[0])));
        props.add(this.inSlotIDs);
        props.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/output_slots"), L10N.label("elementgui.block.output_slots", new Object[0])));
        props.add(this.outSlotIDs);
        ComponentUtils.deriveFont(this.outSlotIDs, 16.0F);
        this.outSlotIDs.setValidator(new CommaSeparatedNumbersValidator(this.outSlotIDs));
        this.outSlotIDs.enableRealtimeValidation();
        ComponentUtils.deriveFont(this.inSlotIDs, 16.0F);
        this.inSlotIDs.setValidator(new CommaSeparatedNumbersValidator(this.inSlotIDs));
        this.inSlotIDs.enableRealtimeValidation();
        this.guiBoundTo.addActionListener((e) -> {
            if (!this.isEditingMode()) {
                String selected = (String)this.guiBoundTo.getSelectedItem();
                if (selected != null) {
                    ModElement element = this.mcreator.getWorkspace().getModElementByName(selected);
                    if (element != null) {
                        GeneratableElement generatableElement = element.getGeneratableElement();
                        if (generatableElement instanceof GUI) {
                            GUI gui = (GUI)generatableElement;
                            this.inventorySize.setValue(gui.getMaxSlotID() + 1);
                            StringBuilder inslots = new StringBuilder();
                            StringBuilder outslots = new StringBuilder();
                            Iterator var8 = gui.components.iterator();

                            while(var8.hasNext()) {
                                GUIComponent slot = (GUIComponent)var8.next();
                                if (slot instanceof InputSlot) {
                                    inslots.append(((Slot)slot).id).append(",");
                                } else if (slot instanceof OutputSlot) {
                                    outslots.append(((Slot)slot).id).append(",");
                                }
                            }

                            this.inSlotIDs.setText(inslots.toString().replaceAll(",$", ""));
                            this.outSlotIDs.setText(outslots.toString().replaceAll(",$", ""));
                        }
                    }
                }
            }

        });
        JPanel pane10 = new JPanel(new BorderLayout(10, 10));
        pane10.setOpaque(false);
        JPanel energyStorage = new JPanel(new GridLayout(5, 2, 10, 2));
        JPanel fluidTank = new JPanel(new GridLayout(3, 2, 10, 2));
        energyStorage.setOpaque(false);
        fluidTank.setOpaque(false);
        energyStorage.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder((Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1), L10N.t("elementgui.block.energy_storage", new Object[0]), 0, 0, this.getFont().deriveFont(12.0F), (Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR")));
        fluidTank.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder((Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1), L10N.t("elementgui.block.fluid_tank", new Object[0]), 0, 0, this.getFont().deriveFont(12.0F), (Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR")));
        this.hasEnergyStorage.setOpaque(false);
        this.isFluidTank.setOpaque(false);
        energyStorage.add(HelpUtils.wrapWithHelpButton(this.withEntry("energy/enable_storage"), L10N.label("elementgui.block.enable_storage_energy", new Object[0])));
        energyStorage.add(this.hasEnergyStorage);
        energyStorage.add(HelpUtils.wrapWithHelpButton(this.withEntry("energy/initial_energy"), L10N.label("elementgui.block.initial_energy", new Object[0])));
        energyStorage.add(this.energyInitial);
        energyStorage.add(HelpUtils.wrapWithHelpButton(this.withEntry("energy/energy_capacity"), L10N.label("elementgui.block.energy_max_capacity", new Object[0])));
        energyStorage.add(this.energyCapacity);
        energyStorage.add(HelpUtils.wrapWithHelpButton(this.withEntry("energy/max_receive"), L10N.label("elementgui.block.energy_max_receive", new Object[0])));
        energyStorage.add(this.energyMaxReceive);
        energyStorage.add(HelpUtils.wrapWithHelpButton(this.withEntry("energy/max_extract"), L10N.label("elementgui.block.energy_max_extract", new Object[0])));
        energyStorage.add(this.energyMaxExtract);
        fluidTank.add(HelpUtils.wrapWithHelpButton(this.withEntry("fluidtank/enable_storage"), L10N.label("elementgui.block.enable_storage_fluid", new Object[0])));
        fluidTank.add(this.isFluidTank);
        fluidTank.add(HelpUtils.wrapWithHelpButton(this.withEntry("fluidtank/fluid_capacity"), L10N.label("elementgui.block.fluid_capacity", new Object[0])));
        fluidTank.add(this.fluidCapacity);
        fluidTank.add(HelpUtils.wrapWithHelpButton(this.withEntry("fluidtank/fluid_restrictions"), L10N.label("elementgui.block.fluid_restrictions", new Object[0])));
        fluidTank.add(this.fluidRestrictions);
        pane10.add(PanelUtils.totalCenterInPanel(PanelUtils.northAndCenterElement(L10N.label("elementgui.block.tile_entity_tip", new Object[0]), PanelUtils.westAndEastElement(energyStorage, PanelUtils.northAndCenterElement(fluidTank, new JEmptyBox())), 10, 10)));
        this.hasInventory.addActionListener((e) -> {
            this.refreshFieldsTileEntity();
        });
        this.refreshFieldsTileEntity();
        props.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder((Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1), L10N.t("elementgui.block.settings_inventory", new Object[0]), 0, 0, this.getFont().deriveFont(12.0F), (Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR")));
        invblock.add("Center", props);
        invblock.add("North", HelpUtils.wrapWithHelpButton(this.withEntry("block/has_inventory"), this.hasInventory));
        pane8.add("Center", PanelUtils.totalCenterInPanel(invblock));
        JPanel enderpanel2 = new JPanel(new BorderLayout(30, 15));
        JPanel genPanel = new JPanel(new GridLayout(8, 2, 20, 2));
        genPanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("common/generate_feature"), L10N.label("elementgui.block.generate_feature")));
        genPanel.add(generateFeature);
        genPanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/gen_replace_blocks"), L10N.label("elementgui.block.gen_replace_blocks", new Object[0])));
        genPanel.add(this.blocksToReplace);
        genPanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("common/restrict_to_biomes"), L10N.label("elementgui.common.restrict_to_biomes", new Object[0])));
        genPanel.add(this.restrictionBiomes);
        genPanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/generation_shape"), L10N.label("elementgui.block.generation_shape", new Object[0])));
        genPanel.add(this.generationShape);
        genPanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/gen_chunk_count"), L10N.label("elementgui.block.gen_chunck_count", new Object[0])));
        genPanel.add(this.frequencyPerChunks);
        genPanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/gen_group_size"), L10N.label("elementgui.block.gen_group_size", new Object[0])));
        genPanel.add(this.frequencyOnChunk);
        genPanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/gen_min_height"), L10N.label("elementgui.block.gen_min_height", new Object[0])));
        genPanel.add(this.minGenerateHeight);
        genPanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/gen_max_height"), L10N.label("elementgui.block.gen_max_height", new Object[0])));
        genPanel.add(this.maxGenerateHeight);
        genPanel.setOpaque(false);
        enderpanel2.add("West", PanelUtils.totalCenterInPanel(new JLabel(UIRES.get("chunk"))));
        enderpanel2.add("Center", PanelUtils.pullElementUp(PanelUtils.northAndCenterElement(genPanel, PanelUtils.westAndCenterElement(new JEmptyBox(5, 5), this.generateCondition), 5, 5)));
        enderpanel2.setOpaque(false);
        JPanel particleParameters = new JPanel(new GridLayout(5, 2, 0, 2));
        particleParameters.add(HelpUtils.wrapWithHelpButton(this.withEntry("particle/gen_particles"), this.spawnParticles));
        particleParameters.add(new JLabel());
        particleParameters.add(HelpUtils.wrapWithHelpButton(this.withEntry("particle/gen_type"), L10N.label("elementgui.block.particle_gen_type", new Object[0])));
        particleParameters.add(this.particleToSpawn);
        particleParameters.add(HelpUtils.wrapWithHelpButton(this.withEntry("particle/gen_shape"), L10N.label("elementgui.block.particle_gen_shape", new Object[0])));
        particleParameters.add(this.particleSpawningShape);
        particleParameters.add(HelpUtils.wrapWithHelpButton(this.withEntry("particle/gen_spawn_radius"), L10N.label("elementgui.block.particle_gen_spawn_radius", new Object[0])));
        particleParameters.add(this.particleSpawningRadious);
        particleParameters.add(HelpUtils.wrapWithHelpButton(this.withEntry("particle/gen_average_amount"), L10N.label("elementgui.block.particle_gen_average_amount", new Object[0])));
        particleParameters.add(this.particleAmount);
        JComponent parpar = PanelUtils.northAndCenterElement(particleParameters, this.particleCondition, 5, 5);
        parpar.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder((Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1), L10N.t("elementgui.block.properties_particle", new Object[0]), 0, 0, this.getFont().deriveFont(12.0F), (Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR")));
        particleParameters.setOpaque(false);
        JPanel redstoneParameters = new JPanel(new GridLayout(3, 2, 0, 2));
        redstoneParameters.setOpaque(false);
        redstoneParameters.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/redstone_connect"), L10N.label("elementgui.block.redstone_connect", new Object[0])));
        redstoneParameters.add(this.canRedstoneConnect);
        redstoneParameters.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/emits_redstone"), L10N.label("elementgui.block.emits_redstone", new Object[0])));
        redstoneParameters.add(this.canProvidePower);
        redstoneParameters.add(HelpUtils.wrapWithHelpButton(this.withEntry("block/redstone_power"), L10N.label("elementgui.block.redstone_power", new Object[0])));
        redstoneParameters.add(this.emittedRedstonePower);
        redstoneParameters.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder((Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1), L10N.t("elementgui.block.properties_redstone", new Object[0]), 0, 0, this.getFont().deriveFont(12.0F), (Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR")));
        JComponent parred = PanelUtils.centerAndSouthElement(parpar, PanelUtils.pullElementUp(redstoneParameters));
        this.canProvidePower.addActionListener((e) -> {
            this.refreshRedstoneEmitted();
        });
        this.refreshRedstoneEmitted();
        this.particleSpawningRadious.setOpaque(false);
        this.spawnParticles.setOpaque(false);

        pane7.add(PanelUtils.totalCenterInPanel(PanelUtils.westAndEastElement(advancedWithCondition, PanelUtils.pullElementUp(parred))));
        pane7.setOpaque(false);
        pane9.setOpaque(false);
        pane9.add("Center", PanelUtils.totalCenterInPanel(PanelUtils.centerInPanel(enderpanel2)));
        this.texture.setValidator(new TileHolderValidator(this.texture));
        this.page1group.addValidationElement(this.texture);
        this.page1group.addValidationElement(this.geoModel);
        this.page1group.addValidationElement(this.displaySettings);
        this.name.setValidator(new TextFieldValidator(this.name, L10N.t("elementgui.block.error_block_must_have_name", new Object[0])));
        this.name.enableRealtimeValidation();
        this.page3group.addValidationElement(this.name);
        this.breakSound.getVTextField().setValidator(new ConditionalTextFieldValidator(this.breakSound.getVTextField(), L10N.t("elementgui.common.error_sound_empty_null", new Object[0]), this.customSoundType, true));
        this.fallSound.getVTextField().setValidator(new ConditionalTextFieldValidator(this.fallSound.getVTextField(), L10N.t("elementgui.common.error_sound_empty_null", new Object[0]), this.customSoundType, true));
        this.hitSound.getVTextField().setValidator(new ConditionalTextFieldValidator(this.hitSound.getVTextField(), L10N.t("elementgui.common.error_sound_empty_null", new Object[0]), this.customSoundType, true));
        this.placeSound.getVTextField().setValidator(new ConditionalTextFieldValidator(this.placeSound.getVTextField(), L10N.t("elementgui.common.error_sound_empty_null", new Object[0]), this.customSoundType, true));
        this.stepSound.getVTextField().setValidator(new ConditionalTextFieldValidator(this.stepSound.getVTextField(), L10N.t("elementgui.common.error_sound_empty_null", new Object[0]), this.customSoundType, true));
        this.page3group.addValidationElement(this.breakSound.getVTextField());
        this.page3group.addValidationElement(this.fallSound.getVTextField());
        this.page3group.addValidationElement(this.hitSound.getVTextField());
        this.page3group.addValidationElement(this.placeSound.getVTextField());
        this.page3group.addValidationElement(this.stepSound.getVTextField());
        this.addPage(L10N.t("elementgui.common.page_visual", new Object[0]), pane2);
        this.addPage(L10N.t("elementgui.common.page_bounding_boxes", new Object[0]), bbPane);
        this.addPage(L10N.t("elementgui.common.page_properties", new Object[0]), pane3);
        this.addPage(L10N.t("elementgui.common.page_advanced_properties", new Object[0]), pane7);
        this.addPage(L10N.t("elementgui.block.page_tile_entity", new Object[0]), pane8);
        this.addPage(L10N.t("elementgui.block.page_energy_fluid_storage", new Object[0]), pane10);
        this.addPage(L10N.t("elementgui.common.page_triggers", new Object[0]), pane4);
        this.addPage(L10N.t("elementgui.common.page_generation", new Object[0]), pane9);
        if (!this.isEditingMode()) {
            String readableNameFromModElement = StringUtils.machineToReadableName(this.modElement.getName());
            this.name.setText(readableNameFromModElement);
        }

        this.updateSoundType();

        geoModel.setValidator(() -> {
            if (geoModel.getSelectedItem() == null || geoModel.getSelectedItem().equals(""))
                return new Validator.ValidationResult(Validator.ValidationResultType.ERROR,
                        L10N.t("elementgui.animatedentity.modelname"));
            return Validator.ValidationResult.PASSED;
        });

        displaySettings.setValidator(() -> {
            if (displaySettings.getSelectedItem() == null || displaySettings.getSelectedItem().equals(""))
                return new Validator.ValidationResult(Validator.ValidationResultType.ERROR,
                        L10N.t("elementgui.aniblockitems.display_settings_missing"));
            return Validator.ValidationResult.PASSED;
        });

        this.textureTop.setVisible(false);
        this.textureLeft.setVisible(false);
        this.textureFront.setVisible(false);
        this.textureRight.setVisible(false);
        this.textureBack.setVisible(false);

        this.lightOpacity.setValue(0);
    }

    private void refreshFieldsTileEntity() {
        this.inventorySize.setEnabled(this.hasInventory.isSelected());
        this.inventoryStackSize.setEnabled(this.hasInventory.isSelected());
        this.inventoryDropWhenDestroyed.setEnabled(this.hasInventory.isSelected());
        this.inventoryComparatorPower.setEnabled(this.hasInventory.isSelected());
        this.outSlotIDs.setEnabled(this.hasInventory.isSelected());
        this.inSlotIDs.setEnabled(this.hasInventory.isSelected());
        this.hasEnergyStorage.setEnabled(this.hasInventory.isSelected());
        this.energyInitial.setEnabled(this.hasInventory.isSelected());
        this.energyCapacity.setEnabled(this.hasInventory.isSelected());
        this.energyMaxReceive.setEnabled(this.hasInventory.isSelected());
        this.energyMaxExtract.setEnabled(this.hasInventory.isSelected());
        this.isFluidTank.setEnabled(this.hasInventory.isSelected());
        this.fluidCapacity.setEnabled(this.hasInventory.isSelected());
        this.fluidRestrictions.setEnabled(this.hasInventory.isSelected());
    }

    private void refreshRedstoneEmitted() {
        this.emittedRedstonePower.setEnabled(this.canProvidePower.isSelected());
    }

    private void updateTextureOptions() {
        texture.setFlipUV(false);
        textureTop.setFlipUV(false);
        textureTop.setVisible(false);
        textureLeft.setVisible(false);
        textureFront.setVisible(false);
        textureRight.setVisible(false);
        textureBack.setVisible(false);

    }

    public void updateParametersBasedOnBoundingBoxSize() {
        if (!this.boundingBoxList.isFullCube()) {
            this.hasTransparency.setSelected(true);
            this.hasTransparency.setEnabled(false);
        } else {
            this.hasTransparency.setSelected(false);
            this.hasTransparency.setEnabled(true);
        }

    }

    private void updateSoundType() {
        this.breakSound.setEnabled(this.customSoundType.isSelected());
        this.fallSound.setEnabled(this.customSoundType.isSelected());
        this.hitSound.setEnabled(this.customSoundType.isSelected());
        this.placeSound.setEnabled(this.customSoundType.isSelected());
        this.stepSound.setEnabled(this.customSoundType.isSelected());
        this.soundOnStep.setEnabled(this.defaultSoundType.isSelected());
    }

    private void updateRequiresCorrectTool() {
        if (!this.isEditingMode() && "pickaxe".equals(this.destroyTool.getSelectedItem())) {
            this.requiresCorrectTool.setSelected(true);
        }

    }

    public void reloadDataLists() {
        super.reloadDataLists();
        this.onBlockAdded.refreshListKeepSelected();
        this.onNeighbourBlockChanges.refreshListKeepSelected();
        this.specialInformation.refreshListKeepSelected();
        this.onEntityCollides.refreshListKeepSelected();
        this.onTickUpdate.refreshListKeepSelected();
        this.onRandomUpdateEvent.refreshListKeepSelected();
        this.onDestroyedByPlayer.refreshListKeepSelected();
        this.onDestroyedByExplosion.refreshListKeepSelected();
        this.onStartToDestroy.refreshListKeepSelected();
        this.onEntityWalksOn.refreshListKeepSelected();
        this.onBlockPlayedBy.refreshListKeepSelected();
        this.onRightClicked.refreshListKeepSelected();
        this.onRedstoneOn.refreshListKeepSelected();
        this.onRedstoneOff.refreshListKeepSelected();
        this.onHitByProjectile.refreshListKeepSelected();
        this.particleCondition.refreshListKeepSelected();
        this.emittedRedstonePower.refreshListKeepSelected();
        this.placingCondition.refreshListKeepSelected();
        this.generateCondition.refreshListKeepSelected();
        ComboBoxUtil.updateComboBoxContents(this.guiBoundTo, ListUtils.merge(Collections.singleton("<NONE>"), (Collection)this.mcreator.getWorkspace().getModElements().stream().filter((var) -> {
            return var.getType() == ModElementType.GUI;
        }).map(ModElement::getName).collect(Collectors.toList())), "<NONE>");
        ComboBoxUtil.updateComboBoxContents(this.creativeTab, ElementUtil.loadAllTabs(this.mcreator.getWorkspace()));
        ComboBoxUtil.updateComboBoxContents(this.colorOnMap, Arrays.asList(ElementUtil.getDataListAsStringArray("mapcolors")), "DEFAULT");
        ComboBoxUtil.updateComboBoxContents(this.aiPathNodeType, Arrays.asList(ElementUtil.getDataListAsStringArray("pathnodetypes")), "DEFAULT");
        ComboBoxUtil.updateComboBoxContents(this.particleToSpawn, ElementUtil.loadAllParticles(this.mcreator.getWorkspace()));

        ComboBoxUtil.updateComboBoxContents(this.geoModel, ListUtils.merge(Collections.singleton(""), (Collection) PluginModelActions.getGeomodels(this.mcreator).stream().map(File::getName).filter((s) -> {
            return s.endsWith(".geo.json");
        }).collect(Collectors.toList())), "");

        ComboBoxUtil.updateComboBoxContents(this.displaySettings, ListUtils.merge(Collections.singleton(""), (Collection) PluginModelActions.getDisplaysettings(this.mcreator).stream().map(File::getName).filter((s) -> {
            return s.endsWith(".json");
        }).collect(Collectors.toList())), "");
    }

    protected AggregatedValidationResult validatePage(int page) {
        if (page == 0) {
            return new AggregatedValidationResult(new ValidationGroup[]{this.page1group});
        } else if (page == 2) {
            return new AggregatedValidationResult(new ValidationGroup[]{this.page3group});
        } else if (page == 4) {
            return new AggregatedValidationResult(new IValidable[]{this.outSlotIDs, this.inSlotIDs});
        } else {
            return (AggregatedValidationResult)(page == 7 && (Integer)this.minGenerateHeight.getValue() >= (Integer)this.maxGenerateHeight.getValue() ? new AggregatedValidationResult.FAIL(L10N.t("elementgui.block.error_minimal_generation_height", new Object[0])) : new AggregatedValidationResult.PASS());
        }
    }

    public void openInEditingMode(AnimatedBlock block) {
        this.itemTexture.setTextureFromTextureName(block.itemTexture);
        this.particleTexture.setTextureFromTextureName(block.particleTexture);
        this.texture.setTextureFromTextureName(block.texture);
        this.textureTop.setTextureFromTextureName(block.textureTop);
        this.textureLeft.setTextureFromTextureName(block.textureLeft);
        this.textureFront.setTextureFromTextureName(block.textureFront);
        this.textureRight.setTextureFromTextureName(block.textureRight);
        this.textureBack.setTextureFromTextureName(block.textureBack);
        this.guiBoundTo.setSelectedItem(block.guiBoundTo);
        this.rotationMode.setSelectedIndex(block.rotationMode);
        this.enablePitch.setSelected(block.enablePitch);
        this.enchantPowerBonus.setValue(block.enchantPowerBonus);
        this.hasTransparency.setSelected(block.hasTransparency);
        this.connectedSides.setSelected(block.connectedSides);
        this.displayFluidOverlay.setSelected(block.displayFluidOverlay);
        this.hasEnergyStorage.setSelected(block.hasEnergyStorage);
        this.isFluidTank.setSelected(block.isFluidTank);
        this.energyInitial.setValue(block.energyInitial);
        this.energyCapacity.setValue(block.energyCapacity);
        this.energyMaxReceive.setValue(block.energyMaxReceive);
        this.energyMaxExtract.setValue(block.energyMaxExtract);
        this.fluidCapacity.setValue(block.fluidCapacity);
        this.outSlotIDs.setText((String)block.inventoryOutSlotIDs.stream().map(String::valueOf).collect(Collectors.joining(",")));
        this.inSlotIDs.setText((String)block.inventoryInSlotIDs.stream().map(String::valueOf).collect(Collectors.joining(",")));
        this.onBlockAdded.setSelectedProcedure(block.onBlockAdded);
        this.onNeighbourBlockChanges.setSelectedProcedure(block.onNeighbourBlockChanges);
        this.onTickUpdate.setSelectedProcedure(block.onTickUpdate);
        this.onRandomUpdateEvent.setSelectedProcedure(block.onRandomUpdateEvent);
        this.onDestroyedByPlayer.setSelectedProcedure(block.onDestroyedByPlayer);
        this.onDestroyedByExplosion.setSelectedProcedure(block.onDestroyedByExplosion);
        this.onStartToDestroy.setSelectedProcedure(block.onStartToDestroy);
        this.onEntityCollides.setSelectedProcedure(block.onEntityCollides);
        this.onEntityWalksOn.setSelectedProcedure(block.onEntityWalksOn);
        this.onBlockPlayedBy.setSelectedProcedure(block.onBlockPlayedBy);
        this.onRightClicked.setSelectedProcedure(block.onRightClicked);
        this.onRedstoneOn.setSelectedProcedure(block.onRedstoneOn);
        this.onRedstoneOff.setSelectedProcedure(block.onRedstoneOff);
        this.onHitByProjectile.setSelectedProcedure(block.onHitByProjectile);
        this.name.setText(block.name);
        this.generationShape.setSelectedItem(block.generationShape);
        this.maxGenerateHeight.setValue(block.maxGenerateHeight);
        this.minGenerateHeight.setValue(block.minGenerateHeight);
        this.frequencyPerChunks.setValue(block.frequencyPerChunks);
        this.frequencyOnChunk.setValue(block.frequencyOnChunk);
        this.spawnParticles.setSelected(block.spawnParticles);
        this.particleToSpawn.setSelectedItem(block.particleToSpawn);
        this.particleSpawningShape.setSelectedItem(block.particleSpawningShape);
        this.particleCondition.setSelectedProcedure(block.particleCondition);
        this.emittedRedstonePower.setSelectedProcedure(block.emittedRedstonePower);
        this.generateCondition.setSelectedProcedure(block.generateCondition);
        this.particleSpawningRadious.setValue(block.particleSpawningRadious);
        this.particleAmount.setValue(block.particleAmount);
        this.hardness.setValue(block.hardness);
        this.resistance.setValue(block.resistance);
        this.hasGravity.setSelected(block.hasGravity);
        this.isWaterloggable.setSelected(block.isWaterloggable);
        this.emissiveRendering.setSelected(block.emissiveRendering);
        this.tickRandomly.setSelected(block.tickRandomly);
        this.creativeTab.setSelectedItem(block.creativeTab);
        this.destroyTool.setSelectedItem(block.destroyTool);
        this.soundOnStep.setSelectedItem(block.soundOnStep.getUnmappedValue());
        this.breakSound.setSound(block.breakSound);
        this.fallSound.setSound(block.fallSound);
        this.hitSound.setSound(block.hitSound);
        this.placeSound.setSound(block.placeSound);
        this.stepSound.setSound(block.stepSound);
        this.defaultSoundType.setSelected(!block.isCustomSoundType);
        this.customSoundType.setSelected(block.isCustomSoundType);
        this.luminance.setValue(block.luminance);
        this.breakHarvestLevel.setValue(block.breakHarvestLevel);
        this.requiresCorrectTool.setSelected(block.requiresCorrectTool);
        this.customDrop.setBlock(block.customDrop);
        this.dropAmount.setValue(block.dropAmount);
        this.isNotColidable.setSelected(block.isNotColidable);
        this.unbreakable.setSelected(block.unbreakable);
        this.canRedstoneConnect.setSelected(block.canRedstoneConnect);
        this.lightOpacity.setValue(block.lightOpacity);
        this.material.setSelectedItem(block.material.getUnmappedValue());
        this.transparencyType.setSelectedItem(block.transparencyType);
        this.tintType.setSelectedItem(block.tintType);
        this.isItemTinted.setSelected(block.isItemTinted);
        this.animationCount.setValue((block.animationCount));
        if (block.blockBase == null) {
            this.blockBase.setSelectedIndex(0);
        } else {
            this.blockBase.setSelectedItem(block.blockBase);
        }

        this.plantsGrowOn.setSelected(block.plantsGrowOn);
        this.hasInventory.setSelected(block.hasInventory);
        this.useLootTableForDrops.setSelected(block.useLootTableForDrops);
        this.openGUIOnRightClick.setSelected(block.openGUIOnRightClick);
        this.inventoryDropWhenDestroyed.setSelected(block.inventoryDropWhenDestroyed);
        this.inventoryComparatorPower.setSelected(block.inventoryComparatorPower);
        this.inventorySize.setValue(block.inventorySize);
        this.inventoryStackSize.setValue(block.inventoryStackSize);
        this.tickRate.setValue(block.tickRate);
        generateFeature.setSelected(block.generateFeature);
        this.blocksToReplace.setListElements(block.blocksToReplace);
        this.restrictionBiomes.setListElements(block.restrictionBiomes);
        this.fluidRestrictions.setListElements(block.fluidRestrictions);
        this.isReplaceable.setSelected(block.isReplaceable);
        this.canProvidePower.setSelected(block.canProvidePower);
        this.colorOnMap.setSelectedItem(block.colorOnMap);
        this.offsetType.setSelectedItem(block.offsetType);
        this.aiPathNodeType.setSelectedItem(block.aiPathNodeType);
        this.creativePickItem.setBlock(block.creativePickItem);
        this.placingCondition.setSelectedProcedure(block.placingCondition);
        this.beaconColorModifier.setColor(block.beaconColorModifier);
        this.flammability.setValue(block.flammability);
        this.fireSpreadSpeed.setValue(block.fireSpreadSpeed);
        this.isLadder.setSelected(block.isLadder);
        this.reactionToPushing.setSelectedItem(block.reactionToPushing);
        this.slipperiness.setValue(block.slipperiness);
        this.jumpFactor.setValue(block.jumpFactor);
        this.speedFactor.setValue(block.speedFactor);
        this.geoModel.setSelectedItem(block.normal);
        this.displaySettings.setSelectedItem(block.displaySettings);
        this.disableOffset.setSelected(block.disableOffset);
        this.boundingBoxList.setEntries(block.boundingBoxes);
        this.specialInformation.setSelectedProcedure(block.specialInformation);
        this.refreshFieldsTileEntity();
        this.refreshRedstoneEmitted();
        this.tickRate.setEnabled(!this.tickRandomly.isSelected());

        this.customDrop.setEnabled(!this.useLootTableForDrops.isSelected());
        this.dropAmount.setEnabled(!this.useLootTableForDrops.isSelected());
        if (this.hasGravity.isEnabled()) {
            this.hasGravity.setEnabled(!this.isWaterloggable.isSelected());
        }

        this.updateSoundType();
    }

    public AnimatedBlock getElementFromGUI() {
        AnimatedBlock block = new AnimatedBlock(this.modElement);
        block.name = this.name.getText();
        block.hasTransparency = this.hasTransparency.isSelected();
        block.connectedSides = this.connectedSides.isSelected();
        block.displayFluidOverlay = this.displayFluidOverlay.isSelected();
        block.transparencyType = (String)this.transparencyType.getSelectedItem();
        block.tintType = (String)this.tintType.getSelectedItem();
        block.isItemTinted = this.isItemTinted.isSelected();
        block.guiBoundTo = (String)this.guiBoundTo.getSelectedItem();
        block.rotationMode = this.rotationMode.getSelectedIndex();
        block.enablePitch = this.enablePitch.isSelected();
        block.enchantPowerBonus = (Double)this.enchantPowerBonus.getValue();
        block.hardness = (Double) this.hardness.getValue();
        block.animationCount = (Number) this.animationCount.getValue();
        block.resistance = (Double) this.resistance.getValue();
        block.hasGravity = this.hasGravity.isSelected();
        block.isWaterloggable = this.isWaterloggable.isSelected();
        block.emissiveRendering = this.emissiveRendering.isSelected();
        block.tickRandomly = this.tickRandomly.isSelected();
        block.creativeTab = new TabEntry(this.mcreator.getWorkspace(), this.creativeTab.getSelectedItem());
        block.destroyTool = (String)this.destroyTool.getSelectedItem();
        block.requiresCorrectTool = this.requiresCorrectTool.isSelected();
        block.customDrop = this.customDrop.getBlock();
        block.dropAmount = (Integer)this.dropAmount.getValue();
        block.plantsGrowOn = this.plantsGrowOn.isSelected();
        block.isFluidTank = this.isFluidTank.isSelected();
        block.hasEnergyStorage = this.hasEnergyStorage.isSelected();
        block.energyInitial = (Integer)this.energyInitial.getValue();
        block.energyCapacity = (Integer)this.energyCapacity.getValue();
        block.energyMaxReceive = (Integer)this.energyMaxReceive.getValue();
        block.energyMaxExtract = (Integer)this.energyMaxExtract.getValue();
        block.fluidCapacity = (Integer)this.fluidCapacity.getValue();
        block.isNotColidable = this.isNotColidable.isSelected();
        block.canRedstoneConnect = this.canRedstoneConnect.isSelected();
        block.lightOpacity = (Integer)this.lightOpacity.getValue();
        block.material = new Material(this.mcreator.getWorkspace(), this.material.getSelectedItem());
        block.tickRate = (Integer)this.tickRate.getValue();
        block.isCustomSoundType = this.customSoundType.isSelected();
        block.soundOnStep = new StepSound(this.mcreator.getWorkspace(), this.soundOnStep.getSelectedItem());
        block.breakSound = this.breakSound.getSound();
        block.fallSound = this.fallSound.getSound();
        block.hitSound = this.hitSound.getSound();
        block.placeSound = this.placeSound.getSound();
        block.stepSound = this.stepSound.getSound();
        block.luminance = (Integer)this.luminance.getValue();
        block.unbreakable = this.unbreakable.isSelected();
        block.breakHarvestLevel = (Integer)this.breakHarvestLevel.getValue();
        block.spawnParticles = this.spawnParticles.isSelected();
        block.particleToSpawn = new Particle(this.mcreator.getWorkspace(), this.particleToSpawn.getSelectedItem());
        block.particleSpawningShape = (String)this.particleSpawningShape.getSelectedItem();
        block.particleSpawningRadious = (Double)this.particleSpawningRadious.getValue();
        block.particleAmount = (Integer)this.particleAmount.getValue();
        block.particleCondition = this.particleCondition.getSelectedProcedure();
        block.emittedRedstonePower = this.emittedRedstonePower.getSelectedProcedure();
        block.generateCondition = this.generateCondition.getSelectedProcedure();
        block.hasInventory = this.hasInventory.isSelected();
        block.useLootTableForDrops = this.useLootTableForDrops.isSelected();
        block.openGUIOnRightClick = this.openGUIOnRightClick.isSelected();
        block.inventorySize = (Integer)this.inventorySize.getValue();
        block.inventoryStackSize = (Integer)this.inventoryStackSize.getValue();
        block.inventoryDropWhenDestroyed = this.inventoryDropWhenDestroyed.isSelected();
        block.inventoryComparatorPower = this.inventoryComparatorPower.isSelected();
        if (this.outSlotIDs.getText().trim().equals("")) {
            block.inventoryOutSlotIDs = new ArrayList();
        } else {
            block.inventoryOutSlotIDs = (java.util.List) Stream.of(this.outSlotIDs.getText().split(",")).filter((e) -> {
                return !e.equals("");
            }).map(Integer::parseInt).collect(Collectors.toList());
        }

        if (this.inSlotIDs.getText().trim().equals("")) {
            block.inventoryInSlotIDs = new ArrayList();
        } else {
            block.inventoryInSlotIDs = (List)Stream.of(this.inSlotIDs.getText().split(",")).filter((e) -> {
                return !e.equals("");
            }).map(Integer::parseInt).collect(Collectors.toList());
        }

        block.frequencyPerChunks = (Integer)this.frequencyPerChunks.getValue();
        block.frequencyOnChunk = (Integer)this.frequencyOnChunk.getValue();
        block.generationShape = (String)this.generationShape.getSelectedItem();
        block.minGenerateHeight = (Integer)this.minGenerateHeight.getValue();
        block.maxGenerateHeight = (Integer)this.maxGenerateHeight.getValue();
        block.onBlockAdded = this.onBlockAdded.getSelectedProcedure();
        block.onNeighbourBlockChanges = this.onNeighbourBlockChanges.getSelectedProcedure();
        block.onTickUpdate = this.onTickUpdate.getSelectedProcedure();
        block.onRandomUpdateEvent = this.onRandomUpdateEvent.getSelectedProcedure();
        block.onDestroyedByPlayer = this.onDestroyedByPlayer.getSelectedProcedure();
        block.onDestroyedByExplosion = this.onDestroyedByExplosion.getSelectedProcedure();
        block.onStartToDestroy = this.onStartToDestroy.getSelectedProcedure();
        block.onEntityCollides = this.onEntityCollides.getSelectedProcedure();
        block.onEntityWalksOn = this.onEntityWalksOn.getSelectedProcedure();
        block.onBlockPlayedBy = this.onBlockPlayedBy.getSelectedProcedure();
        block.onRightClicked = this.onRightClicked.getSelectedProcedure();
        block.onRedstoneOn = this.onRedstoneOn.getSelectedProcedure();
        block.onRedstoneOff = this.onRedstoneOff.getSelectedProcedure();
        block.onHitByProjectile = this.onHitByProjectile.getSelectedProcedure();
        block.texture = this.texture.getID();
        block.itemTexture = this.itemTexture.getID();
        block.particleTexture = this.particleTexture.getID();
        block.textureTop = this.textureTop.getID();
        block.textureLeft = this.textureLeft.getID();
        block.textureFront = this.textureFront.getID();
        block.textureRight = this.textureRight.getID();
        block.textureBack = this.textureBack.getID();
        block.disableOffset = this.disableOffset.isSelected();
        block.boundingBoxes = this.boundingBoxList.getEntries();
        block.beaconColorModifier = this.beaconColorModifier.getColor();
        block.generateFeature = generateFeature.isSelected();
        block.restrictionBiomes = this.restrictionBiomes.getListElements();
        block.fluidRestrictions = this.fluidRestrictions.getListElements();
        //block.blocksToReplace = this.blocksToReplace.getListElements(); WHY DOES THIS LINE BREAK ANIMATED BLOCKS?!?!?!?
        block.isReplaceable = this.isReplaceable.isSelected();
        block.canProvidePower = this.canProvidePower.isSelected();
        block.colorOnMap = (String)this.colorOnMap.getSelectedItem();
        block.offsetType = (String)this.offsetType.getSelectedItem();
        block.aiPathNodeType = (String)this.aiPathNodeType.getSelectedItem();
        block.creativePickItem = this.creativePickItem.getBlock();
        block.placingCondition = this.placingCondition.getSelectedProcedure();
        block.flammability = (Integer)this.flammability.getValue();
        block.fireSpreadSpeed = (Integer)this.fireSpreadSpeed.getValue();
        block.isLadder = this.isLadder.isSelected();
        block.reactionToPushing = (String)this.reactionToPushing.getSelectedItem();
        block.slipperiness = (Double)this.slipperiness.getValue();
        block.speedFactor = (Double)this.speedFactor.getValue();
        block.jumpFactor = (Double)this.jumpFactor.getValue();
        block.specialInformation = this.specialInformation.getSelectedProcedure();
        block.normal = (String)this.geoModel.getSelectedItem();
        block.displaySettings = (String)this.displaySettings.getSelectedItem();
        if (this.blockBase.getSelectedIndex() != 0) {
            block.blockBase = (String)this.blockBase.getSelectedItem();
        }

        return block;
    }

}
