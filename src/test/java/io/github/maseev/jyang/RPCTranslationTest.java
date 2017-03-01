package io.github.maseev.jyang;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.github.maseev.jyang.annotation.NetconfEndpoint;
import io.github.maseev.jyang.annotation.NetconfProcedure;
import io.github.maseev.jyang.dto.PlainDTO;
import io.github.maseev.jyang.dto.PlainEntity;
import io.github.maseev.jyang.model.Container;
import io.github.maseev.jyang.model.Grouping;
import io.github.maseev.jyang.model.Leaf;
import io.github.maseev.jyang.model.Module;
import io.github.maseev.jyang.model.RPC;
import io.github.maseev.jyang.model.Revision;
import io.github.maseev.jyang.model.YANGCollection;
import io.github.maseev.jyang.model.YANGMap;
import io.github.maseev.jyang.util.TemplateUtil;
import javafx.util.Pair;

public class RPCTranslationTest {

  @Test
  public void translatingEndpointWithZeroNetconfProceduresShouldReturnEmptyList() {
    @NetconfEndpoint
    class Endpoint {
    }

    List<RPC> rpcs = new Translator().translateEndpoint(Endpoint.class).getValue();

    assertThat(rpcs, is(Collections.emptyList()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void translatingClassWithoutRequiredEndpointAnnotationShouldThrowException() {
    class Endpoint {
    }

    new Translator().translateEndpoint(Endpoint.class);
  }

  @Test
  public void translatingEndpointShouldReturnOnlyMethodsWithRequiredAnnotation() {
    @NetconfEndpoint
    class Endpoint {

      public void first() {
      }

      @NetconfProcedure
      public void second() {
      }

      public void third() {
      }
    }

    List<RPC> rpcs = new Translator().translateEndpoint(Endpoint.class).getValue();

    assertThat(rpcs.size(), is(equalTo(1)));
  }

  @Test
  public void translatingEndpointWithSpecifiedAnnotationValuesShouldReturnSpecificName() {
    final String ENDPOINT_NAME = "endpoint";
    final String PROCEDURE_NAME = "procedure";
    final String DESCRIPTION = "test something";

    @NetconfEndpoint(ENDPOINT_NAME)
    class Endpoint {

      @NetconfProcedure(value = PROCEDURE_NAME, description = DESCRIPTION)
      public void test() {
      }
    }

    List<RPC> rpcs = new Translator().translateEndpoint(Endpoint.class).getValue();

    assertEquals(1, rpcs.size());

    RPC rpc = rpcs.get(0);

    RPC expectedRpc = new RPC(ENDPOINT_NAME + '.' + PROCEDURE_NAME, DESCRIPTION, null, null);

    assertThat(rpc, is(equalTo(expectedRpc)));
  }

  @Test
  public void translatingEndpointWithoutSpecifyingAnnotationValuesShouldUseClassAndMethodNames() {
    @NetconfEndpoint
    class Endpoint {

      @NetconfProcedure
      public void test() {
      }
    }

    List<RPC> rpcs = new Translator().translateEndpoint(Endpoint.class).getValue();

    assertThat(rpcs.size(), is(equalTo(1)));

    RPC rpc = rpcs.get(0);

    RPC expectedRpc = new RPC("Endpoint.test", "", null, null);

    assertThat(rpc, is(equalTo(expectedRpc)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void duplicateEndpointNamesShouldCauseException() {
    @NetconfEndpoint
    class Endpoint {
    }

    @NetconfEndpoint(value = "Endpoint")
    class DuplicateEndpoint {
    }

    Translator translator = new Translator();

    translator.translateEndpoint(Endpoint.class);
    translator.translateEndpoint(DuplicateEndpoint.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void duplicateProcedureNamesShouldCauseException() {
    @NetconfEndpoint
    class Endpoint {

      @NetconfProcedure
      public void test() {
      }

      @NetconfProcedure
      public void test(int n) {
      }
    }

    new Translator().translateEndpoint(Endpoint.class);
  }

  @Test
  public void translateSimpleValueProcedure() {
    @NetconfEndpoint
    class Endpoint {

      @NetconfProcedure
      public String test(int param) {
        throw new UnsupportedOperationException();
      }
    }

    List<RPC> rpcs = new Translator().translateEndpoint(Endpoint.class).getValue();

    assertEquals(1, rpcs.size());

    RPC rpc = rpcs.get(0);

    Grouping expectedInput = new Grouping("");
    expectedInput.getLeafs().add(new Leaf("int", int.class));
    Grouping expectedOutput = new Grouping("");
    expectedOutput.getLeafs().add(new Leaf("String", String.class));
    RPC expectedRpc = new RPC("Endpoint.test", "", expectedInput, expectedOutput);

    assertThat(rpc, is(equalTo(expectedRpc)));
  }

  @Test
  public void translateEntityProcedure() {
    @NetconfEndpoint
    class Endpoint {

      @NetconfProcedure
      public PlainDTO test(PlainDTO input) {
        throw new UnsupportedOperationException();
      }
    }

    Pair<List<Grouping>, List<RPC>> pair = new Translator().translateEndpoint(Endpoint.class);
    List<RPC> rpcs = pair.getValue();

    assertEquals(1, rpcs.size());

    RPC rpc = rpcs.get(0);

    Grouping expectedInput = new Grouping("");
    expectedInput.getContainers().add(new Container("plain", "plain"));
    Grouping expectedOutput = new Grouping("");
    expectedOutput.getContainers().add(new Container("plain", "plain"));
    RPC expectedRpc = new RPC("Endpoint.test", "", expectedInput, expectedOutput);

    assertThat(rpc, is(equalTo(expectedRpc)));
    assertThat(pair.getKey().size(), is(equalTo(1)));
  }

  @Test
  public void translateListProcedure() {
    @NetconfEndpoint
    class Endpoint {

      @NetconfProcedure
      public List<String> test(List<Integer> integers) {
        throw new UnsupportedOperationException();
      }
    }

    List<RPC> rpcs = new Translator().translateEndpoint(Endpoint.class).getValue();

    assertEquals(1, rpcs.size());

    RPC rpc = rpcs.get(0);

    Grouping expectedInput = new Grouping("");
    expectedInput.getLists().add(new YANGCollection("List", Integer.class));
    Grouping expectedOutput = new Grouping("");
    expectedOutput.getLists().add(new YANGCollection("List", String.class));
    RPC expectedRpc = new RPC("Endpoint.test", "", expectedInput, expectedOutput);

    assertThat(rpc, is(equalTo(expectedRpc)));
  }

  @Test
  public void translateEntityListProcedure() {
    @NetconfEndpoint
    class Endpoint {

      @NetconfProcedure
      public List<PlainDTO> test(List<PlainDTO> dtoList) {
        throw new UnsupportedOperationException();
      }
    }

    List<RPC> rpcs = new Translator().translateEndpoint(Endpoint.class).getValue();

    assertEquals(1, rpcs.size());

    RPC rpc = rpcs.get(0);

    Grouping expectedInput = new Grouping("");
    expectedInput.getLists().add(new YANGCollection("List", PlainDTO.class));
    Grouping expectedOutput = new Grouping("");
    expectedOutput.getLists().add(new YANGCollection("List", PlainDTO.class));
    RPC expectedRpc = new RPC("Endpoint.test", "", expectedInput, expectedOutput);

    assertThat(rpc, is(equalTo(expectedRpc)));
  }

  @Test
  public void translateMapProcedure() {
    @NetconfEndpoint
    class Endpoint {

      @NetconfProcedure
      public Map<String, PlainDTO> test(Map<String, PlainDTO> map) {
        throw new UnsupportedOperationException();
      }
    }

    List<RPC> rpcs = new Translator().translateEndpoint(Endpoint.class).getValue();

    assertEquals(1, rpcs.size());

    RPC rpc = rpcs.get(0);

    Grouping expectedInput = new Grouping("");
    expectedInput.getMaps().add(new YANGMap("Map", String.class, PlainDTO.class));
    Grouping expectedOutput = new Grouping("");
    expectedOutput.getMaps().add(new YANGMap("Map", String.class, PlainDTO.class));
    RPC expectedRpc = new RPC("Endpoint.test", "", expectedInput, expectedOutput);

    assertThat(rpc, is(equalTo(expectedRpc)));
  }

  @Test
  public void transformingModuleShouldPass() {
    @NetconfEndpoint
    class Endpoint {

      @NetconfProcedure(description = "test map")
      public Map<String, PlainDTO> test(int[] data) {
        throw new UnsupportedOperationException();
      }
    }

    Pair<List<Grouping>, List<RPC>> pair = new Translator().translateEndpoint(Endpoint.class);
    Module module =
      new Module("test",
        "test description",
        "test namespace",
        "pref",
        new Revision(new Date(),"revision description"));

    module.getGroupings().addAll(pair.getKey());
    module.getRpcs().addAll(pair.getValue());

    TemplateUtil.transform(module, "templates/module.mustache");
  }

  @Test
  public void translatingEntityWithArrayField() {
    @NetconfEndpoint
    class Endpoint {

      @NetconfProcedure
      public PlainEntity test(int[] data) {
        throw new UnsupportedOperationException();
      }
    }

    List<RPC> rpcs = new Translator().translateEndpoint(Endpoint.class).getValue();

    assertEquals(1, rpcs.size());

    RPC rpc = rpcs.get(0);

    Grouping expectedInput = new Grouping("");
    expectedInput.getLists().add(new YANGCollection("int-array", int.class));
    Grouping expectedOutput = new Grouping("");
    expectedOutput.getContainers().add(new Container("PlainEntity", "PlainEntity"));
    RPC expectedRpc = new RPC("Endpoint.test", "", expectedInput, expectedOutput);

    assertThat(rpc, is(equalTo(expectedRpc)));
  }
}
