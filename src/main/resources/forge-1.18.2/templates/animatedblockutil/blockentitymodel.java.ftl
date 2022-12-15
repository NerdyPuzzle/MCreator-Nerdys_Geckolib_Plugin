package ${package}.block.model;

<#assign geomodel = data.normal>
<#assign texture = data.texture>

public class ${name}BlockModel extends AnimatedGeoModel<${name}TileEntity> {
	@Override
	public ResourceLocation getAnimationFileLocation(${name}TileEntity animatable) {
		return new ResourceLocation("${modid}", "animations/${geomodel}.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(${name}TileEntity animatable) {
		return new ResourceLocation("${modid}", "geo/${geomodel}.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(${name}TileEntity entity) {
		return new ResourceLocation("${modid}", "textures/blocks/${texture}.png");
	}
}