package ${package}.block.listener;

@EventBusSubscriber(modid = ${JavaModName}.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientListener {

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		<#list animatedblocks as ablock>
			event.registerBlockEntityRenderer((BlockEntityType<${ablock.getModElement().getName()}TileEntity>)${JavaModName}BlockEntities.${ablock.getModElement().getRegistryNameUpper()}.get(), context -> new ${ablock.getModElement().getName()}TileRenderer());
		</#list>
	}

}