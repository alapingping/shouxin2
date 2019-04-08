package com.shouxin.shouxin.fragment;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.shouxin.shouxin.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    ImageView imageView;
    TextView wordTitle;
    TextView wordDescription;
    SearchView searchView;

    Drawable drawable;
    String wordDescriptionCont;
    String wordTitleCont;

    private static Fragment searchFragment = new SearchFragment();

    final HandlerThread mhandlerThread = new HandlerThread("downloadThread");
    final Handler mainHandler = new Handler(){

        @Override
        public void handleMessage(Message msg){
            imageView.setBackground(drawable);
            wordDescription.setText(wordDescriptionCont);
            wordTitle.setText(wordTitleCont);
        }

    };
    Handler mhandler;


    public SearchFragment() {
        // Required empty public constructor
    }

    public static Fragment getInstance(){
        if(searchFragment == null){
            searchFragment = new SearchFragment();;
        }
        return searchFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        mhandlerThread.start();
        mhandler = new Handler(mhandlerThread.getLooper());


        imageView = view.findViewById(R.id.wordImage);
        wordTitle = view.findViewById(R.id.wordTitle);
        wordDescription = view.findViewById(R.id.wordDescription);
        searchView = view.findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                final String word = s;
                mhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            wordDescriptionCont = getNetworkData(word);
                            mainHandler.sendEmptyMessage(0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return view;
    }

    public String getNetworkData(String WordName) throws IOException {

        final String BASE_URL = "https://shouyu.51240.com/";
        final String POSTFIX = "__shouyus/";
        String url = BASE_URL + WordName + POSTFIX;

        Document doc = Jsoup.connect(url).timeout(5000).get();
        Elements pngs = doc.select("img[src$=.png]");
        wordTitleCont = WordName;
        drawable = png2Drawable(pngs.get(0).attr("src").substring(2));

        Elements titles = doc.select("td[bgcolor=#FFFFFF]");
        return titles.get(0).text();

    }

    public Drawable png2Drawable(String url){
        Drawable localDrawable = null;
        url = "http://" + url;
        try {
            localDrawable = (Drawable) Drawable.createFromStream((new URL(url)).openStream(), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return localDrawable;
    }


}
