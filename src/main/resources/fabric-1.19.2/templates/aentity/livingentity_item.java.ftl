package ${package}.entity.spawneggs;

public class GeckoLibSpawnEggs {

	<#list animatedentitys as item>
	<#if item.hasSpawnEgg>
	public static Item ${item.getModElement().getRegistryNameUpper()};
	</#if>
	</#list>

	public static void load() {

	<#list animatedentitys as item>
			${item.getModElement().getRegistryNameUpper()} = Registry.register(Registry.ITEM,new ResourceLocation(${JavaModName}.MODID,
				"${item.getModElement().getRegistryName()}_spawn_egg"), new SpawnEggItem(${JavaModName}Entities.${item.getModElement().getRegistryNameUpper()},
					${item.spawnEggBaseColor.getRGB()}, ${item.spawnEggDotColor.getRGB()}, new Item.Properties() <#if item.creativeTab??>.tab(${item.creativeTab})<#else>
						.tab(CreativeModeTab.TAB_MISC)</#if>));
	</#list>

	}

}