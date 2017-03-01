package io.github.maseev.jyang.model;

import java.util.ArrayList;
import java.util.List;

public class Module extends Entity {

  private final String namespace;

  private final String prefix;

  private final List<Grouping> groupings;

  private final List<RPC> rpcs;

  private final Revision revision;

  public Module(final String name, final String description, final String namespace,
                final String prefix, final Revision revision) {
    super(name, description);
    this.namespace = namespace;
    this.prefix = prefix;
    this.revision = revision;
    groupings = new ArrayList<>();
    rpcs = new ArrayList<>();
  }

  public String getNamespace() {
    return namespace;
  }

  public String getPrefix() {
    return prefix;
  }

  public List<Grouping> getGroupings() {
    return groupings;
  }

  public List<RPC> getRpcs() {
    return rpcs;
  }

  public Revision getRevision() {
    return revision;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Module)) {
      return false;
    }

    if (!super.equals(o)) {
      return false;
    }

    Module module = (Module) o;

    return namespace.equals(module.namespace)
      && prefix.equals(module.prefix)
      && groupings.equals(module.groupings)
      && rpcs.equals(module.rpcs)
      && revision.equals(module.revision);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();

    result = 31 * result + namespace.hashCode();
    result = 31 * result + prefix.hashCode();
    result = 31 * result + groupings.hashCode();
    result = 31 * result + rpcs.hashCode();
    result = 31 * result + revision.hashCode();

    return result;
  }

  @Override
  public String toString() {
    return "Module{" +
      "namespace='" + namespace + '\'' +
      ", prefix='" + prefix + '\'' +
      ", groupings=" + groupings +
      ", rpcs=" + rpcs +
      ", revision=" + revision +
      "} " + super.toString();
  }
}
