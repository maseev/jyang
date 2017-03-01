package io.github.maseev.jyang.dto;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ComplexDTO {

  @XmlElement(name = "plain")
  private PlainDTO plainDTO;

  @XmlElement
  private List<PlainDTO> dtos;

  @XmlElement(name = "dto-map")
  private Map<String, PlainDTO> plainDTOMap;
}
