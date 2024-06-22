package dev.nonvocal.gui;

import dev.nonvocal.addon.Addon;
import dev.nonvocal.addon.AddonBundle;

import javax.swing.table.DefaultTableModel;

public class BundleModel extends DefaultTableModel
{
    private final AddonBundle bundle;

    public BundleModel(AddonBundle bundle)
    {
        this.bundle = bundle;
    }

    public AddonBundle bundle()
    {
        return bundle;
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