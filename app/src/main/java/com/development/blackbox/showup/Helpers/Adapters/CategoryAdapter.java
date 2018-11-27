package com.development.blackbox.showup.Helpers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.development.blackbox.showup.Models.CategoryModel;
import com.development.blackbox.showup.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CategoryAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<HashMap<String, Object>> data;
    private static LayoutInflater inflater = null;

    public CategoryAdapter(Context c, ArrayList<HashMap<String, Object>> d) {
        mContext = c;
        data = d;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        //return mThumbIds.length;
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    private class CustomizedListView {
        static final String KEY_TITLE = "title";
        static final String KEY_OBJECT = "objectmodel";
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.grid_item_category, null);

        RelativeLayout rlTest = (RelativeLayout) vi.findViewById(R.id.rlTest);
        ImageView imageView = (ImageView) vi.findViewById(R.id.imgCategory);
        TextView title = (TextView) vi.findViewById(R.id.txtTitle);

        if (convertView == null) {
            rlTest.setLayoutParams(new GridView.LayoutParams(460, 460));
        }

        HashMap<String, Object> categoryInfo = new HashMap<String, Object>();
        categoryInfo = data.get(position);

        CategoryModel cm = (CategoryModel) categoryInfo.get(CategoryAdapter.CustomizedListView.KEY_OBJECT);

        rlTest.setTag(categoryInfo.get(CategoryAdapter.CustomizedListView.KEY_OBJECT));
        imageView.setImageResource(cm.ImageResource);
        title.setText(cm.Name);

        /*ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            //imageView.setBackgroundColor(Color.BLUE);
            imageView.setLayoutParams(new GridView.LayoutParams(410, 410));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);

        return imageView;*/

        return vi;
    }

    // references to our images
    /*private Integer[] mThumbIds = {
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7
    };*/
}
