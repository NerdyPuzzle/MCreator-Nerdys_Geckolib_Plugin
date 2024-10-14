<#include "mcitems.ftl">
if (${mappedMCItemToItem(input$item)} instanceof ${((field$name)?replace("CUSTOM:", ""))?replace("_", "")}Item armor && armor instanceof GeoItem)
CustomData.update(DataComponents.CUSTOM_DATA, ${mappedMCItemToItemStackCode(input$item, 1)}, tag -> tag.putString("geckoAnim", "empty"));