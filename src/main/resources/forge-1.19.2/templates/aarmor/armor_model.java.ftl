package ${package}.item.model;

public class ${name}Model extends AnimatedGeoModel<${name}Item> {
    @Override
    public ResourceLocation getAnimationResource(${name}Item object) {
        return new ResourceLocation("${modid}", "animations/${data.model?replace(".geo.json", "")}.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(${name}Item object) {
        return new ResourceLocation("${modid}", "geo/${data.model}");
    }

    @Override
    public ResourceLocation getTextureResource(${name}Item object) {
        return new ResourceLocation("${modid}", "textures/items/${data.armorTextureFile}");
    }

}