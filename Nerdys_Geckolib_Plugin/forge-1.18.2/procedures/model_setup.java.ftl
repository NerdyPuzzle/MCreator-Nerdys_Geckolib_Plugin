**/

	public class ${name}Procedure extends AnimatedGeoModel<${field$name}Entity> {
    @Override
    public ResourceLocation getAnimationFileLocation(${field$name}Entity entity) {
        return new ResourceLocation("${modid}", "animations/${field$model}.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(${field$name}Entity entity) {
        return new ResourceLocation("${modid}", "geo/${field$model}.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(${field$name}Entity entity) {
        return new ResourceLocation("${modid}", "textures/entities/${field$texture}.png");


    ${field$headanim}
    }
     @Override
     public void setLivingAnimations(${field$name}Entity entity, Integer uniqueID, AnimationEvent customPredicate) {
     super.setLivingAnimations(entity, uniqueID, customPredicate);
     IBone head = this.getAnimationProcessor().getBone("head");

     EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

     AnimationData manager = entity.getFactory().getOrCreateAnimationData(uniqueID);
     int unpausedMultiplier = !Minecraft.getInstance().isPaused() || manager.shouldPlayWhilePaused ? 1 : 0;

     head.setRotationX(head.getRotationX() + extraData.headPitch * ((float) Math.PI / 180F) * unpausedMultiplier);
     head.setRotationY(head.getRotationY() + extraData.netHeadYaw * ((float) Math.PI / 180F) * unpausedMultiplier);
    
     /** **/
