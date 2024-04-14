<#assign mineableblocks = []>
<#list blocks as block>
    <#if block.getModElement().getTypeString() != "plant" && block.getModElement().getTypeString() != "fluid">
        <#if block.destroyTool == var_type>
            <#assign mineableblocks += [block]>
        </#if>
    </#if>
</#list>

{
    "replace": false,
    "values": [<#list mineableblocks as block>"${generator.getResourceLocationForModElement(block.getModElement())}"<#if block?has_next>,</#if></#list>]
}