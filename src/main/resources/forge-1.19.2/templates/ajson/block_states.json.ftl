<#if data.rotationMode?? && (data.rotationMode == 1 || data.rotationMode == 3)>
<#if data.enablePitch>
{
  "variants": {
    "face=floor,facing=north": {
      "model": "${modid}:custom/${registryname}_particle"
    },
    "face=floor,facing=east": {
      "model": "${modid}:custom/${registryname}_particle",
      "y": 90
    },
    "face=floor,facing=south": {
      "model": "${modid}:custom/${registryname}_particle",
      "y": 180
    },
    "face=floor,facing=west": {
      "model": "${modid}:custom/${registryname}_particle",
      "y": 270
    },
    "face=wall,facing=north": {
      "model": "${modid}:custom/${registryname}_particle",
      "x": 90
    },
    "face=wall,facing=east": {
      "model": "${modid}:custom/${registryname}_particle",
      "x": 90,
      "y": 90
    },
    "face=wall,facing=south": {
      "model": "${modid}:custom/${registryname}_particle",
      "x": 90,
      "y": 180
    },
    "face=wall,facing=west": {
      "model": "${modid}:custom/${registryname}_particle",
      "x": 90,
      "y": 270
    },
    "face=ceiling,facing=north": {
      "model": "${modid}:custom/${registryname}_particle",
      "x": 180,
      "y": 180
    },
    "face=ceiling,facing=east": {
      "model": "${modid}:custom/${registryname}_particle",
      "x": 180,
      "y": 270
    },
    "face=ceiling,facing=south": {
      "model": "${modid}:custom/${registryname}_particle",
      "x": 180
    },
    "face=ceiling,facing=west": {
      "model": "${modid}:custom/${registryname}_particle",
      "x": 180,
      "y": 90
    }
  }
}
<#else>
{
  "variants": {
    "facing=north": {
      "model": "${modid}:custom/${registryname}_particle"
    },
    "facing=east": {
      "model": "${modid}:custom/${registryname}_particle",
      "y": 90
    },
    "facing=south": {
      "model": "${modid}:custom/${registryname}_particle",
      "y": 180
    },
    "facing=west": {
      "model": "${modid}:custom/${registryname}_particle",
      "y": 270
    }
  }
}
</#if>
<#elseif data.rotationMode?? && (data.rotationMode == 2 || data.rotationMode == 4)>
{
  "variants": {
    "facing=north": {
      "model": "${modid}:custom/${registryname}_particle"
    },
    "facing=east": {
      "model": "${modid}:custom/${registryname}_particle",
      "y": 90
    },
    "facing=south": {
      "model": "${modid}:custom/${registryname}_particle",
      "y": 180
    },
    "facing=west": {
      "model": "${modid}:custom/${registryname}_particle",
      "y": 270
    },
    "facing=up": {
      "model": "${modid}:custom/${registryname}_particle",
      "x": 270
    },
    "facing=down": {
      "model": "${modid}:custom/${registryname}_particle",
      "x": 90
    }
  }
}
<#elseif data.rotationMode?? && data.rotationMode == 5>
{
  "variants": {
    "axis=x": {
      "model": "${modid}:custom/${registryname}_particle",
      "x": 90,
      "y": 90
    },
    "axis=y": {
      "model": "${modid}:custom/${registryname}_particle",
    },
    "axis=z": {
      "model": "${modid}:custom/${registryname}_particle",
      "x": 90
    }
  }
}
<#else>
{
  "variants": {
    "${var_variant}": {
      "model": "${modid}:custom/${registryname}_particle"
    }
  }
}
</#if>