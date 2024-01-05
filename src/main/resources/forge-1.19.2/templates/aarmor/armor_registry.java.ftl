package ${package}.init;

@Mod.EventBusSubscriber(modid = ${JavaModName}.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ${JavaModName}GeckoLibArmors {

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.AddLayers event) {
		<#list animatedarmors as armor>
		GeoArmorRenderer.registerArmorRenderer(
			${armor.getModElement().getName()}Item.class, () -> new ${armor.getModElement().getName()}ArmorRenderer());
		</#list>
	}

}