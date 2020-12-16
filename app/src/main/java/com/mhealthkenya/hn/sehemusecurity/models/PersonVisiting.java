package com.mhealthkenya.hn.sehemusecurity.models;

import org.json.JSONObject;

public class PersonVisiting {
    private int id;
    private String PersonDetails;
    private String uuid;
    private String created_at;
    private String updated_at;
    private int organization;
    private int created_by;
    private String updated_by;


    public PersonVisiting(int id, String PersonDetails, String uuid, String created_at, String updated_at, int organization, int created_by, String updated_by) {
        this.id = id;
        this.PersonDetails = PersonDetails;
        this.uuid = uuid;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.organization = organization;
        this.created_by = created_by;
        this.updated_by = updated_by;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPersonDetails() {
        return PersonDetails;
    }

    public void PersonDetails(String PersonDetails) {
        this.PersonDetails = PersonDetails;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public int getOrganization() {
        return organization;
    }

    public void setOrganization(int organization) {
        this.organization = organization;
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
        if (!(anotherObject instanceof PersonVisiting)) {
            return false;
        }
        PersonVisiting p = (PersonVisiting) anotherObject;
        return (this.id == p.id);
    }
}
