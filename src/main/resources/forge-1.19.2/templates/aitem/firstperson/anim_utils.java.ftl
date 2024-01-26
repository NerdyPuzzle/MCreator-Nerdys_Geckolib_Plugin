package ${package}.utils;

public class AnimUtils {
	public static void renderPartOverBone(ModelPart model, GeoBone bone, PoseStack stack, VertexConsumer buffer, int packedLightIn,
			int packedOverlayIn, float alpha) {
		renderPartOverBone(model, bone, stack, buffer, packedLightIn, packedOverlayIn, 1.0f, 1.0f, 1.0f, alpha);
	}

	public static void renderPartOverBone(ModelPart model, GeoBone bone, PoseStack stack, VertexConsumer buffer, int packedLightIn,
			int packedOverlayIn, float r, float g, float b, float a) {
		setupModelFromBone(model, bone);
		model.render(stack, buffer, packedLightIn, packedOverlayIn, r, g, b, a);
	}

	public static void setupModelFromBone(ModelPart model, GeoBone bone) {
		model.setPos(bone.rotationPointX, bone.rotationPointY, bone.rotationPointZ);
		model.xRot = 0.0f;
		model.yRot = 0.0f;
		model.zRot = 0.0f;
	}

	@SuppressWarnings("unchecked")
	public static <L extends RenderLayer<?, ?>> List<L> getLayers(Class<L> clazz, LivingEntityRenderer<?, ?> renderer) {
		Field layerField = ObfuscationReflectionHelper.findField(LivingEntityRenderer.class, "f_115291_");
		List<L> result = new ArrayList<>();
		try {
			List<RenderLayer<?, ?>> layers = (List<RenderLayer<?, ?>>) layerField.get(renderer);
			for (int i = 0; i < layers.size(); ++i) {
				RenderLayer<?, ?> layer = layers.get(i);
				Class<?> layerClass = layer.getClass();
				if (layerClass.equals(clazz)) {
					result.add((L) layer);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Something went wrong");
		}
		return result;
	}

	public static void hideLayers(Class<?> clazz, LivingEntityRenderer<?, ?> renderer) {
		// I want to throw up after this
		Field layerField = ObfuscationReflectionHelper.findField(LivingEntityRenderer.class, "f_115291_");
		try {
			@SuppressWarnings("unchecked")
			List<RenderLayer<?, ?>> layers = (List<RenderLayer<?, ?>>) layerField.get(renderer);
			for (int i = 0; i < layers.size(); ++i) {
				RenderLayer<?, ?> layer = layers.get(i);
				Class<?> layerClass = layer.getClass();
				if (layerClass.equals(clazz))
					layers.set(i, new NothingLayer<>(renderer, layer));
			}
		} catch (Exception e) {
			throw new RuntimeException("Something went wrong");
		}
	}

	public static void restoreLayers(LivingEntityRenderer<?, ?> renderer) {
		// I also want to throw up after this
		Field layerField = ObfuscationReflectionHelper.findField(LivingEntityRenderer.class, "f_115291_");
		try {
			@SuppressWarnings("unchecked")
			List<RenderLayer<?, ?>> layers = (List<RenderLayer<?, ?>>) layerField.get(renderer);
			for (int i = 0; i < layers.size(); ++i) {
				RenderLayer<?, ?> layer = layers.get(i);
				if (layer instanceof NothingLayer) {
					layers.set(i, ((NothingLayer<?, ?>) layer).getReplacedLayer());
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Something went wrong");
		}
	}
}
