<#--
 # This file is part of Fabric-Generator-MCreator.
 # Copyright (C) 2012-2020, Pylo
 # Copyright (C) 2020-2021, Pylo, opensource contributors
 # Copyright (C) 2020-2022, Goldorion, opensource contributors
 #
 # Fabric-Generator-MCreator is free software: you can redistribute it and/or modify
 # it under the terms of the GNU Lesser General Public License as published by
 # the Free Software Foundation, either version 3 of the License, or
 # (at your option) any later version.

 # Fabric-Generator-MCreator is distributed in the hope that it will be useful,
 # but WITHOUT ANY WARRANTY; without even the implied warranty of
 # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 # GNU Lesser General Public License for more details.
 #
 # You should have received a copy of the GNU Lesser General Public License
 # along with Fabric-Generator-MCreator.  If not, see <https://www.gnu.org/licenses/>.
-->

<#-- @formatter:off -->
package ${package}.block.entity;

<#assign regname = data.getModElement().getRegistryName()>

import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;

import org.jetbrains.annotations.Nullable;

public class ${name}TileEntity extends RandomizableContainerBlockEntity implements ExtendedScreenHandlerFactory, IAnimatable, WorldlyContainer {
	public AnimationFactory factory = GeckoLibUtil.createFactory(this);
	private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(${data.inventorySize}, ItemStack.EMPTY);

	public ${name}TileEntity(BlockPos position, BlockState state) {
		super(TileRegistry.${(regname)?upper_case}, position, state);
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
	}

	@Override
	public void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		if (!this.trySaveLootTable(compound))
			ContainerHelper.saveAllItems(compound, this.stacks);
	}

	@Override public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override public CompoundTag getUpdateTag() {
		return this.saveWithoutMetadata();
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
				return new ${data.guiBoundTo}Menu(id, inventory, this);
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

	@Override
	public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
		buf.writeBlockPos(worldPosition);
	}

}
<#-- @formatter:on -->