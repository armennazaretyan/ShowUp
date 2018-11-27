package com.development.blackbox.showup.Models;

import java.util.ArrayList;
import java.util.List;

public class ActiveUsersResponse {

    public List<UserUIModel> ActiveUsersList = null;
    public List<Long> MyRequestsToUsers = null;
    public List<Long> RequestsToMe = null;
    public List<Long> PhotoToMe = null;

    public ActiveUsersResponse() {
        ActiveUsersList = new ArrayList<UserUIModel>();
        MyRequestsToUsers = new ArrayList<Long>();
        RequestsToMe = new ArrayList<Long>();
        PhotoToMe = new ArrayList<Long>();
    }
}

