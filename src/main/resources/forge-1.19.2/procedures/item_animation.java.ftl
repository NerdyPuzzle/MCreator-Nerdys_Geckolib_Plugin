<#include "mcitems.ftl">
if (${mappedMCItemToItem(input$item)} instanceof ${(field$name)?replace("CUSTOM:", "")}Item)
${mappedMCItemToItemStackCode(input$item, 1)}.getOrCreateTag().putString("geckoAnim", ${input$animation});