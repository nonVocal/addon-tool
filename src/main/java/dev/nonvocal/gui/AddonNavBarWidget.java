package dev.nonvocal.gui;

import com.dscsag.plm.spi.interfaces.commons.ResourceAccessor;
import dev.nonvocal.addon.Addon;

import javax.swing.*;
import java.awt.*;

public final class AddonNavBarWidget extends JPanel
{
    private final ResourceAccessor resourceAccessor;
    private final Addon addon;

    private final ImageIcon icon;

    public AddonNavBarWidget(ResourceAccessor resourceAccessor, Addon addon)
    {
        this.resourceAccessor = resourceAccessor;
        this.addon = addon;

        this.icon = resourceAccessor.getImageIcon(addon.domain() + "." + addon.name(), ResourceAccessor.IconSize.LARGE);

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 2, 0.2, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(5, 10, 5, 5), 0, 0);

        JLabel iconLabel = new JLabel(icon, JLabel.CENTER);
        add(iconLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 0.7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets.bottom = 0;
        gbc.insets.left = 5;

        JLabel nameLabel = new JLabel(addon.name());
        add(nameLabel, gbc);

        JLabel labelVersion = new JLabel(addon.version() + " " + addon.domain());
        labelVersion.setForeground(UIManager.getColor("Label.disabledForeground"));

        gbc.gridy = 1;
        gbc.insets.bottom = 5;
        gbc.insets.top = 0;

        add(labelVersion, gbc);

        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.1;
        gbc.insets.bottom = 0;
        gbc.insets.top = 5;
        gbc.ipadx = 5;
        gbc.insets.right = 10;

        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(addon.enabled());
        checkBox.addActionListener(e -> {
            if (addon.enabled())
                addon.disable();
            else
                addon.enable();
        });

        add(checkBox, gbc);

        addon.addEnableListener(e -> {
            checkBox.setSelected(e);
            this.setEnabled(e);
            iconLabel.setEnabled(e);
            nameLabel.setEnabled(e);
        });

        setMaximumSize(getPreferredSize());
    }

    public Addon addon()
    {
        return addon;
    }
}