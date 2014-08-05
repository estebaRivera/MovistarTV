package com.smartboxtv.movistartv.data.modelssm.datarecommendation;

import com.smartboxtv.movistartv.services.DataMember;

import java.util.Date;

/**
 * Created by Esteban- on 05-08-14.
 */
public class EpisodeRecommendationSM {
    public String description;
    public Date startDate;
    public Date endate;
    public String name;
    public int id;

    public EpisodeRecommendationSM() {
    }

    @DataMember( member = "description")
    public String getDescription() {
        return description;
    }
    @DataMember( member = "description")
    public void setDescription(String description) {
        this.description = description;
    }
    @DataMember( member = "startDate")
    public Date getStartDate() {
        return startDate;
    }
    @DataMember( member = "startDate")
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    @DataMember( member = "endDate")
    public Date getEndate() {
        return endate;
    }
    @DataMember( member = "endDate")
    public void setEndate(Date endate) {
        this.endate = endate;
    }
    @DataMember( member = "name")
    public String getName() {
        return name;
    }
    @DataMember( member = "name")
    public void setName(String name) {
        this.name = name;
    }
    @DataMember( member = "id")
    public int getId() {
        return id;
    }
    @DataMember( member = "id")
    public void setId(int id) {
        this.id = id;
    }
}
