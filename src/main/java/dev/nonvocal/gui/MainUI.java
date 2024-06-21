package dev.nonvocal.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.dscsag.plm.spi.interfaces.ECTRService;
import com.dscsag.plm.spi.interfaces.logging.PlmLogger;

import dev.nonvocal.addon.Addon;
import dev.nonvocal.addon.AddonBundle;
import dev.nonvocal.addon.AddonCollector;
import dev.nonvocal.gui.tree.AddonBundleTreeNode;
import dev.nonvocal.gui.tree.AddonTreeModel;
import dev.nonvocal.gui.tree.AddonTreeNode;

public class MainUI extends JFrame
{

  private ECTRService ectrService;
  private PlmLogger logger;

  private AddonCollector.AddonCollection addons;

  public MainUI(ECTRService service)
  {
    super("Addon-Tool");

    this.ectrService = service;
    this.logger = ectrService.getPlmLogger();

    AddonCollector addonCollector = new AddonCollector(ectrService);
    addons = addonCollector.collectAddons();

    add(new MainPanel(addons));
    setVisible(true);

  }

  @Override
  public void setVisible(boolean b)
  {
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(800, 800);
    setPreferredSize(new Dimension(800, 800));
    setLocationRelativeTo(null);
    pack();
    super.setVisible(true);
  }

  private static final class MainPanel extends JPanel
  {
    private final AddonCollector.AddonCollection addons;
    private final NavBar navBar;
    private final DetailPanel detailPanel;

    public MainPanel(AddonCollector.AddonCollection addons)
    {
      this.addons = addons;
      setLayout(new BorderLayout());
      setPreferredSize(new Dimension(300, 800));
      setMinimumSize(new Dimension(300, 800));

      this.navBar = new NavBar(addons);
      JScrollPane scrollingNavBar = new JScrollPane(navBar, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
          JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      add(scrollingNavBar, BorderLayout.WEST);

      this.detailPanel = new DetailPanel();
      JScrollPane scrollingDetailPanel = new JScrollPane(detailPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
          JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      add(scrollingDetailPanel, BorderLayout.CENTER);

      navBar.addTreeSelectionListener(new SelectionListener(detailPanel));
    }
  }

  private static final class NavBar extends JTree
  {
    private final AddonTreeModel model;

    private NavBar(AddonCollector.AddonCollection addons)
    {
      super(new AddonTreeModel(addons));
      this.model = (AddonTreeModel) super.getModel();
    }

    @Override
    public AddonTreeModel getModel()
    {
      return model;
    }
  }

  @SuppressWarnings ("ClassCanBeRecord")
  private static final class SelectionListener implements TreeSelectionListener
  {
    private final DetailPanel detailPanel;

    public SelectionListener(DetailPanel detailPanel)
    {
      this.detailPanel = detailPanel;
    }

    @Override
    public void valueChanged(TreeSelectionEvent e)
    {
      Object pathComponent = e.getNewLeadSelectionPath().getLastPathComponent();
      if (pathComponent instanceof AddonBundleTreeNode bundleNode)
        SwingUtilities.invokeLater(() -> detailPanel.updateContent(bundleNode.bundle()));
      else if (pathComponent instanceof AddonTreeNode addonNode)
        SwingUtilities.invokeLater(() -> detailPanel.updateContent(addonNode.addon()));
    }
  }

  private static final class DetailPanel extends JPanel
  {
    private final JLabel header;
    private final JTable table;

    public DetailPanel()
    {
      super(new BorderLayout());
      header = new JLabel("");
      add(header, BorderLayout.NORTH);

      table = new JTable(new DefaultTableModel());
      table.setSize(500, 500);

      JPanel content = new JPanel(new BorderLayout());
      content.add(table.getTableHeader(), BorderLayout.NORTH);
      content.add(new JScrollPane(table), BorderLayout.CENTER);

      add(content, BorderLayout.CENTER);
    }

    void setHeaderText(String text)
    {
      header.setText("<html><h1>" + text + "</h1></html>");
    }

    void updateContent(AddonBundle bundle)
    {
      setHeaderText(bundle.domain());
      table.setModel(new BundleModel(bundle));

      invalidate();
    }

    void updateContent(Addon addon)
    {
      setHeaderText(addon.domain() + ":" + addon.name());
      table.setModel(new AddonModel(addon));

      invalidate();
    }

    private static class BundleModel extends DefaultTableModel
    {
      private final AddonBundle bundle;

      BundleModel(AddonBundle bundle)
      {
        this.bundle = bundle;
      }

      @Override
      public int getRowCount()
      {
        return bundle.addons().size();
      }

      @Override
      public int getColumnCount()
      {
        return 5;
      }

      @Override
      public String getColumnName(int column)
      {
        return switch (column)
        {
          case 0 -> "Domain";
          case 1 -> "Name";
          case 2 -> "Version";
          case 3 -> "Path";
          case 4 -> "Enabled";
          default -> throw new IndexOutOfBoundsException();
        };
      }

      @Override
      public Object getValueAt(int rowIndex, int columnIndex)
      {
        Addon addon = bundle.addons().get(rowIndex);
        return switch (columnIndex)
        {
          case 0 -> addon.domain();
          case 1 -> addon.name();
          case 2 -> addon.moduleInfo().version();
          case 3 -> addon.addonPath();
          case 4 -> addon.enabled();
          default -> null;
        };
      }
    }

    private class AddonModel extends DefaultTableModel
    {
      private final Addon addon;

      AddonModel(Addon addon)
      {
        this.addon = addon;
      }

      @Override
      public int getRowCount()
      {
        return 5;
      }

      @Override
      public int getColumnCount()
      {
        return 2;
      }

      @Override
      public String getColumnName(int column)
      {
        return switch (column)
        {
          case 0 -> "Name";
          case 1 -> "Value";
          default -> throw new IndexOutOfBoundsException();
        };
      }

      @Override
      public Object getValueAt(int rowIndex, int columnIndex)
      {
        return switch (columnIndex)
        {
          case 0 -> switch (rowIndex)
          {
            case 0 -> "domain";
            case 1 -> "name";
            case 2 -> "moduleInfo().version";
            case 3 -> "addonPath";
            case 4 -> "enabled";
            default -> throw new IndexOutOfBoundsException();
          };
          case 1 -> switch (rowIndex)
          {
            case 0 -> addon.domain();
            case 1 -> addon.name();
            case 2 -> addon.moduleInfo().version();
            case 3 -> addon.addonPath();
            case 4 -> addon.enabled();
            default -> throw new IndexOutOfBoundsException();
          };
          default -> throw new IndexOutOfBoundsException();
        };
      }
    }
  }
}
