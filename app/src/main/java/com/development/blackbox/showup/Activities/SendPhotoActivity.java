package com.development.blackbox.showup.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.development.blackbox.showup.Helpers.AsyncTasks.WebAPIAsyncTaskService;
import com.development.blackbox.showup.Helpers.Enums.AsyncCallType;
import com.development.blackbox.showup.Helpers.Enums.GenderEnumType;
import com.development.blackbox.showup.Helpers.Interfaces.ICallbackable;
import com.development.blackbox.showup.Models.PhotoSendModel;
import com.development.blackbox.showup.Models.UserUIModel;
import com.development.blackbox.showup.PresentationLayerBase;
import com.development.blackbox.showup.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SendPhotoActivity extends PresentationLayerBase implements ICallbackable {

    private UserUIModel _MeUserInfo = null;
    private UserUIModel _ActiveUserModel = null;

    ImageView imgEmpty;
    ImageView imgToSend;
    Button btnSend;
    //String sCurrentPhotoBase64;
    PhotoSendModel _PhotoSendModel = null;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_CAMERA = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_photo);

        Intent intent = getIntent();
        _MeUserInfo = (UserUIModel) intent.getSerializableExtra("1");
        _ActiveUserModel = (UserUIModel) intent.getSerializableExtra("2");

        _PhotoSendModel = new PhotoSendModel();
        _PhotoSendModel.SenderUserID = _MeUserInfo.ID;
        _PhotoSendModel.ReceiverUserID = _ActiveUserModel.ID;

        TextView txtSendToName = (TextView) findViewById(R.id.txtSendToName);
        txtSendToName.setText(_ActiveUserModel.UserName);


        //Display display = getWindowManager().getDefaultDisplay();
        //int width = ((display.getWidth() * 55) / 100);
        //int height = ((display.getHeight() * 55) / 100);

        imgEmpty = (ImageView) findViewById(R.id.imgEmpty);
        imgToSend = (ImageView) findViewById(R.id.imgToSend);
        //android.view.ViewGroup.LayoutParams layoutParams = imgToSend.getLayoutParams();
        //layoutParams.width = width;
        //layoutParams.height = height;
        //imgToSend.setLayoutParams(layoutParams);
        if(_MeUserInfo.GenderType == GenderEnumType.MALE) {
            imgEmpty.setImageResource(R.drawable.ic_empty_male);
        } else {
            imgEmpty.setImageResource(R.drawable.ic_empty_female);
        }

        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setEnabled(false);
    }

    @Override
    public void CallbackToContext(Object result) {

        try {
            if(_ProgressDialog != null) {
                _ProgressDialog.dismiss();
            }
        } catch (Exception ex) {

        }

        if(result instanceof Exception) {

            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("ERROR: " + ((Exception)result).getMessage())
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();

            Intent intent = new Intent();
            //intent.putExtra("adition", "1");
            setResult(RESULT_OK, intent);
            finish();

        } else {

            Intent intent = new Intent();
            //intent.putExtra("adition", "1");
            setResult(RESULT_OK, intent);
            finish();

        }

    }


    public void onCancel(View view) {
        Intent intent = new Intent();
        //intent.putExtra("adition", "1");
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onMakePhoto(View view) {
        dispatchTakePictureIntent();
    }

    public void onSend(View view) {

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {

            _ProgressDialog = ProgressDialog.show(this, "Request", "Please wait.");

            correctImageBeforeSend();

            /*Object testobjParams[] = new Object[2];
            testobjParams[0] = AsyncCallType.GET_LOG_TEST.getCode();
            testobjParams[1] = _PhotoSendModel.SenderUserID;
            new WebAPIAsyncTaskService(this).execute(testobjParams);*/

            Object objParams[] = new Object[3];
            objParams[0] = AsyncCallType.SEND_PHOTO.getCode();
            objParams[1] = _PhotoSendModel;
            objParams[2] = mCurrentPhotoPath;
            new WebAPIAsyncTaskService(this).execute(objParams);

        } else {

            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("Check internet connection.")
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();

            return;
        }

    }

    // TODO: Make photo
    public void dispatchTakePictureIntent() {

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            // Callback onRequestPermissionsResult interceptado na Activity MainActivity
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        } else {
            // permission has been granted, continue as usual

            startToMakePhoto();
        }

    }

    private void startToMakePhoto() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (Exception ex) {
            // Error occurred while creating the File
            //...
            String dd = "";
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {

            try {

                //Android/data/com.development.blackbox.showup/files/Pictures

                Uri photoURI;

                if(Build.VERSION.SDK_INT >= 24) {

                    photoURI = FileProvider.getUriForFile(this, "com.development.blackbox.showup", photoFile);
                    /*try {
                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                        m.invoke(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                } else {
                    photoURI = Uri.fromFile(photoFile);
                }

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

                    /*String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/picture.jpg";
                    File imageFile = new File(imageFilePath);
                    Uri imageFileUri = Uri.fromFile(imageFile); // convert path to Uri

                    Intent it = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    it.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
                    startActivityForResult(it, REQUEST_TAKE_PHOTO);*/

            } catch (Exception ex) {

                String ss = ex.getMessage();
            }


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    startToMakePhoto();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            setPic();

            btnSend.setEnabled(true);
            btnSend.setTextColor(Color.BLACK);
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        //arrCurrentPhotoPath[imgNumber - 1] = mCurrentPhotoPath;
        return image;
    }

    private void setPic() {

        try {

            // Get the dimensions of the View
            int targetW = 5000; //mImageView.getWidth();
            int targetH = 5000; //mImageView.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            //Bitmap source = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            //Bitmap bitmap = rotatePhoto(source);
            File imgFile = new File(mCurrentPhotoPath);
            Uri photoURI = Uri.fromFile(imgFile);
            Bitmap bitmapTemp = handleSamplingAndRotationBitmap(this, photoURI);
            Bitmap bitmap = rotatePhoto(bitmapTemp);
            /*if(source.getWidth() > source.getHeight()) {

                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
            } else {
                bitmap = source;
            }*/

            Display display = getWindowManager().getDefaultDisplay();
            int width = ((display.getWidth() * 80) / 100);
            int height = ((display.getHeight() * 80) / 100);
            ViewGroup.LayoutParams layoutParams = imgToSend.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;

            imgEmpty.setVisibility(View.INVISIBLE);
            imgToSend.setVisibility(View.VISIBLE);

            imgToSend.setLayoutParams(layoutParams);
            imgToSend.setImageResource(R.drawable.empty_image);
            imgToSend.setImageBitmap(bitmap);


            /*//mBitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            byte[] imgByte = outputStream.toByteArray();
            String base64Str = Base64.encodeToString(imgByte, Base64.DEFAULT);

            _PhotoSendModel.ImageBase64 = base64Str;*/

        } catch (Exception ex) {
            String ss = ex.getMessage();
        }

    }

    private void correctImageBeforeSend() {

        try {

            // Get the dimensions of the View
            int targetW = 5000; //mImageView.getWidth();
            int targetH = 5000; //mImageView.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;


            int compressPercent = 90;
            int maxSize = Math.max(photoW, photoH);
            if (maxSize > 1200) {
                double delta = (double)maxSize / 1200;
                compressPercent = (int) (compressPercent / delta);
            }

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            //Bitmap source = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            //Bitmap bitmap = rotatePhoto(source);
            File imgFile = new File(mCurrentPhotoPath);
            Uri photoURI = Uri.fromFile(imgFile);
            Bitmap bitmapTemp = handleSamplingAndRotationBitmap(this, photoURI);
            Bitmap bitmap = rotatePhoto(bitmapTemp);

            //mBitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressPercent, outputStream);
            byte[] imgByte = outputStream.toByteArray();
            String base64Str = Base64.encodeToString(imgByte, Base64.DEFAULT);

            _PhotoSendModel.ImageBase64 = base64Str;

        } catch (Exception ex) {

            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("ERROR: " + ex.getMessage())
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();

            return;
        }
    }


    private Bitmap rotatePhoto(Bitmap source) {

        Bitmap bitmap;
        if(source.getWidth() > source.getHeight()) {

            Matrix matrix = new Matrix();
            matrix.postRotate(270);
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        } else {
            bitmap = source;
        }

        return bitmap;
    }

    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage) throws IOException {
        int MAX_HEIGHT = 1200; //1024;
        int MAX_WIDTH = 1200; //1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        img = rotateImageIfRequired(img, selectedImage);
        return img;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    /*private int getScreenOrientation() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int orientation;
        // if the device's natural orientation is portrait:
        if ((rotation == Surface.ROTATION_0
                || rotation == Surface.ROTATION_180) && height > width ||
                (rotation == Surface.ROTATION_90
                        || rotation == Surface.ROTATION_270) && width > height) {
            switch(rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                default:
                    Log.e(TAG, "Unknown screen orientation. Defaulting to " +
                            "portrait.");
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
            }
        }
        // if the device's natural orientation is landscape or if the device
        // is square:
        else {
            switch(rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                default:
                    Log.e(TAG, "Unknown screen orientation. Defaulting to " +
                            "landscape.");
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
            }
        }

        return orientation;
    }*/

}
