package ${package}.block.listener;

@Mod.EventBusSubscriber(modid = ${JavaModName}.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientListener {

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		<#list animatedblocks as ablock>
			event.registerBlockEntityRenderer(${JavaModName}BlockEntities.${ablock.getModElement().getRegistryNameUpper()}.get(), context -> new ${ablock.getModElement().getName()}TileRenderer());
		</#list>
	}

}