package dev.nonvocal.gui;

import com.dscsag.plm.spi.interfaces.ECTRService;
import dev.nonvocal.addon.AddonCollector;
import dev.nonvocal.gui.tree.AddonBundleTreeNode;
import dev.nonvocal.gui.tree.AddonTreeModel;
import dev.nonvocal.gui.tree.AddonTreeNode;
import org.osgi.service.component.annotations.Reference;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.*;

public class MainUI extends JFrame
{
    @Reference
    private ECTRService ectrService;

    private AddonCollector.AddonCollection addons;

    public MainUI()
    {
        super("Addon-Tool");

        AddonCollector addonCollector = new AddonCollector(ectrService);
        addons = addonCollector.collectAddons();



        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
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
            add(navBar, BorderLayout.WEST);

            this.detailPanel = new DetailPanel();
            add(detailPanel, BorderLayout.CENTER);

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

    @SuppressWarnings("ClassCanBeRecord")
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
            TreePath[] paths = e.getPaths();
            if (paths != null && paths.length == 1)
            {
                Object pathComponent = e.getPath().getLastPathComponent();
                if (pathComponent instanceof AddonBundleTreeNode bundleNode)
                {

                }
                else if(pathComponent instanceof AddonTreeNode addonNode)
                {

                }

            }
        }
    }

    private static final class DetailPanel extends JPanel
    {

    }
}
