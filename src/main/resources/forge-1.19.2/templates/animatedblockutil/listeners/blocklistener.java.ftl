package ${package}.block.listener;

@Mod.EventBusSubscriber(modid = ${JavaModName}.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientListener {

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
		<#list animatedblocks as ablock>
			event.registerBlockEntityRenderer(TileRegistry.${ablock.getModElement().getRegistryNameUpper()}.get(), 
			${ablock.getModElement().getName()}TileRenderer::new);
		</#list>
	}

	@SubscribeEvent
	public static void register(FMLConstructModEvent event) {
		event.enqueueWork(() -> {
			TileRegistry.TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
			DisplayRegistry.DISPLAY.register(FMLJavaModLoadingContext.get().getModEventBus());
		});
	}

}