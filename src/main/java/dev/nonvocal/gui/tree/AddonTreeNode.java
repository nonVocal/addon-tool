package dev.nonvocal.gui.tree;

import dev.nonvocal.addon.Addon;

import javax.swing.tree.DefaultMutableTreeNode;

public class AddonTreeNode extends DefaultMutableTreeNode
{
    private final Addon addon;

    public AddonTreeNode(Addon addon)
    {
        this.addon = addon;
    }
}