package dev.nonvocal.gui;

import com.dscsag.plm.spi.interfaces.ECTRService;
import com.dscsag.plm.spi.interfaces.logging.PlmLogger;
import dev.nonvocal.addon.AddonCollector;
import dev.nonvocal.gui.tree.AddonBundleTreeNode;
import dev.nonvocal.gui.tree.AddonTreeNode;
import dev.nonvocal.gui.widget.Searchbar;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.*;

public class MainUI extends JFrame
{

    private final ECTRService ectrService;
    private final PlmLogger logger;

    private final AddonCollector.AddonCollection addons;

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
        setPreferredSize(new Dimension(800, 600));
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
            setPreferredSize(new Dimension(300, 600));
            setMinimumSize(new Dimension(300, 600));

            this.navBar = new NavBar(addons);
            JScrollPane scrollingNavBar = new JScrollPane(navBar, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollingNavBar.setPreferredSize(navBar.getPreferredSize());
            scrollingNavBar.setMinimumSize(navBar.getMinimumSize());

            JPanel sidePanel = new JPanel(new BorderLayout());
            Searchbar searchbar = new Searchbar();
            searchbar.setMargin(new Insets(5, 5, 5, 5));
            sidePanel.add(searchbar, BorderLayout.NORTH);
            sidePanel.add(scrollingNavBar, BorderLayout.CENTER);

            sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            add(sidePanel, BorderLayout.WEST);

            this.detailPanel = new DetailPanel();
            JScrollPane scrollingDetailPanel = new JScrollPane(detailPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollingDetailPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            add(scrollingDetailPanel, BorderLayout.CENTER);

            navBar.addTreeSelectionListener(new SelectionListener(detailPanel));


//            System.out.println(Integer.toBinaryString(-33));
//
////            Color c = new Color(2, 20, -33);
//            Color c = new Color(2, 20, 0b11011111);
//            System.out.println(c.getRGB());
//
//            System.out.println(c.getRed());
//            System.out.println(c.getGreen());
//            System.out.println(c.getBlue());
//
//            navBar.setBackground(c);

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
            TreePath newLeadSelectionPath = e.getNewLeadSelectionPath();
            if (newLeadSelectionPath == null)
                return;

            Object pathComponent = newLeadSelectionPath.getLastPathComponent();
            if (pathComponent instanceof AddonBundleTreeNode bundleNode)
                SwingUtilities.invokeLater(() -> detailPanel.updateContent(bundleNode.bundle()));
            else if (pathComponent instanceof AddonTreeNode addonNode)
                SwingUtilities.invokeLater(() -> detailPanel.updateContent(addonNode.addon()));
        }
    }
}
