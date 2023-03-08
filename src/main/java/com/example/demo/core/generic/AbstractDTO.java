package com.example.demo.core.generic;

import javax.persistence.GeneratedValue;
import java.util.UUID;

public abstract class AbstractDTO {
  @GeneratedValue
  private UUID id;

  protected AbstractDTO() {
  }

  protected AbstractDTO(UUID id) {
    this.id = id;
  }

  public UUID getId() {
    return id;
  }

  public AbstractDTO setId(UUID id) {
    this.id = id;
    return this;
  }
}
