package com.development.blackbox.showup.BusinessLayer;

import android.graphics.BitmapFactory;

import com.development.blackbox.showup.DataAccessLayer.WebAPIServiceProvider;
import com.development.blackbox.showup.Helpers.Enums.GenderEnumType;
import com.development.blackbox.showup.Models.ActiveUsersResponse;
import com.development.blackbox.showup.Models.PhotoSendModel;
import com.development.blackbox.showup.Models.UserUIModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class WebAPIService {

    public UserUIModel Register(String userName, String loginName, String pwd, int age, int gender, String imgPath, String android_id) throws Exception {

        UserUIModel retVal = new UserUIModel();

        try {

            WebAPIServiceProvider dbService = new WebAPIServiceProvider();
            String stringRetValObject = dbService.Register(userName, loginName, pwd, age, gender, imgPath, android_id);

            retVal.ID = Integer.parseInt(stringRetValObject);
            retVal.UserName = userName;
            retVal.LoginName = loginName;
            retVal.Password = pwd;
            retVal.Age = age;
            retVal.GenderType = GenderEnumType.ParseInt(gender);
            retVal.ImageURL = imgPath;

        } catch (Exception ex) {

            int ttt = 1;
            throw new Exception(ex.getMessage());
        }

        return retVal;
    }

    public boolean ChangeProfile(long userID, String userName) throws Exception {

        boolean retVal = true;

        try {

            WebAPIServiceProvider dbService = new WebAPIServiceProvider();
            String stringRetValObject = dbService.ChangeProfile(userID, userName);

            if (Integer.parseInt(stringRetValObject.toString()) == 1) {
                retVal = true;
            } else {
                retVal = false;
            }

        } catch (Exception ex) {

            retVal = false;
            throw new Exception(ex.getMessage());
        }

        return retVal;
    }

    public UserUIModel Login(String loginName, String pwd) throws Exception {

        UserUIModel retVal = null;

        try {

            WebAPIServiceProvider dbService = new WebAPIServiceProvider();
            String stringRetValObject = dbService.Login(loginName, pwd);

            retVal = ParseUserInfoResponseJSON(stringRetValObject);

        } catch (Exception ex) {

            int ttt = 1;
            throw new Exception(ex.getMessage());
        }

        return retVal;
    }

    public String GetNickName(int gender, String nickName) throws Exception {

        //UserUIModel retVal = null;
        String retVal = "";

        try {

            WebAPIServiceProvider dbService = new WebAPIServiceProvider();
            String stringRetValObject = dbService.GetNickName(gender, nickName);

            retVal = stringRetValObject.replace("\"", "");

            //JSONObject jObject = new JSONObject(stringRetValObject);
            //_UserInfoModel.UserName = jObject.getString("UN");
            //int tmp = 10;

        } catch (Exception ex) {

            int ttt = 1;
            throw new Exception(ex.getMessage());
        }

        return retVal;
    }

    public Object GetImageUrl(Boolean maleChecked) throws Exception {

        Object retVal = new Object();

        ArrayList<String> arrayList = new ArrayList<String>();
        if(maleChecked) {
            arrayList.add("http://blackbox.am/images/Avatar/meninblack.jpg");
            arrayList.add("http://blackbox.am/images/Avatar/eric_lange.jpg");
            arrayList.add("http://blackbox.am/images/Avatar/Getty.jpg");
            arrayList.add("http://blackbox.am/images/Avatar/pexels.jpeg");
            arrayList.add("http://blackbox.am/images/Avatar/pexels2.jpeg");
            arrayList.add("http://blackbox.am/images/Avatar/prison.jpg");
            arrayList.add("http://blackbox.am/images/Avatar/strak_trimmen.jpg");
        } else {
            arrayList.add("http://blackbox.am/images/Avatar/woman_tits.jpg");
            arrayList.add("http://blackbox.am/images/Avatar/woman_face.jpg");
            arrayList.add("http://blackbox.am/images/Avatar/girl1.jpg");
            arrayList.add("http://blackbox.am/images/Avatar/girl1_1.jpg");
            arrayList.add("http://blackbox.am/images/Avatar/girl2.jpg");
            arrayList.add("http://blackbox.am/images/Avatar/girl4.jpg");
        }

        Collections.shuffle(arrayList);

        try {
            String imageUrl = arrayList.get(0);
            retVal = BitmapFactory.decodeStream((InputStream)new URL(imageUrl).getContent());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    public UserUIModel LoginExpress(String userName, String loginName, String pwd, int age, int gender, String imgPath, String android_id) throws Exception {

        UserUIModel retVal = new UserUIModel();

        try {

            WebAPIServiceProvider dbService = new WebAPIServiceProvider();
            String stringRetValObject = dbService.LoginExpress(userName, loginName, pwd, age, gender, imgPath, android_id);

            retVal.ID = Integer.parseInt(stringRetValObject);
            retVal.UserName = userName;
            retVal.LoginName = loginName;
            retVal.Password = pwd;
            retVal.Age = age;
            retVal.GenderType = GenderEnumType.ParseInt(gender);
            retVal.ImageURL = imgPath;

        } catch (Exception ex) {

            int ttt = 1;
            throw new Exception(ex.getMessage());
        }

        return retVal;
    }

    public boolean SendPhoto(PhotoSendModel photoSendModel) throws Exception {

        boolean retVal = true;

        try {

            WebAPIServiceProvider dbService = new WebAPIServiceProvider();
            String stringRetValObject = dbService.SendPhoto(photoSendModel);

            if (Integer.parseInt(stringRetValObject.toString()) == 1) {
                retVal = true;
            } else {
                retVal = false;
            }

        } catch (Exception ex) {

            retVal = false;
            throw new Exception(ex.getMessage());
        }

        return retVal;
    }

    public String LoadPhoto(long myUserID, long senderUserID) throws Exception {

        String stringRetValObject = "";

        try {

            WebAPIServiceProvider dbService = new WebAPIServiceProvider();
            stringRetValObject = dbService.LoadPhoto(myUserID, senderUserID);

        } catch (Exception ex) {

            throw new Exception(ex.getMessage());
        }

        return stringRetValObject;
    }

    public boolean DeletePhoto(long myUserID, long senderUserID) throws Exception {

        boolean retVal = true;

        try {

            WebAPIServiceProvider dbService = new WebAPIServiceProvider();
            String stringRetValObject = dbService.DeletePhoto(myUserID, senderUserID);

            if (Integer.parseInt(stringRetValObject.toString()) == 1) {
                retVal = true;
            } else {
                retVal = false;
            }

        } catch (Exception ex) {

            retVal = false;
            throw new Exception(ex.getMessage());
        }

        return retVal;
    }

    public boolean TryRequest(long myUserID, long requestedUserID) throws Exception {

        boolean retVal = true;

        try {

            WebAPIServiceProvider dbService = new WebAPIServiceProvider();
            String stringRetValObject = dbService.TryRequest(myUserID, requestedUserID);

            if (Integer.parseInt(stringRetValObject.toString()) == 1) {
                retVal = true;
            } else {
                retVal = false;
            }

        } catch (Exception ex) {

            retVal = false;
            throw new Exception(ex.getMessage());
        }

        return retVal;
    }

    public ActiveUsersResponse GetActiveUsers(long userID) throws Exception {

        ActiveUsersResponse retVal = null;

        try {

            WebAPIServiceProvider dbService = new WebAPIServiceProvider();
            String stringRetValObject = dbService.GetActiveUsers(userID);

            retVal = ParseActiveUsersInfoResponseJSON(stringRetValObject);

        } catch (Exception ex) {

            throw new Exception(ex.getMessage());
        }

        return retVal;
    }

    public boolean CheckMessages(long userID) throws Exception {

        boolean retVal = true;

        try {

            WebAPIServiceProvider dbService = new WebAPIServiceProvider();
            String stringRetValObject = dbService.CheckMessages(userID);

            if (Integer.parseInt(stringRetValObject.toString()) == 1) {
                retVal = true;
            } else {
                retVal = false;
            }

        } catch (Exception ex) {

            retVal = false;
            throw new Exception(ex.getMessage());
        }

        return retVal;
    }

    public boolean IsAlive(long userID) throws Exception {

        boolean retVal = true;

        try {

            WebAPIServiceProvider dbService = new WebAPIServiceProvider();
            String stringRetValObject = dbService.IsAlive(userID);

            if (Integer.parseInt(stringRetValObject.toString()) == 1) {
                retVal = true;
            } else {
                retVal = false;
            }

        } catch (Exception ex) {

            retVal = false;
            throw new Exception(ex.getMessage());
        }

        return retVal;
    }

    public void GetLogTest(long userID) throws Exception {

        try {

            WebAPIServiceProvider dbService = new WebAPIServiceProvider();
            dbService.GetLogTest(userID);

        } catch (Exception ex) {

            //throw new Exception(ex.getMessage());
        }
    }

    public boolean SendToken(long userID, String clientToken, String android_id) throws Exception {

        boolean retVal = true;

        try {

            WebAPIServiceProvider dbService = new WebAPIServiceProvider();
            String stringRetValObject = dbService.SendToken(userID, clientToken, android_id);

            if (Integer.parseInt(stringRetValObject.toString()) == 1) {
                retVal = true;
            } else {
                retVal = false;
            }

        } catch (Exception ex) {

            retVal = false;
            throw new Exception(ex.getMessage());
        }

        return retVal;
    }

    public boolean LogData(String message, String stackTrace, String android_id) throws Exception {

        boolean retVal = true;

        try {

            WebAPIServiceProvider dbService = new WebAPIServiceProvider();
            String stringRetValObject = dbService.LogData(message, stackTrace, android_id);

            if (Integer.parseInt(stringRetValObject.toString()) == 1) {
                retVal = true;
            } else {
                retVal = false;
            }

        } catch (Exception ex) {

            retVal = false;
            throw new Exception(ex.getMessage());
        }

        return retVal;
    }


    // TODO: Parsers
    private UserUIModel ParseUserInfoResponseJSON(String complexDataInfo) throws JSONException {

        UserUIModel _UserInfoModel = new UserUIModel();

        try {

            JSONObject jObject = new JSONObject(complexDataInfo);

            _UserInfoModel.ID = jObject.getInt("I");
            _UserInfoModel.UserName = jObject.getString("UN");
            _UserInfoModel.Password = jObject.getString("P");
            _UserInfoModel.Age = jObject.getInt("A");
            _UserInfoModel.GenderType = GenderEnumType.ParseInt(jObject.getInt("G"));
            _UserInfoModel.ImageURL = jObject.getString("IP");

        } catch (JSONException e) {
            // Oops
            int ttr = 1;
            String stt = e.getMessage();
        }

        return _UserInfoModel;
    }

    private ActiveUsersResponse ParseActiveUsersInfoResponseJSON(String complexDataInfo) throws JSONException {

        ActiveUsersResponse _UserInfoModelList = new ActiveUsersResponse();

        JSONObject jObject = new JSONObject(complexDataInfo);

        try {

            JSONArray activeUsersList = jObject.getJSONArray("L");

            for (int i = 0; i < activeUsersList.length(); i++) {

                JSONObject activeUserJSON = activeUsersList.getJSONObject(i);

                UserUIModel userModel = new UserUIModel();

                userModel.ID = activeUserJSON.getLong("I");
                userModel.UserName = activeUserJSON.getString("UN");
                userModel.Password = activeUserJSON.getString("P");
                userModel.Age = activeUserJSON.getInt("A");
                userModel.GenderType = GenderEnumType.ParseInt(activeUserJSON.getInt("G"));
                userModel.ImageURL = activeUserJSON.getString("IP");
                userModel.IsActive = (activeUserJSON.getInt("IA") == 0) ? false : true;

                _UserInfoModelList.ActiveUsersList.add(userModel);
            }

            JSONArray myRequestsToUsersList = jObject.getJSONObject("RTM").getJSONArray("M");
            for (int i = 0; i < myRequestsToUsersList.length(); i++) {
                _UserInfoModelList.MyRequestsToUsers.add(myRequestsToUsersList.getLong(i));
            }

            JSONArray requestsToMeUsersList = jObject.getJSONObject("RTM").getJSONArray("R");
            for (int i = 0; i < requestsToMeUsersList.length(); i++) {
                _UserInfoModelList.RequestsToMe.add(requestsToMeUsersList.getLong(i));
            }

            JSONArray photoToMeUsersList = jObject.getJSONObject("RTM").getJSONArray("P");
            for (int i = 0; i < photoToMeUsersList.length(); i++) {
                _UserInfoModelList.PhotoToMe.add(photoToMeUsersList.getLong(i));
            }

        } catch (JSONException e) {
            // Oops
            int ttr = 1;
            String stt = e.getMessage();
        }

        return _UserInfoModelList;
    }
}
