package net.nerdypuzzle.geckolib.parts;

import net.mcreator.element.ModElementType;
import net.mcreator.element.parts.procedure.NumberProcedure;
import net.mcreator.element.types.LivingEntity;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.action.BasicAction;
import net.mcreator.ui.component.util.ComboBoxUtil;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.dialogs.MCreatorDialog;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.init.UIRES;
import net.mcreator.util.ListUtils;
import net.mcreator.workspace.elements.FolderElement;
import net.mcreator.workspace.elements.ModElement;
import net.nerdypuzzle.geckolib.element.types.AnimatedEntity;
import net.nerdypuzzle.geckolib.registry.PluginActions;
import net.nerdypuzzle.geckolib.registry.PluginElementTypes;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class PluginDialogs {

    public static class Entity2GeckoLib {
        public Entity2GeckoLib(MCreator mcreator) {
        }

        public static void open(MCreator mcreator) {
            MCreatorDialog dialog = new MCreatorDialog(mcreator, L10N.t("dialogs.convert_to_geckolib", new Object[0]), true);
            dialog.setLayout(new BorderLayout());
            JPanel main = new JPanel(new BorderLayout(15, 15));

            JComboBox<String> regularEntities = new JComboBox<>();
            ComboBoxUtil.updateComboBoxContents(regularEntities, ListUtils.merge(Collections.singleton(""),
                    (Collection)mcreator.getWorkspace().getModElements().stream().map(ModElement::getName).filter((s) -> {
                return mcreator.getWorkspace().getModElementByName(s).getGeneratableElement() instanceof LivingEntity;
            }).collect(Collectors.toList())));
            main.add("Center", regularEntities);

            dialog.setTitle("Element converter menu");
            JButton ok = L10N.button("dialogs.convert_button", new Object[0]);
            JButton cancel = new JButton(UIManager.getString("OptionPane.cancelButtonText"));
            cancel.addActionListener((e) -> {
                dialog.setVisible(false);
            });
            dialog.getRootPane().setDefaultButton(ok);
            ok.addActionListener((e) -> {
                if (regularEntities.getSelectedItem() != null) {
                    LivingEntity entity = (LivingEntity) mcreator.getWorkspace().getModElementByName(regularEntities.getSelectedItem().toString()).getGeneratableElement();
                    if (entity instanceof LivingEntity) {
                        AnimatedEntity geckoElement = new AnimatedEntity(new ModElement(mcreator.getWorkspace(), entity.getModElement().getName(), PluginElementTypes.ANIMATEDENTITY));
                        geckoElement.mobName = entity.mobName;
                        geckoElement.mobLabel = entity.mobLabel;
                        geckoElement.mobModelTexture = entity.mobModelTexture;
                        geckoElement.mobModelGlowTexture = "";
                        geckoElement.visualScale = new NumberProcedure("Procedure1", 1);
                        geckoElement.boundingBoxScale = new NumberProcedure("Procedure2", 1);
                        geckoElement.modelWidth = entity.modelWidth;
                        geckoElement.modelHeight = entity.modelHeight;
                        geckoElement.modelShadowSize = entity.modelShadowSize;
                        geckoElement.mountedYOffset = entity.mountedYOffset;
                        geckoElement.hasSpawnEgg = entity.hasSpawnEgg;
                        geckoElement.spawnEggBaseColor = entity.spawnEggBaseColor;
                        geckoElement.spawnEggDotColor = entity.spawnEggDotColor;
                        geckoElement.creativeTab = entity.creativeTab;
                        geckoElement.isBoss = entity.isBoss;
                        geckoElement.bossBarColor = entity.bossBarColor;
                        geckoElement.bossBarType = entity.bossBarType;
                        geckoElement.equipmentMainHand = entity.equipmentMainHand;
                        geckoElement.equipmentOffHand = entity.equipmentOffHand;
                        geckoElement.equipmentHelmet = entity.equipmentHelmet;
                        geckoElement.equipmentBody = entity.equipmentBody;
                        geckoElement.equipmentLeggings = entity.equipmentLeggings;
                        geckoElement.equipmentBoots = entity.equipmentBoots;
                        geckoElement.mobBehaviourType = entity.mobBehaviourType;
                        geckoElement.mobCreatureType = entity.mobCreatureType;
                        geckoElement.attackStrength = entity.attackStrength;
                        geckoElement.attackKnockback = entity.attackKnockback;
                        geckoElement.knockbackResistance = entity.knockbackResistance;
                        geckoElement.movementSpeed = entity.movementSpeed;
                        geckoElement.armorBaseValue = entity.armorBaseValue;
                        geckoElement.trackingRange = entity.trackingRange;
                        geckoElement.followRange = entity.followRange;
                        geckoElement.health = entity.health;
                        geckoElement.xpAmount = entity.xpAmount;
                        geckoElement.waterMob = entity.waterMob;
                        geckoElement.flyingMob = entity.flyingMob;
                        geckoElement.guiBoundTo = entity.guiBoundTo;
                        geckoElement.inventorySize = entity.inventorySize;
                        geckoElement.inventoryStackSize = entity.inventoryStackSize;
                        geckoElement.deathTime = 20;
                        geckoElement.lerp = 4;
                        geckoElement.disableCollisions = entity.disableCollisions;
                        geckoElement.ridable = entity.ridable;
                        geckoElement.canControlForward = entity.canControlForward;
                        geckoElement.canControlStrafe = entity.canControlStrafe;
                        geckoElement.immuneToFire = entity.immuneToFire;
                        geckoElement.immuneToArrows = entity.immuneToArrows;
                        geckoElement.immuneToFallDamage = entity.immuneToFallDamage;
                        geckoElement.immuneToCactus = entity.immuneToCactus;
                        geckoElement.immuneToDrowning = entity.immuneToDrowning;
                        geckoElement.immuneToLightning = entity.immuneToLightning;
                        geckoElement.immuneToPotions = entity.immuneToPotions;
                        geckoElement.immuneToPlayer = entity.immuneToPlayer;
                        geckoElement.immuneToExplosion = entity.immuneToExplosion;
                        geckoElement.immuneToTrident = entity.immuneToTrident;
                        geckoElement.immuneToAnvil = entity.immuneToAnvil;
                        geckoElement.immuneToWither = entity.immuneToWither;
                        geckoElement.immuneToDragonBreath = entity.immuneToDragonBreath;
                        geckoElement.mobDrop = entity.mobDrop;
                        geckoElement.livingSound = entity.livingSound;
                        geckoElement.hurtSound = entity.hurtSound;
                        geckoElement.deathSound = entity.deathSound;
                        geckoElement.stepSound = entity.stepSound;
                        geckoElement.onStruckByLightning = entity.onStruckByLightning;
                        geckoElement.whenMobFalls = entity.whenMobFalls;
                        geckoElement.whenMobDies = entity.whenMobDies;
                        geckoElement.whenMobIsHurt = entity.whenMobIsHurt;
                        geckoElement.onRightClickedOn = entity.onRightClickedOn;
                        geckoElement.whenThisMobKillsAnother = entity.whenThisMobKillsAnother;
                        geckoElement.onMobTickUpdate = entity.onMobTickUpdate;
                        geckoElement.onPlayerCollidesWith = entity.onPlayerCollidesWith;
                        geckoElement.onInitialSpawn = entity.onInitialSpawn;
                        geckoElement.hasAI = entity.hasAI;
                        geckoElement.aiBase = entity.aiBase;
                        geckoElement.aixml = entity.aixml;
                        geckoElement.animation1 = "idle";
                        geckoElement.breedable = entity.breedable;
                        geckoElement.tameable = entity.tameable;
                        geckoElement.breedTriggerItems = entity.breedTriggerItems;
                        geckoElement.ranged = entity.ranged;
                        geckoElement.rangedAttackItem = entity.rangedAttackItem;
                        geckoElement.rangedItemType = entity.rangedItemType;
                        geckoElement.rangedAttackInterval = entity.rangedAttackInterval;
                        geckoElement.rangedAttackRadius = entity.rangedAttackRadius;
                        geckoElement.spawnThisMob = entity.spawnThisMob;
                        geckoElement.doesDespawnWhenIdle = entity.doesDespawnWhenIdle;
                        geckoElement.spawningCondition = entity.spawningCondition;
                        geckoElement.spawningProbability = entity.spawningProbability;
                        geckoElement.mobSpawningType = entity.mobSpawningType;
                        geckoElement.minNumberOfMobsPerGroup = entity.minNumberOfMobsPerGroup;
                        geckoElement.maxNumberOfMobsPerGroup = entity.maxNumberOfMobsPerGroup;
                        geckoElement.restrictionBiomes = entity.restrictionBiomes;
                        geckoElement.spawnInDungeons = entity.spawnInDungeons;
                        geckoElement.finalizeModElementGeneration();
                        mcreator.getWorkspace().removeModElement(entity.getModElement());
                        dialog.setVisible(false);
                        geckoElement.getModElement().setParentFolder(FolderElement.dummyFromPath(entity.getModElement().getFolderPath()));
                        mcreator.getWorkspace().getModElementManager().storeModElementPicture(geckoElement);
                        mcreator.getWorkspace().addModElement(geckoElement.getModElement());
                        mcreator.getWorkspace().getGenerator().generateElement(geckoElement);
                        mcreator.getWorkspace().getModElementManager().storeModElement(geckoElement);
                    }
                    else
                        dialog.setVisible(false);
                } else
                    dialog.setVisible(false);
            });

            main.add("South", PanelUtils.join(ok, cancel));
            dialog.add("Center", main);
            dialog.setSize(400, 100);
            dialog.setLocationRelativeTo(mcreator);
            dialog.setVisible(true);
        }

        public static BasicAction getAction(PluginActions actionRegistry) {
            return (new BasicAction(actionRegistry, L10N.t("dialogs.convert_to_geckolib", new Object[0]), (e) -> {
                open(actionRegistry.getMCreator());
            }) {
                public boolean isEnabled() {
                    return true;
                }
            }).setIcon(UIRES.get("16px.editorder"));
        }
    }

    public static class GeckoLib2Entity {
        public GeckoLib2Entity(MCreator mcreator) {
        }

        public static void open(MCreator mcreator) {
            MCreatorDialog dialog = new MCreatorDialog(mcreator, L10N.t("dialogs.convert_to_geckolib", new Object[0]), true);
            dialog.setLayout(new BorderLayout());
            JPanel main = new JPanel(new BorderLayout(15, 15));

            JComboBox<String> animatedEntities = new JComboBox<>();
            ComboBoxUtil.updateComboBoxContents(animatedEntities, ListUtils.merge(Collections.singleton(""),
                    (Collection)mcreator.getWorkspace().getModElements().stream().map(ModElement::getName).filter((s) -> {
                        return mcreator.getWorkspace().getModElementByName(s).getGeneratableElement() instanceof AnimatedEntity;
                    }).collect(Collectors.toList())));
            main.add("Center", animatedEntities);

            dialog.setTitle("Element converter menu");
            JButton ok = L10N.button("dialogs.convert_from_button", new Object[0]);
            JButton cancel = new JButton(UIManager.getString("OptionPane.cancelButtonText"));
            cancel.addActionListener((e) -> {
                dialog.setVisible(false);
            });
            dialog.getRootPane().setDefaultButton(ok);
            ok.addActionListener((e) -> {
                if (animatedEntities.getSelectedItem() != null) {
                    AnimatedEntity entity = (AnimatedEntity) mcreator.getWorkspace().getModElementByName(animatedEntities.getSelectedItem().toString()).getGeneratableElement();
                    if (entity instanceof AnimatedEntity) {
                        LivingEntity geckoElement = new LivingEntity(new ModElement(mcreator.getWorkspace(), entity.getModElement().getName(), ModElementType.LIVINGENTITY));
                        geckoElement.mobName = entity.mobName;
                        geckoElement.mobLabel = entity.mobLabel;
                        geckoElement.mobModelTexture = entity.mobModelTexture;
                        geckoElement.modelWidth = entity.modelWidth;
                        geckoElement.modelHeight = entity.modelHeight;
                        geckoElement.modelShadowSize = entity.modelShadowSize;
                        geckoElement.mountedYOffset = entity.mountedYOffset;
                        geckoElement.hasSpawnEgg = entity.hasSpawnEgg;
                        geckoElement.spawnEggBaseColor = entity.spawnEggBaseColor;
                        geckoElement.spawnEggDotColor = entity.spawnEggDotColor;
                        geckoElement.creativeTab = entity.creativeTab;
                        geckoElement.isBoss = entity.isBoss;
                        geckoElement.bossBarColor = entity.bossBarColor;
                        geckoElement.bossBarType = entity.bossBarType;
                        geckoElement.equipmentMainHand = entity.equipmentMainHand;
                        geckoElement.equipmentOffHand = entity.equipmentOffHand;
                        geckoElement.equipmentHelmet = entity.equipmentHelmet;
                        geckoElement.equipmentBody = entity.equipmentBody;
                        geckoElement.equipmentLeggings = entity.equipmentLeggings;
                        geckoElement.equipmentBoots = entity.equipmentBoots;
                        geckoElement.mobBehaviourType = entity.mobBehaviourType;
                        geckoElement.mobCreatureType = entity.mobCreatureType;
                        geckoElement.attackStrength = entity.attackStrength;
                        geckoElement.attackKnockback = entity.attackKnockback;
                        geckoElement.knockbackResistance = entity.knockbackResistance;
                        geckoElement.movementSpeed = entity.movementSpeed;
                        geckoElement.armorBaseValue = entity.armorBaseValue;
                        geckoElement.trackingRange = entity.trackingRange;
                        geckoElement.followRange = entity.followRange;
                        geckoElement.health = entity.health;
                        geckoElement.xpAmount = entity.xpAmount;
                        geckoElement.waterMob = entity.waterMob;
                        geckoElement.flyingMob = entity.flyingMob;
                        geckoElement.guiBoundTo = entity.guiBoundTo;
                        geckoElement.inventorySize = entity.inventorySize;
                        geckoElement.inventoryStackSize = entity.inventoryStackSize;
                        geckoElement.disableCollisions = entity.disableCollisions;
                        geckoElement.ridable = entity.ridable;
                        geckoElement.canControlForward = entity.canControlForward;
                        geckoElement.canControlStrafe = entity.canControlStrafe;
                        geckoElement.immuneToFire = entity.immuneToFire;
                        geckoElement.immuneToArrows = entity.immuneToArrows;
                        geckoElement.immuneToFallDamage = entity.immuneToFallDamage;
                        geckoElement.immuneToCactus = entity.immuneToCactus;
                        geckoElement.immuneToDrowning = entity.immuneToDrowning;
                        geckoElement.immuneToLightning = entity.immuneToLightning;
                        geckoElement.immuneToPotions = entity.immuneToPotions;
                        geckoElement.immuneToPlayer = entity.immuneToPlayer;
                        geckoElement.immuneToExplosion = entity.immuneToExplosion;
                        geckoElement.immuneToTrident = entity.immuneToTrident;
                        geckoElement.immuneToAnvil = entity.immuneToAnvil;
                        geckoElement.immuneToWither = entity.immuneToWither;
                        geckoElement.immuneToDragonBreath = entity.immuneToDragonBreath;
                        geckoElement.mobDrop = entity.mobDrop;
                        geckoElement.livingSound = entity.livingSound;
                        geckoElement.hurtSound = entity.hurtSound;
                        geckoElement.deathSound = entity.deathSound;
                        geckoElement.stepSound = entity.stepSound;
                        geckoElement.onStruckByLightning = entity.onStruckByLightning;
                        geckoElement.whenMobFalls = entity.whenMobFalls;
                        geckoElement.whenMobDies = entity.whenMobDies;
                        geckoElement.whenMobIsHurt = entity.whenMobIsHurt;
                        geckoElement.onRightClickedOn = entity.onRightClickedOn;
                        geckoElement.whenThisMobKillsAnother = entity.whenThisMobKillsAnother;
                        geckoElement.onMobTickUpdate = entity.onMobTickUpdate;
                        geckoElement.onPlayerCollidesWith = entity.onPlayerCollidesWith;
                        geckoElement.onInitialSpawn = entity.onInitialSpawn;
                        geckoElement.hasAI = entity.hasAI;
                        geckoElement.aiBase = entity.aiBase;
                        geckoElement.aixml = entity.aixml;
                        geckoElement.breedable = entity.breedable;
                        geckoElement.tameable = entity.tameable;
                        geckoElement.breedTriggerItems = entity.breedTriggerItems;
                        geckoElement.ranged = entity.ranged;
                        geckoElement.rangedAttackItem = entity.rangedAttackItem;
                        geckoElement.rangedItemType = entity.rangedItemType;
                        geckoElement.rangedAttackInterval = entity.rangedAttackInterval;
                        geckoElement.rangedAttackRadius = entity.rangedAttackRadius;
                        geckoElement.spawnThisMob = entity.spawnThisMob;
                        geckoElement.doesDespawnWhenIdle = entity.doesDespawnWhenIdle;
                        geckoElement.spawningCondition = entity.spawningCondition;
                        geckoElement.spawningProbability = entity.spawningProbability;
                        geckoElement.mobSpawningType = entity.mobSpawningType;
                        geckoElement.minNumberOfMobsPerGroup = entity.minNumberOfMobsPerGroup;
                        geckoElement.maxNumberOfMobsPerGroup = entity.maxNumberOfMobsPerGroup;
                        geckoElement.restrictionBiomes = entity.restrictionBiomes;
                        geckoElement.spawnInDungeons = entity.spawnInDungeons;
                        geckoElement.mobModelName = "Biped";
                        geckoElement.finalizeModElementGeneration();
                        mcreator.getWorkspace().removeModElement(entity.getModElement());
                        dialog.setVisible(false);
                        geckoElement.getModElement().setParentFolder(FolderElement.dummyFromPath(entity.getModElement().getFolderPath()));
                        mcreator.getWorkspace().getModElementManager().storeModElementPicture(geckoElement);
                        mcreator.getWorkspace().addModElement(geckoElement.getModElement());
                        mcreator.getWorkspace().getGenerator().generateElement(geckoElement);
                        mcreator.getWorkspace().getModElementManager().storeModElement(geckoElement);
                    }
                    else
                        dialog.setVisible(false);
                } else
                    dialog.setVisible(false);
            });

            main.add("South", PanelUtils.join(ok, cancel));
            dialog.add("Center", main);
            dialog.setSize(400, 100);
            dialog.setLocationRelativeTo(mcreator);
            dialog.setVisible(true);
        }

        public static BasicAction getAction(PluginActions actionRegistry) {
            return (new BasicAction(actionRegistry, L10N.t("dialogs.convert_from_geckolib", new Object[0]), (e) -> {
                open(actionRegistry.getMCreator());
            }) {
                public boolean isEnabled() {
                    return true;
                }
            }).setIcon(UIRES.get("16px.editorder"));
        }
    }
}
