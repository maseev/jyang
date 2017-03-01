package io.github.maseev.jyang.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Revision {

  private final String date;

  private final String description;

  public Revision(final Date date, final String description) {
    this.date = new SimpleDateFormat("yyyy-MM-dd").format(date);
    this.description = description;
  }

  public String getDate() {
    return date;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Revision)) {
      return false;
    }

    Revision revision = (Revision) o;

    return date.equals(revision.date) && description.equals(revision.description);
  }

  @Override
  public int hashCode() {
    int result = date.hashCode();

    result = 31 * result + description.hashCode();

    return result;
  }

  @Override
  public String toString() {
    return "Revision{" +
      "date='" + date + '\'' +
      ", description='" + description + '\'' +
      '}';
  }
}
