package dev.nonvocal.gui.widget;

import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JTextField;

import com.dscsag.plm.spi.interfaces.commons.ResourceAccessor;

import dev.nonvocal.infrastructure.Infra;

public class Searchbar extends JTextField
{
  private final ImageIcon searchIcon;

  public Searchbar()
  {
    this.setOpaque(false);
    Infra infra = Infra.getInstance();
    if (infra != null)
      searchIcon = infra.images().getImageIcon("search", ResourceAccessor.IconSize.SMALL);
    else
      searchIcon = null;
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
