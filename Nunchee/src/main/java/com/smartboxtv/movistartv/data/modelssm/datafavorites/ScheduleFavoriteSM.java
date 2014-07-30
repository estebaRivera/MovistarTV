package com.smartboxtv.movistartv.data.modelssm.datafavorites;

import com.smartboxtv.movistartv.services.DataMember;

import java.util.Date;

/**
 * Created by Esteban- on 24-06-14.
 */
public class ScheduleFavoriteSM {

    public int id;
    public String name;
    public String description;
    public Date startDate;
    public Date endDate;
    public ChannelFavoriteSM channel;

    public ScheduleFavoriteSM() {

    }
    @DataMember( member = "id_episode" )
    public int getId() {
        return id;
    }
    @DataMember( member = "id_episode" )
    public void setId(int id) {
        this.id = id;
    }
    @DataMember( member = "name" )
    public String getName() {
        return name;
    }
    @DataMember( member = "name" )
    public void setName(String name) {
        this.name = name;
    }
    @DataMember( member = "description" )
    public String getDescription() {
        return description;
    }
    @DataMember( member = "description" )
    public void setDescription(String description) {
        this.description = description;
    }
    @DataMember( member = "start_date" )
    public Date getStartDate() {
        return startDate;
    }
    @DataMember( member = "start_date" )
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    @DataMember( member = "end_date" )
    public Date getEndDate() {
        return endDate;
    }
    @DataMember( member = "end_date" )
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ChannelFavoriteSM getChannel() {
        return channel;
    }

    public void setChannel(ChannelFavoriteSM channel) {
        this.channel = channel;
    }
}
