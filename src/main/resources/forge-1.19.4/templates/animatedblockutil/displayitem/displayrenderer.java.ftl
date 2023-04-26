package ${package}.block.renderer;

public class ${name}DisplayItemRenderer extends GeoItemRenderer<${name}DisplayItem> {
    public ${name}DisplayItemRenderer() {
        super(new ${name}DisplayModel());
    }

	@Override
	public RenderType getRenderType(${name}DisplayItem animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}