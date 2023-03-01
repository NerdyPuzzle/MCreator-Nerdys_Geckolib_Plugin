package ${package}.block.registry;

public class DisplayRegistry {
	<#list animatedblocks as ablock>
	public static BlockItem ${ablock.getModElement().getRegistryNameUpper()}_ITEM;
	</#list>

	public static void load() {
	<#list animatedblocks as ablock>
	${ablock.getModElement().getRegistryNameUpper()}_ITEM = 
		Registry.register(Registry.ITEM, new ResourceLocation("${modid}:${ablock.getModElement().getRegistryName()}_item"),
	        new ${ablock.getModElement().getName()}DisplayItem(${JavaModName}Blocks.${ablock.getModElement().getRegistryNameUpper()}, 		new Item.Properties().tab(null)));
	</#list>
	}
}