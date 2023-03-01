<#assign mineableblocks = []>
<#list animatedblocks as block>
	<#if block.breakHarvestLevel == var_level?number>
		<#assign mineableblocks += [block]>
	</#if>
</#list>

{
	"replace": false,
	"values": [<#list mineableblocks as block>"${generator.getResourceLocationForModElement(block.getModElement())}"<#if block?has_next>,</#if></#list>]
}