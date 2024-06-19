package dev.nonvocal.gui.tree;

import dev.nonvocal.addon.AddonCollector;

import javax.swing.tree.DefaultMutableTreeNode;

public class AddonBundleTreeNode extends DefaultMutableTreeNode
{
    private final AddonCollector.AddonCollection.AddonBundle bundle;

    public AddonBundleTreeNode(AddonCollector.AddonCollection.AddonBundle bundle)
    {
        this.bundle = bundle;
        bundle.addons().stream()
                .map(AddonTreeNode::new)
                .forEach(this::add);
    }
}