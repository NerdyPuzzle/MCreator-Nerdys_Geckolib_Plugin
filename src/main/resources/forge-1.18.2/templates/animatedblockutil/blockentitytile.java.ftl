package ${package}.block.entity;

import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;

public class ${name}TileEntity extends BlockEntity implements IAnimatable {
	public AnimationFactory factory = GeckoLibUtil.createFactory(this);

	public ${name}TileEntity(BlockPos pos, BlockState state) {
		super(TileRegistry.${data.getModElement().getRegistryNameUpper()}.get(), pos, state);
	}

	private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
	String animationprocedure = (""
    		+ ((this.getBlockState()).getBlock().getStateDefinition().getProperty("animation") instanceof IntegerProperty _getip1
    				? (this.getBlockState()).getValue(_getip1)
    				: 0));
		if (animationprocedure == "0") {
		event.getController().setAnimation(new AnimationBuilder().addAnimation(animationprocedure, EDefaultLoopTypes.LOOP));
		return PlayState.CONTINUE;
		}
	return PlayState.STOP;
	}

	private <E extends BlockEntity & IAnimatable> PlayState procedurePredicate(AnimationEvent<E> event) {
	String animationprocedure = (""
    		+ ((this.getBlockState()).getBlock().getStateDefinition().getProperty("animation") instanceof IntegerProperty _getip1
    				? (this.getBlockState()).getValue(_getip1)
    				: 0));
		if (!(animationprocedure == "0") && event.getController().getAnimationState().equals(software.bernie.geckolib3.core.AnimationState.Stopped)) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation(animationprocedure, EDefaultLoopTypes.PLAY_ONCE));
	        if (event.getController().getAnimationState().equals(software.bernie.geckolib3.core.AnimationState.Stopped)) {
				if (this.getBlockState().getBlock().getStateDefinition().getProperty("animation") instanceof IntegerProperty _integerProp)
					level.setBlock(this.getBlockPos(), this.getBlockState().setValue(_integerProp, 0), 3);
			event.getController().markNeedsReload();
		}
		}  
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<${name}TileEntity>(this, "controller", 0, this::predicate));
		data.addAnimationController(new AnimationController<${name}TileEntity>(this, "procedurecontroller", 0, this::procedurePredicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}
}
