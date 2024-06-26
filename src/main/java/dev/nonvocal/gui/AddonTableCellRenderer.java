package dev.nonvocal.gui;

import dev.nonvocal.addon.Addon;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.IOException;

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
                    addon.disable();
                else
                    addon.enable();
                model.fireTableDataChanged();
            });

            AddonModel model = (AddonModel) table.getModel();
            Addon addon = model.addon();
            addon.addEnableListener(e -> {
                jToggleButton.setText(addon.enabled() ? "enabled" : "disabled");
                table.repaint();

            });


            return jToggleButton;
        }
        else if (column == 1 && row == 5)
        {
            JToggleButton jToggleButton = new JToggleButton("config");
            jToggleButton.addActionListener(e ->
            {
                AddonModel model = (AddonModel) table.getModel();
                Addon addon = model.addon();
                try
                {
                    addon.openConfig();
                } catch (IOException ex)
                {
                    throw new RuntimeException(ex);
                }
            });

            return jToggleButton;
        }


        return super.getTableCellRendererComponent(table, value, false, false, row, column);
    }
}