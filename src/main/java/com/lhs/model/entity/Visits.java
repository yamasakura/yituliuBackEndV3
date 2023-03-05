package com.lhs.model.entity;


import lombok.Data;

@Data
public class Visits {

    private Integer visits = 0;


    public void updateVisits() {
        this.visits ++;
    }


}
