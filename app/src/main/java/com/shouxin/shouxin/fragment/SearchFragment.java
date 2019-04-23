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

import com.shouxin.shouxin.DataModel.ItemEntry;
import com.shouxin.shouxin.R;
import com.shouxin.shouxin.database.DaoImpl.ItemEntryDaoImpl;

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
    ItemEntry itemEntry = new ItemEntry();

    private static Fragment searchFragment = new SearchFragment();

    final HandlerThread mhandlerThread = new HandlerThread("downloadThread");
    final Handler mainHandler = new Handler(){

        @Override
        public void handleMessage(Message msg){
            if(msg.what == 1)
                imageView.setBackground(drawable);
            wordDescription.setText(itemEntry.getDescription());
            wordTitle.setText(itemEntry.getName());
            StoreData(itemEntry);
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
        searchView.setOnQueryTextListener(listener);

        return view;
    }

    public void getNetworkData(String WordName) throws IOException {

        final String BASE_URL = "https://shouyu.51240.com/";
        final String POSTFIX = "__shouyus/";
        String url = BASE_URL + WordName + POSTFIX;
        itemEntry.setPictureUrl(url);

        Document doc = Jsoup.connect(url).timeout(5000).get();
        Elements pngs = doc.select("img[src$=.png]");
        itemEntry.setName(WordName);
        drawable = png2Drawable(pngs.get(0).attr("src").substring(2));

        Elements titles = doc.select("td[bgcolor=#FFFFFF]");
        itemEntry.setDescription(titles.get(0).text());

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

    public void StoreData(ItemEntry itemEntry){
        ItemEntryDaoImpl iedi = new ItemEntryDaoImpl(getContext());
        iedi.add(itemEntry);
    }

    private SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener(){
        @Override
        public boolean onQueryTextSubmit(String s) {
            final String word = s;
            mhandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        getNetworkData(word);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(itemEntry.getDescription().equals("（暂无该词手语）"))
                        mainHandler.sendEmptyMessage(0);
                    else
                        mainHandler.sendEmptyMessage(1);

                }
            });
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };
}
