{
  "parent": "${modid}:custom/${registryname}",
  "textures": {
    "particle": "${modid}:blocks/${data.particleTexture?has_content?then(data.particleTexture, data.texture)}"
  }
}