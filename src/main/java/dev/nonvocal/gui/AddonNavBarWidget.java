package dev.nonvocal.gui;

import com.dscsag.plm.spi.interfaces.commons.ResourceAccessor;
import dev.nonvocal.addon.Addon;
import org.eclipse.jdt.annotation.NonNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public final class AddonNavBarWidget extends JPanel
{
    private final Addon addon;

    private final AddonEnableListener addonEnableListener;

    public AddonNavBarWidget(ResourceAccessor resourceAccessor, Addon addon)
    {
        this.addon = addon;

        ImageIcon icon = resourceAccessor.getImageIcon(addon.domain() + "." + addon.name(), ResourceAccessor.IconSize.LARGE);
        if (icon == null)
        {
            BufferedImage bi = new BufferedImage(32,32, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = (Graphics2D) bi.getGraphics();
            g.setColor(Color.MAGENTA);
            g.draw3DRect(0, 0, 32, 32, true);
            icon = new ImageIcon(bi);
        }

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 2, 1, 1,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets(5, 10, 5, 5), 0, 0);

        JLabel iconLabel = new JLabel(icon, JLabel.LEADING);
        add(iconLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets.bottom = 0;
        gbc.insets.left = 5;

        JLabel nameLabel = new JLabel(addon.name());
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        var ps = nameLabel.getPreferredSize();
        ps.width = 180;
        nameLabel.setPreferredSize(ps);
        add(nameLabel, gbc);

        JLabel versionLabel = new JLabel(addon.version() + " | " + addon.domain());
        versionLabel.setHorizontalAlignment(SwingConstants.LEFT);
        versionLabel.setForeground(UIManager.getColor("Label.disabledForeground"));

        gbc.gridy = 1;
        gbc.insets.bottom = 5;
        gbc.insets.top = 0;

        add(versionLabel, gbc);

        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.insets.bottom = 0;
        gbc.insets.top = 5;
        gbc.ipadx = 5;
        gbc.insets.right = 10;
        gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
        gbc.fill = GridBagConstraints.NONE;

        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(addon.enabled());
        checkBox.addActionListener(new AddonEnableAction(addon));

        add(checkBox, gbc);

        addonEnableListener = e ->
        {
            checkBox.setSelected(e);
            this.setEnabled(e);
            iconLabel.setEnabled(e);
            nameLabel.setEnabled(e);
        };

        addon.addEnableListener(addonEnableListener);
    }

    public Addon addon()
    {
        return addon;
    }

    private record AddonEnableAction(@NonNull Addon addon) implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (addon.enabled())
                addon.disable();
            else
                addon.enable();
        }
    }

    @Override
    public void setVisible(boolean visible)
    {
        super.setVisible(visible);
        if (visible)
            addon.addEnableListener(addonEnableListener);
        else
            addon.removeEnableListener(addonEnableListener);
    }
}