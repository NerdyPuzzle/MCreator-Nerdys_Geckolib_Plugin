package ${package}.block.renderer;

public class ${name}TileRenderer extends GeoBlockRenderer<${name}TileEntity> {
	public ${name}TileRenderer() {
		super(new ${name}BlockModel());
	}

	@Override
	public RenderType getRenderType(${name}TileEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}