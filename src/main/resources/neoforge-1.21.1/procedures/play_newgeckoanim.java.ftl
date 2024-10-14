if (${input$entity} instanceof ${(field$name)?replace("CUSTOM:", "")}Entity) {
((${(field$name)?replace("CUSTOM:", "")}Entity) ${input$entity}).setAnimation(${input$animation});
}