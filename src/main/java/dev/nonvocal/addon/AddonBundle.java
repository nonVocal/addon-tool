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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import dev.nonvocal.util.CollectionUtils;

public record AddonBundle(String domain, List<Addon> addons)
{
  public static AddonBundle of(Map.Entry<String, Collection<Addon>> entry)
  {
    return new AddonBundle(entry.getKey(), CollectionUtils.toImmutableList(entry.getValue()));
  }
}