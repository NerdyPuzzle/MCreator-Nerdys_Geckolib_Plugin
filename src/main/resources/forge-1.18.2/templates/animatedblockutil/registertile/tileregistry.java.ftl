package ${package}.block.registry;

public class TileRegistry {
	public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister
			.create(ForgeRegistries.BLOCK_ENTITIES, ${JavaModName}.MODID);
<#list animatedblocks as ablock>
	public static final RegistryObject<BlockEntityType<${ablock.getModElement().getName()}TileEntity>> ${ablock.getModElement().getRegistryNameUpper()} = TILES.register("${ablock.getModElement().getRegistryName()}_block",
			() -> BlockEntityType.Builder.of(${ablock.getModElement().getName()}TileEntity::new, 
	${JavaModName}Blocks.${ablock.getModElement().getRegistryNameUpper()}.get()).build(null));
</#list>
}