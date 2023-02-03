<#include "../triggers.java.ftl">
package ${package}.init;

import com.google.common.collect.ImmutableMap;

public class ${JavaModName}GeckoLibArmors {

	public static void loadItems() {
		${JavaModName}GeckoLibArmors.GeckoLibArmorItems.load();
	}

	public static void loadRenderers() {
		<#list animatedarmors as armor>
		GeoArmorRenderer.registerArmorRenderer(new ${armor.getModElement().getName()}ArmorRenderer(),
		<#if armor.enableHelmet>
		${JavaModName}GeckoLibArmors.GeckoLibArmorItems.${armor.getModElement().getRegistryNameUpper()}_HELMET
		</#if><#if armor.enableHelmet && (armor.enableBoots || armor.enableLeggings || armor.enableBody)>,</#if>
		<#if armor.enableBody>
		${JavaModName}GeckoLibArmors.GeckoLibArmorItems.${armor.getModElement().getRegistryNameUpper()}_CHESTPLATE
		</#if><#if armor.enableBody && (armor.enableLeggings || armor.enableBoots)>,</#if>
		<#if armor.enableLeggings>
		${JavaModName}GeckoLibArmors.GeckoLibArmorItems.${armor.getModElement().getRegistryNameUpper()}_LEGGINGS
		</#if><#if armor.enableLeggings && armor.enableBoots>,</#if>
		<#if armor.enableBoots>
		${JavaModName}GeckoLibArmors.GeckoLibArmorItems.${armor.getModElement().getRegistryNameUpper()}_BOOTS
		</#if>);
		</#list>
	}

	public class GeckoLibArmorItems {

	<#list animatedarmors as item>
	<#if item.enableHelmet>
	public static ${item.getModElement().getName()}Item ${item.getModElement().getRegistryNameUpper()}_HELMET;
	</#if>
	<#if item.enableBody>
	public static ${item.getModElement().getName()}Item ${item.getModElement().getRegistryNameUpper()}_CHESTPLATE;
	</#if>
	<#if item.enableLeggings>
	public static ${item.getModElement().getName()}Item ${item.getModElement().getRegistryNameUpper()}_LEGGINGS;
	</#if>
	<#if item.enableBoots>
	public static ${item.getModElement().getName()}Item ${item.getModElement().getRegistryNameUpper()}_BOOTS;
	</#if>
	</#list>

	public static void load() {

		<#list animatedarmors as item>
			<#if item.enableHelmet>
            ${item.getModElement().getRegistryNameUpper()}_HELMET =
				Registry.register(Registry.ITEM, new ResourceLocation("${modid}", "${item.getModElement().getRegistryName()}_helmet"), new ${item.getModElement().getName()}Item(EquipmentSlot.HEAD, new Item.Properties().tab(${item.creativeTab})<#if item.helmetImmuneToFire>.fireResistant()</#if>)
	{
		<#if item.helmetSpecialInfo?has_content>
		@Override public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
			<#list item.helmetSpecialInfo as entry>
			list.add(Component.literal("${JavaConventions.escapeStringForJava(entry)}"));
			</#list>
		}
		</#if>

			<#if hasProcedure(item.onHelmetTick)>
				@Override
				public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slotinv, boolean selected) {
					double unique = Math.random();
					ItemStack stack = entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY;
					if (stack.getItem() == (itemstack).getItem()) {
						if (stack.getOrCreateTag().getDouble("_id") != unique)
							stack.getOrCreateTag().putDouble("_id", unique);
						if (itemstack.getOrCreateTag().getDouble("_id") == unique)
							<@onArmorTick item.onHelmetTick/>
					}
				}
			</#if>		

	});
			</#if>
			<#if item.enableBody>
            ${item.getModElement().getRegistryNameUpper()}_CHESTPLATE =
				Registry.register(Registry.ITEM, new ResourceLocation("${modid}", "${item.getModElement().getRegistryName()}_chestplate"), new ${item.getModElement().getName()}Item(EquipmentSlot.CHEST, new Item.Properties().tab(${item.creativeTab})<#if item.bodyImmuneToFire>.fireResistant()</#if>)
	{
		<#if item.bodySpecialInfo?has_content>
		@Override public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
			<#list item.bodySpecialInfo as entry>
			list.add(Component.literal("${JavaConventions.escapeStringForJava(entry)}"));
			</#list>
		}
		</#if>

			<#if hasProcedure(item.onBodyTick)>
				@Override
				public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slotinv, boolean selected) {
					double unique = Math.random();
					ItemStack stack = entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY;
					if (stack.getItem() == (itemstack).getItem()) {
						if (stack.getOrCreateTag().getDouble("_id") != unique)
							stack.getOrCreateTag().putDouble("_id", unique);
						if (itemstack.getOrCreateTag().getDouble("_id") == unique)
							<@onArmorTick item.onBodyTick/>
					}
				}
			</#if>
	});
			</#if>
			<#if item.enableLeggings>
            ${item.getModElement().getRegistryNameUpper()}_LEGGINGS =
				Registry.register(Registry.ITEM, new ResourceLocation("${modid}", "${item.getModElement().getRegistryName()}_leggings"), new ${item.getModElement().getName()}Item(EquipmentSlot.LEGS, new Item.Properties().tab(${item.creativeTab})<#if item.leggingsImmuneToFire>.fireResistant()</#if>)
	{
		<#if item.leggingsSpecialInfo?has_content>
		@Override public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
			<#list item.leggingsSpecialInfo as entry>
			list.add(Component.literal("${JavaConventions.escapeStringForJava(entry)}"));
			</#list>
		}
		</#if>

			<#if hasProcedure(item.onLeggingsTick)>
				@Override
				public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slotinv, boolean selected) {
					double unique = Math.random();
					ItemStack stack = entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY;
					if (stack.getItem() == (itemstack).getItem()) {
						if (stack.getOrCreateTag().getDouble("_id") != unique)
							stack.getOrCreateTag().putDouble("_id", unique);
						if (itemstack.getOrCreateTag().getDouble("_id") == unique)
							<@onArmorTick item.onLeggingsTick/>
					}
				}
			</#if>
	});
			</#if>
			<#if item.enableBoots>
            ${item.getModElement().getRegistryNameUpper()}_BOOTS =
				Registry.register(Registry.ITEM, new ResourceLocation("${modid}", "${item.getModElement().getRegistryName()}_boots"), new ${item.getModElement().getName()}Item(EquipmentSlot.FEET, new Item.Properties().tab(${item.creativeTab})<#if item.bootsImmuneToFire>.fireResistant()</#if>)
	{
		<#if item.bootsSpecialInfo?has_content>
		@Override public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
			<#list item.bootsSpecialInfo as entry>
			list.add(Component.literal("${JavaConventions.escapeStringForJava(entry)}"));
			</#list>
		}
		</#if>

			<#if hasProcedure(item.onBootsTick)>
				@Override
				public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slotinv, boolean selected) {
					double unique = Math.random();
					ItemStack stack = entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY;
					if (stack.getItem() == (itemstack).getItem()) {
						if (stack.getOrCreateTag().getDouble("_id") != unique)
							stack.getOrCreateTag().putDouble("_id", unique);
						if (itemstack.getOrCreateTag().getDouble("_id") == unique)
							<@onArmorTick item.onBootsTick/>
					}
				}
			</#if>
	});
			</#if>
		</#list>
	}
    }
}