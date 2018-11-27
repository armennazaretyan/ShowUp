package com.development.blackbox.showup.Helpers.AsyncTasks;

import android.os.AsyncTask;

import com.development.blackbox.showup.BusinessLayer.WebAPIService;
import com.development.blackbox.showup.Helpers.Enums.AsyncCallType;
import com.development.blackbox.showup.Helpers.Interfaces.ICallbackable;
import com.development.blackbox.showup.Models.PhotoSendModel;
import com.development.blackbox.showup.Models.UserUIModel;

import java.io.File;

public class WebAPIAsyncTaskService extends AsyncTask<Object, Void, Object> {

    private ICallbackable _context;

    public WebAPIAsyncTaskService(ICallbackable context) {
        _context = context;
    }

    @Override
    protected Object doInBackground(Object... objects) {

        try {

            Object retVal = null;

            String asyncCallTypeValue = objects[0].toString();

            if (asyncCallTypeValue == AsyncCallType.LOG_IN.getCode()) {

                WebAPIService service = new WebAPIService();

                String loginName = objects[1].toString();
                String pwd = objects[2].toString();
                retVal = service.Login(loginName, pwd);

            } else if (asyncCallTypeValue == AsyncCallType.GET_NICK_NAME.getCode()) {

                WebAPIService service = new WebAPIService();

                int gender = (int)objects[1];
                String nickName = String.valueOf(objects[2]);
                retVal = service.GetNickName(gender, nickName);

            } else if (asyncCallTypeValue == AsyncCallType.REGISTER.getCode()) {

                WebAPIService service = new WebAPIService();

                UserUIModel userModel = (UserUIModel) objects[1];
                String android_id = objects[2].toString();
                retVal = service.Register(userModel.UserName, userModel.LoginName, userModel.Password, userModel.Age, userModel.GenderType.getCode(), userModel.ImageURL, android_id);

            } else if (asyncCallTypeValue == AsyncCallType.LOG_IN_EXPRESS.getCode()) {

                WebAPIService service = new WebAPIService();

                UserUIModel userModel = (UserUIModel) objects[1];
                String android_id = objects[2].toString();
                retVal = service.LoginExpress(userModel.UserName, userModel.LoginName, userModel.Password, userModel.Age, userModel.GenderType.getCode(), userModel.ImageURL, android_id);

            } else if (asyncCallTypeValue == AsyncCallType.CHANGE_PROFILE.getCode()) {

                WebAPIService service = new WebAPIService();

                UserUIModel userUIModel = (UserUIModel) objects[1];
                retVal = service.ChangeProfile(userUIModel.ID, userUIModel.UserName);

            } else if (asyncCallTypeValue == AsyncCallType.TRY_REQUEST.getCode()) {

                WebAPIService service = new WebAPIService();

                long myUserID = Integer.parseInt(objects[1].toString());
                long requestedUserID = Integer.parseInt(objects[2].toString());
                retVal = service.TryRequest(myUserID, requestedUserID);

            } else if (asyncCallTypeValue == AsyncCallType.ACTIVE_USERS.getCode()) {

                WebAPIService service = new WebAPIService();

                long myUserID = Integer.parseInt(objects[1].toString());
                retVal = service.GetActiveUsers(myUserID);

            } else if (asyncCallTypeValue == AsyncCallType.SEND_PHOTO.getCode()) {

                WebAPIService service = new WebAPIService();

                PhotoSendModel photoSendModel = (PhotoSendModel) objects[1];
                retVal = service.SendPhoto(photoSendModel);

                // TODO: Delete all temp files
                String tmpFilePath = objects[2].toString();
                int index = tmpFilePath.lastIndexOf('/');
                String folderPath = tmpFilePath.substring(0, index);

                File dir = new File(folderPath);
                for (File file : dir.listFiles()) {
                    try {
                        if (!file.isDirectory())
                            file.delete();
                    } catch (Exception r) {

                    }
                }

            } else if (asyncCallTypeValue == AsyncCallType.LOAD_PHOTO.getCode()) {

                WebAPIService service = new WebAPIService();

                long myUserID = Integer.parseInt(objects[1].toString());
                long senderUserID = Integer.parseInt(objects[2].toString());
                retVal = service.LoadPhoto(myUserID, senderUserID);

            } else if (asyncCallTypeValue == AsyncCallType.DELETE_PHOTO.getCode()) {

                WebAPIService service = new WebAPIService();

                long myUserID = Integer.parseInt(objects[1].toString());
                long senderUserID = Integer.parseInt(objects[2].toString());
                retVal = service.DeletePhoto(myUserID, senderUserID);

            } else if (asyncCallTypeValue == AsyncCallType.CHECK_MESSAGES.getCode()) {

                WebAPIService service = new WebAPIService();

                long myUserID = Integer.parseInt(objects[1].toString());
                retVal = service.CheckMessages(myUserID);

            } else if (asyncCallTypeValue == AsyncCallType.IS_ALIVE.getCode()) {

                WebAPIService service = new WebAPIService();

                long myUserID = Integer.parseInt(objects[1].toString());
                retVal = service.IsAlive(myUserID);

            } else if (asyncCallTypeValue == AsyncCallType.GET_LOG_TEST.getCode()) {

                WebAPIService service = new WebAPIService();

                long myUserID = Integer.parseInt(objects[1].toString());
                service.GetLogTest(myUserID);

                retVal = "";

            } else if (asyncCallTypeValue == AsyncCallType.SEND_TOKEN.getCode()) {

                WebAPIService service = new WebAPIService();

                long myUserID = Integer.parseInt(objects[1].toString());
                String android_id = objects[2].toString();
                String token = objects[3].toString();
                retVal = service.SendToken(myUserID, token, android_id);

            }

            return retVal;

        } catch (Exception ex) {

            String s = ex.getMessage();
            return new Exception(s);
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        try {
            if (_context != null) {
                _context.CallbackToContext(result);
            }
        } catch (Exception ex) {

            String str = ex.getMessage();
            int t = 1;
        }
    }
}
