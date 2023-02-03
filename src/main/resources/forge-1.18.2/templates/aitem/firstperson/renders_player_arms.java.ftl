package ${package}.interfaces

//public interface RendersPlayerArms {
//public interface RendersPlayerArms {
//public interface RendersPlayerArms {
//public interface RendersPlayerArms {

	//void setRenderArms(boolean renderArms);

public interface RendersPlayerArms {

	void setRenderArms(boolean renderArms);

	boolean shouldAllowHandRender(ItemStack mainhand, ItemStack offhand, InteractionHand renderingHand);

}
