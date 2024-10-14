<#if data.rotationMode?? && (data.rotationMode == 1 || data.rotationMode == 3)>
<#if data.enablePitch>
{
  "variants": {
    "face=floor,facing=north<#if data.hasBlockstates()>,blockstate=0</#if>": {
      "model": "${modid}:custom/${registryname}_particle"
    },
    "face=floor,facing=east<#if data.hasBlockstates()>,blockstate=0</#if>": {
      "model": "${modid}:custom/${registryname}_particle",
      "y": 90
    },
    "face=floor,facing=south<#if data.hasBlockstates()>,blockstate=0</#if>": {
      "model": "${modid}:custom/${registryname}_particle",
      "y": 180
    },
    "face=floor,facing=west<#if data.hasBlockstates()>,blockstate=0</#if>": {
      "model": "${modid}:custom/${registryname}_particle",
      "y": 270
    },
    "face=wall,facing=north<#if data.hasBlockstates()>,blockstate=0</#if>": {
      "model": "${modid}:custom/${registryname}_particle",
      "x": 90
    },
    "face=wall,facing=east<#if data.hasBlockstates()>,blockstate=0</#if>": {
      "model": "${modid}:custom/${registryname}_particle",
      "x": 90,
      "y": 90
    },
    "face=wall,facing=south<#if data.hasBlockstates()>,blockstate=0</#if>": {
      "model": "${modid}:custom/${registryname}_particle",
      "x": 90,
      "y": 180
    },
    "face=wall,facing=west<#if data.hasBlockstates()>,blockstate=0</#if>": {
      "model": "${modid}:custom/${registryname}_particle",
      "x": 90,
      "y": 270
    },
    "face=ceiling,facing=north<#if data.hasBlockstates()>,blockstate=0</#if>": {
      "model": "${modid}:custom/${registryname}_particle",
      "x": 180,
      "y": 180
    },
    "face=ceiling,facing=east<#if data.hasBlockstates()>,blockstate=0</#if>": {
      "model": "${modid}:custom/${registryname}_particle",
      "x": 180,
      "y": 270
    },
    "face=ceiling,facing=south<#if data.hasBlockstates()>,blockstate=0</#if>": {
      "model": "${modid}:custom/${registryname}_particle",
      "x": 180
    },
    "face=ceiling,facing=west<#if data.hasBlockstates()>,blockstate=0</#if>": {
      "model": "${modid}:custom/${registryname}_particle",
      "x": 180,
      "y": 90
    }<#if data.hasBlockstates()>,
    <#list data.blockstateList as state>
        "face=floor,facing=north,blockstate=${state?index + 1}": {
          "model": "${modid}:block/${data.getModElement().getRegistryName()}_blockstate_${state?index}"
        },
        "face=floor,facing=east,blockstate=${state?index + 1}": {
          "model": "${modid}:block/${data.getModElement().getRegistryName()}_blockstate_${state?index}",
          "y": 90
        },
        "face=floor,facing=south,blockstate=${state?index + 1}": {
          "model": "${modid}:block/${data.getModElement().getRegistryName()}_blockstate_${state?index}",
          "y": 180
        },
        "face=floor,facing=west,blockstate=${state?index + 1}": {
          "model": "${modid}:block/${data.getModElement().getRegistryName()}_blockstate_${state?index}",
          "y": 270
        },
        "face=wall,facing=north,blockstate=${state?index + 1}": {
          "model": "${modid}:block/${data.getModElement().getRegistryName()}_blockstate_${state?index}",
          "x": 90
        },
        "face=wall,facing=east,blockstate=${state?index + 1}": {
          "model": "${modid}:block/${data.getModElement().getRegistryName()}_blockstate_${state?index}",
          "x": 90,
          "y": 90
        },
        "face=wall,facing=south,blockstate=${state?index + 1}": {
          "model": "${modid}:block/${data.getModElement().getRegistryName()}_blockstate_${state?index}",
          "x": 90,
          "y": 180
        },
        "face=wall,facing=west,blockstate=${state?index + 1}": {
          "model": "${modid}:block/${data.getModElement().getRegistryName()}_blockstate_${state?index}",
          "x": 90,
          "y": 270
        },
        "face=ceiling,facing=north,blockstate=${state?index + 1}": {
          "model": "${modid}:block/${data.getModElement().getRegistryName()}_blockstate_${state?index}",
          "x": 180,
          "y": 180
        },
        "face=ceiling,facing=east,blockstate=${state?index + 1}": {
          "model": "${modid}:block/${data.getModElement().getRegistryName()}_blockstate_${state?index}",
          "x": 180,
          "y": 270
        },
        "face=ceiling,facing=south,blockstate=${state?index + 1}": {
          "model": "${modid}:block/${data.getModElement().getRegistryName()}_blockstate_${state?index}",
          "x": 180
        },
        "face=ceiling,facing=west,blockstate=${state?index + 1}": {
          "model": "${modid}:block/${data.getModElement().getRegistryName()}_blockstate_${state?index}",
          "x": 180,
          "y": 90
        }<#sep>,
    </#list>
    </#if>
  }
}
<#else>
{
  "variants": {
    "facing=north<#if data.hasBlockstates()>,blockstate=0</#if>": {
      "model": "${modid}:custom/${registryname}_particle"
    },
    "facing=east<#if data.hasBlockstates()>,blockstate=0</#if>": {
      "model": "${modid}:custom/${registryname}_particle",
      "y": 90
    },
    "facing=south<#if data.hasBlockstates()>,blockstate=0</#if>": {
      "model": "${modid}:custom/${registryname}_particle",
      "y": 180
    },
    "facing=west<#if data.hasBlockstates()>,blockstate=0</#if>": {
      "model": "${modid}:custom/${registryname}_particle",
      "y": 270
    }<#if data.hasBlockstates()>,
    <#list data.blockstateList as state>
        "facing=north,blockstate=${state?index + 1}": {
          "model": "${modid}:block/${data.getModElement().getRegistryName()}_blockstate_${state?index}"
        },
        "facing=east,blockstate=${state?index + 1}": {
          "model": "${modid}:block/${data.getModElement().getRegistryName()}_blockstate_${state?index}",
          "y": 90
        },
        "facing=south,blockstate=${state?index + 1}": {
          "model": "${modid}:block/${data.getModElement().getRegistryName()}_blockstate_${state?index}",
          "y": 180
        },
        "facing=west,blockstate=${state?index + 1}": {
          "model": "${modid}:block/${data.getModElement().getRegistryName()}_blockstate_${state?index}",
          "y": 270
        }<#sep>,
    </#list>
    </#if>
  }
}
</#if>
<#elseif data.rotationMode?? && (data.rotationMode == 2 || data.rotationMode == 4)>
{
  "variants": {
    "facing=north<#if data.hasBlockstates()>,blockstate=0</#if>": {
      "model": "${modid}:custom/${registryname}_particle"
    },
    "facing=east<#if data.hasBlockstates()>,blockstate=0</#if>": {
      "model": "${modid}:custom/${registryname}_particle",
      "y": 90
    },
    "facing=south<#if data.hasBlockstates()>,blockstate=0</#if>": {
      "model": "${modid}:custom/${registryname}_particle",
      "y": 180
    },
    "facing=west<#if data.hasBlockstates()>,blockstate=0</#if>": {
      "model": "${modid}:custom/${registryname}_particle",
      "y": 270
    },
    "facing=up<#if data.hasBlockstates()>,blockstate=0</#if>": {
      "model": "${modid}:custom/${registryname}_particle",
      "x": 270
    },
    "facing=down<#if data.hasBlockstates()>,blockstate=0</#if>": {
      "model": "${modid}:custom/${registryname}_particle",
      "x": 90
    }<#if data.hasBlockstates()>,
    <#list data.blockstateList as state>
        "facing=north,blockstate=${state?index + 1}": {
          "model": "${modid}:block/${data.getModElement().getRegistryName()}_blockstate_${state?index}"
        },
        "facing=east,blockstate=${state?index + 1}": {
          "model": "${modid}:block/${data.getModElement().getRegistryName()}_blockstate_${state?index}",
          "y": 90
        },
        "facing=south,blockstate=${state?index + 1}": {
          "model": "${modid}:block/${data.getModElement().getRegistryName()}_blockstate_${state?index}",
          "y": 180
        },
        "facing=west,blockstate=${state?index + 1}": {
          "model": "${modid}:block/${data.getModElement().getRegistryName()}_blockstate_${state?index}",
          "y": 270
        },
        "facing=up,blockstate=${state?index + 1}": {
          "model": "${modid}:block/${data.getModElement().getRegistryName()}_blockstate_${state?index}",
          "x": 270
        },
        "facing=down,blockstate=${state?index + 1}": {
          "model": "${modid}:block/${data.getModElement().getRegistryName()}_blockstate_${state?index}",
          "x": 90
        }<#sep>,
    </#list>
    </#if>
  }
}
<#elseif data.rotationMode?? && data.rotationMode == 5>
{
  "variants": {
    "axis=x<#if data.hasBlockstates()>,blockstate=0</#if>": {
      "model": "${modid}:custom/${registryname}_particle",
      "x": 90,
      "y": 90
    },
    "axis=y<#if data.hasBlockstates()>,blockstate=0</#if>": {
      "model": "${modid}:custom/${registryname}_particle"
    },
    "axis=z<#if data.hasBlockstates()>,blockstate=0</#if>": {
      "model": "${modid}:custom/${registryname}_particle",
      "x": 90
    }<#if data.hasBlockstates()>,
    <#list data.blockstateList as state>
        "axis=x,blockstate=${state?index + 1}": {
          "model": "${modid}:block/${data.getModElement().getRegistryName()}_blockstate_${state?index}",
          "x": 90,
          "y": 90
        },
        "axis=y,blockstate=${state?index + 1}": {
          "model": "${modid}:block/${data.getModElement().getRegistryName()}_blockstate_${state?index}"
        },
        "axis=z,blockstate=${state?index + 1}": {
          "model": "${modid}:block/${data.getModElement().getRegistryName()}_blockstate_${state?index}",
          "x": 90
        }<#sep>,
    </#list>
    </#if>
  }
}
<#else>
{
  "variants": {
    "<#if data.hasBlockstates()>blockstate=0</#if>": {
      "model": "${modid}:custom/${registryname}_particle"
    }<#if data.hasBlockstates()>,
    <#list data.blockstateList as state>
        "blockstate=${state?index + 1}": {
          "model": "${modid}:block/${data.getModElement().getRegistryName()}_blockstate_${state?index}"
        }<#sep>,
    </#list>
    </#if>
  }
}
</#if>