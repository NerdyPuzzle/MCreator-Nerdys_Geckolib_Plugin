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
	                return ResourceLocation.parse("${modid}:animations/${state.customModelName?replace(".geo.json", "")}.animation.json");
	        </#list>
	    </#if>
		return ResourceLocation.parse("${modid}:animations/${geomodel?replace(".geo.json", "")}.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(${name}TileEntity animatable) {
	   <#if data.hasBlockstates()>
	        final int blockstate = animatable.blockstateNew;
	        <#list data.blockstateList as state>
	            if (blockstate == ${state?index + 1})
	                return ResourceLocation.parse("${modid}:geo/${state.customModelName}");
	        </#list>
	    </#if>
		return ResourceLocation.parse("${modid}:geo/${geomodel}");
	}

	@Override
	public ResourceLocation getTextureResource(${name}TileEntity animatable) {
	   <#if data.hasBlockstates()>
	        final int blockstate = animatable.blockstateNew;
	        <#list data.blockstateList as state>
	            if (blockstate == ${state?index + 1})
	                return ResourceLocation.parse("${modid}:textures/block/${state.texture}.png");
	        </#list>
	    </#if>
		return ResourceLocation.parse("${modid}:textures/block/${texture}.png");
	}
}