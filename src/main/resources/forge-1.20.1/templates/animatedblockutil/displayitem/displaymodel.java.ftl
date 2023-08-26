package ${package}.block.model;

<#assign geomodel = data.normal>
<#assign texture = data.texture>

public class ${name}DisplayModel extends GeoModel<${name}DisplayItem> {
	@Override
	public ResourceLocation getAnimationResource(${name}DisplayItem animatable) {
		return new ResourceLocation("${modid}", "animations/${geomodel?replace(".geo.json", "")}.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(${name}DisplayItem animatable) {
		return new ResourceLocation("${modid}", "geo/${geomodel}");
	}

	@Override
	public ResourceLocation getTextureResource(${name}DisplayItem entity) {
		return new ResourceLocation("${modid}", "textures/block/${texture}.png");
	}
}