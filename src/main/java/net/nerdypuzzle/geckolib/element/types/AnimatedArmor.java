package net.nerdypuzzle.geckolib.element.types;

import net.mcreator.element.GeneratableElement;
import net.mcreator.element.parts.MItemBlock;
import net.mcreator.element.parts.Sound;
import net.mcreator.element.parts.TabEntry;
import net.mcreator.element.parts.procedure.Procedure;
import net.mcreator.element.types.interfaces.IItem;
import net.mcreator.element.types.interfaces.ITabContainedElement;
import net.mcreator.minecraft.MCItem;
import net.mcreator.minecraft.MinecraftImageGenerator;
import net.mcreator.ui.workspace.resources.TextureType;
import net.mcreator.workspace.elements.ModElement;
import net.mcreator.workspace.resources.Model;
import net.mcreator.workspace.resources.TexturedModel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

@SuppressWarnings("unused")
public class AnimatedArmor extends GeneratableElement implements IItem, ITabContainedElement {

    public boolean enableHelmet;
    public String textureHelmet;
    public boolean enableBody;
    public String textureBody;
    public boolean enableLeggings;
    public String textureLeggings;
    public boolean enableBoots;
    public String textureBoots;

    public Procedure onHelmetTick;
    public Procedure onBodyTick;
    public Procedure onLeggingsTick;
    public Procedure onBootsTick;

    public TabEntry creativeTab;
    public String armorTextureFile;
    public String model;
    public String idle;
    public String head;
    public String chest;
    public String rightArm;
    public String leftArm;
    public String rightLeg;
    public String leftLeg;
    public String rightBoot;
    public String leftBoot;
    public String helmetName;
    public String bodyName;
    public String leggingsName;
    public String bootsName;

    public List<String> helmetSpecialInfo;
    public List<String> bodySpecialInfo;
    public List<String> leggingsSpecialInfo;
    public List<String> bootsSpecialInfo;

    public String helmetModelName;

    public String bodyModelName;

    public String leggingsModelName;

    public String bootsModelName;

    public int helmetItemRenderType;
    public String helmetItemCustomModelName;
    public int bodyItemRenderType;
    public String bodyItemCustomModelName;
    public int leggingsItemRenderType;
    public String leggingsItemCustomModelName;
    public int bootsItemRenderType;
    public String bootsItemCustomModelName;

    public boolean helmetImmuneToFire;
    public boolean bodyImmuneToFire;
    public boolean leggingsImmuneToFire;
    public boolean bootsImmuneToFire;
    public boolean fullyEquipped;

    public int maxDamage;
    public int damageValueHelmet;
    public int damageValueBody;
    public int damageValueLeggings;
    public int damageValueBoots;
    public int enchantability;
    public double toughness;
    public double knockbackResistance;
    public Sound equipSound;
    public List<MItemBlock> repairItems;

    private AnimatedArmor() {
        this(null);
    }

    public AnimatedArmor(ModElement element) {
        super(element);

        this.helmetItemRenderType = 0;
        this.helmetItemCustomModelName = "Normal";
        this.bodyItemRenderType = 0;
        this.bodyItemCustomModelName = "Normal";
        this.leggingsItemRenderType = 0;
        this.leggingsItemCustomModelName = "Normal";
        this.bootsItemRenderType = 0;
        this.bootsItemCustomModelName = "Normal";

        this.helmetSpecialInfo = new ArrayList<>();
        this.bodySpecialInfo = new ArrayList<>();
        this.leggingsSpecialInfo = new ArrayList<>();
        this.bootsSpecialInfo = new ArrayList<>();
    }

    @Override public BufferedImage generateModElementPicture() {
        ArrayList<File> armorPieces = new ArrayList<>();
        if (enableHelmet)
            armorPieces.add(getModElement().getFolderManager().getTextureFile(textureHelmet, TextureType.ITEM));
        if (enableBody)
            armorPieces.add(getModElement().getFolderManager().getTextureFile(textureBody, TextureType.ITEM));
        if (enableLeggings)
            armorPieces.add(getModElement().getFolderManager().getTextureFile(textureLeggings, TextureType.ITEM));
        if (enableBoots)
            armorPieces.add(getModElement().getFolderManager().getTextureFile(textureBoots, TextureType.ITEM));

        return MinecraftImageGenerator.Preview.generateArmorPreviewPicture(armorPieces);
    }

    public Model getHelmetItemModel() {
        Model.Type modelType = Model.Type.BUILTIN;
        if (helmetItemRenderType == 1)
            modelType = Model.Type.JSON;
        else if (helmetItemRenderType == 2)
            modelType = Model.Type.OBJ;
        return Model.getModelByParams(getModElement().getWorkspace(), helmetItemCustomModelName, modelType);
    }

