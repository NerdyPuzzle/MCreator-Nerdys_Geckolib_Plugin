{
  "forge_marker": 1,
  "parent": "neoforge:item/default",
  "loader": "neoforge:obj",
  "model": "${modid}:models/item/${data.customModelName.split(":")[0]}.obj",
  "textures": {
    <#if data.getTextureMap()?has_content>
        <#list data.getTextureMap().entrySet() as texture>
            "${texture.getKey()}": "${modid}:block/${texture.getValue()}",
        </#list>
    </#if>
    "particle": "${modid}:item/${data.texture}"
  }
}