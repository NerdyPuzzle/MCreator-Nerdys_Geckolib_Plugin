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
<#include "../armor_trigger.java.ftl">

package ${package}.item;

import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;

import javax.annotation.Nullable;

import net.minecraft.sounds.SoundEvent;
import java.util.function.Consumer;
import net.minecraft.client.model.Model;

public class ${name}Item extends GeoArmorItem implements IAnimatable {
	private AnimationFactory factory = GeckoLibUtil.createFactory(this);
	public String animationprocedure = "empty";

	public ${name}Item(EquipmentSlot slot, Item.Properties properties) {
		super(new ArmorMaterial() {
			@Override public int getDurabilityForSlot(EquipmentSlot slot) {
				return new int[]{13, 15, 16, 11}[slot.getIndex()] * ${data.maxDamage};
			}

			@Override public int getDefenseForSlot(EquipmentSlot slot) {
				return new int[] { ${data.damageValueBoots}, ${data.damageValueLeggings}, ${data.damageValueBody}, ${data.damageValueHelmet} }[slot.getIndex()];
			}

			@Override public int getEnchantmentValue() {
				return ${data.enchantability};
			}

			@Override public SoundEvent getEquipSound() {
				<#if data.equipSound??>
				return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("${data.equipSound}"));
				<#else>
				return null;
				</#if>
			}

			@Override public Ingredient getRepairIngredient() {
				<#if data.repairItems?has_content>
				return Ingredient.of(
				<#list data.repairItems as repairItem>
				${mappedMCItemToItemStackCode(repairItem,1)}<#if repairItem?has_next>,</#if>
				</#list>
				);
				<#else>
				return Ingredient.EMPTY;
				</#if>
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
		}, slot, properties);
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
		<#if data.helmetSpecialInfo?has_content>
		if (itemstack.getItem() instanceof ${name}Item armor && armor.slot == EquipmentSlot.HEAD) {
			<#list data.helmetSpecialInfo as entry>
			list.add(Component.literal("${JavaConventions.escapeStringForJava(entry)}"));
			</#list>
		}
		</#if>
		<#if data.bodySpecialInfo?has_content>
		if (itemstack.getItem() instanceof ${name}Item armor && armor.slot == EquipmentSlot.CHEST) {
			<#list data.bodySpecialInfo as entry>
			list.add(Component.literal("${JavaConventions.escapeStringForJava(entry)}"));
			</#list>
		}
		</#if>
		<#if data.leggingsSpecialInfo?has_content>
		if (itemstack.getItem() instanceof ${name}Item armor && armor.slot == EquipmentSlot.LEGS) {
			<#list data.leggingsSpecialInfo as entry>
			list.add(Component.literal("${JavaConventions.escapeStringForJava(entry)}"));
			</#list>
		}
		</#if>
		<#if data.bootsSpecialInfo?has_content>
		if (itemstack.getItem() instanceof ${name}Item armor && armor.slot == EquipmentSlot.FEET) {
			<#list data.bootsSpecialInfo as entry>
			list.add(Component.literal("${JavaConventions.escapeStringForJava(entry)}"));
			</#list>
		}
		</#if>
	}

	@Override 
	public void onArmorTick(ItemStack itemstack, Level world, Player entity) {
		<#if hasProcedure(data.onHelmetTick)>
		if (itemstack.getItem() instanceof ${name}Item armor && armor.slot == EquipmentSlot.HEAD)
			<@onArmorTick data.onHelmetTick/>
		</#if>
		<#if hasProcedure(data.onBodyTick)>
		if (itemstack.getItem() instanceof ${name}Item armor && armor.slot == EquipmentSlot.CHEST)
			<@onArmorTick data.onBodyTick/>
		</#if>
		<#if hasProcedure(data.onLeggingsTick)>
		if (itemstack.getItem() instanceof ${name}Item armor && armor.slot == EquipmentSlot.LEGS)
			<@onArmorTick data.onLeggingsTick/>
		</#if>
		<#if hasProcedure(data.onBootsTick)>
		if (itemstack.getItem() instanceof ${name}Item armor && armor.slot == EquipmentSlot.FEET)
			<@onArmorTick data.onBootsTick/>
		</#if>
	}

	@SuppressWarnings("unused")
	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		List<EquipmentSlot> slotData = event.getExtraDataOfType(EquipmentSlot.class);
		List<ItemStack> stackData = event.getExtraDataOfType(ItemStack.class);
		LivingEntity livingEntity = event.getExtraDataOfType(LivingEntity.class).get(0);
	if (this.animationprocedure.equals("empty")) {
		event.getController().setAnimation(new AnimationBuilder().addAnimation("${data.idle}", 							EDefaultLoopTypes.LOOP));

		if (livingEntity instanceof ArmorStand) {
			return PlayState.CONTINUE;
		}

		<#if data.fullyEquipped>
		List<Item> armorList = new ArrayList<>(4);
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.getType() == EquipmentSlot.Type.ARMOR) {
				if (livingEntity.getItemBySlot(slot) != null) {
					armorList.add(livingEntity.getItemBySlot(slot).getItem());
				}
			}
		}
		</#if>

		<#if data.fullyEquipped>
		boolean isWearingAll = armorList.containsAll(Arrays.asList(
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

	@SuppressWarnings("unused")
	private <P extends IAnimatable> PlayState procedurePredicate(AnimationEvent<P> event) {
		List<EquipmentSlot> slotData = event.getExtraDataOfType(EquipmentSlot.class);
		List<ItemStack> stackData = event.getExtraDataOfType(ItemStack.class);
		LivingEntity livingEntity = event.getExtraDataOfType(LivingEntity.class).get(0);
	if (!this.animationprocedure.equals("empty") && event.getController().getAnimationState().equals						(software.bernie.geckolib3.core.AnimationState.Stopped)) {
		event.getController().setAnimation(new AnimationBuilder().addAnimation(this.animationprocedure, 						EDefaultLoopTypes.PLAY_ONCE));
	        if (event.getController().getAnimationState().equals(software.bernie.geckolib3.core.AnimationState.Stopped)) {
			this.animationprocedure = "empty";
			event.getController().markNeedsReload();
		}

		if (livingEntity instanceof ArmorStand) {
			return PlayState.CONTINUE;
		}

		<#if data.fullyEquipped>
		List<Item> armorList = new ArrayList<>(4);
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.getType() == EquipmentSlot.Type.ARMOR) {
				if (livingEntity.getItemBySlot(slot) != null) {
					armorList.add(livingEntity.getItemBySlot(slot).getItem());
				}
			}
		}
		</#if>

		<#if data.fullyEquipped>
		boolean isWearingAll = armorList.containsAll(Arrays.asList(
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
	return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
		data.addAnimationController(new AnimationController(this, "procedureController", 5, this::procedurePredicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

}
<#-- @formatter:on -->