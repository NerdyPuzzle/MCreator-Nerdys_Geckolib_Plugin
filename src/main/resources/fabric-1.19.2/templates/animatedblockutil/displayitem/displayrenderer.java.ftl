package ${package}.block.renderer;


public class ${name}DisplayItemRenderer extends GeoItemRenderer<${name}DisplayItem> {
    public ${name}DisplayItemRenderer() {
        super(new ${name}DisplayModel());
    }

	@Override
	public RenderType getRenderType(${name}DisplayItem animatable, float partialTick, PoseStack poseStack,
									MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight,
									ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}