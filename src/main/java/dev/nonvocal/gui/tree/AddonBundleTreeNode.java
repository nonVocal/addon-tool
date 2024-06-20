package dev.nonvocal.gui.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import dev.nonvocal.addon.AddonCollector;

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

  @Override
  public Object getUserObject()
  {
    return bundle();
  }

  public AddonCollector.AddonCollection.AddonBundle bundle()
  {
    return bundle;
  }

  @Override
  public String toString()
  {
    return bundle.domain();
  }
}