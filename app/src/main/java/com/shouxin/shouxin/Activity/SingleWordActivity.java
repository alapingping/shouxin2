package com.shouxin.shouxin.Activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shouxin.shouxin.DataModel.Word;
import com.shouxin.shouxin.R;
import com.shouxin.shouxin.Utils.Util;
import com.shouxin.shouxin.database.Repository.WordRepository;
import com.shouxin.shouxin.databinding.ActivitySignleWordBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class SingleWordActivity extends AppCompatActivity {

    // 当前word
    private Word word;
    // 当前activity的binding
    private ActivitySignleWordBinding binding;
    // 是否已收藏
    private boolean collected;
    // 收藏状态是否更改
    private boolean collectedChangeFlag;

    static{
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivitySignleWordBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        word = (Word) bundle.getSerializable("word");

        binding.wordTitle.setText(word.getName());
        binding.wordDescription.setText(word.getDescription());
        // 使得toolbar支持监听
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle(R.string.toolbar_menu_back);
        binding.toolbar.inflateMenu(R.menu.menu_toolbar);
        binding.toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);
        binding.toolbar.setNavigationOnClickListener(view -> SingleWordActivity.this.finish());
        binding.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.toolbar_favorite:
                    if (word.getCollected() == 0){
                        // 未收藏
                        Util.showMessage(this,"已添加到我的收藏");
                        item.setIcon(getResources().getDrawable(R.drawable.ic_favorite_24dp));
                        word.setCollected(1);
                        collectedChangeFlag = !collectedChangeFlag;
                    } else {
                        // 已收藏
                        Util.showMessage(this,"已从我的收藏中移除");
                        item.setIcon(getResources().getDrawable(R.drawable.ic_favorite_border_white_24dp));
                        word.setCollected(0);
                        collectedChangeFlag = !collectedChangeFlag;
                    }

                    break;
                case R.id.toolbar_share:
                    break;
                default:
                    break;
            }
            return false;
        });

        Glide.with(this)
                .load(word.pictureUrl)
                .into(binding.wordImage);

    }

//    public void getNetworkData(String WordName) throws IOException {
//
//        final String BASE_URL = "https://shouyu.51240.com/";
//        final String POSTFIX = "__shouyus/";
//        String url = BASE_URL + WordName + POSTFIX;
//
//        Document doc = Jsoup.connect(url).timeout(5000).get();
//        Elements pngs = doc.select("img[src$=.png]");
//        png2Drawable(pngs.get(0).attr("src").substring(2));
//
//        Elements titles = doc.select("td[bgcolor=#FFFFFF]");
//        wordDescriptionCont = titles.get(0).text();
//
//        handler.sendEmptyMessage(0);
//
//    }

//    public void png2Drawable(String url){
//
//        url = "http://" + url;
//        try {
//            drawable = (Drawable) Drawable.createFromStream((new URL(url)).openStream(), null);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (word.getCollected() == 1) {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_favorite_24dp));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        new Thread(() -> {
            if (collectedChangeFlag) {
                new WordRepository(getApplication()).updateWordCollectedStatus(word);
            }
        }).start();
        super.onDestroy();
    }
}
