package dev.nonvocal.gui.widget;

import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.dscsag.plm.spi.interfaces.commons.ResourceAccessor;

import dev.nonvocal.infrastructure.Infra;
import dev.nonvocal.util.Searchable;
import org.eclipse.jdt.annotation.NonNull;

public class Searchbar extends JTextField
{
  private final ImageIcon searchIcon;
  private Searchable searchable;

  public Searchbar()
  {
    this.setOpaque(false);
    Infra infra = Infra.getInstance();
    if (infra != null)
      searchIcon = infra.images().getImageIcon("search", ResourceAccessor.IconSize.SMALL);
    else
      searchIcon = null;

      getDocument().addDocumentListener(new DocumentListener()
      {
        @Override
        public void insertUpdate(DocumentEvent e)
        {
          doSearch();
        }

        @Override
        public void removeUpdate(DocumentEvent e)
        {
          doSearch();
        }

        @Override
        public void changedUpdate(DocumentEvent e)
        {
          doSearch();
        }

        private void doSearch()
        {
            if (searchable != null)
                searchable.search(getText());
        }
      });

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

  public void bindToSearch(@NonNull Searchable searchable)
  {
    this.searchable = searchable;
  }
}
