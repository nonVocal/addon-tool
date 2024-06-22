package dev.nonvocal.gui;

import dev.nonvocal.addon.Addon;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class BundleTableCellRenderer extends DefaultTableCellRenderer
{
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        if (column == 3)
        {
            BundleModel model = (BundleModel) table.getModel();

            boolean enabled = (boolean) value;
            JToggleButton jToggleButton = new JToggleButton(enabled ? "enabled" : "disabled");
            jToggleButton.addActionListener(e ->
            {
                Addon addon = model.bundle().addons().get(row);
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