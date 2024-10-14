<#include "mcelements.ftl">
{
	BlockPos _pos = ${toBlockPos(input$x,input$y,input$z)};
	BlockState _bs = world.getBlockState(_pos);
	if (_bs.getBlock().getStateDefinition().getProperty("animation") instanceof IntegerProperty _integerProp)
		world.setBlock(_pos, _bs.setValue(_integerProp, 0), 3);
}