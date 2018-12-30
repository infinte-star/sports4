package com.sports.sportclub.UI.UI.fragment;


import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sports.sportclub.Data.Coach;
import com.sports.sportclub.Data.SimpleDatabase;
import com.sports.sportclub.R;
import com.sports.sportclub.provider.SimpleContentProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CoachsFragment extends Fragment {

    public static final int LOADER_COACHES = 1;

    private ListView listView;
    private View view;
    private SimpleAdapter adapter;

    //create cursor for contentprovider
    private Cursor mCursor;

    //data source for adapter
    private List<Map<String,Object>> list;

    public CoachsFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //获取当前View
        View view = inflater.inflate(R.layout.fragment_coachs,container,false);

        //get internet status
        boolean hasInternet = isNetworkConnected(getContext());

        //再通过View获取ListView
        listView = view.findViewById(R.id.coach_list);
        //设置SimpleAdapter
        list = DataList(hasInternet);
        adapter = new SimpleAdapter(getActivity(),list,R.layout.coach_item,
                new String[]{"coach_photo","coach_name","coach_introduction"},
                new int[]{R.id.coach_photo_image,R.id.coach_name_text,R.id.coach_introduction_text}){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = null;

                convertView = super.getView(position, convertView, parent);

                ImageButton goodBtn = convertView.findViewById(R.id.GoodButton);

                TextView textView = convertView.findViewById(R.id.coach_name_text);
                String coach_name = textView.getText().toString();
                String show_msg = "你收藏了" + coach_name;
                goodBtn.setOnClickListener(v -> {
                    Toast.makeText(getContext(),show_msg,Toast.LENGTH_LONG).show();
                });

                ImageButton collectBtn = convertView.findViewById(R.id.collectButton);
                String show_msg2 = "你点赞了" + coach_name;

                collectBtn.setOnClickListener(v ->{
                    Toast.makeText(getContext(),show_msg2,Toast.LENGTH_LONG).show();
                });

                return convertView;
            }


        };
        //set adapter
        listView.setAdapter(adapter);

        //create loader callback
        LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                switch (id){
                    case LOADER_COACHES:
                        return new CursorLoader(getActivity().getApplicationContext(),
                                SimpleContentProvider.URI_COACH,
                                new String[]{Coach.COLUMN_NAME},
                                null, null, null);
                        default:
                            throw new IllegalArgumentException();
                }
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                switch (loader.getId()){
                    case LOADER_COACHES:
                        mCursor = data;
                        setData();
                        System.out.print("set data");
                        break;
                }
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {
                return ;
            }
        };

        //begin loader
        getActivity().getSupportLoaderManager().initLoader(LOADER_COACHES, null, mLoaderCallback);

        return view;
    }

    //填充数据列表
    public List<Map<String,Object>> DataList(boolean hasInternet){

        List<String> coachNames = new ArrayList<>();
        if(hasInternet){
            coachNames.add("Sun Yang");
            coachNames.add("Sun Shou");
        } else{
            coachNames.add("Sun Yang");
            coachNames.add("Sun Shou");
        }

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("coach_photo",R.drawable.coach_6);
        map.put("coach_name",coachNames.get(0));
        map.put("coach_introduction","champion coach");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("coach_photo",R.drawable.coach_5);
        map.put("coach_name",coachNames.get(1));
        map.put("coach_introduction","fat coach");
        list.add(map);
        return list;
    }

    @Override
    public void onStart() {
        super.onStart();

        ListView listView = getActivity().findViewById(R.id.coach_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getContext(), "1", Toast.LENGTH_SHORT).show();

            }
        });
    }

    /**
     * 判断网络状态
     * @param context
     * @return boolean
     */
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public void setData(){
        String name;
        if(mCursor != null)
            name = mCursor.getString(mCursor.getColumnIndexOrThrow(Coach.COLUMN_NAME));
        return ;
    }


}
