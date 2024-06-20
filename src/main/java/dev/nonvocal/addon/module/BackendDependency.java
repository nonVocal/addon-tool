package dev.nonvocal.addon.module;

import com.fasterxml.jackson.annotation.JsonProperty;

// TODO version and tuple (release, patch) mutially exclusive! only one reprasentation only!
public record BackendDependency(String name, VersionComp release, VersionComp patch, VersionComp version)
{
  public BackendDependency(@JsonProperty ("name") String name, @JsonProperty ("release") String release,
      @JsonProperty ("patch") String patch, @JsonProperty ("version") String version)
  {
    this(name, VersionComp.of(release), VersionComp.of(patch), VersionComp.of(version));
  }

  @Override
  public String toString()
  {
    return "BackendDependency{" +
        "name='" + name + '\'' +
        ", release=" + release +
        ", patch=" + patch +
        ", version=" + version +
        '}';
  }
}
