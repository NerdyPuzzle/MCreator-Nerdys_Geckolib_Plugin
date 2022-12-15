package ${package}.block.model;

<#assign geomodel = data.normal>
<#assign texture = data.texture>

public class ${name}DisplayModel extends AnimatedGeoModel<${name}DisplayItem> {
	@Override
	public ResourceLocation getAnimationResource(${name}DisplayItem animatable) {
		return new ResourceLocation("${modid}", "animations/${geomodel}.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(${name}DisplayItem animatable) {
		return new ResourceLocation("${modid}", "geo/${geomodel}.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(${name}DisplayItem entity) {
		return new ResourceLocation("${modid}", "textures/blocks/${texture}.png");
	}
}