package ${package}.block.model;

<#assign geomodel = data.normal>
<#assign texture = data.texture>

public class ${name}BlockModel extends GeoModel<${name}TileEntity> {
	@Override
	public ResourceLocation getAnimationResource(${name}TileEntity animatable) {
	   <#if data.hasBlockstates()>
	        final int blockstate = animatable.blockstateNew;
	        <#list data.blockstateList as state>
	            if (blockstate == ${state?index + 1})
	                return new ResourceLocation("${modid}", "animations/${state.customModelName?replace(".geo.json", "")}.animation.json");
	        </#list>
	    </#if>
		return new ResourceLocation("${modid}", "animations/${geomodel?replace(".geo.json", "")}.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(${name}TileEntity animatable) {
	   <#if data.hasBlockstates()>
	        final int blockstate = animatable.blockstateNew;
	        <#list data.blockstateList as state>
	            if (blockstate == ${state?index + 1})
	                return new ResourceLocation("${modid}", "geo/${state.customModelName}");
	        </#list>
	    </#if>
		return new ResourceLocation("${modid}", "geo/${geomodel}");
	}

	@Override
	public ResourceLocation getTextureResource(${name}TileEntity animatable) {
	   <#if data.hasBlockstates()>
	        final int blockstate = animatable.blockstateNew;
	        <#list data.blockstateList as state>
	            if (blockstate == ${state?index + 1})
	                return new ResourceLocation("${modid}", "textures/block/${state.texture}.png");
	        </#list>
	    </#if>
		return new ResourceLocation("${modid}", "textures/block/${texture}.png");
	}
}