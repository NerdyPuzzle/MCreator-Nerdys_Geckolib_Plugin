<#include "mcitems.ftl">
if (${mappedMCItemToItem(input$item)} instanceof ${((field$name)?replace("CUSTOM:", ""))?replace("_", "")}Item armor && armor instanceof ArmorItem)
armor.animationprocedure = ${input$animation};