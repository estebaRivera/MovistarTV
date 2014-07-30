package com.smartboxtv.movistartv.data.models;

import com.smartboxtv.movistartv.services.DataMember;

import java.io.Serializable;

/**
 * Created by Esteban- on 18-04-14.
 */
public class Image implements Serializable{

    public String IType;
    public String IdImage;
    public String ImageLanguage;
    public String ImageName;
    public String ImagePath;
    public String ImageSize;
    public String ImageSource;
    public String ImageType;
    public String ImageUploadTime;
    public int ImageHeight;
    public int ImageWidth;

    public Image(){

    }

    @DataMember( member = "ImageWidth")
    public int getImageWidth() {
        return ImageWidth;
    }
    @DataMember( member = "ImageWidth")
    public void setImageWidth(int imageWidth) {
        ImageWidth = imageWidth;
    }
    @DataMember( member = "ImageHeigth")
    public int getImageHeight() {
        return ImageHeight;
    }
    @DataMember( member = "ImageHeigth")
    public void setImageHeight(int imageHeight) {
        ImageHeight = imageHeight;
    }
    @DataMember( member = "ImageUploadTime")
    public String getImageUploadTime() {
        return ImageUploadTime;
    }
    @DataMember( member = "ImageUploadTime")
    public void setImageUploadTime(String imageUploadTime) {
        ImageUploadTime = imageUploadTime;
    }
    @DataMember( member = "ImageType")
    public String getImageType() {
        return ImageType;
    }
    @DataMember( member = "ImageType")
    public void setImageType(String imageType) {
        ImageType = imageType;
    }
    @DataMember( member = "ImageSource")
    public String getImageSource() {
        return ImageSource;
    }
    @DataMember( member = "ImageSource")
    public void setImageSource(String imageSource) {
        ImageSource = imageSource;
    }
    @DataMember( member = "ImageSize")
    public String getImageSize() {
        return ImageSize;
    }
    @DataMember( member = "ImageSize")
    public void setImageSize(String imageSize) {
        ImageSize = imageSize;
    }
    @DataMember( member = "ImagePath")
    public String getImagePath() {
        return ImagePath;
    }
    @DataMember( member = "ImagePath")
    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }
    @DataMember( member = "ImageName")
    public String getImageName() {
        return ImageName;
    }
    @DataMember( member = "ImageName")
    public void setImageName(String imageName) {
        ImageName = imageName;
    }
    @DataMember( member = "ImageLanguage")
    public String getImageLanguage() {
        return ImageLanguage;
    }
    @DataMember( member = "ImageLanguage")
    public void setImageLanguage(String imageLanguage) {
        ImageLanguage = imageLanguage;
    }
    @DataMember( member = "IdImage")
    public String getIdImage() {
        return IdImage;
    }
    @DataMember( member = "IdImage")
    public void setIdImage(String idImage) {
        IdImage = idImage;
    }
    @DataMember( member = "IType")
    public String getIType() {
        return IType;
    }
    @DataMember( member = "IType")
    public void setIType(String IType) {
        this.IType = IType;
    }
}
