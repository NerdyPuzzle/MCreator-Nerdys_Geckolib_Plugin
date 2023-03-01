package ${package}.block.registry;

public class TileRegistry {
	<#list animatedblocks as blockentity>
	public static BlockEntityType<?> ${blockentity.getModElement().getRegistryNameUpper()};
	</#list>

	public static void load() {
		<#list animatedblocks as blockentity>
			${blockentity.getModElement().getRegistryNameUpper()} = Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(${JavaModName}.MODID,
				"${blockentity.getModElement().getRegistryName()}_block"), FabricBlockEntityTypeBuilder.create(${blockentity.getModElement().getName()}TileEntity::new,
				${JavaModName}Blocks.${blockentity.getModElement().getRegistryNameUpper()}).build(null));
		</#list>
	}
}