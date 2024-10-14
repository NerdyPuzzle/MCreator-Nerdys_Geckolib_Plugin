package ${package}.init;

@EventBusSubscriber
public class ItemAnimationFactory {

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
				    }
                }
			}
			if (offhandItem.getItem() instanceof ${item.getModElement().getName()}Item animatable) {
				animation = offhandItem.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("geckoAnim");
				if (!animation.isEmpty()) {
				    CustomData.update(DataComponents.CUSTOM_DATA, event.getEntity().getOffhandItem(), tag -> tag.putString("geckoAnim", ""));
				    if (event.getEntity().level().isClientSide()) {
					    ((${item.getModElement().getName()}Item)event.getEntity().getOffhandItem().getItem()).animationprocedure = animation;
				    }
                }
			}
		</#list>
		}
	}

}