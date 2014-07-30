package com.smartboxtv.movistartv.data.models;

import com.smartboxtv.movistartv.services.DataMember;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Esteban- on 18-04-14.
 */
public class Program implements Comparable,Serializable {

    public int CheckIn;
    public int Like;
    public Date StartDate;
    public Date EndDate;
    public String date;
    public String Description;
    public String EpisodeTitle;
    public String descriptionEpisode;
    public String Hashtags;
    public String IdProgram;
    public String IdEpisode;
    public String PActor;
    public String PCategory;
    public String Title;
    public String PFlags;
    public String PValues;
    public String RDescription;
    public String urlImage;
    public Channel PChannel;
    public List<Tweets> Tweets;
    public boolean ICheckIn;
    public boolean ILike;
    public boolean IsFavorite;
    public ArrayList<Image> listaImage = null;

    public Program() {

    }

    @Override
    public int compareTo(Object o) {

        Program p = (Program)o;

        if(this.getStartDate().getTime()> p.getStartDate().getTime())
            return 1;
        else if(this.getStartDate().getTime()< p.getStartDate().getTime())
            return -1;

        return 0;
    }
    public Image getImageWidthType(String width, String type){

        if(listaImage!= null){
            if(!listaImage.isEmpty()){
                for (Image aListaImage : listaImage) {
                    if (aListaImage.getImageType().equals(type) && aListaImage.getImageSize().equals(width)) {

                        return aListaImage;
                    }
                }
            }
            else{
                return null;
            }
        }
        else{
            return  null;
        }
        return null;
    }
    @Override
    public boolean equals(Object o) {

        Program p = (Program)o;

        return p.getIdProgram().equals(this.getIdProgram()) && p.getStartDate().equals(this.getStartDate()) && p.getEndDate().equals(this.getEndDate()) ;

    }

    @DataMember( member = "CheckIn" )
    public int getCheckIn() {
        return CheckIn;
    }
    @DataMember( member = "CheckIn" )
    public void setCheckIn(int checkIn) {
        CheckIn = checkIn;
    }

    @DataMember( member = "Date" )
    public String getDate() {
        return this.date;
    }

    @DataMember( member = "Date" )
    public void setDate(String date) {
        this.date = date;
    }

    @DataMember( member = "Description" )
    public String getDescription() {
        return Description;
    }
    @DataMember( member = "Description" )
    public void setDescription(String description) {
        Description = description;
    }

    @DataMember( member = "EndDate" )
    public Date getEndDate() {
        return EndDate;
    }
    @DataMember( member = "EndDate" )
    public void setEndDate(Date endDate) {
        EndDate = endDate;
    }

    @DataMember( member = "EpisodeTitle" )
    public String getEpisodeTitle() {
        return EpisodeTitle;
    }
    @DataMember( member = "EpisodeTitle" )
    public void setEpisodeTitle(String episodeTitle) {
        EpisodeTitle = episodeTitle;
    }

    @DataMember( member = "Hashtags" )
    public String getHashtags() {
        return Hashtags;
    }
    @DataMember( member = "Hashtags" )
    public void setHashtags(String hashtags) {
        Hashtags = hashtags;
    }

    @DataMember( member = "IChekIn" )
    public boolean isICheckIn() {
        return ICheckIn;
    }
    @DataMember( member = "ICheckIn" )
    public void setICheckIn(boolean ICheckIn) {
        this.ICheckIn = ICheckIn;
    }

    @DataMember( member = "ILike" )
    public boolean isILike() {
        return ILike;
    }
    @DataMember( member = "ILike" )
    public void setILike(boolean ILike) {
        this.ILike = ILike;
    }

    @DataMember( member = "IdPrgram" )
    public String getIdProgram() {
        return IdProgram;
    }
    @DataMember( member = "IdProgram" )
    public void setIdProgram(String idProgram) {
        IdProgram = idProgram;
    }

    @DataMember( member = "IsFavorite" )
    public boolean isFavorite() {
        return IsFavorite;
    }
    @DataMember( member = "IsFavorite" )
    public void setFavorite(boolean isFavorite) {
        IsFavorite = isFavorite;
    }

    @DataMember( member = "Likes" )
    public int getLike() {
        return Like;
    }
    @DataMember( member = "Likes" )
    public void setLike(int like) {
        Like = like;
    }

   /* @DataMember( member = "PActor" )
    public String getPActor() {
        return PActor;
    }
    @DataMember( member = "PActor" )
    public void setPActor(String PActor) {
        this.PActor = PActor;
    }*/

    @DataMember( member = "PCategory" )
    public String getPCategory() {
        return PCategory;
    }
    @DataMember( member = "PCategory" )
    public void setPCategory(String PCategory) {
        this.PCategory = PCategory;
    }

    public Channel getPChannel() {
        return PChannel;
    }
    public void setPChannel(Channel PChannel) {
        this.PChannel = PChannel;
    }

    @DataMember( member = "PFlags" )
    public String getPFlags() {
        return PFlags;
    }
    @DataMember( member = "PFlags" )
    public void setPFlags(String PFlags) {
        this.PFlags = PFlags;
    }

    @DataMember( member = "PValues" )
    public String getPValues() {
        return PValues;
    }
    @DataMember( member = "PValues" )
    public void setPValues(String PValues) {
        this.PValues = PValues;
    }

    @DataMember( member = "RDescription" )
    public String getRDescription() {
        return RDescription;
    }
    @DataMember( member = "RDescription" )
    public void setRDescription(String RDescription) {
        this.RDescription = RDescription;
    }

    @DataMember( member = "StartDate" )
    public Date getStartDate() {

        return StartDate;
    }
    @DataMember( member = "StartDate" )
    public void setStartDate(Date startDate) {
        StartDate = startDate;
    }

    @DataMember( member = "Title" )
    public String getTitle() {
        return Title;
    }
    @DataMember( member = "Title" )
    public void setTitle(String title) {
        Title = title;
    }

    public List<Tweets> getTweets() {
        return Tweets;
    }
    public void setTweets(List<Tweets> tweets) {
        Tweets = tweets;
    }

    public ArrayList<Image> getListaImage() {
        return listaImage;
    }
    public void setListaImage(ArrayList<Image> listaImage) {
        this.listaImage = listaImage;
    }

}
