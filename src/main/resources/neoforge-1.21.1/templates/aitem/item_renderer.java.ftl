package ${package}.item.renderer;

public class ${name}ItemRenderer extends GeoItemRenderer<${name}Item> {
    public ${name}ItemRenderer() {
        super(new ${name}ItemModel());
    }

	@Override
	public RenderType getRenderType(${name}Item animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	private static final float SCALE_RECIPROCAL = 1.0f / 16.0f;
	protected boolean renderArms = false;
	protected MultiBufferSource currentBuffer;
	protected RenderType renderType;
	public ItemDisplayContext transformType;
	protected ${name}Item animatable;
	private final Set<String> hiddenBones = new HashSet<>();
	private final Set<String> suppressedBones = new HashSet<>();


	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack matrixStack, MultiBufferSource bufferIn, int combinedLightIn, int p_239207_6_) {
		this.transformType = transformType;
		super.renderByItem(stack, transformType, matrixStack, bufferIn, combinedLightIn, p_239207_6_);
	}

	@Override
	public void actuallyRender(PoseStack matrixStackIn, ${name}Item animatable, BakedGeoModel model, RenderType type, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, boolean isRenderer, float partialTicks, int packedLightIn, int packedOverlayIn, int color) {
		this.currentBuffer = renderTypeBuffer;
		this.renderType = type;
		this.animatable = animatable;
		super.actuallyRender(matrixStackIn, animatable, model, type, renderTypeBuffer, vertexBuilder, isRenderer, partialTicks, packedLightIn, packedOverlayIn, color);
		if (this.renderArms) {
			this.renderArms = false;
		}
	}

	<#if data.firstPersonArms>
	@Override
	public void renderRecursively(PoseStack stack, ${name}Item animatable, GeoBone bone, RenderType type, MultiBufferSource buffer, VertexConsumer bufferIn, boolean isReRender, float partialTick, int packedLightIn, int packedOverlayIn, int color) {
		Minecraft mc = Minecraft.getInstance();
		String name = bone.getName();
		boolean renderingArms = false;
		if (name.equals("${data.leftArm}") || name.equals("${data.rightArm}")) {
			bone.setHidden(true);
			renderingArms = true;
		} else {
			bone.setHidden(this.hiddenBones.contains(name));
		}
		if (this.transformType.firstPerson() && renderingArms) {
			AbstractClientPlayer player = mc.player;
			PlayerRenderer playerRenderer = (PlayerRenderer) mc.getEntityRenderDispatcher().getRenderer(player);
			PlayerModel<AbstractClientPlayer> model = playerRenderer.getModel();
			stack.pushPose();
			RenderUtil.translateMatrixToBone(stack, bone);
			RenderUtil.translateToPivotPoint(stack, bone);
			RenderUtil.rotateMatrixAroundBone(stack, bone);
			RenderUtil.scaleMatrixForBone(stack, bone);
			RenderUtil.translateAwayFromPivotPoint(stack, bone);
			ResourceLocation loc = player.getSkin().texture();
			if (name.equals("${data.leftArm}")) {
				stack.translate(-1.0f * SCALE_RECIPROCAL, 2.0f * SCALE_RECIPROCAL, 0.0f);
				if (!player.isInvisible()) {
				    AnimUtils.renderPartOverBone(model.leftArm, bone, stack, this.currentBuffer.getBuffer(RenderType.entitySolid(loc)), packedLightIn, OverlayTexture.NO_OVERLAY);
				    AnimUtils.renderPartOverBone(model.leftSleeve, bone, stack, this.currentBuffer.getBuffer(RenderType.entityTranslucent(loc)), packedLightIn, OverlayTexture.NO_OVERLAY);
			    }
			} else if (name.equals("${data.rightArm}")) {
				stack.translate(1.0f * SCALE_RECIPROCAL, 2.0f * SCALE_RECIPROCAL, 0.0f);
				if (!player.isInvisible()) {
				    AnimUtils.renderPartOverBone(model.rightArm, bone, stack, this.currentBuffer.getBuffer(RenderType.entitySolid(loc)), packedLightIn, OverlayTexture.NO_OVERLAY);
				    AnimUtils.renderPartOverBone(model.rightSleeve, bone, stack, this.currentBuffer.getBuffer(RenderType.entityTranslucent(loc)), packedLightIn, OverlayTexture.NO_OVERLAY);
			    }
			}
			stack.popPose();
		}
		super.renderRecursively(stack, animatable, bone, type, buffer, bufferIn, isReRender, partialTick, packedLightIn, packedOverlayIn, color);
	}
	</#if>

	@Override
	public ResourceLocation getTextureLocation(${name}Item instance) {
		return super.getTextureLocation(instance);
	}


}