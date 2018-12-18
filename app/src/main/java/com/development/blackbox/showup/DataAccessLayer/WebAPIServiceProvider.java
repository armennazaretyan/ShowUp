package com.development.blackbox.showup.DataAccessLayer;

import com.development.blackbox.showup.Models.PhotoSendModel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WebAPIServiceProvider {

    private final static String ServerURL = "blackbox.am";
    //private final static String ServerURL = "192.168.0.104/AvianetService";

    private final String postRegisterURL = "http://" + ServerURL + "/api/MediaChatTesnem/PostRegister?";
    private final String getChangeProfileURL = "http://" + ServerURL + "/api/MediaChatTesnem/GetChangeProfile?";
    private final String getNickNameURL = "http://" + ServerURL + "/api/MediaChatTesnem/GetNickName?";
    private final String getLoginURL = "http://" + ServerURL + "/api/MediaChatTesnem/GetLogin?";
    private final String postLoginExpressURL = "http://" + ServerURL + "/api/MediaChatTesnem/PostLoginExpress?";

    private final String postSendPhotoURL = "http://" + ServerURL + "/api/MediaChatTesnem/PostSendPhoto?";
    private final String getLoadPhotoURL = "http://" + ServerURL + "/api/MediaChatTesnem/GetLoadPhoto?";
    private final String getDeletePhotoURL = "http://" + ServerURL + "/api/MediaChatTesnem/GetDeletePhoto?";
    private final String getTryRequestURL = "http://" + ServerURL + "/api/MediaChatTesnem/GetTryRequest?";
    private final String getActiveUsersURL = "http://" + ServerURL + "/api/MediaChatTesnem/GetActiveUsers?";
    private final String getCheckMessagesURL = "http://" + ServerURL + "/api/MediaChatTesnem/GetCheckMessages?";
    private final String getIsAliveURL = "http://" + ServerURL + "/api/MediaChatTesnem/GetIsAlive?";
    private final String postSendTokenURL = "http://" + ServerURL + "/api/MediaChatTesnem/PostSendToken?";

    private final String getGetLogTestURL = "http://" + ServerURL + "/api/MediaChatTesnem/GetLogTest?";
    private final String postLogDataURL = "http://" + ServerURL + "/api/MediaChatTesnem/PostLogData?";

    //public static String imageUrl = "http://192.168.0.104/AvianetService/Images/Tesnem/";
    public static String imageUrl = "http://" + ServerURL + "/Images/Tesnem/";


    public String Register(String userName, String loginName, String pwd, int age, int gender, String imgPath, String android_id) throws Exception {
        return postRegisterRequest(postRegisterURL, userName, loginName, pwd, age, gender, imgPath, android_id);
    }

    public String ChangeProfile(long userID, String userName, int age) throws Exception {
        return getChangeProfileRequest(getChangeProfileURL, userID, userName, age);
    }

    public String Login(String loginName, String pwd) throws Exception {
        return getLoginRequest(getLoginURL, loginName, pwd);
    }

    public String LoginExpress(String userName, String loginName, String pwd, int age, int gender, String imgPath, String android_id) throws Exception {
        return postLoginExpressRequest(postLoginExpressURL, userName, loginName, pwd, age, gender, imgPath, android_id);
    }

    public String GetNickName(int gender, String nickName) throws Exception {
        return getNickNameRequest(getNickNameURL, gender, nickName);
    }

    public String SendPhoto(PhotoSendModel photoSendModel) throws Exception {
        return postSendPhotoRequest(postSendPhotoURL, photoSendModel);
    }

    public String LoadPhoto(long myUserID, long senderUserID) throws Exception {
        return getLoadPhotoRequest(getLoadPhotoURL, myUserID, senderUserID);
    }

    public String DeletePhoto(long myUserID, long senderUserID) throws Exception {
        return getLoadPhotoRequest(getDeletePhotoURL, myUserID, senderUserID);
    }

    public String TryRequest(long myUserID, long requestedUserID) throws Exception {
        return getTryRequest(getTryRequestURL, myUserID, requestedUserID);
    }

    public String GetActiveUsers(long userID) throws Exception {
        return getCommonRequest(getActiveUsersURL, userID);
    }

    public String CheckMessages(long userID) throws Exception {
        return getCommonRequest(getCheckMessagesURL, userID);
    }

    public String IsAlive(long userID) throws Exception {
        return getCommonRequest(getIsAliveURL, userID);
    }

    public String GetLogTest(long userID) throws Exception {
        return getCommonRequest(getGetLogTestURL, userID);
    }

    public String SendToken(long userID, String clientToken, String android_id) throws Exception {
        return postSendTokenRequest(postSendTokenURL, userID, clientToken, android_id);
    }

    public String LogData(String message, String stackTrace, String android_id) throws Exception {
        return postLogDataRequest(postLogDataURL, message, stackTrace, android_id);
    }


    private String postRegisterRequest(String serviceURL, String userName, String loginName, String pwd, int age, int gender, String imgPath, String android_id) throws IOException {

        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 8000);

        HttpClient client = new DefaultHttpClient(params);
        HttpPost post = new HttpPost(serviceURL);

        JSONObject regUser = new JSONObject();
        try {

            regUser.put("UN", userName);
            regUser.put("LN", loginName);
            regUser.put("P", pwd);
            regUser.put("A", age);
            regUser.put("G", gender);
            regUser.put("IP", imgPath);
            regUser.put("DI", android_id);

        } catch (Exception ex) {

            int yyy = 151;
            ex.printStackTrace();
        }

        StringEntity se = new StringEntity(regUser.toString(), "UTF-8");
        post.setEntity(se);

        //post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");

        HttpResponse response = client.execute(post);
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }

    private String getChangeProfileRequest(String serviceURL, long userID, String userName, int age) throws Exception {

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("userID", String.valueOf(userID)));
        urlParameters.add(new BasicNameValuePair("userName", userName));
        urlParameters.add(new BasicNameValuePair("userAge", String.valueOf(age)));
        String paramString = URLEncodedUtils.format(urlParameters, "utf-8");
        serviceURL += paramString;


        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 8000);

        HttpClient httpclient = new DefaultHttpClient(params);
        HttpGet httpget = new HttpGet(serviceURL);
        HttpResponse response;


        InputStream instream = null;

        try {

            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();

            String result = "";

            if (entity != null) {

                // A Simple JSON Response Read
                instream = entity.getContent();
                result = convertStreamToString(instream);
                // now you have the string representation of the HTML request
                instream.close();
            }

            return result;

        } catch (Exception ex) {
            int tt = 1;
            throw new Exception(ex);
        }
        finally {
            if (instream != null) {
                instream.close();
            }
        }
    }

    private String getLoginRequest(String serviceURL, String loginName, String pwd) throws Exception {

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("loginName", loginName));
        urlParameters.add(new BasicNameValuePair("userPwd", pwd));
        String paramString = URLEncodedUtils.format(urlParameters, "utf-8");
        serviceURL += paramString;


        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 8000);

        HttpClient httpclient = new DefaultHttpClient(params);
        HttpGet httpget = new HttpGet(serviceURL);
        HttpResponse response;


        InputStream instream = null;

        try {

            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();

            String result = "";

            if (entity != null) {

                // A Simple JSON Response Read
                instream = entity.getContent();
                result = convertStreamToString(instream);
                // now you have the string representation of the HTML request
                instream.close();
            }

            return result;

        } catch (Exception ex) {
            int tt = 1;
            throw new Exception(ex);
        }
        finally {
            if (instream != null) {
                instream.close();
            }
        }
    }

    private String getNickNameRequest(String serviceURL, int gender, String nickName) throws Exception {

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("gender", String.valueOf(gender)));
        urlParameters.add(new BasicNameValuePair("nickName", nickName));
        String paramString = URLEncodedUtils.format(urlParameters, "utf-8");
        serviceURL += paramString;


        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 8000);

        HttpClient httpclient = new DefaultHttpClient(params);
        HttpGet httpget = new HttpGet(serviceURL);
        HttpResponse response;


        InputStream instream = null;

        try {

            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();

            String result = "";

            if (entity != null) {

                // A Simple JSON Response Read
                instream = entity.getContent();
                result = convertStreamToString(instream);
                // now you have the string representation of the HTML request
                instream.close();
            }

            return result;

        } catch (Exception ex) {
            int tt = 1;
            throw new Exception(ex);
        }
        finally {
            if (instream != null) {
                instream.close();
            }
        }
    }

    private String postLoginExpressRequest(String serviceURL, String userName, String loginName, String pwd, int age, int gender, String imgPath, String android_id) throws IOException {

        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 8000);

        HttpClient client = new DefaultHttpClient(params);
        HttpPost post = new HttpPost(serviceURL);

        JSONObject regUser = new JSONObject();
        try {

            regUser.put("UN", userName);
            regUser.put("LN", loginName);
            regUser.put("P", pwd);
            regUser.put("A", age);
            regUser.put("G", gender);
            regUser.put("IP", imgPath);
            regUser.put("DI", android_id);

        } catch (Exception ex) {

            int yyy = 151;
            ex.printStackTrace();
        }

        StringEntity se = new StringEntity(regUser.toString(), "UTF-8");
        post.setEntity(se);

        //post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");

        HttpResponse response = client.execute(post);
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }

    private String postSendPhotoRequest(String serviceURL, PhotoSendModel photoSendModel) throws Exception {

        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 25000);
        HttpConnectionParams.setSoTimeout(params, 28000);

        HttpClient client = new DefaultHttpClient(params);
        HttpPost post = new HttpPost(serviceURL);

        JSONObject photoJsonObject = new JSONObject();
        try {


            JSONArray imagesArray = new JSONArray();
            imagesArray.put(photoSendModel.ImageBase64);

            photoJsonObject.put("SU", String.valueOf(photoSendModel.SenderUserID));
            photoJsonObject.put("RU", String.valueOf(photoSendModel.ReceiverUserID));
            photoJsonObject.put("P", imagesArray);

        } catch (Exception ex) {
            String seee = ex.getMessage();
            throw new Exception(ex.getMessage());
        }

        StringEntity se = new StringEntity(photoJsonObject.toString(), "UTF-8");
        post.setEntity(se);

        //post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");

        HttpResponse response = client.execute(post);
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }

    private String getLoadPhotoRequest(String serviceURL, long myUserID, long senderUserID) throws Exception {

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("myUserID", String.valueOf(myUserID)));
        urlParameters.add(new BasicNameValuePair("senderUserID", String.valueOf(senderUserID)));
        String paramString = URLEncodedUtils.format(urlParameters, "utf-8");
        serviceURL += paramString;


        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 8000);

        HttpClient httpclient = new DefaultHttpClient(params);
        HttpGet httpget = new HttpGet(serviceURL);
        HttpResponse response;


        InputStream instream = null;

        try {

            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();

            String result = "";

            if (entity != null) {

                // A Simple JSON Response Read
                instream = entity.getContent();
                result = convertStreamToString(instream);
                // now you have the string representation of the HTML request
                instream.close();
            }

            return result;

        } catch (Exception ex) {
            int tt = 1;
            throw new Exception(ex);
        }
        finally {
            if (instream != null) {
                instream.close();
            }
        }
    }

    private String getTryRequest(String serviceURL, long myUserID, long requestedUserID) throws Exception {

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("myUserID", String.valueOf(myUserID)));
        urlParameters.add(new BasicNameValuePair("requestedUserID", String.valueOf(requestedUserID)));
        String paramString = URLEncodedUtils.format(urlParameters, "utf-8");
        serviceURL += paramString;


        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 8000);

        HttpClient httpclient = new DefaultHttpClient(params);
        HttpGet httpget = new HttpGet(serviceURL);
        HttpResponse response;


        InputStream instream = null;

        try {

            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();

            String result = "";

            if (entity != null) {

                // A Simple JSON Response Read
                instream = entity.getContent();
                result = convertStreamToString(instream);
                // now you have the string representation of the HTML request
                instream.close();
            }

            return result;

        } catch (Exception ex) {
            int tt = 1;
            throw new Exception(ex);
        }
        finally {
            if (instream != null) {
                instream.close();
            }
        }
    }

    // TODO: ActiveUsers
    // TODO: CheckMessages
    // TODO: IsAlive
    // TODO: GetLogTest
    private String getCommonRequest(String serviceURL, long userID) throws Exception {

        Date dt = new Date();

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("userID", String.valueOf(userID)));
        urlParameters.add(new BasicNameValuePair("unused", String.valueOf(dt.getTime())));
        String paramString = URLEncodedUtils.format(urlParameters, "utf-8");
        serviceURL += paramString;


        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 4000);
        HttpConnectionParams.setSoTimeout(params, 6000);

        HttpClient httpclient = new DefaultHttpClient(params);
        HttpGet httpget = new HttpGet(serviceURL);
        httpget.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        HttpResponse response;


        InputStream instream = null;

        try {

            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();

            String result = "";

            if (entity != null) {

                // A Simple JSON Response Read
                instream = entity.getContent();
                result = convertStreamToString(instream);
                // now you have the string representation of the HTML request
                instream.close();
            }

            return result;

        } catch (Exception ex) {
            int tt = 1;
            throw new Exception(ex);
        }
        finally {
            if (instream != null) {
                instream.close();
            }
        }
    }

    private String postSendTokenRequest(String serviceURL, long userID, String clientToken, String android_id) throws IOException {

        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 8000);

        HttpClient client = new DefaultHttpClient(params);
        HttpPost post = new HttpPost(serviceURL);

        JSONObject photoJsonObject = new JSONObject();
        try {

            photoJsonObject.put("U", String.valueOf(userID));
            photoJsonObject.put("T", clientToken);
            photoJsonObject.put("DI", android_id);

        } catch (Exception ex) {
            String seee = ex.getMessage();
        }

        StringEntity se = new StringEntity(photoJsonObject.toString(), "UTF-8");
        post.setEntity(se);

        //post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");

        HttpResponse response = client.execute(post);
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }

    private String postLogDataRequest(String serviceURL, String message, String stackTrace, String android_id) throws Exception {

        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 25000);
        HttpConnectionParams.setSoTimeout(params, 28000);

        HttpClient client = new DefaultHttpClient(params);
        HttpPost post = new HttpPost(serviceURL);

        JSONObject photoJsonObject = new JSONObject();
        try {

            photoJsonObject.put("M", message);
            photoJsonObject.put("ST", stackTrace);
            photoJsonObject.put("DI", android_id);

        } catch (Exception ex) {
            String seee = ex.getMessage();
        }

        StringEntity se = new StringEntity(photoJsonObject.toString(), "UTF-8");
        post.setEntity(se);

        //post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");

        HttpResponse response = client.execute(post);
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }


    private static String convertStreamToString(InputStream is)  throws IOException, UnsupportedEncodingException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            int i = 0;
            while ((line = reader.readLine()) != null) {
                if(i == 0) {
                    sb.append(line);
                }
                else {
                    sb.append("\n" + line);
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
