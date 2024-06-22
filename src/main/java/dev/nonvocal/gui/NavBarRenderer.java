package dev.nonvocal.gui;

import com.dscsag.plm.spi.interfaces.commons.ResourceAccessor;
import dev.nonvocal.addon.Addon;
import dev.nonvocal.gui.tree.AddonTreeNode;
import org.eclipse.jdt.annotation.NonNull;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class NavBarRenderer extends DefaultTreeCellRenderer
{
    private static final boolean ALT_ADDON_NODE_RENDER = false;

    private final ResourceAccessor resourceAccessor;

    public NavBarRenderer(@NonNull ResourceAccessor resourceAccessor)
    {
        this.resourceAccessor = resourceAccessor;
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        if (ALT_ADDON_NODE_RENDER)
            return getTreeCellRendererComponentAlt(tree, value, sel, expanded, leaf, row, hasFocus);

        Component treeCellRendererComponent = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        if (value instanceof AddonTreeNode addonNode && treeCellRendererComponent instanceof JLabel label
                && !addonNode.addon().enabled())
        {
            Color c = UIManager.getColor("Label.disabledForeground");
            label.setForeground(c);
        }

        return treeCellRendererComponent;
    }

    public Component getTreeCellRendererComponentAlt(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        if (!(value instanceof AddonTreeNode addonNode))
            return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        return new AddonCell(resourceAccessor, addonNode.addon());
    }

    private static final class AddonCell extends JPanel
    {
        private final ResourceAccessor resourceAccessor;
        private final Addon addon;

        private final ImageIcon icon;

        private AddonCell(ResourceAccessor resourceAccessor, Addon addon)
        {
            this.resourceAccessor = resourceAccessor;
            this.addon = addon;

            this.icon = resourceAccessor.getImageIcon(addon.domain() + "." + addon.name(), ResourceAccessor.IconSize.LARGE);

            setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 2, 0.2, 1,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(5, 5, 5, 5), 0, 0);

            add(new JLabel(icon, JLabel.CENTER), gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.gridheight = 1;
            gbc.gridwidth = 3;
            gbc.weightx = 0.7;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.WEST;

            add(new JLabel(addon.name()), gbc);

            JLabel labelVersion = new JLabel(addon.version() + " " + addon.domain());
            labelVersion.setForeground(UIManager.getColor("Label.disabledForeground"));

            gbc.gridy = 1;

            add(labelVersion, gbc);

            gbc.gridx = 5;
            gbc.gridy = 0;
            gbc.gridheight = 1;
            gbc.gridwidth = 1;
            gbc.weightx = 0.1;

            JCheckBox checkBox = new JCheckBox();
            checkBox.setSelected(addon.enabled());

            add(checkBox, gbc);
        }
    }


}
