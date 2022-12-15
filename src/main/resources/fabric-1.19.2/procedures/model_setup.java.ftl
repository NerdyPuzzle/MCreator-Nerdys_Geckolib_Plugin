**/

	public class ${name}Procedure extends AnimatedGeoModel<${field$name}Entity> {
    @Override
    public ResourceLocation getAnimationResource(${field$name}Entity entity) {
        return new ResourceLocation("${modid}", "animations/${field$model}.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(${field$name}Entity entity) {
        return new ResourceLocation("${modid}", "geo/${field$model}.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(${field$name}Entity entity) {
        return new ResourceLocation("${modid}", "textures/entities/${field$texture}.png");
    
     
    ${field$headanim}
    }
    @Override
    public void setCustomAnimations(${field$name}Entity animatable, int instanceId, AnimationEvent animationEvent) {
	super.setCustomAnimations(animatable, instanceId, animationEvent);
	IBone head = this.getAnimationProcessor().getBone("head");
	EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
	AnimationData manager = animatable.getFactory().getOrCreateAnimationData(instanceId);
	int unpausedMultiplier = !Minecraft.getInstance().isPaused() || manager.shouldPlayWhilePaused ? 1 : 0;
	head.setRotationX(head.getRotationX() + extraData.headPitch * ((float) Math.PI / 180F) * unpausedMultiplier);
	head.setRotationY(head.getRotationY() + extraData.netHeadYaw * ((float) Math.PI / 180F) * unpausedMultiplier);

     /** **/