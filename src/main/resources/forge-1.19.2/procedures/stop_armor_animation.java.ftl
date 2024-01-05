<#include "mcitems.ftl">
if (${mappedMCItemToItem(input$item)} instanceof ${((field$name)?replace("CUSTOM:", ""))?replace("_", "")}Item armor && armor instanceof GeoArmorItem)
${mappedMCItemToItemStackCode(input$item, 1)}.getOrCreateTag().putString("geckoAnim", "empty");