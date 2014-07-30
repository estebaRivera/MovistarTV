package com.smartboxtv.movistartv.data.modelssm.datacategory;

import com.smartboxtv.movistartv.data.modelssm.EpisodeSM;
import com.smartboxtv.movistartv.services.DataMember;

import java.util.Date;

/**
 * Created by Esteban- on 20-06-14.
 */
public class ProgramsCategorySM implements Comparable{

    public int id;
    public int like;
    public int favorite;
    public String image;
    public String title;
    public Date end;
    public Date start;
    public String description;
    public EpisodeSM episode;
    public ChannelCategorySM channel;


    public ProgramsCategorySM() {

    }

    @DataMember( member = "id_program")
    public int getId() {
        return id;
    }
    @DataMember( member = "id_program")
    public void setId(int id) {
        this.id = id;
    }
    @DataMember( member = "like")
    public int getLike() {
        return like;
    }
    @DataMember( member = "like")
    public void setLike(int like) {
        this.like = like;
    }
    @DataMember( member = "favorite")
    public int getFavorite() {
        return favorite;
    }
    @DataMember( member = "favorite")
    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
    @DataMember( member = "image")
    public String getImage() {
        return image;
    }
    @DataMember( member = "image")
    public void setImage(String image) {
        this.image = image;
    }
    @DataMember( member = "original_title")
    public String getTitle() {
        return title;
    }
    @DataMember( member = "original_title")
    public void setTitle(String title) {
        this.title = title;
    }
    @DataMember( member = "end_date")
    public Date getEnd() {
        return end;
    }
    @DataMember( member = "end_date")
    public void setEnd(Date end) {
        this.end = end;
    }
    @DataMember( member = "start_date")
    public Date getStart() {
        return start;
    }
    @DataMember( member = "start_date")
    public void setStart(Date start) {
        this.start = start;
    }
    @DataMember( member = "description")
    public String getDescription() {
        return description;
    }
    @DataMember( member = "description")
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int compareTo(Object o) {

        ProgramsCategorySM p = (ProgramsCategorySM)o;

        if(this.start.getTime()> p.start.getTime())
            return 1;
        else if(this.start.getTime()< p.start.getTime())
            return -1;

        return 0;
    }
}
