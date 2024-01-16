package net.nerdypuzzle.geckolib.ui.modgui;

import net.mcreator.blockly.BlocklyCompileNote;
import net.mcreator.blockly.data.*;
import net.mcreator.blockly.java.BlocklyToJava;
import net.mcreator.element.GeneratableElement;
import net.mcreator.element.ModElementType;
import net.mcreator.element.parts.TabEntry;
import net.mcreator.element.types.GUI;
import net.mcreator.element.types.Item;
import net.mcreator.generator.blockly.BlocklyBlockCodeGenerator;
import net.mcreator.generator.blockly.ProceduralBlockCodeGenerator;
import net.mcreator.generator.template.TemplateGeneratorException;
import net.mcreator.minecraft.DataListEntry;
import net.mcreator.minecraft.ElementUtil;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.blockly.BlocklyEditorToolbar;
import net.mcreator.ui.blockly.BlocklyEditorType;
import net.mcreator.ui.blockly.BlocklyPanel;
import net.mcreator.ui.blockly.CompileNotesPanel;
import net.mcreator.ui.component.JColor;
import net.mcreator.ui.component.JEmptyBox;
import net.mcreator.ui.component.SearchableComboBox;
import net.mcreator.ui.component.util.ComboBoxUtil;
import net.mcreator.ui.component.util.ComponentUtils;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.dialogs.TextureImportDialogs;
import net.mcreator.ui.help.HelpUtils;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.init.UIRES;
import net.mcreator.ui.laf.renderer.WTextureComboBoxRenderer;
import net.mcreator.ui.minecraft.*;
import net.mcreator.ui.minecraft.states.entity.JEntityDataList;
import net.mcreator.ui.modgui.IBlocklyPanelHolder;
import net.mcreator.ui.modgui.ModElementGUI;
import net.mcreator.ui.procedure.AbstractProcedureSelector;
import net.mcreator.ui.procedure.NumberProcedureSelector;
import net.mcreator.ui.procedure.ProcedureSelector;
import net.mcreator.ui.validation.AggregatedValidationResult;
import net.mcreator.ui.validation.Validator;
import net.mcreator.ui.validation.component.VComboBox;
import net.mcreator.ui.validation.component.VTextField;
import net.mcreator.ui.validation.validators.TextFieldValidator;
import net.mcreator.ui.workspace.resources.TextureType;
import net.mcreator.util.ListUtils;
import net.mcreator.util.StringUtils;
import net.mcreator.workspace.elements.ModElement;
import net.mcreator.workspace.elements.VariableTypeLoader;
import net.nerdypuzzle.geckolib.element.types.AnimatedEntity;
import net.nerdypuzzle.geckolib.element.types.GeckolibElement;
import net.nerdypuzzle.geckolib.parts.GeomodelRenderer;
import net.nerdypuzzle.geckolib.parts.PluginModelActions;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnimatedEntityGUI extends ModElementGUI<AnimatedEntity> implements GeckolibElement, IBlocklyPanelHolder {

    private ProcedureSelector onStruckByLightning;
    private ProcedureSelector whenMobFalls;
    private ProcedureSelector whenMobDies;
    private ProcedureSelector whenMobIsHurt;
    private ProcedureSelector onRightClickedOn;
    private ProcedureSelector whenThisMobKillsAnother;
    private ProcedureSelector onMobTickUpdate;
    private ProcedureSelector onPlayerCollidesWith;
    private ProcedureSelector onInitialSpawn;

    private ProcedureSelector spawningCondition;
    public NumberProcedureSelector visualScale;
    public NumberProcedureSelector boundingBoxScale;
    private ProcedureSelector solidBoundingBox;

    private final SoundSelector livingSound = new SoundSelector(mcreator);
    private final SoundSelector hurtSound = new SoundSelector(mcreator);
    private final SoundSelector deathSound = new SoundSelector(mcreator);
    private final SoundSelector stepSound = new SoundSelector(mcreator);

    private final VTextField mobName = new VTextField();

    private final JSpinner attackStrength = new JSpinner(new SpinnerNumberModel(3, 0, 10000, 1));
    private final JSpinner movementSpeed = new JSpinner(new SpinnerNumberModel(0.3, 0, 50, 0.1));
    private final JSpinner armorBaseValue = new JSpinner(new SpinnerNumberModel(0.0, 0, 100, 0.1));
    private final JSpinner health = new JSpinner(new SpinnerNumberModel(10, 0, 1024, 1));
    private final JSpinner knockbackResistance = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 0.1));
    private final JSpinner attackKnockback = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 0.1));

    private final JSpinner trackingRange = new JSpinner(new SpinnerNumberModel(64, 0, 10000, 1));
    private final JSpinner followRange = new JSpinner(new SpinnerNumberModel(16, 0, 2048, 1));

    private final JSpinner rangedAttackInterval = new JSpinner(new SpinnerNumberModel(20, 0, 1024, 1));
    private final JSpinner rangedAttackRadius = new JSpinner(new SpinnerNumberModel(10, 0, 1024, 0.1));

    private final JSpinner spawningProbability = new JSpinner(new SpinnerNumberModel(20, 1, 1000, 1));
    private final JSpinner minNumberOfMobsPerGroup = new JSpinner(new SpinnerNumberModel(4, 1, 1000, 1));
    private final JSpinner maxNumberOfMobsPerGroup = new JSpinner(new SpinnerNumberModel(4, 1, 1000, 1));

    private final JSpinner modelWidth = new JSpinner(new SpinnerNumberModel(0.6, 0, 1024, 0.1));
    private final JSpinner modelHeight = new JSpinner(new SpinnerNumberModel(1.8, 0, 1024, 0.1));
    private final JSpinner mountedYOffset = new JSpinner(new SpinnerNumberModel(0, -1024, 1024, 0.1));
    private final JSpinner modelShadowSize = new JSpinner(new SpinnerNumberModel(0.5, 0, 20, 0.1));
    private final JCheckBox disableCollisions = L10N.checkbox("elementgui.living_entity.disable_collisions");

    private final JSpinner xpAmount = new JSpinner(new SpinnerNumberModel(0, 0, 100000, 1));

    private final JCheckBox hasAI = L10N.checkbox("elementgui.living_entity.has_ai");
    private final JCheckBox isBoss = new JCheckBox();

    private final JCheckBox immuneToFire = L10N.checkbox("elementgui.living_entity.immune_fire");
    private final JCheckBox immuneToArrows = L10N.checkbox("elementgui.living_entity.immune_arrows");
    private final JCheckBox immuneToFallDamage = L10N.checkbox("elementgui.living_entity.immune_fall_damage");
    private final JCheckBox immuneToCactus = L10N.checkbox("elementgui.living_entity.immune_cactus");
    private final JCheckBox immuneToDrowning = L10N.checkbox("elementgui.living_entity.immune_drowning");
    private final JCheckBox immuneToLightning = L10N.checkbox("elementgui.living_entity.immune_lightning");
    private final JCheckBox immuneToPotions = L10N.checkbox("elementgui.living_entity.immune_potions");
    private final JCheckBox immuneToPlayer = L10N.checkbox("elementgui.living_entity.immune_player");
    private final JCheckBox immuneToExplosion = L10N.checkbox("elementgui.living_entity.immune_explosions");
    private final JCheckBox immuneToTrident = L10N.checkbox("elementgui.living_entity.immune_trident");
    private final JCheckBox immuneToAnvil = L10N.checkbox("elementgui.living_entity.immune_anvil");
    private final JCheckBox immuneToWither = L10N.checkbox("elementgui.living_entity.immune_wither");
    private final JCheckBox immuneToDragonBreath = L10N.checkbox("elementgui.living_entity.immune_dragon_breath");

    private final JCheckBox waterMob = L10N.checkbox("elementgui.living_entity.is_water_mob");
    private final JCheckBox flyingMob = L10N.checkbox("elementgui.living_entity.is_flying_mob");

    private final JCheckBox hasSpawnEgg = new JCheckBox();
    private final DataListComboBox creativeTab = new DataListComboBox(mcreator);

    private final JComboBox<String> mobSpawningType = new JComboBox<>(
            ElementUtil.getDataListAsStringArray("mobspawntypes"));

    private MCItemHolder mobDrop;
    private MCItemHolder equipmentMainHand;
    private MCItemHolder equipmentHelmet;
    private MCItemHolder equipmentBody;
    private MCItemHolder equipmentLeggings;
    private MCItemHolder equipmentBoots;
    private MCItemHolder equipmentOffHand;

    private final JComboBox<String> guiBoundTo = new JComboBox<>();
    private final JSpinner inventorySize = new JSpinner(new SpinnerNumberModel(9, 0, 256, 1));
    private final JSpinner inventoryStackSize = new JSpinner(new SpinnerNumberModel(64, 1, 1024, 1));

    private MCItemHolder rangedAttackItem;

    private final JComboBox<String> rangedItemType = new JComboBox<>();

    private final JTextField mobLabel = new JTextField();

    private final JCheckBox spawnInDungeons = L10N.checkbox("elementgui.living_entity.spawn_dungeons");
    private final JColor spawnEggBaseColor = new JColor(mcreator, false, false);
    private final JColor spawnEggDotColor = new JColor(mcreator, false, false);

    private final VComboBox<String> mobModelTexture = new SearchableComboBox<>();
    private final VComboBox<String> mobModelGlowTexture = new SearchableComboBox<>();

    private JEntityDataList entityDataList;
    private static final BlocklyCompileNote aiUnmodifiableCompileNote = new BlocklyCompileNote(
            BlocklyCompileNote.Type.INFO, L10N.t("blockly.warnings.unmodifiable_ai_bases"));

    private final JComboBox<String> aiBase = new JComboBox<>(
            Stream.of("(none)", "Creeper", "Skeleton", "Enderman", "Blaze", "Slime", "Witch", "Zombie", "MagmaCube",
                    "Pig", "Villager", "Wolf", "Cow", "Bat", "Chicken", "Ocelot", "Squid", "Horse", "Spider",
                    "IronGolem").sorted().toArray(String[]::new));

    private final JComboBox<String> mobBehaviourType = new JComboBox<>(new String[] { "Mob", "Creature" });
    private final JComboBox<String> mobCreatureType = new JComboBox<>(
            new String[] { "UNDEFINED", "UNDEAD", "ARTHROPOD", "ILLAGER", "WATER" });
    private final JComboBox<String> bossBarColor = new JComboBox<>(
            new String[] { "PINK", "BLUE", "RED", "GREEN", "YELLOW", "PURPLE", "WHITE" });
    private final JComboBox<String> bossBarType = new JComboBox<>(
            new String[] { "PROGRESS", "NOTCHED_6", "NOTCHED_10", "NOTCHED_12", "NOTCHED_20" });

    private final JCheckBox ridable = L10N.checkbox("elementgui.living_entity.is_rideable");

    private final JCheckBox canControlForward = L10N.checkbox("elementgui.living_entity.control_forward");
    private final JCheckBox canControlStrafe = L10N.checkbox("elementgui.living_entity.control_strafe");

    private final JCheckBox breedable = L10N.checkbox("elementgui.living_entity.is_breedable");

    private final JCheckBox tameable = L10N.checkbox("elementgui.living_entity.is_tameable");

    private final JCheckBox ranged = L10N.checkbox("elementgui.living_entity.is_ranged");

    private MCItemListField breedTriggerItems;

    private final JCheckBox spawnThisMob = new JCheckBox();
    private final JCheckBox doesDespawnWhenIdle = new JCheckBox();

    private BiomeListField restrictionBiomes;

    private BlocklyPanel blocklyPanel;
    private final CompileNotesPanel compileNotesPanel = new CompileNotesPanel();
    private boolean hasErrors = false;
    private Map<String, ToolboxBlock> externalBlocks;

    private boolean disableMobModelCheckBoxListener = false;

    //animations page start
    private final JCheckBox disableDeathRotation = L10N.checkbox("elementgui.common.enable", new Object[0]);
    private final JSpinner deathTime = new JSpinner(new SpinnerNumberModel(20, 0, 10000, 1));
    private final JSpinner lerp = new JSpinner(new SpinnerNumberModel(4, 0, 1000, 1));
    private final JSpinner height = new JSpinner(new SpinnerNumberModel(1, 0.1, 100, 0.1));

    private final VTextField animation1 = new VTextField();
    private final JTextField animation2 = new JTextField();
    private final JTextField animation3 = new JTextField();
    private final JTextField animation4 = new JTextField();
    private final JTextField animation5 = new JTextField();
    private final JTextField animation6 = new JTextField();
    private final JTextField animation7 = new JTextField();
    private final JTextField animation8 = new JTextField();
    private final JTextField animation9 = new JTextField();
    private final JTextField animation10 = new JTextField();

    private final JCheckBox enable2 = L10N.checkbox("elementgui.common.enable", new Object[0]);
    private final JCheckBox enable3 = L10N.checkbox("elementgui.common.enable", new Object[0]);
    private final JCheckBox enable4 = L10N.checkbox("elementgui.common.enable", new Object[0]);
    private final JCheckBox enable5 = L10N.checkbox("elementgui.common.enable", new Object[0]);
    private final JCheckBox enable6 = L10N.checkbox("elementgui.common.enable", new Object[0]);
    private final JCheckBox enable7 = L10N.checkbox("elementgui.common.enable", new Object[0]);
    private final JCheckBox enable8 = L10N.checkbox("elementgui.common.enable", new Object[0]);
    private final JCheckBox enable9 = L10N.checkbox("elementgui.common.enable", new Object[0]);
    private final JCheckBox enable10 = L10N.checkbox("elementgui.common.enable", new Object[0]);

    private ProcedureSelector finishedDying;

    private final JCheckBox headMovement = L10N.checkbox("elementgui.common.enable", new Object[0]);
    private final JCheckBox eyeHeight = L10N.checkbox("elementgui.animatedentity.eye_height");

    private final JTextField groupName = new JTextField();

    private final VComboBox<String> geoModel;

    public AnimatedEntityGUI(MCreator mcreator, ModElement modElement, boolean editingMode) {
        super(mcreator, modElement, editingMode);
        this.geoModel = new SearchableComboBox();
        this.initGUI();
        super.finalizeGUI();
    }

    private void setDefaultAISet() {
        blocklyPanel.setXML("<xml xmlns=\"https://developers.google.com/blockly/xml\">"
                + "<block type=\"aitasks_container\" deletable=\"false\" x=\"40\" y=\"40\"><next>"
                + "<block type=\"attack_on_collide\"><field name=\"speed\">1.2</field><field name=\"longmemory\">FALSE</field><next>"
                + "<block type=\"wander\"><field name=\"speed\">1</field><next>"
                + "<block type=\"attack_action\"><field name=\"callhelp\">FALSE</field><next>"
                + "<block type=\"look_around\"><next><block type=\"swim_in_water\"/></next>"
                + "</block></next></block></next></block></next></block></next></block></xml>");
    }

    private void regenerateAITasks() {
        BlocklyBlockCodeGenerator blocklyBlockCodeGenerator = new BlocklyBlockCodeGenerator(externalBlocks,
                mcreator.getGeneratorStats().getBlocklyBlocks(BlocklyEditorType.AI_TASK));

        BlocklyToJava blocklyToJava;
        try {
            blocklyToJava = new BlocklyToJava(mcreator.getWorkspace(), this.modElement, BlocklyEditorType.AI_TASK,
                    blocklyPanel.getXML(), null, new ProceduralBlockCodeGenerator(blocklyBlockCodeGenerator));
        } catch (TemplateGeneratorException e) {
            return;
        }

        List<BlocklyCompileNote> compileNotesArrayList = blocklyToJava.getCompileNotes();

        List<?> unmodifiableAIBases = (List<?>) mcreator.getWorkspace().getGenerator().getGeneratorConfiguration()
                .getDefinitionsProvider().getModElementDefinition(ModElementType.LIVINGENTITY)
                .get("unmodifiable_ai_bases");
        if (unmodifiableAIBases != null && unmodifiableAIBases.contains(aiBase.getSelectedItem()))
            compileNotesArrayList = List.of(aiUnmodifiableCompileNote);

        List<BlocklyCompileNote> finalCompileNotesArrayList = compileNotesArrayList;
        SwingUtilities.invokeLater(() -> {
            compileNotesPanel.updateCompileNotes(finalCompileNotesArrayList);
            hasErrors = false;

            for (BlocklyCompileNote note : finalCompileNotesArrayList) {
                if (note.type() == BlocklyCompileNote.Type.ERROR) {
                    hasErrors = true;
                    break;
                }
            }
        });
    }

    @Override protected void initGUI() {
        onStruckByLightning = new ProcedureSelector(this.withEntry("entity/when_struck_by_lightning"), mcreator,
                L10N.t("elementgui.living_entity.event_struck_by_lightning"),
                Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity"));
        whenMobFalls = new ProcedureSelector(this.withEntry("entity/when_falls"), mcreator,
                L10N.t("elementgui.living_entity.event_mob_falls"),
                Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity"));
        whenMobDies = new ProcedureSelector(this.withEntry("entity/when_dies"), mcreator,
                L10N.t("elementgui.living_entity.event_mob_dies"),
                Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/sourceentity:entity"));
        whenMobIsHurt = new ProcedureSelector(this.withEntry("entity/when_hurt"), mcreator,
                L10N.t("elementgui.living_entity.event_mob_is_hurt"),
                Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/sourceentity:entity"));
        onRightClickedOn = new ProcedureSelector(this.withEntry("entity/when_right_clicked"), mcreator,
                L10N.t("elementgui.living_entity.event_mob_right_clicked"),
                VariableTypeLoader.BuiltInTypes.ACTIONRESULTTYPE, Dependency.fromString(
                "x:number/y:number/z:number/world:world/entity:entity/sourceentity:entity/itemstack:itemstack")).makeReturnValueOptional();
        whenThisMobKillsAnother = new ProcedureSelector(this.withEntry("entity/when_kills_another"), mcreator,
                L10N.t("elementgui.living_entity.event_mob_kills_another"),
                Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/sourceentity:entity"));
        onMobTickUpdate = new ProcedureSelector(this.withEntry("entity/on_tick_update"), mcreator,
                L10N.t("elementgui.living_entity.event_mob_tick_update"),
                Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity"));
        onPlayerCollidesWith = new ProcedureSelector(this.withEntry("entity/when_player_collides"), mcreator,
                L10N.t("elementgui.living_entity.event_player_collides_with"),
                Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/sourceentity:entity"));
        onInitialSpawn = new ProcedureSelector(this.withEntry("entity/on_initial_spawn"), mcreator,
                L10N.t("elementgui.living_entity.event_initial_spawn"),
                Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity"));
        finishedDying = new ProcedureSelector(this.withEntry("geckolib/finished_dying"), mcreator,
                L10N.t("elementgui.animatedentity.finished_dying"),
                Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity"));

        spawningCondition = new ProcedureSelector(this.withEntry("entity/condition_natural_spawning"), mcreator,
                L10N.t("elementgui.living_entity.condition_natural_spawn"), VariableTypeLoader.BuiltInTypes.LOGIC,
                Dependency.fromString("x:number/y:number/z:number/world:world")).setDefaultName(
                L10N.t("condition.common.use_vanilla")).makeInline();
        visualScale = new NumberProcedureSelector(this.withEntry("geckolib/visual_scale"), mcreator,
                L10N.t("elementgui.animatedentity.visual_scale"), AbstractProcedureSelector.Side.BOTH,
                new JSpinner(new SpinnerNumberModel(1, 0.1, 1024, 0.1)), 300, Dependency.fromString(
                "x:number/y:number/z:number/world:world/entity:entity"));
        boundingBoxScale = new NumberProcedureSelector(this.withEntry("geckolib/bounding_box_scale.md"), mcreator,
                L10N.t("elementgui.animatedentity.bounding_box_scale"), AbstractProcedureSelector.Side.BOTH,
                new JSpinner(new SpinnerNumberModel(1, 0.1, 1024, 0.1)), 300, Dependency.fromString(
                "x:number/y:number/z:number/world:world/entity:entity"));
        solidBoundingBox = new ProcedureSelector(this.withEntry("geckolib/condition_solid_bounding_box"), mcreator,
                L10N.t("elementgui.living_entity.condition_solid_bounding_box"),
                VariableTypeLoader.BuiltInTypes.LOGIC,
                Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity")).setDefaultName(
                L10N.t("condition.common.false")).makeInline();

        restrictionBiomes = new BiomeListField(mcreator);
        breedTriggerItems = new MCItemListField(mcreator, ElementUtil::loadBlocksAndItems);

        entityDataList = new JEntityDataList(mcreator, this);

        mobModelTexture.setRenderer(
                new WTextureComboBoxRenderer.TypeTextures(mcreator.getWorkspace(), TextureType.ENTITY));
        mobModelGlowTexture.setRenderer(
                new WTextureComboBoxRenderer.TypeTextures(mcreator.getWorkspace(), TextureType.ENTITY));

        guiBoundTo.addActionListener(e -> {
            if (!isEditingMode()) {
                String selected = (String) guiBoundTo.getSelectedItem();
                if (selected != null) {
                    ModElement element = mcreator.getWorkspace().getModElementByName(selected);
                    if (element != null) {
                        GeneratableElement generatableElement = element.getGeneratableElement();
                        if (generatableElement instanceof GUI) {
                            inventorySize.setValue(((GUI) generatableElement).getMaxSlotID() + 1);
                        }
                    }
                }
            }
        });

        spawnInDungeons.setOpaque(false);
        mobModelTexture.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXX");
        mobModelGlowTexture.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXX");

        mobDrop = new MCItemHolder(mcreator, ElementUtil::loadBlocksAndItems);
        equipmentMainHand = new MCItemHolder(mcreator, ElementUtil::loadBlocksAndItems);
        equipmentHelmet = new MCItemHolder(mcreator, ElementUtil::loadBlocksAndItems);
        equipmentBody = new MCItemHolder(mcreator, ElementUtil::loadBlocksAndItems);
        equipmentLeggings = new MCItemHolder(mcreator, ElementUtil::loadBlocksAndItems);
        equipmentBoots = new MCItemHolder(mcreator, ElementUtil::loadBlocksAndItems);
        equipmentOffHand = new MCItemHolder(mcreator, ElementUtil::loadBlocksAndItems);
        rangedAttackItem = new MCItemHolder(mcreator, ElementUtil::loadBlocksAndItems);

        JPanel pane1 = new JPanel(new BorderLayout(0, 0));
        JPanel pane2 = new JPanel(new BorderLayout(0, 0));
        JPanel pane3 = new JPanel(new BorderLayout(0, 0));
        JPanel pane4 = new JPanel(new BorderLayout(0, 0));
        JPanel pane5 = new JPanel(new BorderLayout(0, 0));
        JPanel pane7 = new JPanel(new BorderLayout(0, 0));
        JPanel pane8 = new JPanel(new BorderLayout(0, 0));

        JPanel subpane1 = new JPanel(new GridLayout(12, 2, 0, 2));

        immuneToFire.setOpaque(false);
        immuneToArrows.setOpaque(false);
        immuneToFallDamage.setOpaque(false);
        immuneToCactus.setOpaque(false);
        immuneToDrowning.setOpaque(false);
        immuneToLightning.setOpaque(false);
        immuneToPotions.setOpaque(false);
        immuneToPlayer.setOpaque(false);
        immuneToExplosion.setOpaque(false);
        immuneToTrident.setOpaque(false);
        immuneToAnvil.setOpaque(false);
        immuneToDragonBreath.setOpaque(false);
        immuneToWither.setOpaque(false);

        subpane1.setOpaque(false);

        subpane1.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/behaviour"),
                L10N.label("elementgui.living_entity.behaviour")));
        subpane1.add(mobBehaviourType);

        subpane1.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/drop"),
                L10N.label("elementgui.living_entity.mob_drop")));
        subpane1.add(PanelUtils.totalCenterInPanel(mobDrop));

        subpane1.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/creature_type"),
                L10N.label("elementgui.living_entity.creature_type")));
        subpane1.add(mobCreatureType);

        subpane1.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/movement_speed"),
                L10N.label("elementgui.living_entity.movement_speed")));
        subpane1.add(movementSpeed);

        subpane1.add(PanelUtils.join(FlowLayout.LEFT, L10N.label("elementgui.living_entity.health_xp_amount"),
                HelpUtils.helpButton(this.withEntry("entity/health")),
                HelpUtils.helpButton(this.withEntry("entity/xp_amount"))));
        subpane1.add(PanelUtils.gridElements(1, 2, 2, 0, health, xpAmount));

        subpane1.add(
                PanelUtils.join(FlowLayout.LEFT, L10N.label("elementgui.living_entity.follow_range_tracking_range"),
                        HelpUtils.helpButton(this.withEntry("entity/follow_range")),
                        HelpUtils.helpButton(this.withEntry("entity/tracking_range"))));
        subpane1.add(PanelUtils.gridElements(1, 2, 2, 0, followRange, trackingRange));

        subpane1.add(
                PanelUtils.join(FlowLayout.LEFT, L10N.label("elementgui.living_entity.attack_strenght_armor_value"),
                        HelpUtils.helpButton(this.withEntry("entity/attack_strength")),
                        HelpUtils.helpButton(this.withEntry("entity/armor_base_value"))));
        subpane1.add(PanelUtils.gridElements(1, 2, 2, 0, attackStrength, armorBaseValue));

        subpane1.add(PanelUtils.join(FlowLayout.LEFT, L10N.label("elementgui.living_entity.knockback"),
                HelpUtils.helpButton(this.withEntry("entity/attack_knockback")),
                HelpUtils.helpButton(this.withEntry("entity/knockback_resistance"))));
        subpane1.add(PanelUtils.gridElements(1, 2, 2, 0, attackKnockback, knockbackResistance));

        subpane1.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/equipment"),
                L10N.label("elementgui.living_entity.equipment")));
        subpane1.add(PanelUtils.join(FlowLayout.LEFT, 0, 2, PanelUtils.totalCenterInPanel(
                PanelUtils.join(FlowLayout.LEFT, 2, 0, equipmentMainHand, equipmentOffHand, equipmentHelmet,
                        equipmentBody, equipmentLeggings, equipmentBoots))));

        subpane1.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/ridable"),
                L10N.label("elementgui.living_entity.ridable")));
        subpane1.add(PanelUtils.join(FlowLayout.LEFT, 0, 0, ridable, canControlForward, canControlStrafe));

        subpane1.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/water_entity"),
                L10N.label("elementgui.living_entity.water_mob")));
        subpane1.add(waterMob);

        subpane1.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/flying_entity"),
                L10N.label("elementgui.living_entity.flying_mob")));
        subpane1.add(flyingMob);

        hasAI.setOpaque(false);
        isBoss.setOpaque(false);
        waterMob.setOpaque(false);
        flyingMob.setOpaque(false);
        hasSpawnEgg.setOpaque(false);
        disableCollisions.setOpaque(false);

        livingSound.setText("");
        hurtSound.setText("entity.generic.hurt");
        deathSound.setText("entity.generic.death");

        JPanel subpanel2 = new JPanel(new GridLayout(1, 2, 0, 2));
        subpanel2.setOpaque(false);

        subpanel2.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/immunity"),
                L10N.label("elementgui.living_entity.is_immune_to")));
        subpanel2.add(
                PanelUtils.gridElements(4, 4, 0, 0, immuneToFire, immuneToArrows, immuneToFallDamage, immuneToCactus,
                        immuneToDrowning, immuneToLightning, immuneToPotions, immuneToPlayer, immuneToExplosion,
                        immuneToAnvil, immuneToTrident, immuneToDragonBreath, immuneToWither));

        pane1.add("Center", PanelUtils.totalCenterInPanel(PanelUtils.northAndCenterElement(subpane1, subpanel2)));

        JPanel entityDataListPanel = new JPanel(new GridLayout());

        JComponent entityDataListComp = PanelUtils.northAndCenterElement(
                HelpUtils.wrapWithHelpButton(this.withEntry("entity/entity_data"),
                        L10N.label("elementgui.living_entity.entity_data")), entityDataList);
        entityDataListPanel.setOpaque(false);
        entityDataListComp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        entityDataListPanel.add(entityDataListComp);

        JPanel spo2 = new JPanel(new GridLayout(15, 2, 2, 2));

        spo2.setOpaque(false);

        spo2.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/name"),
                L10N.label("elementgui.living_entity.name")));
        spo2.add(mobName);

        spo2.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/model"),
                L10N.label("elementgui.animatedentity.entity_model")));
        this.geoModel.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXX");
        this.geoModel.setRenderer(new GeomodelRenderer());
        ComponentUtils.deriveFont(this.geoModel, 16.0F);
        spo2.add(this.geoModel);

        spo2.add(new JEmptyBox());
        spo2.add(visualScale);

        spo2.add(new JEmptyBox());
        spo2.add(boundingBoxScale);

        spo2.add(new JEmptyBox());
        spo2.add(solidBoundingBox);

        JButton importmobtexture = new JButton(UIRES.get("18px.add"));
        importmobtexture.setToolTipText(L10N.t("elementgui.living_entity.entity_model_import"));
        importmobtexture.setOpaque(false);
        importmobtexture.addActionListener(e -> {
            TextureImportDialogs.importMultipleTextures(mcreator, TextureType.ENTITY);
            mobModelTexture.removeAllItems();
            mobModelTexture.addItem("");
            mcreator.getFolderManager().getTexturesList(TextureType.ENTITY)
                    .forEach(el -> mobModelTexture.addItem(el.getName()));
            mobModelGlowTexture.removeAllItems();
            mobModelGlowTexture.addItem("");
            mcreator.getFolderManager().getTexturesList(TextureType.ENTITY)
                    .forEach(el -> mobModelGlowTexture.addItem(el.getName()));
        });

        spo2.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/texture"),
                L10N.label("elementgui.living_entity.texture")));
        spo2.add(PanelUtils.centerAndEastElement(mobModelTexture, importmobtexture, 0, 0));

        spo2.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/glow_texture"),
                L10N.label("elementgui.living_entity.glow_texture")));
        spo2.add(mobModelGlowTexture);

        ComponentUtils.deriveFont(mobModelTexture, 16);
        ComponentUtils.deriveFont(mobModelGlowTexture, 16);
        ComponentUtils.deriveFont(aiBase, 16);
        ComponentUtils.deriveFont(rangedItemType, 16);

        spawnEggBaseColor.setOpaque(false);
        spawnEggDotColor.setOpaque(false);

        modelWidth.setPreferredSize(new Dimension(85, 32));
        mountedYOffset.setPreferredSize(new Dimension(85, 32));
        modelHeight.setPreferredSize(new Dimension(85, 32));
        modelShadowSize.setPreferredSize(new Dimension(85, 32));

        armorBaseValue.setPreferredSize(new Dimension(250, 32));
        movementSpeed.setPreferredSize(new Dimension(250, 32));
        trackingRange.setPreferredSize(new Dimension(250, 32));
        attackStrength.setPreferredSize(new Dimension(250, 32));
        attackKnockback.setPreferredSize(new Dimension(250, 32));
        knockbackResistance.setPreferredSize(new Dimension(250, 32));
        followRange.setPreferredSize(new Dimension(250, 32));
        health.setPreferredSize(new Dimension(250, 32));
        xpAmount.setPreferredSize(new Dimension(250, 32));

        rangedAttackInterval.setPreferredSize(new Dimension(85, 32));
        rangedAttackRadius.setPreferredSize(new Dimension(85, 32));


        spo2.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/bounding_box"),
                L10N.label("elementgui.living_entity.bounding_box")));
        spo2.add(PanelUtils.join(FlowLayout.LEFT, 0, 0, modelWidth, new JEmptyBox(7, 7), modelHeight, new JEmptyBox(7, 7), modelShadowSize,
                new JEmptyBox(7, 7), mountedYOffset, new JEmptyBox(7, 7), disableCollisions));

        spo2.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/spawn_egg_options"),
                L10N.label("elementgui.living_entity.spawn_egg_options")));
        spo2.add(PanelUtils.join(FlowLayout.LEFT, 5, 0, hasSpawnEgg, spawnEggBaseColor, spawnEggDotColor, creativeTab));

        bossBarColor.setEnabled(false);
        bossBarType.setEnabled(false);

        spo2.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/boss_entity"),
                L10N.label("elementgui.living_entity.mob_boss")));
        spo2.add(PanelUtils.join(FlowLayout.LEFT, 5, 0, isBoss, bossBarColor, bossBarType));

        spo2.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/label"),
                L10N.label("elementgui.living_entity.label")));
        spo2.add(mobLabel);

        spo2.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/sound"),
                L10N.label("elementgui.living_entity.sound")));
        spo2.add(livingSound);

        spo2.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/step_sound"),
                L10N.label("elementgui.living_entity.step_sound")));
        spo2.add(stepSound);

        spo2.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/hurt_sound"),
                L10N.label("elementgui.living_entity.hurt_sound")));
        spo2.add(hurtSound);

        spo2.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/death_sound"),
                L10N.label("elementgui.living_entity.death_sound")));
        spo2.add(deathSound);

        ComponentUtils.deriveFont(mobLabel, 16);

        pane2.setOpaque(false);

        pane2.add("Center", PanelUtils.totalCenterInPanel(spo2));

        JPanel aitop = new JPanel(new GridLayout(3, 1, 0, 2));
        aitop.setOpaque(false);
        aitop.add(PanelUtils.join(FlowLayout.LEFT,
                HelpUtils.wrapWithHelpButton(this.withEntry("entity/enable_ai"), hasAI)));

        aitop.add(PanelUtils.join(FlowLayout.LEFT,
                HelpUtils.wrapWithHelpButton(this.withEntry("entity/breedable"), breedable), breedTriggerItems,
                tameable));

        breedTriggerItems.setPreferredSize(new Dimension(230, 32));

        aitop.add(PanelUtils.join(FlowLayout.LEFT, new JEmptyBox(5, 5),
                HelpUtils.wrapWithHelpButton(this.withEntry("entity/base"),
                        L10N.label("elementgui.living_entity.mob_base")), aiBase));

        aiBase.setPreferredSize(new Dimension(250, 32));
        aiBase.addActionListener(e -> regenerateAITasks());

        JPanel aitopoveral = new JPanel(new BorderLayout(5, 0));
        aitopoveral.setOpaque(false);

        aitopoveral.add("West", aitop);

        JPanel aitop2 = new JPanel(new GridLayout(2, 1, 0, 0));
        aitop2.setOpaque(false);

        aitop2.add(PanelUtils.join(FlowLayout.LEFT,
                HelpUtils.wrapWithHelpButton(this.withEntry("entity/do_ranged_attacks"), ranged),
                rangedItemType, rangedAttackItem, rangedAttackInterval, rangedAttackRadius));

        JPanel aitop3 = new JPanel(new GridLayout(1, 1, 0, 0));
        aitop3.setOpaque(false);

        aitop3.add(PanelUtils.join(FlowLayout.LEFT,
                HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/eye_height"),
                        eyeHeight), height));

        aitop2.add(aitop3);

        aitopoveral.add("Center", aitop2);

        rangedAttackItem.setEnabled(false);

        rangedItemType.addActionListener(
                e -> rangedAttackItem.setEnabled("Default item".equals(rangedItemType.getSelectedItem())));

        ridable.setOpaque(false);
        canControlStrafe.setOpaque(false);
        canControlForward.setOpaque(false);

        aitopoveral.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder((Color) UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1),
                L10N.t("elementgui.living_entity.ai_parameters"), 0, 0, getFont().deriveFont(12.0f),
                (Color) UIManager.get("MCreatorLAF.BRIGHT_COLOR")));

        JPanel aipan = new JPanel(new BorderLayout(0, 5));
        aipan.setOpaque(false);

        externalBlocks = BlocklyLoader.INSTANCE.getBlockLoader(BlocklyEditorType.AI_TASK).getDefinedBlocks();

        blocklyPanel = new BlocklyPanel(mcreator, BlocklyEditorType.AI_TASK);
        blocklyPanel.addTaskToRunAfterLoaded(() -> {
            BlocklyLoader.INSTANCE.getBlockLoader(BlocklyEditorType.AI_TASK)
                    .loadBlocksAndCategoriesInPanel(blocklyPanel, ToolboxType.AI_BUILDER);
            blocklyPanel.getJSBridge()
                    .setJavaScriptEventListener(() -> new Thread(AnimatedEntityGUI.this::regenerateAITasks).start());
            if (!isEditingMode()) {
                setDefaultAISet();
            }
        });

        aipan.add("North", aitopoveral);

        JPanel bpb = new JPanel(new GridLayout());
        bpb.setOpaque(false);
        bpb.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder((Color) UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1),
                L10N.t("elementgui.living_entity.ai_tasks"), TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
                getFont(), Color.white));
        BlocklyEditorToolbar blocklyEditorToolbar = new BlocklyEditorToolbar(mcreator, BlocklyEditorType.AI_TASK,
                blocklyPanel);
        blocklyEditorToolbar.setTemplateLibButtonWidth(156);
        bpb.add(PanelUtils.northAndCenterElement(blocklyEditorToolbar, blocklyPanel));
        aipan.add("Center", bpb);
        aipan.add("South", compileNotesPanel);

        blocklyPanel.setPreferredSize(new Dimension(150, 150));

        pane3.add("Center", PanelUtils.maxMargin(aipan, 10, true, true, true, true));

        breedable.setOpaque(false);
        tameable.setOpaque(false);
        ranged.setOpaque(false);

        hasAI.setSelected(true);

        breedable.addActionListener(actionEvent -> {
            if (breedable.isSelected()) {
                hasAI.setSelected(true);
                hasAI.setEnabled(false);
                this.breedTriggerItems.setEnabled(true);
                this.tameable.setEnabled(true);
            } else {
                hasAI.setEnabled(true);
                this.breedTriggerItems.setEnabled(false);
                this.tameable.setEnabled(false);
            }
        });

        isBoss.addActionListener(e -> {
            bossBarColor.setEnabled(isBoss.isSelected());
            bossBarType.setEnabled(isBoss.isSelected());
        });

        pane3.setOpaque(false);

        JPanel events = new JPanel(new GridLayout(3, 4, 8, 8));
        events.add(onStruckByLightning);
        events.add(whenMobFalls);
        events.add(whenMobDies);
        events.add(whenMobIsHurt);
        events.add(onRightClickedOn);
        events.add(whenThisMobKillsAnother);
        events.add(onMobTickUpdate);
        events.add(onPlayerCollidesWith);
        events.add(onInitialSpawn);
        events.setOpaque(false);
        pane4.add("Center", PanelUtils.totalCenterInPanel(events));

        isBoss.setOpaque(false);

        pane4.setOpaque(false);

        JPanel selp = new JPanel(new GridLayout(8, 2, 30, 2));

        ComponentUtils.deriveFont(mobName, 16);

        spawnThisMob.setSelected(true);
        doesDespawnWhenIdle.setSelected(true);

        spawnThisMob.setOpaque(false);
        doesDespawnWhenIdle.setOpaque(false);

        hasSpawnEgg.setSelected(true);

        selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/enable_spawning"),
                L10N.label("elementgui.living_entity.enable_mob_spawning")));
        selp.add(spawnThisMob);

        selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/despawn_idle"),
                L10N.label("elementgui.living_entity.despawn_idle")));
        selp.add(doesDespawnWhenIdle);

        selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/spawn_weight"),
                L10N.label("elementgui.living_entity.spawn_weight")));
        selp.add(spawningProbability);

        selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/spawn_type"),
                L10N.label("elementgui.living_entity.spawn_type")));
        selp.add(mobSpawningType);

        selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/spawn_group_size"),
                L10N.label("elementgui.living_entity.min_spawn_group_size")));
        selp.add(minNumberOfMobsPerGroup);

        selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/spawn_group_size"),
                L10N.label("elementgui.living_entity.max_spawn_group_size")));
        selp.add(maxNumberOfMobsPerGroup);

        selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("common/restrict_to_biomes"),
                L10N.label("elementgui.living_entity.restrict_to_biomes")));
        selp.add(restrictionBiomes);

        selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/spawn_in_dungeons"),
                L10N.label("elementgui.living_entity.does_spawn_in_dungeons")));
        selp.add(spawnInDungeons);

        selp.setOpaque(false);

        JComponent selpcont = PanelUtils.northAndCenterElement(selp,
                PanelUtils.gridElements(1, 2, 5, 5, L10N.label("elementgui.living_entity.spawn_general_condition"),
                        PanelUtils.westAndCenterElement(new JEmptyBox(12, 5), spawningCondition)), 5, 5);

        pane5.add("Center", PanelUtils.totalCenterInPanel(selpcont));

        pane5.setOpaque(false);

        JPanel props = new JPanel(new GridLayout(3, 2, 35, 2));
        props.setOpaque(false);

        props.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/bind_gui"),
                L10N.label("elementgui.living_entity.bind_to_gui")));
        props.add(guiBoundTo);

        props.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/inventory_size"),
                L10N.label("elementgui.living_entity.inventory_size")));
        props.add(inventorySize);

        props.add(HelpUtils.wrapWithHelpButton(this.withEntry("entity/inventory_stack_size"),
                L10N.label("elementgui.common.max_stack_size")));
        props.add(inventoryStackSize);

        pane7.add(PanelUtils.totalCenterInPanel(props));
        pane7.setOpaque(false);
        pane7.setOpaque(false);

        mobModelTexture.setValidator(() -> {
            if (mobModelTexture.getSelectedItem() == null || mobModelTexture.getSelectedItem().equals(""))
                return new Validator.ValidationResult(Validator.ValidationResultType.ERROR,
                        L10N.t("elementgui.living_entity.error_entity_model_needs_texture"));
            return Validator.ValidationResult.PASSED;
        });

        mobName.setValidator(
                new TextFieldValidator(mobName, L10N.t("elementgui.living_entity.error_entity_needs_name")));
        mobName.enableRealtimeValidation();

        geoModel.setValidator(() -> {
            if (geoModel.getSelectedItem() == null || geoModel.getSelectedItem().equals(""))
                return new Validator.ValidationResult(Validator.ValidationResultType.ERROR,
                        L10N.t("elementgui.animatedentity.modelname"));
            return Validator.ValidationResult.PASSED;
        });

        pane1.setOpaque(false);

        //animations page start

        //built in animations panel
        JPanel animations = new JPanel(new GridLayout(21, 2, 20, 2));

        animations.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder((Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1),
                L10N.t("elementgui.animatedentity.animations_boarder", new Object[0]),
                0, 0, this.getFont().deriveFont(12.0F), (Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR")));

        animations.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/idle_animation"),
                L10N.label("elementgui.animatedentity.idle_animation")));
        animations.add(new JEmptyBox());
        animations.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/animation_name"),
                L10N.label("elementgui.animatedentity.animation_name")));
        animations.add(animation1);

        animation1.setValidator(
                new TextFieldValidator(animation1, L10N.t("elementgui.animatedentity.idle_required")));
        animation1.enableRealtimeValidation();

        animations.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/walk_animation"),
                L10N.label("elementgui.animatedentity.walk_animation")));
        animations.add(enable2);
        animations.add(L10N.label("elementgui.animatedentity.animation_name"));
        animations.add(animation2);

        animations.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/death_animation"),
                L10N.label("elementgui.animatedentity.death_animation")));
        animations.add(enable3);
        animations.add(L10N.label("elementgui.animatedentity.animation_name"));
        animations.add(animation3);

        animations.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/attack_animation"),
                L10N.label("elementgui.animatedentity.attack_animation")));
        animations.add(enable4);
        animations.add(L10N.label("elementgui.animatedentity.animation_name"));
        animations.add(animation4);

        animations.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/swim_animation"),
                L10N.label("elementgui.animatedentity.swim_animation")));
        animations.add(enable5);
        animations.add(L10N.label("elementgui.animatedentity.animation_name"));
        animations.add(animation5);

        animations.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/sneak_animation"),
                L10N.label("elementgui.animatedentity.sneak_animation")));
        animations.add(enable6);
        animations.add(L10N.label("elementgui.animatedentity.animation_name"));
        animations.add(animation6);

        animations.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/sprint_animation"),
                L10N.label("elementgui.animatedentity.sprint_animation")));
        animations.add(enable7);
        animations.add(L10N.label("elementgui.animatedentity.animation_name"));
        animations.add(animation7);

        animations.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/flight_animation"),
                L10N.label("elementgui.animatedentity.flight_animation")));
        animations.add(enable8);
        animations.add(L10N.label("elementgui.animatedentity.animation_name"));
        animations.add(animation8);

        animations.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/riding_animation"),
                L10N.label("elementgui.animatedentity.riding_animation")));
        animations.add(enable9);
        animations.add(L10N.label("elementgui.animatedentity.animation_name"));
        animations.add(animation9);

        animations.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/aggression_animation"),
                L10N.label("elementgui.animatedentity.aggression_animation")));
        animations.add(enable10);
        animations.add(L10N.label("elementgui.animatedentity.animation_name"));
        animations.add(animation10);

        animations.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/lerp"),
                L10N.label("elementgui.animatedentity.lerp")));
        animations.add(lerp);


        //death animation settings panel
        JPanel animations_extras = new JPanel(new GridLayout(2, 2, 20, 2));

        animations_extras.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder((Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1),
                L10N.t("elementgui.animatedentity.death_animations_boarder", new Object[0]),
                0, 0, this.getFont().deriveFont(12.0F), (Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR")));

        JPanel dpanel = new JPanel(new GridLayout(2, 2, 10, 2));

        dpanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/disable_death_rotation"),
                L10N.label("elementgui.animatedentity.disable_death_rotation")));
        dpanel.add(disableDeathRotation);

        dpanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/death_time"),
                L10N.label("elementgui.animatedentity.death_time")));
        dpanel.add(deathTime);

        JPanel dpanel_procedure = new JPanel(new GridLayout(1, 1, 10, 2));

        dpanel_procedure.add(finishedDying);

        animations_extras.add(PanelUtils.centerInPanel(dpanel));
        animations_extras.add(PanelUtils.centerInPanel(dpanel_procedure));


        //extra animations panel
        JPanel extras = new JPanel(new GridLayout(2, 2, 20, 2));

        extras.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder((Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1),
                L10N.t("elementgui.animatedentity.extras_boarder", new Object[0]),
                0, 0, this.getFont().deriveFont(12.0F), (Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR")));

        JPanel extras_head = new JPanel(new GridLayout(2, 2, 10, 2));

        extras_head.add(HelpUtils.wrapWithHelpButton(this.withEntry("geckolib/head_movement"),
                L10N.label("elementgui.animatedentity.head_movement")));
        extras_head.add(headMovement);
        extras_head.add(L10N.label("elementgui.animatedentity.group_name"));
        extras_head.add(groupName);

        extras.add(PanelUtils.centerInPanel(extras_head));

        JPanel extras_condition = new JPanel(new GridLayout(2, 1, 10, 2));

        // empty panel where the procedure selectors were

        extras.add(PanelUtils.centerInPanel(extras_condition));

        //merge the right side panels
        JPanel merged_extras = new JPanel(new GridLayout(2, 1, 20, 2));

        merged_extras.add(animations_extras);
        merged_extras.add(extras);


        //main displayed panel
        JPanel animations_master = new JPanel(new GridLayout(1, 2, 20, 2));

        animations_master.add(animations);
        animations_master.add(merged_extras);

        pane8.add(PanelUtils.totalCenterInPanel(animations_master));

        animations_master.setOpaque(false);
        animations_extras.setOpaque(false);
        animations.setOpaque(false);
        extras.setOpaque(false);
        merged_extras.setOpaque(false);
        dpanel_procedure.setOpaque(false);
        dpanel.setOpaque(false);
        extras_head.setOpaque(false);
        pane8.setOpaque(false);

        enable2.setOpaque(false);
        enable3.setOpaque(false);
        enable4.setOpaque(false);
        enable5.setOpaque(false);
        enable6.setOpaque(false);
        enable7.setOpaque(false);
        enable8.setOpaque(false);
        enable9.setOpaque(false);
        enable10.setOpaque(false);

        disableDeathRotation.setOpaque(false);
        headMovement.setOpaque(false);
        eyeHeight.setOpaque(false);

        addPage(L10N.t("elementgui.living_entity.page_visual_and_sound"), pane2);
        addPage(L10N.t("elementgui.living_entity.page_behaviour"), pane1);
        addPage(L10N.t("elementgui.living_entity.page_entity_data"), entityDataListPanel);
        addPage(L10N.t("elementgui.common.page_inventory"), pane7);
        addPage(L10N.t("elementgui.common.page_triggers"), pane4);
        addPage(L10N.t("elementgui.living_entity.page_ai_and_goals"), pane3);
        addPage(L10N.t("elementgui.living_entity.page_spawning"), pane5);
        addPage(L10N.t("elementgui.animatedentity.animations_page"), pane8);

        animation2.setEnabled(enable2.isSelected());
        animation3.setEnabled(enable3.isSelected());
        animation4.setEnabled(enable4.isSelected());
        animation5.setEnabled(enable5.isSelected());
        animation6.setEnabled(enable6.isSelected());
        animation7.setEnabled(enable7.isSelected());
        animation8.setEnabled(enable8.isSelected());
        animation9.setEnabled(enable9.isSelected());
        animation10.setEnabled(enable10.isSelected());
        groupName.setEnabled(headMovement.isSelected());
        height.setEnabled(eyeHeight.isSelected());

        enable2.addActionListener(actionEvent -> {
            animation2.setEnabled(enable2.isSelected());
        });

        enable3.addActionListener(actionEvent -> {
            animation3.setEnabled(enable3.isSelected());
        });

        enable4.addActionListener(actionEvent -> {
            animation4.setEnabled(enable4.isSelected());
        });

        enable5.addActionListener(actionEvent -> {
            animation5.setEnabled(enable5.isSelected());
        });

        enable6.addActionListener(actionEvent -> {
            animation6.setEnabled(enable6.isSelected());
        });

        enable7.addActionListener(actionEvent -> {
            animation7.setEnabled(enable7.isSelected());
        });

        enable8.addActionListener(actionEvent -> {
            animation8.setEnabled(enable8.isSelected());
        });

        enable9.addActionListener(actionEvent -> {
            animation9.setEnabled(enable9.isSelected());
        });

        enable10.addActionListener(actionEvent -> {
            animation10.setEnabled(enable10.isSelected());
        });

        headMovement.addActionListener(actionEvent -> {
            groupName.setEnabled(headMovement.isSelected());
        });

        eyeHeight.addActionListener(actionEvent -> {
            height.setEnabled(eyeHeight.isSelected());
        });

        if (!isEditingMode()) {
            String readableNameFromModElement = StringUtils.machineToReadableName(modElement.getName());
            mobName.setText(readableNameFromModElement);
        }
    }

    @Override public void reloadDataLists() {
        disableMobModelCheckBoxListener = true;

        super.reloadDataLists();
        onStruckByLightning.refreshListKeepSelected();
        whenMobFalls.refreshListKeepSelected();
        whenMobDies.refreshListKeepSelected();
        whenMobIsHurt.refreshListKeepSelected();
        onRightClickedOn.refreshListKeepSelected();
        whenThisMobKillsAnother.refreshListKeepSelected();
        onMobTickUpdate.refreshListKeepSelected();
        onPlayerCollidesWith.refreshListKeepSelected();
        onInitialSpawn.refreshListKeepSelected();
        finishedDying.refreshListKeepSelected();;

        spawningCondition.refreshListKeepSelected();
        visualScale.refreshListKeepSelected();
        boundingBoxScale.refreshListKeepSelected();
        solidBoundingBox.refreshListKeepSelected();

        ComboBoxUtil.updateComboBoxContents(mobModelTexture, ListUtils.merge(Collections.singleton(""),
                mcreator.getFolderManager().getTexturesList(TextureType.ENTITY).stream().map(File::getName)
                        .collect(Collectors.toList())), "");

        ComboBoxUtil.updateComboBoxContents(mobModelGlowTexture, ListUtils.merge(Collections.singleton(""),
                mcreator.getFolderManager().getTexturesList(TextureType.ENTITY).stream().map(File::getName)
                        .collect(Collectors.toList())), "");

        ComboBoxUtil.updateComboBoxContents(creativeTab, ElementUtil.loadAllTabs(mcreator.getWorkspace()),
                new DataListEntry.Dummy("MISC"));

        ComboBoxUtil.updateComboBoxContents(rangedItemType, ListUtils.merge(Collections.singleton("Default item"),
                mcreator.getWorkspace().getModElements().stream()
                        .filter(var -> var.getType() == ModElementType.PROJECTILE).map(ModElement::getName)
                        .collect(Collectors.toList())), "Default item");

        ComboBoxUtil.updateComboBoxContents(guiBoundTo, ListUtils.merge(Collections.singleton("<NONE>"),
                mcreator.getWorkspace().getModElements().stream().filter(var -> var.getType() == ModElementType.GUI)
                        .map(ModElement::getName).collect(Collectors.toList())), "<NONE>");

        disableMobModelCheckBoxListener = false;

        ComboBoxUtil.updateComboBoxContents(this.geoModel, ListUtils.merge(Collections.singleton(""), (Collection)PluginModelActions.getGeomodels(this.mcreator).stream().map(File::getName).filter((s) -> {
            return s.endsWith(".geo.json");
        }).collect(Collectors.toList())), "");
    }

    @Override protected AggregatedValidationResult validatePage(int page) {
        if (page == 0) {
            return new AggregatedValidationResult(mobModelTexture, mobName, geoModel, animation1);
        } else if (page == 5) {
            if (hasErrors)
                return new AggregatedValidationResult.MULTIFAIL(compileNotesPanel.getCompileNotes().stream()
                        .map(compileNote -> "Living entity AI builder: " + compileNote.message())
                        .collect(Collectors.toList()));
        } else if (page == 6) {
            if ((int) minNumberOfMobsPerGroup.getValue() > (int) maxNumberOfMobsPerGroup.getValue()) {
                return new AggregatedValidationResult.FAIL("Minimal mob group size can't be bigger than maximal size");
            }
        }
        return new AggregatedValidationResult.PASS();
    }

    @Override public void openInEditingMode(AnimatedEntity livingEntity) {
        disableMobModelCheckBoxListener = true;
        this.geoModel.setSelectedItem(livingEntity.model);
        disableDeathRotation.setSelected(livingEntity.disableDeathRotation);
        deathTime.setValue(livingEntity.deathTime);
        //animation stuff
        animation1.setText(livingEntity.animation1);
        animation2.setText(livingEntity.animation2);
        animation3.setText(livingEntity.animation3);
        animation4.setText(livingEntity.animation4);
        animation5.setText(livingEntity.animation5);
        animation6.setText(livingEntity.animation6);
        animation7.setText(livingEntity.animation7);
        animation8.setText(livingEntity.animation8);
        animation9.setText(livingEntity.animation9);
        animation10.setText(livingEntity.animation10);
        enable2.setSelected(livingEntity.enable2);
        enable3.setSelected(livingEntity.enable3);
        enable4.setSelected(livingEntity.enable4);
        enable5.setSelected(livingEntity.enable5);
        enable6.setSelected(livingEntity.enable6);
        enable7.setSelected(livingEntity.enable7);
        enable8.setSelected(livingEntity.enable8);
        enable9.setSelected(livingEntity.enable9);
        enable10.setSelected(livingEntity.enable10);
        //
        finishedDying.setSelectedProcedure(livingEntity.finishedDying);
        headMovement.setSelected(livingEntity.headMovement);
        groupName.setText(livingEntity.groupName);
        lerp.setValue(livingEntity.lerp);
        eyeHeight.setSelected(livingEntity.eyeHeight);
        height.setValue(livingEntity.height);
        mobName.setText(livingEntity.mobName);
        mobModelTexture.setSelectedItem(livingEntity.mobModelTexture);
        mobModelGlowTexture.setSelectedItem(livingEntity.mobModelGlowTexture);
        visualScale.setSelectedProcedure(livingEntity.visualScale);
        boundingBoxScale.setSelectedProcedure(livingEntity.boundingBoxScale);
        solidBoundingBox.setSelectedProcedure(livingEntity.solidBoundingBox);
        mobSpawningType.setSelectedItem(livingEntity.mobSpawningType);
        rangedItemType.setSelectedItem(livingEntity.rangedItemType);
        spawnEggBaseColor.setColor(livingEntity.spawnEggBaseColor);
        spawnEggDotColor.setColor(livingEntity.spawnEggDotColor);
        mobLabel.setText(livingEntity.mobLabel);
        onStruckByLightning.setSelectedProcedure(livingEntity.onStruckByLightning);
        whenMobFalls.setSelectedProcedure(livingEntity.whenMobFalls);
        whenMobDies.setSelectedProcedure(livingEntity.whenMobDies);
        whenMobIsHurt.setSelectedProcedure(livingEntity.whenMobIsHurt);
        onRightClickedOn.setSelectedProcedure(livingEntity.onRightClickedOn);
        whenThisMobKillsAnother.setSelectedProcedure(livingEntity.whenThisMobKillsAnother);
        onMobTickUpdate.setSelectedProcedure(livingEntity.onMobTickUpdate);
        onPlayerCollidesWith.setSelectedProcedure(livingEntity.onPlayerCollidesWith);
        onInitialSpawn.setSelectedProcedure(livingEntity.onInitialSpawn);
        mobBehaviourType.setSelectedItem(livingEntity.mobBehaviourType);
        mobCreatureType.setSelectedItem(livingEntity.mobCreatureType);
        attackStrength.setValue(livingEntity.attackStrength);
        attackKnockback.setValue(livingEntity.attackKnockback);
        knockbackResistance.setValue(livingEntity.knockbackResistance);
        movementSpeed.setValue(livingEntity.movementSpeed);
        mobDrop.setBlock(livingEntity.mobDrop);
        equipmentMainHand.setBlock(livingEntity.equipmentMainHand);
        equipmentHelmet.setBlock(livingEntity.equipmentHelmet);
        equipmentBody.setBlock(livingEntity.equipmentBody);
        equipmentLeggings.setBlock(livingEntity.equipmentLeggings);
        equipmentBoots.setBlock(livingEntity.equipmentBoots);
        health.setValue(livingEntity.health);
        trackingRange.setValue(livingEntity.trackingRange);
        followRange.setValue(livingEntity.followRange);
        immuneToFire.setSelected(livingEntity.immuneToFire);
        immuneToArrows.setSelected(livingEntity.immuneToArrows);
        immuneToFallDamage.setSelected(livingEntity.immuneToFallDamage);
        immuneToCactus.setSelected(livingEntity.immuneToCactus);
        immuneToDrowning.setSelected(livingEntity.immuneToDrowning);
        immuneToLightning.setSelected(livingEntity.immuneToLightning);
        immuneToPotions.setSelected(livingEntity.immuneToPotions);
        immuneToPlayer.setSelected(livingEntity.immuneToPlayer);
        immuneToExplosion.setSelected(livingEntity.immuneToExplosion);
        immuneToTrident.setSelected(livingEntity.immuneToTrident);
        immuneToAnvil.setSelected(livingEntity.immuneToAnvil);
        immuneToWither.setSelected(livingEntity.immuneToWither);
        immuneToDragonBreath.setSelected(livingEntity.immuneToDragonBreath);
        xpAmount.setValue(livingEntity.xpAmount);
        livingSound.setSound(livingEntity.livingSound);
        hurtSound.setSound(livingEntity.hurtSound);
        deathSound.setSound(livingEntity.deathSound);
        stepSound.setSound(livingEntity.stepSound);
        hasAI.setSelected(livingEntity.hasAI);
        isBoss.setSelected(livingEntity.isBoss);
        hasSpawnEgg.setSelected(livingEntity.hasSpawnEgg);
        disableCollisions.setSelected(livingEntity.disableCollisions);
        aiBase.setSelectedItem(livingEntity.aiBase);
        spawningProbability.setValue(livingEntity.spawningProbability);
        minNumberOfMobsPerGroup.setValue(livingEntity.minNumberOfMobsPerGroup);
        maxNumberOfMobsPerGroup.setValue(livingEntity.maxNumberOfMobsPerGroup);
        spawnInDungeons.setSelected(livingEntity.spawnInDungeons);
        restrictionBiomes.setListElements(livingEntity.restrictionBiomes);
        spawningCondition.setSelectedProcedure(livingEntity.spawningCondition);
        breedTriggerItems.setListElements(livingEntity.breedTriggerItems);
        bossBarColor.setSelectedItem(livingEntity.bossBarColor);
        bossBarType.setSelectedItem(livingEntity.bossBarType);
        equipmentOffHand.setBlock(livingEntity.equipmentOffHand);
        ridable.setSelected(livingEntity.ridable);
        canControlStrafe.setSelected(livingEntity.canControlStrafe);
        canControlForward.setSelected(livingEntity.canControlForward);
        breedable.setSelected(livingEntity.breedable);
        tameable.setSelected(livingEntity.tameable);
        ranged.setSelected(livingEntity.ranged);
        rangedAttackItem.setBlock(livingEntity.rangedAttackItem);
        rangedAttackInterval.setValue(livingEntity.rangedAttackInterval);
        rangedAttackRadius.setValue(livingEntity.rangedAttackRadius);
        spawnThisMob.setSelected(livingEntity.spawnThisMob);
        doesDespawnWhenIdle.setSelected(livingEntity.doesDespawnWhenIdle);
        modelWidth.setValue(livingEntity.modelWidth);
        modelHeight.setValue(livingEntity.modelHeight);
        mountedYOffset.setValue(livingEntity.mountedYOffset);
        modelShadowSize.setValue(livingEntity.modelShadowSize);
        armorBaseValue.setValue(livingEntity.armorBaseValue);
        waterMob.setSelected(livingEntity.waterMob);
        flyingMob.setSelected(livingEntity.flyingMob);
        guiBoundTo.setSelectedItem(livingEntity.guiBoundTo);
        inventorySize.setValue(livingEntity.inventorySize);
        inventoryStackSize.setValue(livingEntity.inventoryStackSize);
        entityDataList.setEntries(livingEntity.entityDataEntries);

        if (livingEntity.creativeTab != null)
            creativeTab.setSelectedItem(livingEntity.creativeTab);

        blocklyPanel.setXMLDataOnly(livingEntity.aixml);
        blocklyPanel.addTaskToRunAfterLoaded(() -> {
            blocklyPanel.clearWorkspace();
            blocklyPanel.setXML(livingEntity.aixml);
            regenerateAITasks();
        });

        if (breedable.isSelected()) {
            hasAI.setSelected(true);
            hasAI.setEnabled(false);
            this.breedTriggerItems.setEnabled(true);
            this.tameable.setEnabled(true);
        } else {
            hasAI.setEnabled(true);
            this.breedTriggerItems.setEnabled(false);
            this.tameable.setEnabled(false);
        }

        bossBarColor.setEnabled(isBoss.isSelected());
        bossBarType.setEnabled(isBoss.isSelected());

        rangedAttackItem.setEnabled("Default item".equals(rangedItemType.getSelectedItem()));

        disableMobModelCheckBoxListener = false;

        //these are here just in case, ok??
        animation2.setEnabled(enable2.isSelected());
        animation3.setEnabled(enable3.isSelected());
        animation4.setEnabled(enable4.isSelected());
        animation5.setEnabled(enable5.isSelected());
        animation6.setEnabled(enable6.isSelected());
        animation7.setEnabled(enable7.isSelected());
        animation8.setEnabled(enable8.isSelected());
        animation9.setEnabled(enable9.isSelected());
        animation10.setEnabled(enable10.isSelected());

        groupName.setEnabled(headMovement.isSelected());
        height.setEnabled(eyeHeight.isSelected());

    }

    @Override public AnimatedEntity getElementFromGUI() {
        AnimatedEntity livingEntity = new AnimatedEntity(modElement);
        livingEntity.model = (String)this.geoModel.getSelectedItem();
        livingEntity.disableDeathRotation = disableDeathRotation.isSelected();
        livingEntity.deathTime = (int) deathTime.getValue();
        //animation stuff
        livingEntity.animation1 = animation1.getText();
        livingEntity.animation2 = animation2.getText();
        livingEntity.animation3 = animation3.getText();
        livingEntity.animation4 = animation4.getText();
        livingEntity.animation5 = animation5.getText();
        livingEntity.animation6 = animation6.getText();
        livingEntity.animation7 = animation7.getText();
        livingEntity.animation8 = animation8.getText();
        livingEntity.animation9 = animation9.getText();
        livingEntity.animation10 = animation10.getText();
        livingEntity.enable2 = enable2.isSelected();
        livingEntity.enable3 = enable3.isSelected();
        livingEntity.enable4 = enable4.isSelected();
        livingEntity.enable5 = enable5.isSelected();
        livingEntity.enable6 = enable6.isSelected();
        livingEntity.enable7 = enable7.isSelected();
        livingEntity.enable8 = enable8.isSelected();
        livingEntity.enable9 = enable9.isSelected();
        livingEntity.enable10 = enable10.isSelected();
        //
        livingEntity.finishedDying = finishedDying.getSelectedProcedure();
        livingEntity.headMovement = headMovement.isSelected();
        livingEntity.groupName = groupName.getText();
        livingEntity.lerp = (int) lerp.getValue();
        livingEntity.eyeHeight = eyeHeight.isSelected();
        livingEntity.height = (double) height.getValue();
        livingEntity.mobName = mobName.getText();
        livingEntity.mobLabel = mobLabel.getText();
        livingEntity.mobModelTexture = mobModelTexture.getSelectedItem();
        livingEntity.mobModelGlowTexture = mobModelGlowTexture.getSelectedItem();
        livingEntity.spawnEggBaseColor = spawnEggBaseColor.getColor();
        livingEntity.visualScale = visualScale.getSelectedProcedure();
        livingEntity.boundingBoxScale = boundingBoxScale.getSelectedProcedure();
        livingEntity.solidBoundingBox = solidBoundingBox.getSelectedProcedure();
        livingEntity.spawnEggDotColor = spawnEggDotColor.getColor();
        livingEntity.hasSpawnEgg = hasSpawnEgg.isSelected();
        livingEntity.disableCollisions = disableCollisions.isSelected();
        livingEntity.isBoss = isBoss.isSelected();
        livingEntity.bossBarColor = (String) bossBarColor.getSelectedItem();
        livingEntity.bossBarType = (String) bossBarType.getSelectedItem();
        livingEntity.equipmentMainHand = equipmentMainHand.getBlock();
        livingEntity.equipmentOffHand = equipmentOffHand.getBlock();
        livingEntity.equipmentHelmet = equipmentHelmet.getBlock();
        livingEntity.equipmentBody = equipmentBody.getBlock();
        livingEntity.equipmentLeggings = equipmentLeggings.getBlock();
        livingEntity.equipmentBoots = equipmentBoots.getBlock();
        livingEntity.mobBehaviourType = (String) mobBehaviourType.getSelectedItem();
        livingEntity.mobCreatureType = (String) mobCreatureType.getSelectedItem();
        livingEntity.attackStrength = (int) attackStrength.getValue();
        livingEntity.attackKnockback = (double) attackKnockback.getValue();
        livingEntity.knockbackResistance = (double) knockbackResistance.getValue();
        livingEntity.movementSpeed = (double) movementSpeed.getValue();
        livingEntity.health = (int) health.getValue();
        livingEntity.trackingRange = (int) trackingRange.getValue();
        livingEntity.followRange = (int) followRange.getValue();
        livingEntity.immuneToFire = immuneToFire.isSelected();
        livingEntity.immuneToArrows = immuneToArrows.isSelected();
        livingEntity.immuneToFallDamage = immuneToFallDamage.isSelected();
        livingEntity.immuneToCactus = immuneToCactus.isSelected();
        livingEntity.immuneToDrowning = immuneToDrowning.isSelected();
        livingEntity.immuneToLightning = immuneToLightning.isSelected();
        livingEntity.immuneToPotions = immuneToPotions.isSelected();
        livingEntity.immuneToPlayer = immuneToPlayer.isSelected();
        livingEntity.immuneToExplosion = immuneToExplosion.isSelected();
        livingEntity.immuneToTrident = immuneToTrident.isSelected();
        livingEntity.immuneToAnvil = immuneToAnvil.isSelected();
        livingEntity.immuneToWither = immuneToWither.isSelected();
        livingEntity.immuneToDragonBreath = immuneToDragonBreath.isSelected();
        livingEntity.xpAmount = (int) xpAmount.getValue();
        livingEntity.ridable = ridable.isSelected();
        livingEntity.canControlForward = canControlForward.isSelected();
        livingEntity.canControlStrafe = canControlStrafe.isSelected();
        livingEntity.mobDrop = mobDrop.getBlock();
        livingEntity.livingSound = livingSound.getSound();
        livingEntity.hurtSound = hurtSound.getSound();
        livingEntity.deathSound = deathSound.getSound();
        livingEntity.stepSound = stepSound.getSound();
        livingEntity.spawningCondition = spawningCondition.getSelectedProcedure();
        livingEntity.onStruckByLightning = onStruckByLightning.getSelectedProcedure();
        livingEntity.whenMobFalls = whenMobFalls.getSelectedProcedure();
        livingEntity.whenMobDies = whenMobDies.getSelectedProcedure();
        livingEntity.whenMobIsHurt = whenMobIsHurt.getSelectedProcedure();
        livingEntity.onRightClickedOn = onRightClickedOn.getSelectedProcedure();
        livingEntity.whenThisMobKillsAnother = whenThisMobKillsAnother.getSelectedProcedure();
        livingEntity.onMobTickUpdate = onMobTickUpdate.getSelectedProcedure();
        livingEntity.onPlayerCollidesWith = onPlayerCollidesWith.getSelectedProcedure();
        livingEntity.onInitialSpawn = onInitialSpawn.getSelectedProcedure();
        livingEntity.hasAI = hasAI.isSelected();
        livingEntity.aiBase = (String) aiBase.getSelectedItem();
        livingEntity.aixml = blocklyPanel.getXML();
        livingEntity.breedable = breedable.isSelected();
        livingEntity.tameable = tameable.isSelected();
        livingEntity.breedTriggerItems = breedTriggerItems.getListElements();
        livingEntity.ranged = ranged.isSelected();
        livingEntity.rangedAttackItem = rangedAttackItem.getBlock();
        livingEntity.rangedAttackInterval = (int) rangedAttackInterval.getValue();
        livingEntity.rangedAttackRadius = (double) rangedAttackRadius.getValue();
        livingEntity.spawnThisMob = spawnThisMob.isSelected();
        livingEntity.doesDespawnWhenIdle = doesDespawnWhenIdle.isSelected();
        livingEntity.spawningProbability = (int) spawningProbability.getValue();
        livingEntity.mobSpawningType = (String) mobSpawningType.getSelectedItem();
        livingEntity.rangedItemType = (String) rangedItemType.getSelectedItem();
        livingEntity.minNumberOfMobsPerGroup = (int) minNumberOfMobsPerGroup.getValue();
        livingEntity.maxNumberOfMobsPerGroup = (int) maxNumberOfMobsPerGroup.getValue();
        livingEntity.restrictionBiomes = restrictionBiomes.getListElements();
        livingEntity.spawnInDungeons = spawnInDungeons.isSelected();
        livingEntity.modelWidth = (double) modelWidth.getValue();
        livingEntity.modelHeight = (double) modelHeight.getValue();
        livingEntity.mountedYOffset = (double) mountedYOffset.getValue();
        livingEntity.modelShadowSize = (double) modelShadowSize.getValue();
        livingEntity.armorBaseValue = (double) armorBaseValue.getValue();
        livingEntity.waterMob = waterMob.isSelected();
        livingEntity.flyingMob = flyingMob.isSelected();
        livingEntity.creativeTab = new TabEntry(mcreator.getWorkspace(), creativeTab.getSelectedItem());
        livingEntity.inventorySize = (int) inventorySize.getValue();
        livingEntity.inventoryStackSize = (int) inventoryStackSize.getValue();
        livingEntity.guiBoundTo = (String) guiBoundTo.getSelectedItem();
        livingEntity.entityDataEntries = entityDataList.getEntries();
        return livingEntity;
    }

    @Override
    public List<BlocklyPanel> getBlocklyPanels() {
        return List.of(this.blocklyPanel);
    }
}
