<#-- @formatter:off -->
<#if data.itemTexture?has_content>
{
  "parent": "item/generated",
  "textures": {
    "layer0": "${modid}:items/${data.itemTexture}"
  }
}
<#else>
{
    "parent": "${modid}:custom/${registryname}"
}
</#if>
<#-- @formatter:on -->