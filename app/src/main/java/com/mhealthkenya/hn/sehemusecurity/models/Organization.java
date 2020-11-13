package com.mhealthkenya.hn.sehemusecurity.models;

public class Organization {
    private int id;
    private String uuid;
    private String name;
    private String logo;
    private String floor;
    private String extra_info;
    private String created_at;
    private String updated_at;
    private int unit_id;
    private int created_by;
    private String updated_by;



    public Organization(int id, String uuid, String name, String logo, String floor, String extra_info, String created_at, String updated_at, int unit_id, int created_by, String updated_by) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.logo = logo;
        this.floor = floor;
        this.extra_info = extra_info;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.unit_id = unit_id;
        this.created_by = created_by;
        this.updated_by = updated_by;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getExtra_info() {
        return extra_info;
    }

    public void setExtra_info(String extra_info) {
        this.extra_info = extra_info;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(int unit_id) {
        this.unit_id = unit_id;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }


    @Override
    public boolean equals(Object anotherObject) {
        if (!(anotherObject instanceof Organization)) {
            return false;
        }
        Organization o = (Organization) anotherObject;
        return (this.id == o.id);
    }
}