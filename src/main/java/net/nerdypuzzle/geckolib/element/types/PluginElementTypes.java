package net.nerdypuzzle.geckolib.element.types;

import net.mcreator.element.BaseType;
import net.mcreator.element.ModElementType;
import net.mcreator.element.RecipeType;
import net.nerdypuzzle.geckolib.ui.modgui.AnimatedBlockGUI;
import net.nerdypuzzle.geckolib.ui.modgui.AnimatedItemGUI;

import static net.mcreator.element.ModElementTypeLoader.register;

public class PluginElementTypes {
    public static ModElementType<?> ANIMATEDBLOCK;
    public static ModElementType<?> ANIMATEDITEM;

    public static void load(){

        ANIMATEDBLOCK = register(
                new ModElementType<>("animatedblock", (Character) null, BaseType.BLOCK, RecipeType.BLOCK, AnimatedBlockGUI::new, AnimatedBlock.class)
        );

        ANIMATEDITEM = register(
                new ModElementType<>("animateditem", (Character) null, BaseType.ITEM, RecipeType.ITEM, AnimatedItemGUI::new, AnimatedItem.class)
        );

    }
}
