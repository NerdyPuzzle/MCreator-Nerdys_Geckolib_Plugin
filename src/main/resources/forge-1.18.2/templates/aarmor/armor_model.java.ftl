package ${package}.item.model;

public class ${name}Model extends AnimatedGeoModel<${name}Item> {
    @Override
    public ResourceLocation getAnimationFileLocation(${name}Item object) {
        return new ResourceLocation("${modid}", "animations/${data.model?replace(".geo.json", "")}.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(${name}Item object) {
        return new ResourceLocation("${modid}", "geo/${data.model}");
    }

    @Override
    public ResourceLocation getTextureLocation(${name}Item object) {
        return new ResourceLocation("${modid}", "textures/items/${data.armorTextureFile}");
    }

}