package com.development.blackbox.showup.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

import com.development.blackbox.showup.Helpers.Adapters.ActiveUsersListAdapter;
import com.development.blackbox.showup.Helpers.AsyncTasks.WebAPIAsyncTaskService;
import com.development.blackbox.showup.Helpers.Config;
import com.development.blackbox.showup.Helpers.Enums.AsyncCallType;
import com.development.blackbox.showup.Helpers.Enums.OrderEnumType;
import com.development.blackbox.showup.Helpers.Interfaces.ICallbackable;
import com.development.blackbox.showup.Models.ActiveUsersResponse;
import com.development.blackbox.showup.Models.UserUIModel;
import com.development.blackbox.showup.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ActiveUsersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActiveUsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActiveUsersFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_USER_ID = "meUser";
    private static final String ARG_PARAM2 = "param2";

    protected ProgressDialog _ProgressDialog;

    // TODO: Rename and change types of parameters
    private UserUIModel mMeUser;
    private ActiveUsersResponse _ActiveUsersResponse;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private boolean _IsScreenDisabled = false;

    public ActiveUsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param meUser Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActiveUsersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActiveUsersFragment newInstance(UserUIModel meUser, String param2) {
        ActiveUsersFragment fragment = new ActiveUsersFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_USER_ID, meUser);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMeUser = (UserUIModel) getArguments().getSerializable(ARG_PARAM_USER_ID);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_active_users, container, false);
        final View view = v;

        mMeUser = (UserUIModel) getArguments().getSerializable(ARG_PARAM_USER_ID);

        SwipeRefreshLayout swipeRefresh = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActiveUsers(view, mMeUser.ID);
            }
        });


        if(_IsScreenDisabled) {
            LinearLayout llSortingLayout = (LinearLayout) v.findViewById(R.id.llSortingLayout);
            llSortingLayout.setVisibility(View.GONE);

            //ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams();
            //swipeRefresh.setLayoutParams();
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) swipeRefresh.getLayoutParams();
            marginLayoutParams.setMargins(0, 0, 0, 0);
            swipeRefresh.setLayoutParams(marginLayoutParams);
        }

        //fillDropDown(v);
        getActiveUsers(v, mMeUser.ID);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /*protected final String SORTING_SETTINGS = "sortingInfo";
    protected final String SORT_BY_KEY = "SortBy";*/
    /*private void fillDropDown(View v) {

        ArrayList<DropDownAdapterModel> ddAdapterModelList = new ArrayList<DropDownAdapterModel>();
        ddAdapterModelList.add(new DropDownAdapterModel(1, "Name", null));
        ddAdapterModelList.add(new DropDownAdapterModel(2, "See photo", null));
        ddAdapterModelList.add(new DropDownAdapterModel(3, "Make photo", null));

        DropDownAdapter ddAdapter = new DropDownAdapter(getActivity(), ddAdapterModelList);
        ddAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner ddSorting = (Spinner) v.findViewById(R.id.ddSorting);
        ddSorting.setAdapter(ddAdapter);
        ddSorting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                DropDownAdapterModel ddaModel = (DropDownAdapterModel) parentView.getSelectedItem();

                SharedPreferences languageprefT = getActivity().getSharedPreferences(SORTING_SETTINGS, MODE_PRIVATE);
                SharedPreferences.Editor editorT = languageprefT.edit();
                editorT.putLong(SORT_BY_KEY, ddaModel.ID);
                editorT.commit();

                if(_ActiveUsersResponse != null) {
                    FillActiveUsers(_ActiveUsersResponse);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

    }*/

    static final String KEY_ID = "id";
    static final String KEY_OBJECT = "objectmodel";

    public void FillActiveUsers(ActiveUsersResponse activeUsersResponse) {

        try {
            if(_ProgressDialog != null) {
                _ProgressDialog.dismiss();
            }
        } catch (Exception ex) {

        }

        //_ActiveUsersResponse = activeUsersResponse;

        //List<UserModel> lUserModel = activeUsersResponse.ActiveUsersList;

        ListView listView = (ListView) getView().findViewById(R.id.lvActiveUsers);

        ArrayList<HashMap<String, Object>> activeUsersList = new ArrayList<HashMap<String, Object>>();

        for (UserUIModel item :
                activeUsersResponse.ActiveUsersList) {

            UserUIModel userUIModel = new UserUIModel();
            userUIModel.ID = item.ID;
            userUIModel.UserName = item.UserName;
            userUIModel.Password = item.Password;
            userUIModel.ImageURL = item.ImageURL;
            userUIModel.GenderType = item.GenderType;
            userUIModel.Age = item.Age;
            userUIModel.IsMeRequestedAlready = IsUserInList(activeUsersResponse, item.ID, 1);
            userUIModel.IsPhotoToMe = IsUserInList(activeUsersResponse, item.ID, 2);
            userUIModel.IsRequestToMe = IsUserInList(activeUsersResponse, item.ID, 3);
            userUIModel.IsActive = item.IsActive;


            HashMap<String, Object> activeUsers = new HashMap<String, Object>();

            activeUsers.put(KEY_ID, String.valueOf(userUIModel.ID));
            activeUsers.put(KEY_OBJECT, userUIModel);
            activeUsersList.add(activeUsers);
        }

        SharedPreferences userInfoShPref = getActivity().getSharedPreferences(Config.SORTING_SETTINGS, MODE_PRIVATE);
        long orderBy = userInfoShPref.getLong(Config.SORT_BY_KEY, -1);
        OrderEnumType order = OrderEnumType.ParseInt((int)orderBy);
        switch (order) {
            case NAME:

                Collections.sort(activeUsersList, new Comparator<HashMap<String, Object>>() {
                    @Override
                    public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
                        return ((UserUIModel)o1.get(KEY_OBJECT)).UserName.toLowerCase().compareTo(((UserUIModel)o2.get(KEY_OBJECT)).UserName.toLowerCase());
                    }
                });
                break;

            case SEE_PHOTO:

                Collections.sort(activeUsersList, new Comparator<HashMap<String, Object>>() {
                    @Override
                    public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {

                        boolean b1 = ((UserUIModel)o1.get(KEY_OBJECT)).IsPhotoToMe;
                        boolean b2 = ((UserUIModel)o2.get(KEY_OBJECT)).IsPhotoToMe;

                        if (b1 != b2){

                            if (b1 == true){
                                return -1;
                            }

                            if (b1 == false){
                                return 1;
                            }
                        }
                        return 0;
                    }
                });
                break;

            case MAKE_PHOTO:

                Collections.sort(activeUsersList, new Comparator<HashMap<String, Object>>() {
                    @Override
                    public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {

                        boolean b1 = ((UserUIModel)o1.get(KEY_OBJECT)).IsRequestToMe;
                        boolean b2 = ((UserUIModel)o2.get(KEY_OBJECT)).IsRequestToMe;

                        if (b1 != b2){

                            if (b1 == true){
                                return -1;
                            }

                            if (b1 == false){
                                return 1;
                            }
                        }
                        return 0;
                    }
                });
                break;
        }

        ActiveUsersListAdapter adapter = new ActiveUsersListAdapter(getActivity(), activeUsersList, _IsScreenDisabled);
        listView.setAdapter(adapter);
        listView.setItemsCanFocus(true);


        swipeRefreshFinish();
    }

    private boolean IsUserInList(ActiveUsersResponse activeUsersResponse, long activeUserID, int isType) {

        boolean retVal = false;

        if (isType == 1) {

            for (long userID :
                    activeUsersResponse.MyRequestsToUsers) {

                if(activeUserID == userID) {
                    retVal = true;
                    break;
                }
            }

        } else if (isType == 2) {

            for (long userID :
                    activeUsersResponse.PhotoToMe) {

                if(activeUserID == userID) {
                    retVal = true;
                    break;
                }
            }

        } else if (isType == 3) {

            for (long userID :
                    activeUsersResponse.RequestsToMe) {

                if(activeUserID == userID) {
                    retVal = true;
                    break;
                }
            }

        }

        return retVal;
    }

    private void getActiveUsers(View view, long userID) {

        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {

            //_ProgressDialog = ProgressDialog.show(getContext(), "Request", "Please wait.");
            swipeRefreshStart(view);

            Object objParams[] = new Object[2];
            objParams[0] = AsyncCallType.ACTIVE_USERS.getCode();
            objParams[1] = userID;
            new WebAPIAsyncTaskService((ICallbackable)getActivity()).execute(objParams);

        } else {

            new AlertDialog.Builder(getActivity())
                    .setTitle("Alert")
                    .setMessage("Check internet")
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();

            return;
        }
    }

    public void swipeRefreshStart(View view) {
        SwipeRefreshLayout swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        swipeRefresh.setRefreshing(true);
    }

    public void swipeRefreshFinish() {
        SwipeRefreshLayout swipeRefresh = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefresh);
        swipeRefresh.setRefreshing(false);
    }

    public void setDisabledScreen() {
        _IsScreenDisabled = true;
    }

    public void setSortingSettings(OrderEnumType order) {

        SharedPreferences languageprefT = getActivity().getSharedPreferences(Config.SORTING_SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor editorT = languageprefT.edit();
        editorT.putLong(Config.SORT_BY_KEY, order.getCode());
        editorT.commit();

        FillActiveUsers(_ActiveUsersResponse);
    }

    public void clickedToName() {

        RadioButton rbName = (RadioButton) getView().findViewById(R.id.rbName);
        RadioButton rbDownload = (RadioButton) getView().findViewById(R.id.rbDownload);
        ImageView imgDownload = (ImageView) getView().findViewById(R.id.imgDownload);
        RadioButton rbMakePhoto = (RadioButton) getView().findViewById(R.id.rbMakePhoto);
        ImageView imgMakePhoto = (ImageView) getView().findViewById(R.id.imgMakePhoto);

        rbName.setChecked(true);
        rbDownload.setChecked(false);
        imgDownload.setImageResource(R.drawable.download_disabled);
        rbMakePhoto.setChecked(false);
        imgMakePhoto.setImageResource(R.drawable.make_photo_disabled);
    }

    public void clickedToDownload() {

        RadioButton rbName = (RadioButton) getView().findViewById(R.id.rbName);
        RadioButton rbDownload = (RadioButton) getView().findViewById(R.id.rbDownload);
        ImageView imgDownload = (ImageView) getView().findViewById(R.id.imgDownload);
        RadioButton rbMakePhoto = (RadioButton) getView().findViewById(R.id.rbMakePhoto);
        ImageView imgMakePhoto = (ImageView) getView().findViewById(R.id.imgMakePhoto);

        rbName.setChecked(false);
        rbDownload.setChecked(true);
        imgDownload.setImageResource(R.drawable.download_enabled);
        rbMakePhoto.setChecked(false);
        imgMakePhoto.setImageResource(R.drawable.make_photo_disabled);
    }

    public void clickedToMakePhoto() {

        RadioButton rbName = (RadioButton) getView().findViewById(R.id.rbName);
        RadioButton rbDownload = (RadioButton) getView().findViewById(R.id.rbDownload);
        RadioButton rbMakePhoto = (RadioButton) getView().findViewById(R.id.rbMakePhoto);
        ImageView imgDownload = (ImageView) getView().findViewById(R.id.imgDownload);
        ImageView imgMakePhoto = (ImageView) getView().findViewById(R.id.imgMakePhoto);

        rbName.setChecked(false);
        rbDownload.setChecked(false);
        rbMakePhoto.setChecked(true);
        imgDownload.setImageResource(R.drawable.download_disabled);

        imgMakePhoto.setImageResource(R.drawable.make_photo_enabled);
    }

    public void CallbackFromActivity(ActiveUsersResponse activeUsersResponse) {

        _ActiveUsersResponse = activeUsersResponse;

        SharedPreferences userInfoShPref = getActivity().getSharedPreferences(Config.SORTING_SETTINGS, MODE_PRIVATE);
        long orderBy = userInfoShPref.getLong(Config.SORT_BY_KEY, -1);
        if(orderBy == -1) {

            RadioButton rbName = (RadioButton) getView().findViewById(R.id.rbName);
            rbName.setChecked(true);
            setSortingSettings(OrderEnumType.NAME);

        } else {

            OrderEnumType orderEnumType = OrderEnumType.ParseInt((int)orderBy);
            switch (orderEnumType) {
                case NAME:
                    clickedToName();
                    break;
                case SEE_PHOTO:
                    clickedToDownload();
                    break;
                case MAKE_PHOTO:
                    clickedToMakePhoto();
                    break;
            }

            setSortingSettings(orderEnumType);
        }
    }

    // TODO: Sorting
    public void sortByName() {

        clickedToName();
        setSortingSettings(OrderEnumType.NAME);
    }
    public void sortByDownload() {

        clickedToDownload();
        setSortingSettings(OrderEnumType.SEE_PHOTO);
    }
    public void sortByMakePhoto() {

        clickedToMakePhoto();
        setSortingSettings(OrderEnumType.MAKE_PHOTO);
    }
}
