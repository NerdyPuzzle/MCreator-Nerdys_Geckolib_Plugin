{
 "parent": "${modid}:custom/${registryname}_particle"
 <#if data.particleTexture?has_content>,
   "textures": {
     "particle": "${modid}:block/${data.particleTexture}"
   }
 </#if>
}