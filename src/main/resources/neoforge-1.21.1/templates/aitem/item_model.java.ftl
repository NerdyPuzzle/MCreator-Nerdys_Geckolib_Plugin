package ${package}.item.model;

<#assign geomodel = data.normal>
<#assign texture = data.texture>

public class ${name}ItemModel extends GeoModel<${name}Item> {
	@Override
	public ResourceLocation getAnimationResource(${name}Item animatable) {
		return ResourceLocation.parse("${modid}:animations/${geomodel?replace(".geo.json", "")}.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(${name}Item animatable) {
		return ResourceLocation.parse("${modid}:geo/${geomodel}");
	}

	@Override
	public ResourceLocation getTextureResource(${name}Item animatable) {
		return ResourceLocation.parse("${modid}:textures/item/${texture}.png");
	}
}