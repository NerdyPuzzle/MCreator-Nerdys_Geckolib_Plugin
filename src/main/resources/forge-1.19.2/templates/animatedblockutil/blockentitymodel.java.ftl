package ${package}.block.model;

<#assign geomodel = data.normal>
<#assign texture = data.texture>

public class ${name}BlockModel extends AnimatedGeoModel<${name}TileEntity> {
	@Override
	public ResourceLocation getAnimationResource(${name}TileEntity animatable) {
	   <#if data.hasBlockstates()>
	        final int blockstate = animatable.getBlockState().getValue(${name}Block.BLOCKSTATE);
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
	        final int blockstate = animatable.getBlockState().getValue(${name}Block.BLOCKSTATE);
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
	        final int blockstate = animatable.getBlockState().getValue(${name}Block.BLOCKSTATE);
	        <#list data.blockstateList as state>
	            if (blockstate == ${state?index + 1})
	                return new ResourceLocation("${modid}", "textures/blocks/${state.texture}.png");
	        </#list>
	    </#if>
		return new ResourceLocation("${modid}", "textures/blocks/${texture}.png");
	}
}