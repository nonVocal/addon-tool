package dev.nonvocal.gui.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import dev.nonvocal.addon.Addon;

public class AddonTreeNode extends DefaultMutableTreeNode
{
  private final Addon addon;

  public AddonTreeNode(Addon addon)
  {
    this.addon = addon;
  }

  @Override
  public Object getUserObject()
  {
    return addon();
  }

  public Addon addon()
  {
    return addon;
  }

  @Override
  public boolean isLeaf()
  {
    return true;
  }

  @Override
  public String toString()
  {
    return addon.name();
  }
}