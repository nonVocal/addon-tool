package dev.nonvocal.addon.module;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Dependency(@JsonProperty("name") String name, @JsonProperty("version") String version)
{

}
