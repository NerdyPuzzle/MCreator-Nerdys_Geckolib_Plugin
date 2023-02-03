package ${package}.init;

@Mod.EventBusSubscriber(modid = ${JavaModName}.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ${JavaModName}GeckoLibArmors {

	@SubscribeEvent
	public static void register(FMLConstructModEvent event) {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		event.enqueueWork(() -> {
			${JavaModName}GeckoLibArmors.GeckoLibArmorItems.REGISTRY.register(bus);
		});
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.AddLayers event) {
		<#list animatedarmors as armor>
		GeoArmorRenderer.registerArmorRenderer(
			${armor.getModElement().getName()}Item.class, () -> new ${armor.getModElement().getName()}ArmorRenderer());
		</#list>
	}

	public class GeckoLibArmorItems {
		public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ${JavaModName}.MODID);

		<#list animatedarmors as item>
			<#if item.enableHelmet>
            public static final RegistryObject<${item.getModElement().getName()}Item> ${item.getModElement().getRegistryNameUpper()}_HELMET = 
				REGISTRY.register("${item.getModElement().getRegistryName()}_helmet", () -> new ${item.getModElement().getName()}Item(EquipmentSlot.HEAD, new Item.Properties().tab(${item.creativeTab})<#if item.helmetImmuneToFire>.fireResistant()</#if>));
	
			</#if>
			<#if item.enableBody>
            public static final RegistryObject<${item.getModElement().getName()}Item> ${item.getModElement().getRegistryNameUpper()}_CHESTPLATE =
				REGISTRY.register("${item.getModElement().getRegistryName()}_chestplate", () -> new ${item.getModElement().getName()}Item(EquipmentSlot.CHEST, new Item.Properties().tab(${item.creativeTab})<#if item.bodyImmuneToFire>.fireResistant()</#if>));
			</#if>
			<#if item.enableLeggings>
            public static final RegistryObject<${item.getModElement().getName()}Item> ${item.getModElement().getRegistryNameUpper()}_LEGGINGS =
				REGISTRY.register("${item.getModElement().getRegistryName()}_leggings", () -> new ${item.getModElement().getName()}Item(EquipmentSlot.LEGS, new Item.Properties().tab(${item.creativeTab})<#if item.leggingsImmuneToFire>.fireResistant()</#if>));
			</#if>
			<#if item.enableBoots>
            public static final RegistryObject<${item.getModElement().getName()}Item> ${item.getModElement().getRegistryNameUpper()}_BOOTS =
				REGISTRY.register("${item.getModElement().getRegistryName()}_boots", () -> new ${item.getModElement().getName()}Item(EquipmentSlot.FEET, new Item.Properties().tab(${item.creativeTab})<#if item.bootsImmuneToFire>.fireResistant()</#if>));
			</#if>
		</#list>
	}


}