package ${package}.block.registry;

public class DisplayRegistry {
	public static final DeferredRegister<Item> DISPLAY = DeferredRegister.create(ForgeRegistries.ITEMS, ${JavaModName}.MODID);

<#list animatedblocks as ablock>
	public static final RegistryObject<Item> ${ablock.getModElement().getRegistryNameUpper()}_ITEM = 
		DISPLAY.register("${ablock.getModElement().getRegistryName()}_item",
	        () -> new ${ablock.getModElement().getName()}DisplayItem(${JavaModName}Blocks.${ablock.getModElement().getRegistryNameUpper()}.get(),
		new Item.Properties().tab(null)));
</#list>
}