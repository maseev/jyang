package io.github.maseev.jyang.model;

public class RPC extends Entity {

  private final Grouping input;

  private final Grouping output;

  public RPC(final String name, final String description, final Grouping input,
             final Grouping output) {
    super(name, description);
    this.input = input;
    this.output = output;
  }

  public Grouping getInput() {
    return input;
  }

  public Grouping getOutput() {
    return output;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof RPC)) {
      return false;
    }

    if (!super.equals(o)) {
      return false;
    }

    RPC rpc = (RPC) o;

    if (input != null) {
      if (!input.equals(rpc.input)) {
        return false;
      }
    } else {
      if (rpc.input != null) {
        return false;
      }
    }

    return output != null ? output.equals(rpc.output) : rpc.output == null;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (input != null ? input.hashCode() : 0);
    result = 31 * result + (output != null ? output.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "RPC{" +
      "input=" + input +
      ", output=" + output +
      "} " + super.toString();
  }
}
