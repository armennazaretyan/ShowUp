package com.development.blackbox.showup.Models;

import com.development.blackbox.showup.Helpers.Enums.GenderEnumType;

import java.io.Serializable;

public class UserUIModel implements Serializable {

    public long ID = 0;
    public String UserName = null;
    public String LoginName = null;
    public String Password = "";
    public String ImageURL = null;
    public GenderEnumType GenderType = null;
    public int Age = -1;
    public boolean IsActive = false;

    public boolean IsMeRequestedAlready = false;
    public boolean IsPhotoToMe = false;
    public boolean IsRequestToMe = false;
}
