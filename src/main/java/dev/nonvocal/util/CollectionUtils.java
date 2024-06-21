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

package dev.nonvocal.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CollectionUtils
{
  private static final Class<?> CLASS_ABSTRACT_IMMUTABLE_LIST = List.of().getClass().getSuperclass();
  private static final Class<?> CLASS_UNMODIFIABLE_LIST = Collections.unmodifiableList(new LinkedList<>()).getClass();
  private static final Class<?> CLASS_UNMODIFIABLE_RANDOM_LIST = Collections.unmodifiableList(new ArrayList<>()).getClass();

  public static <T> List<T> toList(Collection<T> collection)
  {
    if (collection instanceof List<T> l)
      return l;

    return new ArrayList<>(collection);
  }

  public static <T> List<T> toImmutableList(Collection<T> collection)
  {
    if (isImmutableList(collection))
      return (List<T>) collection;
    else
      return List.copyOf(collection);
  }

  public static boolean isImmutableList(Collection<?> collection)
  {
    Class<?> myClass = collection.getClass();
    return myClass == CLASS_ABSTRACT_IMMUTABLE_LIST || myClass == CLASS_UNMODIFIABLE_LIST || myClass == CLASS_UNMODIFIABLE_RANDOM_LIST;
  }
}
