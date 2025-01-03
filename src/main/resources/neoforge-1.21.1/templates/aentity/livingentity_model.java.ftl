package ${package}.entity.model;

import software.bernie.geckolib.animation.AnimationState;

public class ${name}Model extends GeoModel<${name}Entity> {
    @Override
    public ResourceLocation getAnimationResource(${name}Entity entity) {
        return ResourceLocation.parse("${modid}:animations/${data.model?replace(".geo.json", "")}.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(${name}Entity entity) {
        return ResourceLocation.parse("${modid}:geo/${data.model}");
    }

    @Override
    public ResourceLocation getTextureResource(${name}Entity entity) {
        return ResourceLocation.parse("${modid}:textures/entities/" + entity.getTexture() + ".png");
    }
     
    <#if data.headMovement>
    @Override
    public void setCustomAnimations(${name}Entity animatable, long instanceId, AnimationState animationState) {
	    GeoBone head = getAnimationProcessor().getBone("${data.groupName}");
	    if (head != null) {
		    EntityModelData entityData = (EntityModelData) animationState.getData(DataTickets.ENTITY_MODEL_DATA);
			head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
			head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
		}
	
    }
    </#if>
}