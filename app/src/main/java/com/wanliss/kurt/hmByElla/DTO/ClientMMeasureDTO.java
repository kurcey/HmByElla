/*
        Copyright 2018 Kurt Wanliss

        All rights reserved under the copyright laws of the United States
        and applicable international laws, treaties, and conventions.

        You may freely redistribute and use this sample code, with or
        without modification, provided you include the original copyright
        notice and use restrictions.

*/

package com.wanliss.kurt.hmByElla.DTO;

import java.io.Serializable;

public class ClientMMeasureDTO implements Serializable {
    private String chest;
    private String waist;
    private String hips;
    private String rise;
    private String length;
    private String inseam;
    private String outseam;

    public ClientMMeasureDTO() {

    }

    public ClientMMeasureDTO(String chest, String waist, String hips, String rise, String length, String inseam, String outseam) {
        this.chest = chest;
        this.waist = waist;
        this.hips = hips;
        this.rise = rise;
        this.length = length;
        this.inseam = inseam;
        this.outseam = outseam;
    }

    public String getChest() {
        return chest;
    }

    public String getWaist() {
        return waist;
    }

    public String getHips() {
        return hips;
    }

    public String getRise() {
        return rise;
    }

    public String getLength() {
        return length;
    }

    public String getInseam() {
        return inseam;
    }

    public String getOutseam() {
        return outseam;
    }

}
