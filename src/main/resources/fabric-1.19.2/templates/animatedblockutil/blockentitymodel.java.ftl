package ${package}.block.model;

<#assign geomodel = data.normal>
<#assign texture = data.texture>

public class ${name}BlockModel extends AnimatedGeoModel<${name}TileEntity> {
	@Override
	public ResourceLocation getAnimationResource(${name}TileEntity animatable) {
		return new ResourceLocation("${modid}", "animations/${geomodel?replace(".geo.json", "")}.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(${name}TileEntity animatable) {
		return new ResourceLocation("${modid}", "geo/${geomodel}");
	}

	@Override
	public ResourceLocation getTextureResource(${name}TileEntity entity) {
		return new ResourceLocation("${modid}", "textures/blocks/${texture}.png");
	}
}