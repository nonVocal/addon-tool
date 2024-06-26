package dev.nonvocal.gui;

import dev.nonvocal.addon.Addon;

import javax.swing.table.DefaultTableModel;

public class AddonModel extends DefaultTableModel
{
    private final Addon addon;

    public AddonModel(Addon addon)
    {
        this.addon = addon;
    }

    public Addon addon()
    {
        return addon;
    }

    @Override
    public int getRowCount()
    {
        if (addon == null)
            return 0;

        return 6;
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
                case 5 -> "config";
                default -> throw new IndexOutOfBoundsException();
            };
            case 1 -> switch (rowIndex)
            {
                case 0 -> addon.domain();
                case 1 -> addon.name();
                case 2 -> addon.version();
                case 3 -> addon.addonPath();
                case 4 -> addon.enabled();
                case 5 -> "config";
                default -> throw new IndexOutOfBoundsException();
            };
            default -> throw new IndexOutOfBoundsException();
        };
    }
}