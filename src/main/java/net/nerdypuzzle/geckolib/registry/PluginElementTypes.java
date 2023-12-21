package net.nerdypuzzle.geckolib.registry;

import net.mcreator.element.BaseType;
import net.mcreator.element.ModElementType;
import net.nerdypuzzle.geckolib.element.types.AnimatedArmor;
import net.nerdypuzzle.geckolib.element.types.AnimatedBlock;
import net.nerdypuzzle.geckolib.element.types.AnimatedEntity;
import net.nerdypuzzle.geckolib.element.types.AnimatedItem;
import net.nerdypuzzle.geckolib.ui.modgui.AnimatedArmorGUI;
import net.nerdypuzzle.geckolib.ui.modgui.AnimatedBlockGUI;
import net.nerdypuzzle.geckolib.ui.modgui.AnimatedEntityGUI;
import net.nerdypuzzle.geckolib.ui.modgui.AnimatedItemGUI;

import static net.mcreator.element.ModElementTypeLoader.register;

public class PluginElementTypes {
    public static ModElementType<?> ANIMATEDBLOCK;
    public static ModElementType<?> ANIMATEDITEM;
    public static ModElementType<?> ANIMATEDENTITY;
    public static ModElementType<?> ANIMATEDARMOR;

    public static void load() {

        ANIMATEDBLOCK = register(
                new ModElementType<>("animatedblock", (Character) 'D', AnimatedBlockGUI::new, AnimatedBlock.class)
        );

        ANIMATEDITEM = register(
                new ModElementType<>("animateditem", (Character) 'I', AnimatedItemGUI::new, AnimatedItem.class)
        );

        ANIMATEDENTITY = register(
                new ModElementType<>("animatedentity", (Character) 'E', AnimatedEntityGUI::new, AnimatedEntity.class)
        );

        ANIMATEDARMOR = register(
                new ModElementType<>("animatedarmor", (Character) 'A', AnimatedArmorGUI::new, AnimatedArmor.class)
        );

    }
}
