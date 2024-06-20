package dev.nonvocal.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.dscsag.plm.spi.interfaces.ECTRService;
import com.dscsag.plm.spi.interfaces.logging.PlmLogger;

import dev.nonvocal.addon.Addon;
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
    setSize(500, 500);
    setLocationRelativeTo(null);
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

      this.navBar = new NavBar(addons);
      JScrollPane scrollingNavBar = new JScrollPane(navBar, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
          JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      add(scrollingNavBar, BorderLayout.WEST);

      this.detailPanel = new DetailPanel();
      JScrollPane scrollingDetailPanel = new JScrollPane(detailPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
          JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      add(scrollingDetailPanel, BorderLayout.CENTER);
      //      add(detailPanel, BorderLayout.CENTER);

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
      {
        AddonCollector.AddonCollection.AddonBundle bundle = bundleNode.bundle();
        SwingUtilities.invokeLater(() -> detailPanel.setHeaderText(bundle.domain()));
      }
      else if (pathComponent instanceof AddonTreeNode addonNode)
      {
        Addon addon = addonNode.addon();
        SwingUtilities.invokeLater(() -> detailPanel.setHeaderText(addon.domain() + ":" + addon.name()));
      }
    }
  }

  private static final class DetailPanel extends JPanel
  {
    private JLabel header;

    public DetailPanel()
    {
      super(new BorderLayout());
      header = new JLabel("");
      add(header, BorderLayout.NORTH);
    }

    void setHeaderText(String text)
    {
      header.setText("<html><h1>" + text + "</h1></html>");
      invalidate();
      repaint();
    }
  }
}
