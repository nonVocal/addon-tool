package dev.nonvocal.gui;

import com.dscsag.plm.spi.interfaces.commons.ResourceAccessor;
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

        return new AddonNavBarWidget(resourceAccessor, addonNode.addon());
    }
}
