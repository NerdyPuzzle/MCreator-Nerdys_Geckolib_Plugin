{
 "parent": "${modid}:custom/${registryname}_particle"
 <#if data.particleTexture?has_content>,
   "textures": {
     "particle": "${modid}:blocks/${data.particleTexture}"
   }
 </#if>
}