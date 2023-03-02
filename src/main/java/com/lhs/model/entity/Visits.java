package com.lhs.model.entity;



public class Visits {

    private Integer visits = 0;

    public Integer getVisits() {
        return visits;
    }

    public void setVisits(Integer visits) {
        this.visits = visits;
    }

    public void updateVisits() {
        this.visits ++;
    }

    @Override
    public String toString() {
        return "Visits{" +
                "visits=" + visits +
                '}';
    }
}
