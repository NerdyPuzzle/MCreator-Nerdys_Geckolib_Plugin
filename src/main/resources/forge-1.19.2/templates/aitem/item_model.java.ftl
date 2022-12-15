package ${package}.item.model;

<#assign geomodel = data.normal>
<#assign texture = data.texture>

public class ${name}ItemModel extends AnimatedGeoModel<${name}Item> {
	@Override
	public ResourceLocation getAnimationResource(${name}Item animatable) {
		return new ResourceLocation("${modid}", "animations/${geomodel}.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(${name}Item animatable) {
		return new ResourceLocation("${modid}", "geo/${geomodel}.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(${name}Item animatable) {
		return new ResourceLocation("${modid}", "textures/items/${texture}.png");
	}
}