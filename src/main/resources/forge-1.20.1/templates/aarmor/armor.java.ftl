<#--
 # MCreator (https://mcreator.net/)
 # Copyright (C) 2012-2020, Pylo
 # Copyright (C) 2020-2022, Pylo, opensource contributors
 # 
 # This program is free software: you can redistribute it and/or modify
 # it under the terms of the GNU General Public License as published by
 # the Free Software Foundation, either version 3 of the License, or
 # (at your option) any later version.
 # 
 # This program is distributed in the hope that it will be useful,
 # but WITHOUT ANY WARRANTY; without even the implied warranty of
 # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 # GNU General Public License for more details.
 # 
 # You should have received a copy of the GNU General Public License
 # along with this program.  If not, see <https://www.gnu.org/licenses/>.
 # 
 # Additional permission for code generator templates (*.ftl files)
 # 
 # As a special exception, you may create a larger work that contains part or 
 # all of the MCreator code generator templates (*.ftl files) and distribute 
 # that work under terms of your choice, so long as that work isn't itself a 
 # template for code generation. Alternatively, if you modify or redistribute 
 # the template itself, you may (at your option) remove this special exception, 
 # which will cause the template and the resulting code generator output files 
 # to be licensed under the GNU General Public License without this special 
 # exception.
-->

<#-- @formatter:off -->
<#include "../mcitems.ftl">
<#include "../procedures.java.ftl">

package ${package}.item;

import javax.annotation.Nullable;

import net.minecraft.sounds.SoundEvent;
import java.util.function.Consumer;
import net.minecraft.client.model.Model;

import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationState;

public class ${name}Item extends ArmorItem implements GeoItem {
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	public String animationprocedure = "empty";

	public ${name}Item(ArmorItem.Type type, Item.Properties properties) {
		super(new ArmorMaterial() {
			@Override public int getDurabilityForType(ArmorItem.Type type) {
				return new int[]{13, 15, 16, 11}[type.getSlot().getIndex()] * ${data.maxDamage};
			}

			@Override public int getDefenseForType(ArmorItem.Type type) {
				return new int[] { ${data.damageValueBoots}, ${data.damageValueLeggings}, ${data.damageValueBody}, ${data.damageValueHelmet} }[type.getSlot().getIndex()];
			}

			@Override public int getEnchantmentValue() {
				return ${data.enchantability};
			}

			@Override public SoundEvent getEquipSound() {
				<#if data.equipSound?? && data.equipSound.getUnmappedValue()?has_content>
				return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("${data.equipSound}"));
				<#else>
				return SoundEvents.EMPTY;
				</#if>
			}

			@Override public Ingredient getRepairIngredient() {
				return ${mappedMCItemsToIngredient(data.repairItems)};
			}

			@Override public String getName() {
				return "${registryname}";
			}

			@Override public float getToughness() {
				return ${data.toughness}f;
			}

			@Override public float getKnockbackResistance() {
				return ${data.knockbackResistance}f;
			}
		}, type, properties);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			private GeoArmorRenderer<?> renderer;

			@Override
			public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
				if (this.renderer == null)
					this.renderer = new ${name}ArmorRenderer();

				this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

