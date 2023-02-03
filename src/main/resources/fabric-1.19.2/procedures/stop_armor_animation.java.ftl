<#include "mcitems.ftl">
if (${mappedMCItemToItem(input$item)} instanceof ${(field$name)?replace("CUSTOM:", "")}Item armor && armor instanceof ArmorItem)
armor.animationprocedure = "empty";