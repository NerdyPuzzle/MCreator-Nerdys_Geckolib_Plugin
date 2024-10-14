package ${package}.init;

@EventBusSubscriber
public class ArmorAnimationFactory {

	@SubscribeEvent
	public static void animatedArmors(PlayerTickEvent.Post event) {
    	String animation = "";
		if(event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getItem() != (ItemStack.EMPTY).getItem() && event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof GeoItem) {
			if (!event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("geckoAnim").equals("")) {
				animation = event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("geckoAnim");
				CustomData.update(DataComponents.CUSTOM_DATA, event.getEntity().getItemBySlot(EquipmentSlot.HEAD), tag -> tag.putString("geckoAnim", ""));
				<#list animatedarmors as aarmor>
				if (event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ${aarmor.getModElement().getName()}Item animatable && event.getEntity().level().isClientSide())
					animatable.animationprocedure = animation;
				</#list>
			}
		}
		if(event.getEntity().getItemBySlot(EquipmentSlot.CHEST).getItem() != (ItemStack.EMPTY).getItem() && event.getEntity().getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof GeoItem) {
			if (!event.getEntity().getItemBySlot(EquipmentSlot.CHEST).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("geckoAnim").equals("")) {
				animation = event.getEntity().getItemBySlot(EquipmentSlot.CHEST).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("geckoAnim");
				CustomData.update(DataComponents.CUSTOM_DATA, event.getEntity().getItemBySlot(EquipmentSlot.CHEST), tag -> tag.putString("geckoAnim", ""));
				<#list animatedarmors as aarmor>
				if (event.getEntity().getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ${aarmor.getModElement().getName()}Item animatable && event.getEntity().level().isClientSide())
					animatable.animationprocedure = animation;
				</#list>
			}
		}
		if(event.getEntity().getItemBySlot(EquipmentSlot.LEGS).getItem() != (ItemStack.EMPTY).getItem() && event.getEntity().getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof GeoItem) {
			if (!event.getEntity().getItemBySlot(EquipmentSlot.LEGS).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("geckoAnim").equals("")) {
				animation = event.getEntity().getItemBySlot(EquipmentSlot.LEGS).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("geckoAnim");
				CustomData.update(DataComponents.CUSTOM_DATA, event.getEntity().getItemBySlot(EquipmentSlot.HEAD), tag -> tag.putString("geckoAnim", ""));
				<#list animatedarmors as aarmor>
				if (event.getEntity().getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ${aarmor.getModElement().getName()}Item animatable && event.getEntity().level().isClientSide())
					animatable.animationprocedure = animation;
				</#list>
			}
		}
		if(event.getEntity().getItemBySlot(EquipmentSlot.FEET).getItem() != (ItemStack.EMPTY).getItem() && event.getEntity().getItemBySlot(EquipmentSlot.FEET).getItem() instanceof GeoItem) {
			if (!event.getEntity().getItemBySlot(EquipmentSlot.FEET).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("geckoAnim").equals("")) {
				animation = event.getEntity().getItemBySlot(EquipmentSlot.FEET).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("geckoAnim");
				CustomData.update(DataComponents.CUSTOM_DATA, event.getEntity().getItemBySlot(EquipmentSlot.FEET), tag -> tag.putString("geckoAnim", ""));
				<#list animatedarmors as aarmor>
				if (event.getEntity().getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ${aarmor.getModElement().getName()}Item animatable && event.getEntity().level().isClientSide())
					animatable.animationprocedure = animation;
				</#list>
			}
	    }
	}
}