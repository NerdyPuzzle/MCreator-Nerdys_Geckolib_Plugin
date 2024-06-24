package ${package}.init;

@Mod.EventBusSubscriber
public class ArmorAnimationFactory {

	@SubscribeEvent
	public static void animatedArmors(TickEvent.PlayerTickEvent event) {
		String animation = "";
		if (event.phase == TickEvent.Phase.END) {
			if(event.player.getItemBySlot(EquipmentSlot.HEAD).getItem() != (ItemStack.EMPTY).getItem() && event.player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof GeoItem) {
				if (!event.player.getItemBySlot(EquipmentSlot.HEAD).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("geckoAnim").equals("")) {
					animation = event.player.getItemBySlot(EquipmentSlot.HEAD).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("geckoAnim");
					CustomData.update(DataComponents.CUSTOM_DATA, event.player.getItemBySlot(EquipmentSlot.HEAD), tag -> tag.putString("geckoAnim", ""));
					<#list animatedarmors as aarmor>
					if (event.player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ${aarmor.getModElement().getName()}Item animatable && event.player.level().isClientSide())
						animatable.animationprocedure = animation;
					</#list>
				}
			}
			if(event.player.getItemBySlot(EquipmentSlot.CHEST).getItem() != (ItemStack.EMPTY).getItem() && event.player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof GeoItem) {
				if (!event.player.getItemBySlot(EquipmentSlot.CHEST).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("geckoAnim").equals("")) {
					animation = event.player.getItemBySlot(EquipmentSlot.CHEST).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("geckoAnim");
					CustomData.update(DataComponents.CUSTOM_DATA, event.player.getItemBySlot(EquipmentSlot.CHEST), tag -> tag.putString("geckoAnim", ""));
					<#list animatedarmors as aarmor>
					if (event.player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ${aarmor.getModElement().getName()}Item animatable && event.player.level().isClientSide())
						animatable.animationprocedure = animation;
					</#list>
				}
			}
			if(event.player.getItemBySlot(EquipmentSlot.LEGS).getItem() != (ItemStack.EMPTY).getItem() && event.player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof GeoItem) {
				if (!event.player.getItemBySlot(EquipmentSlot.LEGS).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("geckoAnim").equals("")) {
					animation = event.player.getItemBySlot(EquipmentSlot.LEGS).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("geckoAnim");
					CustomData.update(DataComponents.CUSTOM_DATA, event.player.getItemBySlot(EquipmentSlot.HEAD), tag -> tag.putString("geckoAnim", ""));
					<#list animatedarmors as aarmor>
					if (event.player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ${aarmor.getModElement().getName()}Item animatable && event.player.level().isClientSide())
						animatable.animationprocedure = animation;
					</#list>
				}
			}
			if(event.player.getItemBySlot(EquipmentSlot.FEET).getItem() != (ItemStack.EMPTY).getItem() && event.player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof GeoItem) {
				if (!event.player.getItemBySlot(EquipmentSlot.FEET).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("geckoAnim").equals("")) {
					animation = event.player.getItemBySlot(EquipmentSlot.FEET).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("geckoAnim");
					CustomData.update(DataComponents.CUSTOM_DATA, event.player.getItemBySlot(EquipmentSlot.FEET), tag -> tag.putString("geckoAnim", ""));
					<#list animatedarmors as aarmor>
					if (event.player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ${aarmor.getModElement().getName()}Item animatable && event.player.level().isClientSide())
						animatable.animationprocedure = animation;
					</#list>
				}
			}		
		}
	}
}