package dev.nonvocal.gui;

import com.dscsag.plm.spi.interfaces.ECTRService;
import com.dscsag.plm.spi.interfaces.logging.PlmLogger;
import dev.nonvocal.addon.Addon;
import dev.nonvocal.addon.AddonBundle;
import dev.nonvocal.addon.AddonCollector;
import dev.nonvocal.gui.tree.AddonBundleTreeNode;
import dev.nonvocal.gui.tree.AddonTreeModel;
import dev.nonvocal.gui.tree.AddonTreeNode;
import dev.nonvocal.gui.widget.Searchbar;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.OptionalInt;

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
//            add(scrollingNavBar, BorderLayout.WEST);

            JPanel sidePanel = new JPanel(new BorderLayout());
            Searchbar searchbar = new Searchbar();
            searchbar.setMargin(new Insets(5,5,5,5));
//            searchbar.setBorder(BorderFactory.createEmptyBorder(0, 0,10,0));
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

    private static final class NavBar extends JTree
    {
        private final AddonTreeModel model;

        private NavBar(AddonCollector.AddonCollection addons)
        {
            super(new AddonTreeModel(addons));
            this.setRootVisible(false);
            this.setShowsRootHandles(true);

            this.setOpaque(false);
            this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));


            this.model = (AddonTreeModel) super.getModel();

            var fm = getFontMetrics(getFont());
            int widthRoot = fm.stringWidth(model.getRoot().toString());

            OptionalInt max = addons.bundles()
                    .flatMap(bundle -> bundle.addons().stream())
                    .mapToInt(a -> fm.stringWidth(a.domain() + a.name()))
                    .max();

            int maxLength = widthRoot + max.getAsInt();

            setMinimumSize(new Dimension(maxLength, 100));
            setPreferredSize(new Dimension(maxLength, 100));
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

    private static final class DetailPanel extends JPanel
    {
        private final JLabel header;
        private final JTable table;

        public DetailPanel()
        {
            super(new BorderLayout());
            header = new JLabel("");
            add(header, BorderLayout.NORTH);

            table = new JTable(new DefaultTableModel())
            {
                @Override
                public boolean editCellAt(int row, int column)
                {
                    return false;
                }

                @Override
                public boolean editCellAt(int row, int column, EventObject e)
                {
                    return false;
                }
            };
            table.setSize(500, 500);

            JPanel content = new JPanel(new BorderLayout());
            content.add(table.getTableHeader(), BorderLayout.NORTH);
            content.add(new JScrollPane(table), BorderLayout.CENTER);

            add(content, BorderLayout.CENTER);

            table.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    var model = table.getModel();

                    var row = table.rowAtPoint(e.getPoint());
                    var col = table.columnAtPoint(e.getPoint());

                    var comp = table.getCellRenderer(row, col).getTableCellRendererComponent(table, model.getValueAt(row, col), false, false, row, col);
                    if (comp instanceof JToggleButton button)
                        button.doClick();
                }
            });
        }

        void setHeaderText(String text)
        {
            header.setText("<html><h1>" + text + "</h1></html>");
        }

        void updateContent(AddonBundle bundle)
        {
            setHeaderText(bundle.domain());
            table.setModel(new BundleModel(bundle));
            table.setDefaultRenderer(Object.class, new BundleTableCellRenderer());

            invalidate();
        }

        void updateContent(Addon addon)
        {
            setHeaderText(addon.domain() + ":" + addon.name());
            table.setModel(new AddonModel(addon));
            table.setDefaultRenderer(Object.class, new AddonTableCellRenderer());

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
                if (bundle == null)
                    return 0;

                return bundle.addons().size();
            }

            @Override
            public int getColumnCount()
            {
                if (bundle == null)
                    return 0;

                return 4;
            }

            @Override
            public String getColumnName(int column)
            {
                return switch (column)
                {
                    case 0 -> "Name";
                    case 1 -> "Version";
                    case 2 -> "Path";
                    case 3 -> "Enabled";
                    default -> throw new IndexOutOfBoundsException();
                };
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex)
            {
                Addon addon = bundle.addons().get(rowIndex);
                return switch (columnIndex)
                {
                    case 0 -> addon.name();
                    case 1 -> addon.version();
                    case 2 -> addon.addonPath();
                    case 3 -> addon.enabled();
                    default -> null;
                };
            }
        }

        private static class AddonModel extends DefaultTableModel
        {
            private final Addon addon;

            AddonModel(Addon addon)
            {
                this.addon = addon;
            }

            @Override
            public int getRowCount()
            {
                if (addon == null)
                    return 0;

                return 5;
            }

            @Override
            public int getColumnCount()
            {
                if (addon == null)
                    return 0;

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
                        case 2 -> "version";
                        case 3 -> "addonPath";
                        case 4 -> "enabled";
                        default -> throw new IndexOutOfBoundsException();
                    };
                    case 1 -> switch (rowIndex)
                    {
                        case 0 -> addon.domain();
                        case 1 -> addon.name();
                        case 2 -> addon.version();
                        case 3 -> addon.addonPath();
                        case 4 -> addon.enabled();
                        default -> throw new IndexOutOfBoundsException();
                    };
                    default -> throw new IndexOutOfBoundsException();
                };
            }
        }
    }

    private static final class AddonTableCellRenderer extends DefaultTableCellRenderer
    {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            if (column == 1 && row == 4)
            {
                boolean enabled = (boolean) value;
                JToggleButton jToggleButton = new JToggleButton(enabled ? "enabled" : "disabled");
                jToggleButton.addActionListener(e ->
                {
                    DetailPanel.AddonModel model = (DetailPanel.AddonModel) table.getModel();
                    Addon addon = model.addon;
                    if (addon.enabled())
                    {
                        addon.disable();
                        jToggleButton.setText("disabled");
                        model.fireTableDataChanged();
                    }
                    else
                    {
                        addon.enable();
                        jToggleButton.setText("enabled");
                        model.fireTableDataChanged();
                    }
                });


                return jToggleButton;
            }


            return super.getTableCellRendererComponent(table, value, false, false, row, column);
        }
    }

    private static final class BundleTableCellRenderer extends DefaultTableCellRenderer
    {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            if (column == 3)
            {
                DetailPanel.BundleModel model = (DetailPanel.BundleModel) table.getModel();

                boolean enabled = (boolean) value;
                JToggleButton jToggleButton = new JToggleButton(enabled ? "enabled" : "disabled");
                jToggleButton.addActionListener(e ->
                {
                    Addon addon = model.bundle.addons().get(row);
                    if (addon.enabled())
                    {
                        addon.disable();
                        jToggleButton.setText("disabled");
                        model.fireTableDataChanged();
                    }
                    else
                    {
                        addon.enable();
                        jToggleButton.setText("enabled");
                        model.fireTableDataChanged();
                    }
                });

                return jToggleButton;
            }

            return super.getTableCellRendererComponent(table, value, false, false, row, column);
        }
    }
}
