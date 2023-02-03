package ${package}.init;

@Mod.EventBusSubscriber(modid = ${JavaModName}.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ${JavaModName}GeckoLibEntities {

	@SubscribeEvent
	public static void register(FMLConstructModEvent event) {
		event.enqueueWork(() -> {
			GeckoLibSpawnEggs.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		});
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			<#list animatedentitys as aentity>
			${aentity.getModElement().getName()}Entity.init();
			</#list>
		});
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		<#list animatedentitys as aentity>
		event.put(${JavaModName}Entities.${aentity.getModElement().getRegistryNameUpper()}.get(),
		${aentity.getModElement().getName()}Entity.createAttributes().build());
		</#list>
	}

}