package ${package}.init;


@Mod.EventBusSubscriber
public class ItemAnimationFactory {

	public static void disableUseAnim() {
	   	try {
		    ItemInHandRenderer renderer = Minecraft.getInstance().gameRenderer.itemInHandRenderer;
		    if (renderer != null) {
		        renderer.mainHandHeight = 1F;
		        renderer.oMainHandHeight = 1F;
		        renderer.offHandHeight = 1F;
		        renderer.oOffHandHeight = 1F;
			}
		} catch(Exception e) {
		    e.printStackTrace();
		}
	}

	@SubscribeEvent
	public static void animatedItems(TickEvent.PlayerTickEvent event) {
		String animation = "";
		if (event.phase == TickEvent.Phase.START && (event.player.getMainHandItem().getItem() instanceof GeoItem || event.player.getOffhandItem().getItem() instanceof GeoItem)) {
			if (!event.player.getMainHandItem().getOrCreateTag().getString("geckoAnim").equals("") && !(event.player.getMainHandItem().getItem() instanceof ArmorItem)) {
				animation = event.player.getMainHandItem().getOrCreateTag().getString("geckoAnim");
				event.player.getMainHandItem().getOrCreateTag().putString("geckoAnim", "");
				<#list animateditems as aitem>
				if (event.player.getMainHandItem().getItem() instanceof ${aitem.getModElement().getName()}Item animatable)
				if (event.player.level.isClientSide()) {
					animatable.animationprocedure = animation;
					disableUseAnim();
				}
				</#list>
			}
			if (!event.player.getOffhandItem().getOrCreateTag().getString("geckoAnim").equals("") && !(event.player.getOffhandItem().getItem() instanceof ArmorItem)) {
				animation = event.player.getOffhandItem().getOrCreateTag().getString("geckoAnim");
				event.player.getOffhandItem().getOrCreateTag().putString("geckoAnim", "");
				<#list animateditems as aitem>
				if (event.player.getOffhandItem().getItem() instanceof ${aitem.getModElement().getName()}Item animatable)
				if (event.player.level.isClientSide()) {
					animatable.animationprocedure = animation;
					disableUseAnim();
				}
				</#list>
			}
		}
	}

}