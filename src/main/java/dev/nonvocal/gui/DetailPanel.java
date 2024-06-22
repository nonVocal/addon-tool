package dev.nonvocal.gui;

import dev.nonvocal.addon.Addon;
import dev.nonvocal.addon.AddonBundle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;

public class DetailPanel extends JPanel
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
}