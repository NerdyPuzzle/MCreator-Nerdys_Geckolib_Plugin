package ${package}.block.display;

public class ${name}DisplayItem extends BlockItem implements IAnimatable {
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public ${name}DisplayItem(Block block, Properties settings) {
        super(block, settings);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}