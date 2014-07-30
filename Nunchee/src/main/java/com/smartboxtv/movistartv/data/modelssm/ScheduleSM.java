package com.smartboxtv.movistartv.data.modelssm;

import com.smartboxtv.movistartv.services.DataMember;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Esteban- on 17-06-14.
 */
public class ScheduleSM implements Comparable{

    public EpisodeSM episode;
    public ProgramSM program;

    public int id;
    public String startDate;
    public String endDate;
    public String title;

    public Date start;
    public Date end;

    public SimpleDateFormat format = new SimpleDateFormat();



    public ScheduleSM() {

    }

    @Override
    public int compareTo(Object o) {

        ScheduleSM s = (ScheduleSM)o;

        if(this.start.getTime()> s.start.getTime())
            return 1;
        else if(this.start.getTime()< s.start.getTime())
            return -1;

        return 0;
    }

    @DataMember( member = "id_schedule" )
    public int getId() {
        return id;
    }
    @DataMember( member = "id_schedule" )
    public void setId(int id) {
        this.id = id;
    }

    @DataMember( member = "start_date" )
    public String getStartDate() {
        return startDate;
    }
    @DataMember( member = "start_date" )
    public void setStartDate(String startDate) {
        this.startDate = startDate;
        try{
            start = format.parse(startDate);
        }
        catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    @DataMember( member = "end_date" )
    public String getEndDate() {
        return endDate;
    }
    @DataMember( member = "end_date" )
    public void setEndDate(String endDate) {
        this.endDate = endDate;
        try{
            end = format.parse(endDate);
        }
        catch (ParseException ex) {
            ex.printStackTrace();
        }
    }
    @DataMember( member = "original_title" )
    public String getTitle() {
        return title;
    }
    @DataMember( member = "original_title" )
    public void setTitle(String title) {
        this.title = title;
    }

    /////////////////////////////////////

    public EpisodeSM getEpisode() {
        return episode;
    }

    public void setEpisode(EpisodeSM episode) {
        this.episode = episode;
    }

    public ProgramSM getProgram() {
        return program;
    }

    public void setProgram(ProgramSM program) {
        this.program = program;
    }

}
