package net.nerdypuzzle.geckolib.element.types;

import net.mcreator.element.GeneratableElement;
import net.mcreator.element.parts.MItemBlock;
import net.mcreator.element.parts.TabEntry;
import net.mcreator.element.parts.procedure.Procedure;
import net.mcreator.element.parts.procedure.StringListProcedure;
import net.mcreator.element.types.interfaces.IItem;
import net.mcreator.element.types.interfaces.IItemWithModel;
import net.mcreator.element.types.interfaces.IItemWithTexture;
import net.mcreator.element.types.interfaces.ITabContainedElement;
import net.mcreator.minecraft.MCItem;
import net.mcreator.ui.workspace.resources.TextureType;
import net.mcreator.util.image.ImageUtils;
import net.mcreator.workspace.elements.ModElement;
import net.mcreator.workspace.resources.Model;
import net.mcreator.workspace.resources.TexturedModel;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnimatedItem extends GeneratableElement implements IItem, IItemWithModel, ITabContainedElement, IItemWithTexture {
    public int renderType;
    public String texture;
    public String customModelName;
    public String name;
    public String idle;
    public String rarity;
    public String displaySettings;
    public String leftArm;
    public String rightArm;
    public String perspective;
    public TabEntry creativeTab;
    public boolean firstPersonArms;
    public int stackSize;
    public int enchantability;
    public int useDuration;
    public double toolType;
    public int damageCount;
    public MItemBlock recipeRemainder;
    public boolean destroyAnyBlock;
    public boolean immuneToFire;
    public boolean stayInGridWhenCrafting;
    public boolean enableArmPose;
    public boolean damageOnCrafting;
    public boolean enableMeleeDamage;
    public double damageVsEntity;
    public StringListProcedure specialInformation;
    public boolean hasGlow;
    public Procedure glowCondition;
    public String guiBoundTo;
    public int inventorySize;
    public int inventoryStackSize;
    public Procedure onRightClickedInAir;
    public Procedure onRightClickedOnBlock;
    public Procedure onCrafted;
    public Procedure onEntityHitWith;
    public Procedure onItemInInventoryTick;
    public Procedure onItemInUseTick;
    public Procedure onStoppedUsing;
    public Procedure onEntitySwing;
    public Procedure onDroppedByPlayer;
    public Procedure onFinishUsingItem;
    public boolean isFood;
    public int nutritionalValue;
    public double saturation;
    public MItemBlock eatResultItem;
    public boolean isMeat;
    public boolean isAlwaysEdible;
    public String animation;
    public String normal;
    public List<ArmPoseEntry> armPoseList;

    private AnimatedItem() {
        this((ModElement)null);
    }

    public static class ArmPoseEntry {
        public String armHeld;
        public String arm;
        public String angle;
        public Number rotation;
        public boolean swings;
        public boolean followsHead;
    }

    public AnimatedItem(ModElement element) {
        super(element);
        this.rarity = "COMMON";
        this.inventorySize = 9;
        this.inventoryStackSize = 64;
        this.saturation = 0.30000001192092896;
        this.animation = "eat";
        this.armPoseList = new ArrayList<>();
    }

    public BufferedImage generateModElementPicture() {
        return ImageUtils.resizeAndCrop(this.getModElement().getFolderManager().getTextureImageIcon(this.texture, TextureType.ITEM).getImage(), 32);
    }

    @Override public List<MCItem> providedMCItems() {
        return List.of(new MCItem.Custom(this.getModElement(), null, "item"));
    }

    @Override public List<MCItem> getCreativeTabItems() {
        return providedMCItems();
    }

    public Model getItemModel() {
        Model.Type modelType = Model.Type.BUILTIN;
        if (this.renderType == 1) {
            modelType = Model.Type.JSON;
        } else if (this.renderType == 2) {
            modelType = Model.Type.OBJ;
        }

        return Model.getModelByParams(this.getModElement().getWorkspace(), this.customModelName, modelType);
    }

    public Map<String, String> getTextureMap() {
        Model model = this.getItemModel();
        return model instanceof TexturedModel && ((TexturedModel)model).getTextureMapping() != null ? ((TexturedModel)model).getTextureMapping().getTextureMap() : null;
    }

    public TabEntry getCreativeTab() {
        return this.creativeTab;
    }

    public String getTexture() {
        return this.texture;
    }

    public boolean hasNormalModel() {
        return this.getItemModel().getType() == Model.Type.BUILTIN && this.getItemModel().getReadableName().equals("Normal");
    }

    public boolean hasToolModel() {
        return this.getItemModel().getType() == Model.Type.BUILTIN && this.getItemModel().getReadableName().equals("Tool");
    }

    public boolean hasInventory() {
        return this.guiBoundTo != null && !this.guiBoundTo.isEmpty() && !this.guiBoundTo.equals("<NONE>");
    }

    public boolean hasNonDefaultAnimation() {
        return this.isFood ? !this.animation.equals("eat") : !this.animation.equals("none");
    }

    public boolean hasEatResultItem() {
        return this.isFood && this.eatResultItem != null && !this.eatResultItem.isEmpty();
    }
}