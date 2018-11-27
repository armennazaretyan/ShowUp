package com.development.blackbox.showup.Helpers.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.development.blackbox.showup.Helpers.Enums.GenderEnumType;
import com.development.blackbox.showup.Models.UserUIModel;
import com.development.blackbox.showup.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ActiveUsersListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, Object>> data;
    private static LayoutInflater inflater = null;
    private boolean _IsScreenDisabled = false;


    public ActiveUsersListAdapter(Activity a, ArrayList<HashMap<String, Object>> d, boolean isScreenDisabled) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _IsScreenDisabled = isScreenDisabled;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class CustomizedListView {
        static final String KEY_ID = "id";
        static final String KEY_OBJECT = "objectmodel";
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_row_active_user, null);


        ImageView userImage = (ImageView) vi.findViewById(R.id.userImage);
        ImageView userImageOnline = (ImageView) vi.findViewById(R.id.userImageOnline);
        TextView nick = (TextView) vi.findViewById(R.id.userName);
        TextView age = (TextView) vi.findViewById(R.id.userAge);
        TextView userGender = (TextView) vi.findViewById(R.id.userGender);

        Button btnPhotoToMe = (Button) vi.findViewById(R.id.btnPhotoToMe);
        Button btnRequestToMe = (Button) vi.findViewById(R.id.btnRequestToMe);
        Button btnTry = (Button) vi.findViewById(R.id.btnTry);

        HashMap<String, Object> activeUser = new HashMap<String, Object>();
        activeUser = data.get(position);
        UserUIModel userUIModel = (UserUIModel)activeUser.get(ActiveUsersListAdapter.CustomizedListView.KEY_OBJECT);

        //new DownloadImageTask((ImageView) vi.findViewById(R.id.userImage)).execute(userUIModel.ImageURL);
        if(userUIModel.ImageURL.isEmpty()) {

            if(userUIModel.GenderType == GenderEnumType.MALE) {
                userImage.setImageResource(R.drawable.ic_empty_male);
            } else {
                userImage.setImageResource(R.drawable.ic_empty_female);
            }
        } else {
            Picasso.with(activity).load(userUIModel.ImageURL).into(userImage);
        }

        nick.setText(userUIModel.UserName);
        if(userUIModel.IsActive) {
            userImageOnline.setVisibility(View.VISIBLE);
        } else {
            userImageOnline.setVisibility(View.INVISIBLE);
        }
        age.setText(String.valueOf(userUIModel.Age));
        userGender.setText(GenderEnumType.toString(userUIModel.GenderType));

        if(userUIModel.IsPhotoToMe) {
            btnPhotoToMe.setEnabled(true);
            //btnPhotoToMe.setTextColor(Color.GREEN);
            btnPhotoToMe.setBackgroundResource(R.drawable.download_enabled);
        } else {
            btnPhotoToMe.setEnabled(false);
            //btnPhotoToMe.setTextColor(Color.BLACK);
            btnPhotoToMe.setBackgroundResource(R.drawable.download_disabled);
        }
        if(userUIModel.IsRequestToMe) {
            btnRequestToMe.setEnabled(true);
            //btnRequestToMe.setTextColor(Color.BLUE);
            btnRequestToMe.setBackgroundResource(R.drawable.make_photo_enabled);
        } else {
            btnRequestToMe.setEnabled(false);
            //btnRequestToMe.setTextColor(Color.BLACK);
            btnRequestToMe.setBackgroundResource(R.drawable.make_photo_disabled);
        }
        if(userUIModel.IsMeRequestedAlready) {
            btnTry.setEnabled(false);
            btnTry.setTextColor(Color.RED);
        } else {
            btnTry.setEnabled(true);
            btnTry.setTextColor(Color.BLACK);
        }

        btnPhotoToMe.setTag(userUIModel);
        btnRequestToMe.setTag(userUIModel);
        btnTry.setTag(userUIModel);

        if(_IsScreenDisabled) {
            btnPhotoToMe.setVisibility(View.GONE);
            btnRequestToMe.setVisibility(View.GONE);
            btnTry.setVisibility(View.GONE);
        }

        return vi;
    }

}