				return this.renderer;
			}
		});
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
		<#if data.helmetSpecialInfo?has_content>
		if (itemstack.getItem() instanceof ${name}Item armor && armor.getType() == ArmorItem.Type.HELMET) {
			<#list data.helmetSpecialInfo as entry>
			list.add(Component.literal("${JavaConventions.escapeStringForJava(entry)}"));
			</#list>
		}
		</#if>
		<#if data.bodySpecialInfo?has_content>
		if (itemstack.getItem() instanceof ${name}Item armor && armor.getType() == ArmorItem.Type.CHESTPLATE) {
			<#list data.bodySpecialInfo as entry>
			list.add(Component.literal("${JavaConventions.escapeStringForJava(entry)}"));
			</#list>
		}
		</#if>
		<#if data.leggingsSpecialInfo?has_content>
		if (itemstack.getItem() instanceof ${name}Item armor && armor.getType() == ArmorItem.Type.LEGGINGS) {
			<#list data.leggingsSpecialInfo as entry>
			list.add(Component.literal("${JavaConventions.escapeStringForJava(entry)}"));
			</#list>
		}
		</#if>
		<#if data.bootsSpecialInfo?has_content>
		if (itemstack.getItem() instanceof ${name}Item armor && armor.getType() == ArmorItem.Type.BOOTS) {
			<#list data.bootsSpecialInfo as entry>
			list.add(Component.literal("${JavaConventions.escapeStringForJava(entry)}"));
			</#list>
		}
		</#if>
	}

	private PlayState predicate(AnimationState event) {
	if (this.animationprocedure.equals("empty")) {
		event.getController().setAnimation(RawAnimation.begin().thenLoop("${data.idle}"));

		Entity entity = (Entity) event.getData(DataTickets.ENTITY);

		if (entity instanceof ArmorStand) {
			return PlayState.CONTINUE;
		}

		<#if data.fullyEquipped>
			Set<Item> wornArmor = new ObjectOpenHashSet<>();

			for (ItemStack stack : entity.getArmorSlots()) {
				if (stack.isEmpty())
					return PlayState.STOP;

				wornArmor.add(stack.getItem());
			}
		</#if>

		<#if data.fullyEquipped>
		boolean isWearingAll = wornArmor.containsAll(ObjectArrayList.of(
		<#if data.enableBoots>
		${JavaModName}Items.${(registryname)?upper_case}_BOOTS.get()
		</#if><#if data.enableBoots && (data.enableLeggings || data.enableBody || data.enableHelmet)>,</#if>
		<#if data.enableLeggings>
		${JavaModName}Items.${(registryname)?upper_case}_LEGGINGS.get()
		</#if><#if data.enableLeggings && (data.enableBody || data.enableHelmet)>,</#if>
		<#if data.enableBody> 
		${JavaModName}Items.${(registryname)?upper_case}_CHESTPLATE.get()
		</#if><#if data.enableBody && data.enableBoots>,</#if>
		<#if data.enableBoots> 
		${JavaModName}Items.${(registryname)?upper_case}_HELMET.get()
		</#if>));
		</#if>

		return <#if data.fullyEquipped>isWearingAll ? PlayState.CONTINUE : PlayState.STOP<#else>PlayState.CONTINUE</#if>;
		}
		return PlayState.STOP;
	}

	String prevAnim = "empty";
	private PlayState procedurePredicate(AnimationState event) {
		if (!this.animationprocedure.equals("empty") && event.getController().getAnimationState() == AnimationController.State.STOPPED || (!this.animationprocedure.equals(prevAnim) && !this.animationprocedure.equals("empty"))) {
			if (!this.animationprocedure.equals(prevAnim))
				event.getController().forceAnimationReset();
			event.getController().setAnimation(RawAnimation.begin().thenPlay(this.animationprocedure));
			if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
				this.animationprocedure = "empty";
				event.getController().forceAnimationReset();
			}

			Entity entity = (Entity) event.getData(DataTickets.ENTITY);

			if (entity instanceof ArmorStand) {
				return PlayState.CONTINUE;
			}

			<#if data.fullyEquipped>
				Set<Item> wornArmor = new ObjectOpenHashSet<>();

				for (ItemStack stack : entity.getArmorSlots()) {
					if (stack.isEmpty())
						return PlayState.STOP;

					wornArmor.add(stack.getItem());
				}
			</#if>

			<#if data.fullyEquipped>
			boolean isWearingAll = wornArmor.containsAll(ObjectArrayList.of(
			<#if data.enableBoots>
			${JavaModName}Items.${(registryname)?upper_case}_BOOTS.get()
			</#if><#if data.enableBoots && (data.enableLeggings || data.enableBody || data.enableHelmet)>,</#if>
			<#if data.enableLeggings>
			${JavaModName}Items.${(registryname)?upper_case}_LEGGINGS.get()
			</#if><#if data.enableLeggings && (data.enableBody || data.enableHelmet)>,</#if>
			<#if data.enableBody>
			${JavaModName}Items.${(registryname)?upper_case}_CHESTPLATE.get()
			</#if><#if data.enableBody && data.enableBoots>,</#if>
			<#if data.enableBoots>
			${JavaModName}Items.${(registryname)?upper_case}_HELMET.get()
			</#if>));
			</#if>

			return <#if data.fullyEquipped>isWearingAll ? PlayState.CONTINUE : PlayState.STOP<#else>PlayState.CONTINUE</#if>;
		} else if (animationprocedure.equals("empty")) {
			prevAnim = "empty";
			return PlayState.STOP;
		}
		prevAnim = this.animationprocedure;
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar data) {
		data.add(new AnimationController(this, "controller", 5, this::predicate));
		data.add(new AnimationController(this, "procedureController", 5, this::procedurePredicate));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}

}
<#-- @formatter:on -->