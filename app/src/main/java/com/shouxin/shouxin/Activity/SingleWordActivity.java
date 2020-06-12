package com.shouxin.shouxin.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import io.reactivex.schedulers.Schedulers;

import android.view.LayoutInflater;
import android.view.Menu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shouxin.shouxin.DataModel.Word;
import com.shouxin.shouxin.R;
import com.shouxin.shouxin.Utils.Util;
import com.shouxin.shouxin.database.Repository.WordRepository;
import com.shouxin.shouxin.databinding.ActivitySignleWordBinding;


public class SingleWordActivity extends AppCompatActivity {

    // 当前word
    private Word word;
    // 当前activity的binding
    private ActivitySignleWordBinding binding;
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

        binding.wordTitle.setText(getString(R.string.word_name, word.getName()));
        binding.wordDescription.setText(getString(R.string.word_description, word.getDescription()));
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
                        item.setIcon(getDrawable(R.drawable.ic_favorite_24dp));
                        word.setCollected(1);
                        collectedChangeFlag = !collectedChangeFlag;
                    } else {
                        // 已收藏
                        Util.showMessage(this,"已从我的收藏中移除");
                        item.setIcon(getDrawable(R.drawable.ic_favorite_border_white_24dp));
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (word.getCollected() == 1) {
            menu.getItem(1).setIcon(getDrawable(R.drawable.ic_favorite_24dp));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        if (collectedChangeFlag) {
            WordRepository.getWordRepository().update(word)
                    .subscribeOn(Schedulers.io());
        }
        super.onDestroy();
    }
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