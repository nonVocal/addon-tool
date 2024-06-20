package dev.nonvocal.addon.module;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Dependency(@JsonProperty("name") String name, @JsonProperty("version") String version)
{

  @Override
  public String toString()
  {
    return "Dependency{" +
        "name='" + name + '\'' +
        ", version='" + version + '\'' +
        '}';
  }
}
