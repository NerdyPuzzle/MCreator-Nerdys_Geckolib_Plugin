package ${package}.block.listener;

public class ClientListener {

	public static void registerRenderers() {
		<#list animatedblocks as ablock>
			BlockEntityRendererRegistry.register(${JavaModName}BlockEntities.${ablock.getModElement().getRegistryNameUpper()}, 
			(BlockEntityRendererProvider.Context ctx) -> new ${ablock.getModElement().getName()}TileRenderer());
			GeoItemRenderer.registerItemRenderer(${JavaModName}Items.${ablock.getModElement().getRegistryNameUpper()},
			new ${ablock.getModElement().getName()}DisplayItemRenderer());
		</#list>
	}

}