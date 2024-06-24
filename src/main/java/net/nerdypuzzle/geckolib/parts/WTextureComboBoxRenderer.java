package net.nerdypuzzle.geckolib.parts;

import net.mcreator.ui.workspace.resources.TextureType;
import net.mcreator.util.FilenameUtilsPatched;
import net.mcreator.util.image.EmptyIcon;
import net.mcreator.util.image.ImageUtils;
import net.mcreator.workspace.Workspace;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.function.Function;

public class WTextureComboBoxRenderer extends JLabel implements ListCellRenderer<String> {

    private final Function<String, ImageIcon> textureProvider;

    public WTextureComboBoxRenderer(Function<String, ImageIcon> textureProvider) {
        this.textureProvider = textureProvider;

        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        setText(value);

        ImageIcon imageIcon = textureProvider.apply(value);
        if (imageIcon != null) {
            setIcon(new ImageIcon(ImageUtils.resize(imageIcon.getImage(), 30)));
        } else {
            setIcon(new EmptyIcon(30, 30));
        }

        setHorizontalTextPosition(SwingConstants.RIGHT);
        setHorizontalAlignment(SwingConstants.LEFT);

        return this;
    }

    public static class TypeTextures extends WTextureComboBoxRenderer {

        public TypeTextures(Workspace workspace, TextureType type) {
            super(element -> {
                File file = workspace.getFolderManager()
                        .getTextureFile(FilenameUtilsPatched.removeExtension(element), type);
                if (file.isFile())
                    return new ImageIcon(file.getAbsolutePath());
                return null;
            });
        }
    }

}
