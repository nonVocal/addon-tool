package dev.nonvocal.gui.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import dev.nonvocal.addon.AddonBundle;

public class AddonBundleTreeNode extends DefaultMutableTreeNode
{
  private final AddonBundle bundle;

  public AddonBundleTreeNode(AddonBundle bundle)
  {
    this.bundle = bundle;
    bundle.addons().stream()
        .map(AddonTreeNode::new)
        .forEach(this::add);
  }

  @Override
  public Object getUserObject()
  {
    return bundle();
  }

  public AddonBundle bundle()
  {
    return bundle;
  }

  @Override
  public String toString()
  {
    return bundle.domain();
  }
}