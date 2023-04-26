<#--
 # This file is part of Fabric-Generator-MCreator.
 # Copyright (C) 2020-2021, Goldorion, opensource contributors
 #
 # Fabric-Generator-MCreator is free software: you can redistribute it and/or modify
 # it under the terms of the GNU Lesser General Public License as published by
 # the Free Software Foundation, either version 3 of the License, or
 # (at your option) any later version.
 # Fabric-Generator-MCreator is distributed in the hope that it will be useful,
 # but WITHOUT ANY WARRANTY; without even the implied warranty of
 # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 # GNU Lesser General Public License for more details.
 #
 # You should have received a copy of the GNU Lesser General Public License
 # along with Fabric-Generator-MCreator.  If not, see <https://www.gnu.org/licenses/>.
-->

<#-- @formatter:off -->

/*
 *	MCreator note: This file will be REGENERATED on each build.
 */

package ${package}.init;

<#include "../triggers.java.ftl">
<#assign hasBlocks = false>

public class ${JavaModName}Items {

	<#list items as item>
		<#if item.getModElement().getTypeString() == "armor">
			<#if item.enableHelmet>
				public static Item ${item.getModElement().getRegistryNameUpper()}_HELMET;
			</#if>
			<#if item.enableBody>
				public static Item ${item.getModElement().getRegistryNameUpper()}_CHESTPLATE;
			</#if>
			<#if item.enableLeggings>
				public static Item ${item.getModElement().getRegistryNameUpper()}_LEGGINGS;
			</#if>
			<#if item.enableBoots>
				public static Item ${item.getModElement().getRegistryNameUpper()}_BOOTS;
			</#if>
		<#elseif item.getModElement().getTypeString() == "animatedarmor">
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
		<#elseif item.getModElement().getTypeString() == "livingentity" || item.getModElement().getTypeString() == "animatedentity">
			public static Item ${item.getModElement().getRegistryNameUpper()}_SPAWN_EGG;
		<#elseif item.getModElement().getTypeString() != "dimension">
			public static Item ${item.getModElement().getRegistryNameUpper()};
		</#if>
	</#list>

	public static void load() {
		<#list items as item>
		<#if item.getModElement().getTypeString() == "armor">
			<#if item.enableHelmet>
				${item.getModElement().getRegistryNameUpper()}_HELMET = Registry.register(Registry.ITEM,
					new ResourceLocation(${JavaModName}.MODID, "${item.getModElement().getRegistryName()}_helmet"), new ${item.getModElement().getName()}Item.Helmet());
			</#if>
			<#if item.enableBody>
				${item.getModElement().getRegistryNameUpper()}_CHESTPLATE = Registry.register(Registry.ITEM,
					new ResourceLocation(${JavaModName}.MODID, "${item.getModElement().getRegistryName()}_chestplate"), new ${item.getModElement().getName()}Item.Chestplate());
			</#if>
			<#if item.enableLeggings>
				${item.getModElement().getRegistryNameUpper()}_LEGGINGS = Registry.register(Registry.ITEM,
					new ResourceLocation(${JavaModName}.MODID, "${item.getModElement().getRegistryName()}_leggings"), new ${item.getModElement().getName()}Item.Leggings());
			</#if>
			<#if item.enableBoots>
				${item.getModElement().getRegistryNameUpper()}_BOOTS = Registry.register(Registry.ITEM,
					new ResourceLocation(${JavaModName}.MODID, "${item.getModElement().getRegistryName()}_boots"), new ${item.getModElement().getName()}Item.Boots());
			</#if>
		<#elseif item.getModElement().getTypeString() == "animatedarmor">
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
		<#elseif item.getModElement().getTypeString() == "livingentity" || item.getModElement().getTypeString() == "animatedentity">
			${item.getModElement().getRegistryNameUpper()}_SPAWN_EGG = Registry.register(Registry.ITEM,new ResourceLocation(${JavaModName}.MODID,
				"${item.getModElement().getRegistryName()}_spawn_egg"), new SpawnEggItem(${JavaModName}Entities.${item.getModElement().getRegistryNameUpper()},
					${item.spawnEggBaseColor.getRGB()}, ${item.spawnEggDotColor.getRGB()}, new Item.Properties() <#if item.creativeTab??>.tab(${item.creativeTab})<#else>
						.tab(CreativeModeTab.TAB_MISC)</#if>));
		<#elseif item.getModElement().getTypeString() == "animatedblock">
			${item.getModElement().getRegistryNameUpper()} = Registry.register(Registry.ITEM,
			new ResourceLocation("${modid}:${item.getModElement().getRegistryName()}"),
				new ${item.getModElement().getName()}DisplayItem(${JavaModName}Blocks.${item.getModElement().getRegistryNameUpper()}, new Item.Properties().tab(${item.creativeTab})));
		<#elseif item.getModElement().getType().getBaseType()?string == "BLOCK">
			${item.getModElement().getRegistryNameUpper()} = Registry.register(Registry.ITEM,new ResourceLocation(${JavaModName}.MODID,
				"${item.getModElement().getRegistryName()}"), new BlockItem(${JavaModName}Blocks.${item.getModElement().getRegistryNameUpper()}, new Item.Properties().tab(${item.creativeTab})));
		<#else>
			<#if item.getModElement().getTypeString() != "dimension">
				${item.getModElement().getRegistryNameUpper()} = Registry.register(Registry.ITEM,
					new ResourceLocation(${JavaModName}.MODID, "${item.getModElement().getRegistryName()}"), new ${item.getModElement().getName()}Item());
			</#if>
		</#if>
		</#list>

	}

}

<#-- @formatter:on -->