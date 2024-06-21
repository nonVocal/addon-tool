//****************************************************************************
//
//                             Copyright (c) 2024
//                     DSC Software AG, Karlsruhe, Germany
//                             All rights reserved
//
//         The contents of this file is an unpublished work protected
//         under the copyright law of the Federal Republic of Germany
//
//         This software is proprietary to and embodies confidential
//         technology of DSC Software AG. Possession, use and copying
//         of the software and media is authorized only pursuant to a
//         valid written license from DSC Software AG. This copyright
//         statement must be visibly included in all copies.
//
//****************************************************************************

package dev.nonvocal.addon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class AddonCollection
{
  private final Map<String, Collection<Addon>> addons = new HashMap<>();

  public Collection<Addon> get(String domain)
  {
    return addons.getOrDefault(domain, List.of());
  }

  public Collection<String> domains()
  {
    return addons.keySet();
  }

  public Stream<AddonBundle> bundles()
  {
    return addons.entrySet().stream()
        .map(AddonBundle::of);
  }

  private void addAddon(String domain, Addon addon)
  {
    addons.computeIfAbsent(domain, k -> new ArrayList<>()).add(addon);
  }
}