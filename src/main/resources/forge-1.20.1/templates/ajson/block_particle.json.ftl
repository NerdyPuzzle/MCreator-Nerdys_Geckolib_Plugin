{
  "parent": "${modid}:displaysettings/${data.displaySettings?replace(".json", "")}",
  "textures": {
    "particle": "${modid}:block/${data.particleTexture?has_content?then(data.particleTexture, data.texture)}"
  }
}