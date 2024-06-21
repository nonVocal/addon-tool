package dev.nonvocal.gui.widget;

import com.dscsag.plm.spi.interfaces.commons.ResourceAccessor;
import dev.nonvocal.infrastructure.Infra;

import javax.swing.*;
import java.awt.*;

public class Searchbar extends JTextField
{
    private final ImageIcon searchIcon;

    public Searchbar()
    {
        this.setOpaque(false);
        searchIcon = Infra.getInstance().images().getImageIcon("search", ResourceAccessor.IconSize.SMALL);

    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        int marginLft = 5;
        if (searchIcon != null)
        {
            searchIcon.paintIcon(this, g, 0, 0);
            marginLft = searchIcon.getIconWidth() + 5;
        }

        setMargin(new Insets(5, marginLft, 5, 5));
    }
}
