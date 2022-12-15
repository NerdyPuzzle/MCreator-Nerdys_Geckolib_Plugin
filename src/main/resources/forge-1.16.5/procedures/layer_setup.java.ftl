public class ${name}Procedure extends GeoLayerRenderer {

    private static final ResourceLocation LAYER = new ResourceLocation("${modid}", "textures/entities/${field$texture}.png");
    private static final ResourceLocation MODEL = new ResourceLocation("${modid}", "geo/${field$model}.geo.json");

	public ${name}Procedure(IGeoRenderer<?> entityRendererIn) {
        super(entityRendererIn);
    }
    
	@Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType cameo =  RenderType.getEyes(LAYER);
        matrixStackIn.push();
        matrixStackIn.scale(1.0f, 1.0f, 1.0f);
        matrixStackIn.translate(0.0d, 0.0d, 0.0d);
        this.getRenderer().render(this.getEntityModel().getModel(MODEL), entityLivingBaseIn, partialTicks, cameo, matrixStackIn, bufferIn,
                bufferIn.getBuffer(cameo), packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
        matrixStackIn.pop();
    }
}