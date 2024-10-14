package ${package}.client.renderer;

public class ${name}ArmorRenderer extends GeoArmorRenderer<${name}Item> {
	public ${name}ArmorRenderer() {
		super(new ${name}Model());

		this.head = new GeoBone(null, "${data.head}", false, (double) 0, false, false);
		this.body = new GeoBone(null, "${data.chest}", false, (double) 0, false, false);
		this.rightArm = new GeoBone(null, "${data.rightArm}", false, (double) 0, false, false);
		this.leftArm = new GeoBone(null, "${data.leftArm}", false, (double) 0, false, false);
		this.rightLeg = new GeoBone(null, "${data.rightLeg}", false, (double) 0, false, false);
		this.leftLeg = new GeoBone(null, "${data.leftLeg}", false, (double) 0, false, false);
		this.rightBoot = new GeoBone(null, "${data.rightBoot}", false, (double) 0, false, false);
		this.leftBoot = new GeoBone(null, "${data.leftBoot}", false, (double) 0, false, false);
	}

	@Override
	public RenderType getRenderType(${name}Item animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}