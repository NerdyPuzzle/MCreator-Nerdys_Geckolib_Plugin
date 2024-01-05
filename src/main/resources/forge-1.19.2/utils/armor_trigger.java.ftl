<#include "procedures.java.ftl">

<#macro onArmorTick procedure="">
	<#if hasProcedure(procedure)>
	<@procedureCode procedure, {
	"x": "entity.getX()",
	"y": "entity.getY()",
	"z": "entity.getZ()",
	"world": "world",
	"entity": "entity",
	"itemstack": "itemstack"
	}/>
	</#if>
</#macro>