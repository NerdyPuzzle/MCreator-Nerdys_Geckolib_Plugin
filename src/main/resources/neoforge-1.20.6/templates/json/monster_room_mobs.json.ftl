<#assign hasLiving = false>
{
  "values": {
    <#if w.hasElementsOfType("livingentity")>
      <#list w.getGElementsOfType("livingentity")?filter(e -> e.spawnInDungeons) as livingentity>
        "${modid}:${livingentity.getModElement().getRegistryName()}": {
          "weight": 100
        }
	  <#sep>,<#assign hasLiving = true></#list>
	</#if>
    <#if w.hasElementsOfType("animatedentity")><#if hasLiving>,</#if>
      <#list w.getGElementsOfType("animatedentity")?filter(e -> e.spawnInDungeons) as aentity>
        "${modid}:${aentity.getModElement().getRegistryName()}": {
          "weight": 100
        }
	  <#sep>,</#list>
	</#if>
  }
}