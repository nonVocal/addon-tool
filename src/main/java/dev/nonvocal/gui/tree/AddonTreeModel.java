package dev.nonvocal.gui.tree;

import dev.nonvocal.addon.AddonCollector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

;

public class AddonTreeModel extends DefaultTreeModel
{
    private final DefaultMutableTreeNode root;

    public AddonTreeModel(AddonCollector.AddonCollection addons)
    {
        super(new DefaultMutableTreeNode("Addons"));
        root = (DefaultMutableTreeNode) super.root;

        addons.bundles()
                .map(AddonTreeModel::createSubTree)
                .forEach(this.root::add);

    }

    private static AddonBundleTreeNode createSubTree(AddonCollector.AddonCollection.AddonBundle bundle)
    {
        return new AddonBundleTreeNode(bundle);
    }
}
