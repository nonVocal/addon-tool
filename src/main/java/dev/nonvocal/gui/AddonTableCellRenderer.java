package dev.nonvocal.gui;

import dev.nonvocal.addon.Addon;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class AddonTableCellRenderer extends DefaultTableCellRenderer
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
                AddonModel model = (AddonModel) table.getModel();
                Addon addon = model.addon();
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