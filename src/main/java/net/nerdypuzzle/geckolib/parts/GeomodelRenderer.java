package net.nerdypuzzle.geckolib.parts;

import net.mcreator.ui.init.UIRES;
import net.mcreator.util.image.EmptyIcon;

import javax.swing.*;
import java.awt.*;

public class GeomodelRenderer extends JLabel implements ListCellRenderer<String> {
    public GeomodelRenderer() {
    }

    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
        this.setOpaque(true);
        if (isSelected) {
            this.setBackground(list.getSelectionBackground());
            this.setForeground(list.getSelectionForeground());
        } else {
            this.setBackground(list.getBackground());
            this.setForeground(list.getForeground());
        }

        if (value == null)
            return this;
        else {
            this.setText(value);
            if (!value.isEmpty())
                this.setIcon(UIRES.get("model.small_geckolib"));
            else
                this.setIcon(new EmptyIcon(32, 32));


            this.setHorizontalTextPosition(4);
            this.setHorizontalAlignment(2);
            return this;
        }
    }
}
