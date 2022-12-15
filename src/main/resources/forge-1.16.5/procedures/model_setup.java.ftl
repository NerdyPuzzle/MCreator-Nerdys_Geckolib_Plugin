	public class ${name}Procedure extends AnimatedGeoModel<${field$name}Entity.CustomEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(${field$name}Entity.CustomEntity entity) {
        return new ResourceLocation("${modid}", "animations/${field$model}.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(${field$name}Entity.CustomEntity entity) {
        return new ResourceLocation("${modid}", "geo/${field$model}.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(${field$name}Entity.CustomEntity entity) {
        return new ResourceLocation("${modid}", "textures/entities/${field$texture}.png");

}
    ${field$headanim}
	@Override
	public void setCustomAnimations(${field$name}Entity.CustomEntity animatable, int instanceId, AnimationEvent animationEvent) {
	super.setCustomAnimations(animatable, instanceId, animationEvent);
	IBone head = this.getAnimationProcessor().getBone("head");
	EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
	AnimationData manager = animatable.getFactory().getOrCreateAnimationData(instanceId);
	head.setRotationX(head.getRotationX() + extraData.headPitch * ((float) Math.PI / 180F));
	head.setRotationY(head.getRotationY() + extraData.netHeadYaw * ((float) Math.PI / 180F));
    }
     /** **/
}