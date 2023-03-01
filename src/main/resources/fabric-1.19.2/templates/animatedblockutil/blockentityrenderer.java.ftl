package ${package}.block.renderer;

public class ${name}TileRenderer extends GeoBlockRenderer<${name}TileEntity> {
	public ${name}TileRenderer() {
		super(new ${name}BlockModel());
	}

	@Override
	public RenderType getRenderType(${name}TileEntity animatable, float partialTick, PoseStack poseStack,
									MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight,
									ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}