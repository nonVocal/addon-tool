package dev.nonvocal.gui.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import dev.nonvocal.addon.AddonBundle;
import dev.nonvocal.addon.AddonCollector;

;

public class AddonTreeModel extends DefaultTreeModel
{
    private final DefaultMutableTreeNode root;

    public AddonTreeModel(AddonCollector.AddonCollection addons)
    {
        super(new DefaultMutableTreeNode("Addons"));
        root = (DefaultMutableTreeNode) super.root;

        addons.bundles()
                .filter(bundle -> !bundle.addons().isEmpty())
                .map(AddonTreeModel::createSubTree)
                .forEach(this.root::add);

        reload();
    }

    private static AddonBundleTreeNode createSubTree(AddonBundle bundle)
    {
        return new AddonBundleTreeNode(bundle);
    }

    @Override
    public DefaultMutableTreeNode getRoot()
    {
        return root;
    }

}
