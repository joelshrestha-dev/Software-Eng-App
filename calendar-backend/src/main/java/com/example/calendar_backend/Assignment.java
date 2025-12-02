package com.example.calendar_backend;

import java.time.LocalDate;

public class Assignment {
  private final String title;
  private final LocalDate dueDate;
  private final int estTime;

  public Assignment(String title, LocalDate dueDate, int estTime) {
    this.title = title;
    this.dueDate = dueDate;
    this.estTime = estTime;
  }

  public String getTitle() {
    return title;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public int getEstTime() {
    return estTime;
  }
}
