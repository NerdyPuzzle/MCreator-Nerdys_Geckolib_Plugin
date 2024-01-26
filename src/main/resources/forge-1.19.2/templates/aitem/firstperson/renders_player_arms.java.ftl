package ${package}.interfaces;

public interface RendersPlayerArms {

	void setRenderArms(boolean renderArms);

	boolean shouldAllowHandRender(ItemStack mainhand, ItemStack offhand, InteractionHand renderingHand);

}
