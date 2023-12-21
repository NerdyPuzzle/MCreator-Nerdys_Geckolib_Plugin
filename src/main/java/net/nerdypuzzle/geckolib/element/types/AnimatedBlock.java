package net.nerdypuzzle.geckolib.element.types;

import net.mcreator.element.BaseType;
import net.mcreator.element.GeneratableElement;
import net.mcreator.element.parts.*;
import net.mcreator.element.parts.procedure.NumberProcedure;
import net.mcreator.element.parts.procedure.Procedure;
import net.mcreator.element.parts.procedure.StringListProcedure;
import net.mcreator.element.types.interfaces.IBlock;
import net.mcreator.element.types.interfaces.IBlockWithBoundingBox;
import net.mcreator.element.types.interfaces.ITabContainedElement;
import net.mcreator.generator.GeneratorFlavor;
import net.mcreator.minecraft.MCItem;
import net.mcreator.minecraft.MinecraftImageGenerator;
import net.mcreator.ui.workspace.resources.TextureType;
import net.mcreator.util.image.ImageUtils;
import net.mcreator.workspace.elements.ModElement;
import net.mcreator.workspace.references.ModElementReference;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class AnimatedBlock extends GeneratableElement implements IBlock, ITabContainedElement, IBlockWithBoundingBox {
    public String texture;
    public String textureTop;
    public String textureLeft;
    public String textureFront;
    public String textureRight;
    public String textureBack;
    public int renderType;
    public int rotationMode;
    public boolean enablePitch;
    public boolean emissiveRendering;
    public boolean displayFluidOverlay;
    public String itemTexture;
    public String particleTexture;
    public String blockBase;
    public String tintType;
    public boolean isItemTinted;
    public boolean hasTransparency;
    public boolean connectedSides;
    public String transparencyType;
    public boolean disableOffset;
    public List<BoxEntry> boundingBoxes;
    public String name;
    public StringListProcedure specialInformation;
    public double hardness;
    public double resistance;
    public boolean hasGravity;
    public boolean isWaterloggable;
    public TabEntry creativeTab;
    public String destroyTool;
    public MItemBlock customDrop;
    public int dropAmount;
    public boolean useLootTableForDrops;
    public boolean requiresCorrectTool;
    public double enchantPowerBonus;
    public boolean plantsGrowOn;
    public boolean canRedstoneConnect;
    public int lightOpacity;
    public Material material;
    public int tickRate;
    public boolean tickRandomly;
    public boolean isReplaceable;
    public boolean canProvidePower;
    public NumberProcedure emittedRedstonePower;
    public String colorOnMap;
    public MItemBlock creativePickItem;
    public String offsetType;
    public String aiPathNodeType;
    public Color beaconColorModifier;
    public int flammability;
    public int fireSpreadSpeed;
    public boolean isLadder;
    public double slipperiness;
    public double speedFactor;
    public double jumpFactor;
    public Number animationCount;
    public String reactionToPushing;
    public boolean isNotColidable;
    public boolean isCustomSoundType;
    public StepSound soundOnStep;
    public Sound breakSound;
    public Sound fallSound;
    public Sound hitSound;
    public Sound placeSound;
    public Sound stepSound;
    public int luminance;
    public boolean unbreakable;
    public int breakHarvestLevel;
    public boolean spawnParticles;
    public Particle particleToSpawn;
    public String particleSpawningShape;
    public double particleSpawningRadious;
    public int particleAmount;
    public Procedure particleCondition;
    public Procedure placingCondition;
    public boolean hasInventory;
    public String guiBoundTo;
    public boolean openGUIOnRightClick;
    public int inventorySize;
    public int inventoryStackSize;
    public boolean inventoryDropWhenDestroyed;
    public boolean inventoryComparatorPower;
    public boolean generateFeature;
    public List<Integer> inventoryOutSlotIDs;
    public List<Integer> inventoryInSlotIDs;
    public boolean hasEnergyStorage;
    public int energyInitial;
    public int energyCapacity;
    public int energyMaxReceive;
    public int energyMaxExtract;
    public boolean isFluidTank;
    public int fluidCapacity;
    public List<Fluid> fluidRestrictions;
    public Procedure onRightClicked;
    public Procedure onBlockAdded;
    public Procedure onNeighbourBlockChanges;
    public Procedure onTickUpdate;
    public Procedure onRandomUpdateEvent;
    public Procedure onDestroyedByPlayer;
    public Procedure onDestroyedByExplosion;
    public Procedure onStartToDestroy;
    public Procedure onEntityCollides;
    public Procedure onEntityWalksOn;
    public Procedure onBlockPlayedBy;
    public Procedure onRedstoneOn;
    public Procedure onRedstoneOff;
    public Procedure onHitByProjectile;

    public List<BiomeEntry> restrictionBiomes;
    @ModElementReference
    public List<MItemBlock> blocksToReplace;
    public String generationShape;
    public int frequencyPerChunks;
    public int frequencyOnChunk;
    public int minGenerateHeight;
    public int maxGenerateHeight;
    public Procedure generateCondition;
    public String normal;
    public String displaySettings;


    private AnimatedBlock() {
        this((ModElement)null);
    }

    public AnimatedBlock(ModElement element) {
        super(element);
        this.tintType = "No tint";
        this.boundingBoxes = new ArrayList();
        this.restrictionBiomes = new ArrayList();
        this.blocksToReplace = new ArrayList<>();
        this.reactionToPushing = "NORMAL";
        this.slipperiness = 0.6;
        this.speedFactor = 1.0;
        this.jumpFactor = 1.0;
        this.colorOnMap = "DEFAULT";
        this.aiPathNodeType = "DEFAULT";
        this.offsetType = "NONE";
        this.generationShape = "UNIFORM";
        this.inventoryInSlotIDs = new ArrayList();
        this.inventoryOutSlotIDs = new ArrayList();
        this.energyCapacity = 400000;
        this.energyMaxReceive = 200;
        this.energyMaxExtract = 200;
        this.fluidCapacity = 8000;
        this.animationCount = 1;
    }

    public int renderType() {
        return this.blockBase != null && !this.blockBase.equals("") ? -1 : this.renderType;
    }

    public boolean hasCustomDrop() {
        return !this.customDrop.isEmpty();
    }

    public boolean isBlockTinted() {
        return !"No tint".equals(this.tintType);
    }

    public boolean isDoubleBlock() {
        return "Door".equals(this.blockBase);
    }

    public boolean shouldOpenGUIOnRightClick() {
        return this.guiBoundTo != null && !this.guiBoundTo.equals("<NONE>") && this.openGUIOnRightClick;
    }

    public boolean shouldScheduleTick() {
        return this.tickRate > 0 && !this.tickRandomly;
    }

    public boolean shouldDisableOffset() {
        return this.disableOffset || this.offsetType.equals("NONE");
    }

    public boolean isFullCube() {
        return !"Stairs".equals(this.blockBase) && !"Slab".equals(this.blockBase) && !"Fence".equals(this.blockBase) && !"Wall".equals(this.blockBase) && !"TrapDoor".equals(this.blockBase) && !"Door".equals(this.blockBase) && !"FenceGate".equals(this.blockBase) && !"EndRod".equals(this.blockBase) && !"PressurePlate".equals(this.blockBase) && !"Button".equals(this.blockBase) ? IBlockWithBoundingBox.super.isFullCube() : false;
    }

    public TabEntry getCreativeTab() {
        return this.creativeTab;
    }

    @Nonnull
    public List<IBlockWithBoundingBox.BoxEntry> getValidBoundingBoxes() {
        return (List)this.boundingBoxes.stream().filter(IBlockWithBoundingBox.BoxEntry::isNotEmpty).collect(Collectors.toList());
    }

    @Override public List<MCItem> providedMCItems() {
        return List.of(new MCItem.Custom(this.getModElement(), null, "block"));
    }

    @Override public List<MCItem> getCreativeTabItems() {
        return providedMCItems();
    }

    public BufferedImage generateModElementPicture() {
        if (this.renderType() == 10) {
            return (BufferedImage) MinecraftImageGenerator.Preview.generateBlockIcon(this.getTextureWithFallback(this.textureTop), this.getTextureWithFallback(this.textureLeft), this.getTextureWithFallback(this.textureFront));
        } else if (this.renderType() == 11 || this.renderType() == 110 || this.blockBase != null && this.blockBase.equals("Leaves")) {
            return (BufferedImage) MinecraftImageGenerator.Preview.generateBlockIcon(this.getMainTexture(), this.getMainTexture(), this.getMainTexture());
        } else if (this.blockBase != null && this.blockBase.equals("Slab")) {
            return (BufferedImage) MinecraftImageGenerator.Preview.generateSlabIcon(this.getTextureWithFallback(this.textureTop), this.getTextureWithFallback(this.textureFront));
        } else if (this.blockBase != null && this.blockBase.equals("TrapDoor")) {
            return (BufferedImage) MinecraftImageGenerator.Preview.generateTrapdoorIcon(this.getMainTexture());
        } else if (this.blockBase != null && this.blockBase.equals("Stairs")) {
            return (BufferedImage) MinecraftImageGenerator.Preview.generateStairsIcon(this.getTextureWithFallback(this.textureTop), this.getTextureWithFallback(this.textureFront));
        } else if (this.blockBase != null && this.blockBase.equals("Wall")) {
            return (BufferedImage) MinecraftImageGenerator.Preview.generateWallIcon(this.getMainTexture());
        } else if (this.blockBase != null && this.blockBase.equals("Fence")) {
            return (BufferedImage) MinecraftImageGenerator.Preview.generateFenceIcon(this.getMainTexture());
        } else if (this.blockBase != null && this.blockBase.equals("FenceGate")) {
            return (BufferedImage) MinecraftImageGenerator.Preview.generateFenceGateIcon(this.getMainTexture());
        } else if (this.blockBase != null && this.blockBase.equals("EndRod")) {
            return (BufferedImage) MinecraftImageGenerator.Preview.generateEndRodIcon(this.getMainTexture());
        } else if (this.blockBase != null && this.blockBase.equals("PressurePlate")) {
            return (BufferedImage) MinecraftImageGenerator.Preview.generatePressurePlateIcon(this.getMainTexture());
        } else if (this.blockBase != null && this.blockBase.equals("Button")) {
            return (BufferedImage) MinecraftImageGenerator.Preview.generateButtonIcon(this.getMainTexture());
        } else if (this.renderType() == 14) {
            Image side = ImageUtils.drawOver(new ImageIcon(this.getTextureWithFallback(this.textureFront)), new ImageIcon(this.getTextureWithFallback(this.textureLeft))).getImage();
            return (BufferedImage) MinecraftImageGenerator.Preview.generateBlockIcon(this.getTextureWithFallback(this.textureTop), side, side);
        } else {
            return ImageUtils.resizeAndCrop(this.getMainTexture(), 32);
        }
    }

    private Image getMainTexture() {
        return this.getModElement().getFolderManager().getTextureImageIcon(this.texture, TextureType.BLOCK).getImage();
    }

    private Image getTextureWithFallback(String textureName) {
        return textureName.equals("") ? this.getMainTexture() : this.getModElement().getFolderManager().getTextureImageIcon(textureName, TextureType.BLOCK).getImage();
    }

    public String getRenderType() {
        return this.hasTransparency && this.transparencyType.equals("solid") ? "cutout" : this.transparencyType.toLowerCase(Locale.ENGLISH);
    }

    public Collection<BaseType> getBaseTypesProvided() {
        List<BaseType> baseTypes = new ArrayList(List.of(BaseType.BLOCK, BaseType.ITEM, BaseType.BLOCKENTITY));
        if (generateFeature && getModElement().getGenerator().getGeneratorConfiguration().getGeneratorFlavor()
                == GeneratorFlavor.FABRIC) {
            baseTypes.add(BaseType.FEATURE);
        }

        return baseTypes;
    }
}