package ${package}.utils;

public class AnimUtils {

	public static void renderPartOverBone(ModelPart model, GeoBone bone, PoseStack stack, VertexConsumer buffer, int packedLightIn,
			int packedOverlayIn) {
		setupModelFromBone(model, bone);
		model.render(stack, buffer, packedLightIn, packedOverlayIn);
	}

	public static void setupModelFromBone(ModelPart model, GeoBone bone) {
		model.setPos(bone.getPivotX(), bone.getPivotY(), bone.getPivotZ());
		model.xRot = 0.0f;
		model.yRot = 0.0f;
		model.zRot = 0.0f;
	}

}
