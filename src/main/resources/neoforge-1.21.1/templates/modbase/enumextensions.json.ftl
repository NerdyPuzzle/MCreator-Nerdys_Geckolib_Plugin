<#assign raiders = w.getGElementsOfType("livingentity")?filter(e -> e.mobBehaviourType == "Raider")>
<#assign animatedraiders = w.getGElementsOfType("animatedentity")?filter(e -> e.mobBehaviourType == "Raider")>
<#assign armposes = w.getGElementsOfType("animateditem")?filter(e -> e.enableArmPose)>
{
  "entries": [
    <#list animatedraiders as raider>
	{
      "enum": "net/minecraft/world/entity/raid/Raid$RaiderType",
      "name": "${modid?upper_case}_${raider.getModElement().getRegistryNameUpper()}",
      "constructor": "(Ljava/util/function/Supplier;[I)V",
      "parameters": {
        "class": "${package?replace(".", "/")}/entity/${raider.getModElement().getName()}Entity",
        "field": "RAIDER_TYPE"
      }
    }<#sep>,
	</#list><#if (raiders?has_content || armposes?has_content) && animatedraiders?has_content>,</#if>
    <#list raiders as raider>
	{
      "enum": "net/minecraft/world/entity/raid/Raid$RaiderType",
      "name": "${modid?upper_case}_${raider.getModElement().getRegistryNameUpper()}",
      "constructor": "(Ljava/util/function/Supplier;[I)V",
      "parameters": {
        "class": "${package?replace(".", "/")}/entity/${raider.getModElement().getName()}Entity",
        "field": "RAIDER_TYPE"
      }
    }<#sep>,
	</#list><#if armposes?has_content && raiders?has_content>,</#if>
	<#list armposes as pose>
	{
      "enum": "net/minecraft/client/model/HumanoidModel$ArmPose",
      "name": "${modid?upper_case}_${pose.getModElement().getRegistryNameUpper()}",
      "constructor": "(ZLnet/neoforged/neoforge/client/IArmPoseTransformer;)V",
      "parameters": {
        "class": "${package?replace(".", "/")}/item/${pose.getModElement().getName()}Item",
        "field": "ARM_POSE"
      }
    }<#sep>,
	</#list>
  ]
}