package ${package}.init;

@EventBusSubscriber
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
	public static void animatedItems(PlayerTickEvent.Post event) {
		String animation = "";
		ItemStack mainhandItem = event.getEntity().getMainHandItem().copy();
		ItemStack offhandItem = event.getEntity().getOffhandItem().copy();
		if (mainhandItem.getItem() instanceof GeoItem || offhandItem.getItem() instanceof GeoItem) {
		<#list animateditems as item>
			if (mainhandItem.getItem() instanceof ${item.getModElement().getName()}Item animatable) {
				animation = mainhandItem.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("geckoAnim");
				if (!animation.isEmpty()) {
				    CustomData.update(DataComponents.CUSTOM_DATA, event.getEntity().getMainHandItem(), tag -> tag.putString("geckoAnim", ""));
				    if (event.getEntity().level().isClientSide()) {
					    ((${item.getModElement().getName()}Item)event.getEntity().getMainHandItem().getItem()).animationprocedure = animation;
					    <#if item.disableSwing == false>
					        disableUseAnim("right");
					    </#if>
				    }
                }
			}
			if (offhandItem.getItem() instanceof ${item.getModElement().getName()}Item animatable) {
				animation = offhandItem.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("geckoAnim");
				if (!animation.isEmpty()) {
				    CustomData.update(DataComponents.CUSTOM_DATA, event.getEntity().getOffhandItem(), tag -> tag.putString("geckoAnim", ""));
				    if (event.getEntity().level().isClientSide()) {
					    ((${item.getModElement().getName()}Item)event.getEntity().getOffhandItem().getItem()).animationprocedure = animation;
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