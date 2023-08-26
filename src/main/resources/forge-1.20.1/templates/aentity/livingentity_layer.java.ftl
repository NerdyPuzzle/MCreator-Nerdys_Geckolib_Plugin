package ${package}.entity.layer;

public class ${name}Layer extends GeoRenderLayer<${name}Entity> {
    private static final ResourceLocation LAYER = new ResourceLocation("${modid}", "textures/entities/${data.mobModelGlowTexture}");

    public ${name}Layer(GeoRenderer<${name}Entity> entityRenderer) {
        super(entityRenderer);
    }
    @Override
    public void render(PoseStack poseStack,${name}Entity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType glowRenderType = RenderType.eyes(LAYER);
        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, glowRenderType,
                bufferSource.getBuffer(glowRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1);
    }
}