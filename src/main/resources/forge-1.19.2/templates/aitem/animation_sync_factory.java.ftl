package ${package}.init;

@Mod.EventBusSubscriber
public class ItemAnimationFactory {

	public static void disableUseAnim(String hand) {
		ItemInHandRenderer renderer = Minecraft.getInstance().gameRenderer.itemInHandRenderer;
		if (renderer != null) {
		    if (hand.equals("right")) {
		        renderer.mainHandHeight = 1F;
		        renderer.oMainHandHeight = 1F;
		    }
		    if (hand.equals("left")) {
		        renderer.offHandHeight = 1F;
		        renderer.oOffHandHeight = 1F;
		    }
	    }
	}

	@SubscribeEvent
	public static void animatedItems(TickEvent.PlayerTickEvent event) {
		String animation = "";
		ItemStack mainhandItem = event.player.getMainHandItem().copy();
		ItemStack offhandItem = event.player.getOffhandItem().copy();
		if (event.phase == TickEvent.Phase.START && (mainhandItem.getItem() instanceof GeoItem || offhandItem.getItem() instanceof GeoItem)) {
		<#list animateditems as item>
			if (mainhandItem.getItem() instanceof ${item.getModElement().getName()}Item animatable) {
				animation = mainhandItem.getOrCreateTag().getString("geckoAnim");
				if (!animation.isEmpty()) {
				    event.player.getMainHandItem().getOrCreateTag().putString("geckoAnim", "");
				    if (event.player.level.isClientSide()) {
					    ((${item.getModElement().getName()}Item)event.player.getMainHandItem().getItem()).animationprocedure = animation;
					    <#if item.disableSwing == false>
					        disableUseAnim("right");
					    </#if>
				    }
                }
			}
			if (offhandItem.getItem() instanceof ${item.getModElement().getName()}Item animatable) {
				animation = offhandItem.getOrCreateTag().getString("geckoAnim");
				if (!animation.isEmpty()) {
				    event.player.getOffhandItem().getOrCreateTag().putString("geckoAnim", "");
				    if (event.player.level.isClientSide()) {
					    ((${item.getModElement().getName()}Item)event.player.getOffhandItem().getItem()).animationprocedure = animation;
					    <#if item.disableSwing == false>
					        disableUseAnim("left");
					    </#if>
				    }
                }
			}
		</#list>
		}
	}

}