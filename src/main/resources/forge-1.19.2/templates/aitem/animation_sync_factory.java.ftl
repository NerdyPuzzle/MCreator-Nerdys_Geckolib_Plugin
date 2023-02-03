package ${package}.init;


@Mod.EventBusSubscriber
public class ItemAnimationFactory {

	@SubscribeEvent
	public static void animatedItems(TickEvent.PlayerTickEvent event) {
		String animation = "";
		if (event.phase == TickEvent.Phase.END && (event.player.getMainHandItem().getItem() instanceof IAnimatable || event.player.getOffhandItem().getItem() instanceof IAnimatable)) {
			if (!event.player.getMainHandItem().getOrCreateTag().getString("geckoAnim").equals("") && !(event.player.getMainHandItem().getItem() instanceof GeoArmorItem)) {
				animation = event.player.getMainHandItem().getOrCreateTag().getString("geckoAnim");
				event.player.getMainHandItem().getOrCreateTag().putString("geckoAnim", "");
				<#list animateditems as aitem>
				if (event.player.getMainHandItem().getItem() instanceof ${aitem.getModElement().getName()}Item animatable)
				if (event.player.level.isClientSide())
					animatable.animationprocedure = animation;
				</#list>
			}
			if (!event.player.getOffhandItem().getOrCreateTag().getString("geckoAnim").equals("") && !(event.player.getOffhandItem().getItem() instanceof GeoArmorItem)) {
				animation = event.player.getOffhandItem().getOrCreateTag().getString("geckoAnim");
				event.player.getOffhandItem().getOrCreateTag().putString("geckoAnim", "");
				<#list animateditems as aitem>
				if (event.player.getOffhandItem().getItem() instanceof ${aitem.getModElement().getName()}Item animatable)
				if (event.player.level.isClientSide())
					animatable.animationprocedure = animation;
				</#list>
			}
		}
	}

}