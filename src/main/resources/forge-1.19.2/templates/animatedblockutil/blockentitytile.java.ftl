package ${package}.block.entity;

<#assign regname = data.getModElement().getRegistryName()>

import javax.annotation.Nullable;

import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;

public class ${name}TileEntity extends RandomizableContainerBlockEntity implements IAnimatable, WorldlyContainer {
	public AnimationFactory factory = GeckoLibUtil.createFactory(this);
	private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(${data.inventorySize}, ItemStack.EMPTY);
	private final LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.values());

	public ${name}TileEntity(BlockPos pos, BlockState state) {
		super(TileRegistry.${(regname)?upper_case}.get(), pos, state);
	}

	private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
	String animationprocedure = (""
    		+ ((this.getBlockState()).getBlock().getStateDefinition().getProperty("animation") instanceof IntegerProperty _getip1
    				? (this.getBlockState()).getValue(_getip1)
    				: 0));
		if (animationprocedure.equals("0")) {
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
		if (!(animationprocedure.equals("0")) && event.getController().getAnimationState().equals(software.bernie.geckolib3.core.AnimationState.Stopped)) {
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

	@Override public void load(CompoundTag compound) {
		super.load(compound);

		if (!this.tryLoadLootTable(compound))
			this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);

		ContainerHelper.loadAllItems(compound, this.stacks);

		<#if data.hasEnergyStorage>
		if(compound.get("energyStorage") instanceof IntTag intTag)
			energyStorage.deserializeNBT(intTag);
		</#if>

		<#if data.isFluidTank>
		if(compound.get("fluidTank") instanceof CompoundTag compoundTag)
			fluidTank.readFromNBT(compoundTag);
		</#if>
	}

	@Override public void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);

		if (!this.trySaveLootTable(compound)) {
			ContainerHelper.saveAllItems(compound, this.stacks);
		}

		<#if data.hasEnergyStorage>
		compound.put("energyStorage", energyStorage.serializeNBT());
		</#if>

		<#if data.isFluidTank>
		compound.put("fluidTank", fluidTank.writeToNBT(new CompoundTag()));
		</#if>
	}

	@Override public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override public CompoundTag getUpdateTag() {
		return this.saveWithFullMetadata();
	}

	@Override public int getContainerSize() {
		return stacks.size();
	}

	@Override public boolean isEmpty() {
		for (ItemStack itemstack : this.stacks)
			if (!itemstack.isEmpty())
				return false;
		return true;
	}

	@Override public Component getDefaultName() {
		return Component.literal("${registryname}");
	}

	@Override public int getMaxStackSize() {
		return ${data.inventoryStackSize};
	}

	@Override public AbstractContainerMenu createMenu(int id, Inventory inventory) {
		<#if !data.guiBoundTo?has_content || data.guiBoundTo == "<NONE>" || !(data.guiBoundTo)?has_content>
		return ChestMenu.threeRows(id, inventory);
		<#else>
		return new ${data.guiBoundTo}Menu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(this.worldPosition));
		</#if>
	}

	@Override public Component getDisplayName() {
		return Component.literal("${data.name}");
	}

	@Override protected NonNullList<ItemStack> getItems() {
		return this.stacks;
	}

	@Override protected void setItems(NonNullList<ItemStack> stacks) {
		this.stacks = stacks;
	}

	@Override public boolean canPlaceItem(int index, ItemStack stack) {
		<#list data.inventoryOutSlotIDs as id>
		if (index == ${id})
			return false;
		</#list>
		return true;
	}

	<#-- START: ISidedInventory -->
	@Override public int[] getSlotsForFace(Direction side) {
		return IntStream.range(0, this.getContainerSize()).toArray();
	}

	@Override public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
		return this.canPlaceItem(index, stack);
	}

	@Override public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
		<#list data.inventoryInSlotIDs as id>
		if (index == ${id})
			return false;
        </#list>
		return true;
	}
	<#-- END: ISidedInventory -->

	<#if data.hasEnergyStorage>
	private final EnergyStorage energyStorage = new EnergyStorage(${data.energyCapacity}, ${data.energyMaxReceive}, ${data.energyMaxExtract}, ${data.energyInitial}) {
		@Override public int receiveEnergy(int maxReceive, boolean simulate) {
			int retval = super.receiveEnergy(maxReceive, simulate);
			if(!simulate) {
				setChanged();
				level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 2);
			}
			return retval;
		}

		@Override public int extractEnergy(int maxExtract, boolean simulate) {
			int retval = super.extractEnergy(maxExtract, simulate);
			if(!simulate) {
				setChanged();
				level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 2);
			}
			return retval;
		}
	};
    </#if>

	<#if data.isFluidTank>
        <#if data.fluidRestrictions?has_content>
		private final FluidTank fluidTank = new FluidTank(${data.fluidCapacity}, fs -> {
			<#list data.fluidRestrictions as fluidRestriction>
                <#if fluidRestriction.getUnmappedValue().startsWith("CUSTOM:")>
					if(fs.getFluid() ==
					${JavaModName}Fluids.<#if fluidRestriction.getUnmappedValue().endsWith(":Flowing")>FLOWING_</#if>${generator.getRegistryNameForModElement(fluidRestriction.getUnmappedValue()?remove_beginning("CUSTOM:")?remove_ending(":Flowing"))?upper_case}.get()) return true;
                <#else>
				if(fs.getFluid() == Fluids.${fluidRestriction}) return true;
                </#if>
            </#list>

			return false;
		}) {
			@Override protected void onContentsChanged() {
				super.onContentsChanged();
				setChanged();
				level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 2);
			}
		};
        <#else>
		private final FluidTank fluidTank = new FluidTank(${data.fluidCapacity}) {
			@Override protected void onContentsChanged() {
				super.onContentsChanged();
				setChanged();
				level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 2);
			}
		};
        </#if>
    </#if>

	@Override public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
		if (!this.remove && facing != null && capability == ForgeCapabilities.ITEM_HANDLER)
			return handlers[facing.ordinal()].cast();

		<#if data.hasEnergyStorage>
		if (!this.remove && capability == ForgeCapabilities.ENERGY)
			return LazyOptional.of(() -> energyStorage).cast();
        </#if>

		<#if data.isFluidTank>
		if (!this.remove && capability == ForgeCapabilities.FLUID_HANDLER)
			return LazyOptional.of(() -> fluidTank).cast();
        </#if>

		return super.getCapability(capability, facing);
	}

	@Override public void setRemoved() {
		super.setRemoved();
		for(LazyOptional<? extends IItemHandler> handler : handlers)
			handler.invalidate();
	}
}
