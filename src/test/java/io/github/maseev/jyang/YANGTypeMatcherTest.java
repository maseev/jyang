package io.github.maseev.jyang;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import io.github.maseev.jyang.model.YANGType;
import io.github.maseev.jyang.util.YANGTypeMatcher;

public class YANGTypeMatcherTest {

  @Test
  public void matchingExpectedTypeShouldPass() {
    YANGType yangType = YANGTypeMatcher.match(Boolean.class);

    assertThat(yangType, is(equalTo(YANGType.BOOLEAN)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void matchingUnexpectedTypeShouldThrowException() {
    YANGTypeMatcher.match(YANGTypeMatcher.class);
  }
}