package ${package}.client.renderer;

public class ${name}ArmorRenderer extends GeoArmorRenderer<${name}Item> {
	public ${name}ArmorRenderer() {
		super(new ${name}Model());

		this.headBone = "${data.head}";
		this.bodyBone = "${data.chest}";
		this.rightArmBone = "${data.rightArm}";
		this.leftArmBone = "${data.leftArm}";
		this.rightLegBone = "${data.rightLeg}";
		this.leftLegBone = "${data.leftLeg}";
		this.rightBootBone = "${data.rightBoot}";
		this.leftBootBone = "${data.leftBoot}";
	}

	@Override
	public RenderType getRenderType(${name}Item animatable, float partialTick, PoseStack poseStack,
	MultiBufferSource bufferSource, VertexConsumer buffer, int 											packedLight, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}