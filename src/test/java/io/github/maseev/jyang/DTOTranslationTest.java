package io.github.maseev.jyang;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import io.github.maseev.jyang.dto.ComplexDTO;
import io.github.maseev.jyang.dto.DtoEntity;
import io.github.maseev.jyang.dto.Entity;
import io.github.maseev.jyang.dto.PlainDTO;
import io.github.maseev.jyang.model.Container;
import io.github.maseev.jyang.model.Grouping;
import io.github.maseev.jyang.model.Leaf;
import io.github.maseev.jyang.model.YANGCollection;
import io.github.maseev.jyang.model.YANGMap;

public class DTOTranslationTest {

  @Test
  public void translatePlainDTO() {
    Grouping expectedGrouping = new Grouping("plain");

    expectedGrouping.getLeafs().addAll(asList(
      new Leaf("id", Integer.class),
      new Leaf("name", String.class),
      new Leaf("is-active", boolean.class)));

    Grouping grouping = new Translator().translateGrouping(PlainDTO.class);

    assertThat(grouping, is(equalTo(expectedGrouping)));
  }

  @Test
  public void translateComplexDTO() {
    Grouping grouping = new Translator().translateGrouping(ComplexDTO.class);

    Grouping expectedGrouping = new Grouping(ComplexDTO.class.getSimpleName(), "");
    Container plainDtoContainer = new Container("plain", "plain");

    assertEquals("ComplexDTO", grouping.getName());
    assertEquals(0, grouping.getLeafs().size());
    assertEquals(1, grouping.getContainers().size());
    assertEquals(1, grouping.getLists().size());
    assertEquals(1, grouping.getMaps().size());

    YANGCollection yangCollection = grouping.getLists().get(0);
    assertEquals("dtos", yangCollection.getName());
    assertEquals("plain", yangCollection.getType());
    assertFalse(yangCollection.isLeafList());

    YANGMap yangMap = grouping.getMaps().get(0);
    assertEquals("dto-map", yangMap.getName());
    assertTrue(yangMap.getKey().isPrimitive());
    assertFalse(yangMap.getValue().isPrimitive());
    assertEquals("string", yangMap.getKey().getType());
    assertEquals("plain", yangMap.getValue().getType());
  }

  @Test
  public void translatingEntityWithMapsToAnnotationShouldUseTheSpecifiedClassAsTarget() {
    Grouping expectedGrouping = new Grouping(DtoEntity.class.getSimpleName());
    expectedGrouping.getLeafs().addAll(asList(
      new Leaf("i", int.class),
      new Leaf("d", double.class)
    ));

    Grouping grouping = new Translator().translateGrouping(Entity.class);

    assertThat(grouping, is(equalTo(expectedGrouping)));
  }
}
