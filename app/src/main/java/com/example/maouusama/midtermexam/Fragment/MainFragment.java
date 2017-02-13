package com.example.maouusama.midtermexam.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.maouusama.midtermexam.Album.AlbumAdapter;
import com.example.maouusama.midtermexam.Model.Album;
import com.example.maouusama.midtermexam.Query.Query;
import com.example.maouusama.midtermexam.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment {
    private AlbumAdapter mAdapter;
    private RecyclerView recyclerView;
    private RelativeLayout relativeLayout;
    private SwipeRefreshLayout refreshLayout;
    private AlbumAsync async;
    private String ALBUM_API1;
    private String ALBUM_API2;
    private String userInput = null;
    private String ALBUM_API;

    private EditText txtSearch;

    private String TAG;
    private String KEY_LAYOUT_MANAGER;
    private static final int SPAN_COUNT = 2;

    protected RecyclerView.LayoutManager mLayoutManager;
    protected LayoutManagerType mCurrentLayoutManagerType;

    private enum LayoutManagerType {
        LINEAR_LAYOUT_MANAGER
    }


    public MainFragment() {
    }


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        TAG = getString(R.string.TAG);
        view.setTag(TAG);

        //strings
        ALBUM_API1 = getString(R.string.album_api1);
        ALBUM_API2 = getString(R.string.album_api2);

        KEY_LAYOUT_MANAGER = getString(R.string.KEY_LAYOUT_MANAGER);

        setHasOptionsMenu(true);

        //so layout wont push up
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);




        async = new AlbumAsync();

        txtSearch = (EditText) view.findViewById(R.id.etSearch);

        txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    //hide keyboard
                    txtSearch.clearFocus();
                    InputMethodManager in = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(txtSearch.getWindowToken(), 0);

                    //async
                    async.cancel(true);
                    async = new AlbumAsync();
                    userInput = replaceSpaces(txtSearch.getText().toString());
                    ALBUM_API = ALBUM_API1.concat(userInput).concat(ALBUM_API2);
                    async.execute();


                }
                return false;
            }

        });


        recyclerView = (RecyclerView) view.findViewById(R.id.rvCard);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState.getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        mAdapter = new AlbumAdapter(new ArrayList<Album>());

        recyclerView.setAdapter(mAdapter);


        relativeLayout = (RelativeLayout) view.findViewById(R.id.rel_layout);

        /*refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_main_swipe_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                async.cancel(true);
                async = new AlbumAsync();
                async.execute(ALBUM_API);
                refreshLayout.setRefreshing(false);
            }
        });*/





        return view;
    }

    private class AlbumAsync extends AsyncTask<String, String, List<Album>> {
        private ProgressDialog progressDialog;

        @Override
        protected List<Album> doInBackground(String... params) {

            List<Album> items = null;
            items = Query.fetchAlbumData(ALBUM_API);
            return items;
        }

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading albums...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(List<Album> albumList) {
            mAdapter.clear();
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (albumList != null && !albumList.isEmpty()) {
                mAdapter.addAll(albumList);
                //change layout image/color
                ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#FAFAFA"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    relativeLayout.setBackground(colorDrawable);
                }
            } else {
                relativeLayout.setBackgroundResource(R.mipmap.no_album_error);
            }

            super.onPostExecute(albumList);
        }
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
        }

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }


    public String replaceSpaces(String string) {
        String input = string;
        input = input.replace(" ", "-");
        return input;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_clear) {
            mAdapter.clear();
            txtSearch.setText("");
            ALBUM_API = ALBUM_API1.concat("").concat(ALBUM_API2); //make sure not to display previous search on refresh
            async.cancel(true);
            relativeLayout.setBackgroundResource(R.mipmap.no_album_error);
        }
        return super.onOptionsItemSelected(item);
    }


}
