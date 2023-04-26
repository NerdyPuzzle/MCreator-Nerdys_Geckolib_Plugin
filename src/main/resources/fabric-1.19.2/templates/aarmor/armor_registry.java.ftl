package ${package}.init;

import com.google.common.collect.ImmutableMap;

public class ${JavaModName}GeckoLibArmors {

	public static void loadRenderers() {
		<#list animatedarmors as armor>
		GeoArmorRenderer.registerArmorRenderer(new ${armor.getModElement().getName()}ArmorRenderer(),
		<#if armor.enableHelmet>
		${JavaModName}Items.${armor.getModElement().getRegistryNameUpper()}_HELMET
		</#if><#if armor.enableHelmet && (armor.enableBoots || armor.enableLeggings || armor.enableBody)>,</#if>
		<#if armor.enableBody>
		${JavaModName}Items.${armor.getModElement().getRegistryNameUpper()}_CHESTPLATE
		</#if><#if armor.enableBody && (armor.enableLeggings || armor.enableBoots)>,</#if>
		<#if armor.enableLeggings>
		${JavaModName}Items.${armor.getModElement().getRegistryNameUpper()}_LEGGINGS
		</#if><#if armor.enableLeggings && armor.enableBoots>,</#if>
		<#if armor.enableBoots>
		${JavaModName}Items.${armor.getModElement().getRegistryNameUpper()}_BOOTS
		</#if>);
		</#list>
	}

}