package ${package}.block.listener;

public class ClientListener {

	public static void registerRenderers() {
		<#list animatedblocks as ablock>
			BlockEntityRendererRegistry.register(TileRegistry.${ablock.getModElement().getRegistryNameUpper()}, 
			(BlockEntityRendererProvider.Context ctx) -> new ${ablock.getModElement().getName()}TileRenderer());
			GeoItemRenderer.registerItemRenderer(DisplayRegistry.${ablock.getModElement().getRegistryNameUpper()}_ITEM,
			new ${ablock.getModElement().getName()}DisplayItemRenderer());
		</#list>
	}

}