    public Model getBodyItemModel() {
        Model.Type modelType = Model.Type.BUILTIN;
        if (bodyItemRenderType == 1)
            modelType = Model.Type.JSON;
        else if (bodyItemRenderType == 2)
            modelType = Model.Type.OBJ;
        return Model.getModelByParams(getModElement().getWorkspace(), bodyItemCustomModelName, modelType);
    }

    public Model getLeggingsItemModel() {
        Model.Type modelType = Model.Type.BUILTIN;
        if (leggingsItemRenderType == 1)
            modelType = Model.Type.JSON;
        else if (leggingsItemRenderType == 2)
            modelType = Model.Type.OBJ;
        return Model.getModelByParams(getModElement().getWorkspace(), leggingsItemCustomModelName, modelType);
    }

    public Model getBootsItemModel() {
        Model.Type modelType = Model.Type.BUILTIN;
        if (bootsItemRenderType == 1)
            modelType = Model.Type.JSON;
        else if (bootsItemRenderType == 2)
            modelType = Model.Type.OBJ;
        return Model.getModelByParams(getModElement().getWorkspace(), bootsItemCustomModelName, modelType);
    }

    public String getItemCustomModelNameFor(String part) {
        return switch (part) {
            case "helmet" -> helmetItemCustomModelName.split(":")[0];
            case "body" -> bodyItemCustomModelName.split(":")[0];
            case "leggings" -> leggingsItemCustomModelName.split(":")[0];
            case "boots" -> bootsItemCustomModelName.split(":")[0];
            default -> "";
        };
    }

    public Map<String, String> getItemModelTextureMap(String part) {
        Model model = switch (part) {
            case "helmet" -> getHelmetItemModel();
            case "body" -> getBodyItemModel();
            case "leggings" -> getLeggingsItemModel();
            case "boots" -> getBootsItemModel();
            default -> null;
        };
        if (model instanceof TexturedModel && ((TexturedModel) model).getTextureMapping() != null)
            return ((TexturedModel) model).getTextureMapping().getTextureMap();
        return new HashMap<>();
    }

    public String getItemTextureFor(String part) {
        return switch (part) {
            case "helmet" -> textureHelmet;
            case "body" -> textureBody;
            case "leggings" -> textureLeggings;
            case "boots" -> textureBoots;
            default -> null;
        };
    }

    public boolean hasHelmetNormalModel() {
        return getHelmetItemModel().getType() == Model.Type.BUILTIN && getHelmetItemModel().getReadableName()
                .equals("Normal");
    }

    public boolean hasHelmetToolModel() {
        return getHelmetItemModel().getType() == Model.Type.BUILTIN && getHelmetItemModel().getReadableName()
                .equals("Tool");
    }

    public boolean hasBodyNormalModel() {
        return getBodyItemModel().getType() == Model.Type.BUILTIN && getBodyItemModel().getReadableName()
                .equals("Normal");
    }

    public boolean hasBodyToolModel() {
        return getBodyItemModel().getType() == Model.Type.BUILTIN && getBodyItemModel().getReadableName()
                .equals("Tool");
    }

    public boolean hasLeggingsNormalModel() {
        return getLeggingsItemModel().getType() == Model.Type.BUILTIN && getLeggingsItemModel().getReadableName()
                .equals("Normal");
    }

    public boolean hasLeggingsToolModel() {
        return getLeggingsItemModel().getType() == Model.Type.BUILTIN && getLeggingsItemModel().getReadableName()
                .equals("Tool");
    }

    public boolean hasBootsNormalModel() {
        return getBootsItemModel().getType() == Model.Type.BUILTIN && getBootsItemModel().getReadableName()
                .equals("Normal");
    }

    public boolean hasBootsToolModel() {
        return getBootsItemModel().getType() == Model.Type.BUILTIN && getBootsItemModel().getReadableName()
                .equals("Tool");
    }

    @Override public TabEntry getCreativeTab() {
        return creativeTab;
    }

    @Override
    public List<MCItem> providedMCItems() {
        ArrayList<MCItem> retval = new ArrayList();
        if (this.enableHelmet) {
            retval.add(new MCItem.Custom(this.getModElement(), "helmet", "item", "Helmet"));
        }

        if (this.enableBody) {
            retval.add(new MCItem.Custom(this.getModElement(), "body", "item", "Chestplate"));
        }

        if (this.enableLeggings) {
            retval.add(new MCItem.Custom(this.getModElement(), "legs", "item", "Leggings"));
        }

        if (this.enableBoots) {
            retval.add(new MCItem.Custom(this.getModElement(), "boots", "item", "Boots"));
        }

        return retval;
    }

    @Override public List<MCItem> getCreativeTabItems() {
        return providedMCItems();
    }

}
