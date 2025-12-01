package com.example.calendar_backend;

import java.util.List;

public class PlanResponse {
  private final List<Assignment> assignments;

  public PlanResponse(List<Assignment> assignments) {
    this.assignments = assignments;
  }

  public List<Assignment> getAssignments() {
    return assignments;
  }
}
