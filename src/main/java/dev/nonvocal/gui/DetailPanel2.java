package dev.nonvocal.gui;

import dev.nonvocal.addon.Addon;
import dev.nonvocal.addon.AddonBundle;
import dev.nonvocal.gui.widget.MarkdownPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;

public class DetailPanel2 extends JPanel
{
    private final JLabel header;
    private final JTable table;

    public DetailPanel2()
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

        JPanel tablePane = new JPanel(new BorderLayout());
        tablePane.add(table.getTableHeader(), BorderLayout.NORTH);
        tablePane.add(new JScrollPane(table), BorderLayout.CENTER);
        tablePane.add((table), BorderLayout.CENTER);


        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(tablePane);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Overview", new JPanel());
        MarkdownPanel readMeMD = new MarkdownPanel();
        readMeMD.setMarkdown(readMe());
        tabs.addTab("ReadMe", readMeMD);
        tabs.addTab("Changelog", new MarkdownPanel());
        tabs.addTab("Other Infos", new JPanel());

        content.setPreferredSize(new Dimension(500, 0));
        content.add(tabs);

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

        invalidate();
        revalidate();
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

//        table.getColumnModel().getColumn(0).setMaxWidth(250);

        invalidate();
    }



    String readMe()
    {
        return """
                ######  This addon can show all installed SAP ECTR addons and enable/disable individual addons.

                If you wanna use this then
                 - clone
                 - build
                 - add a new addon under <ECTR_INST_IDR>\\addons\\<YOUR_CHOSEN_DIR_NAME>
                   - *NOTE:* I don't explain what folder structure you need. You pretend to be a big boy, then solve this yourself.
                 - Add the OMF fnc.edit.addons
                   - *NOTE:* Be brave and ask your Admin to explain this to you.
                   - *NOTE2:* Hopefully for you, your admins don't have ANY sharp or heavy stuff in arms reach.
                
                After you successfully installed this addon you just simply need to call fnc.edit.addons

                """;
    }
}