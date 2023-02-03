package ${package}.entity.model;

public class ${name}Model extends AnimatedGeoModel<${name}Entity> {
    @Override
    public ResourceLocation getAnimationFileLocation(${name}Entity entity) {
        return new ResourceLocation("${modid}", "animations/${data.model?replace(".geo.json", "")}.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(${name}Entity entity) {
        return new ResourceLocation("${modid}", "geo/${data.model}");
    }

    @Override
    public ResourceLocation getTextureLocation(${name}Entity entity) {
        return new ResourceLocation("${modid}", "textures/entities/" + entity.getTexture() + ".png");
    }
     
    <#if data.headMovement>
    @Override
    public void setCustomAnimations(${name}Entity animatable, int instanceId, AnimationEvent animationEvent) {
	super.setCustomAnimations(animatable, instanceId, animationEvent);
	IBone head = this.getAnimationProcessor().getBone("${data.groupName}");
	EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
	AnimationData manager = animatable.getFactory().getOrCreateAnimationData(instanceId);
	int unpausedMultiplier = !Minecraft.getInstance().isPaused() || manager.shouldPlayWhilePaused ? 1 : 0;
	head.setRotationX(head.getRotationX() + extraData.headPitch * ((float) Math.PI / 180F) * unpausedMultiplier);
	head.setRotationY(head.getRotationY() + extraData.netHeadYaw * ((float) Math.PI / 180F) * unpausedMultiplier);
    }
     </#if>
}