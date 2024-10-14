<#-- @formatter:off -->
<#if data.itemTexture?has_content>
{
  "parent": "item/generated",
  "textures": {
    "layer0": "${modid}:item/${data.itemTexture}"
  }
}
<#else>
{
    "parent": "${modid}:displaysettings/${data.displaySettings?replace(".json", "")}"
}
</#if>
<#-- @formatter:on -->