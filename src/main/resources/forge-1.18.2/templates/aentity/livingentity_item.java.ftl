package ${package}.entity.spawneggs;

public class GeckoLibSpawnEggs {
public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ${JavaModName}.MODID);
	
	<#list animatedentitys as item>
<#if item.hasSpawnEgg>
            public static final RegistryObject<Item> ${item.getModElement().getRegistryNameUpper()} =
				REGISTRY.register("${item.getModElement().getRegistryName()}_spawn_egg", () -> new ForgeSpawnEggItem(${JavaModName}Entities.${item.getModElement().getRegistryNameUpper()},
					${item.spawnEggBaseColor.getRGB()}, ${item.spawnEggDotColor.getRGB()}, new Item.Properties() <#if item.creativeTab??>.tab(${item.creativeTab})<#else>
                    .tab(CreativeModeTab.TAB_MISC)</#if>));
</#if>
	</#list>
}