package com.shouxin.shouxin.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shouxin.shouxin.API.Client;
import com.shouxin.shouxin.API.Service;
import com.shouxin.shouxin.dummy.DummyContent;
import com.shouxin.shouxin.Adapter.DictionartAdapter;
import com.shouxin.shouxin.DataModel.Word;
import com.shouxin.shouxin.database.Repository.WordRepository;
import com.shouxin.shouxin.databinding.FragmentDictionaryBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DictionaryFragment extends Fragment {

    private static volatile DictionaryFragment fragment;
    private FragmentDictionaryBinding binding;
    private ArrayMap datas;
    private static List<Word> words;
    private DictionartAdapter mAdapter;

    public static DictionaryFragment getInstance(){
        if (fragment == null) {
            synchronized (DictionaryFragment.class) {
                if (fragment == null) {
                    fragment = new DictionaryFragment();
                }
            }
        }
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDictionaryBinding.inflate(inflater, container, false);
        datas = new ArrayMap<String, Integer>();

        getAllWords();
        RecyclerView rv = binding.recycler;

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setHasFixedSize(true);
        mAdapter = new DictionartAdapter(getActivity(), new ArrayMap<>());
        mAdapter.setData(datas);
        rv.setAdapter(mAdapter);

        return binding.getRoot();
    }

    @SuppressLint("CheckResult")
    private void getAllWords() {
        WordRepository.getWordRepository().getAllWords()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<List<Word>>() {
                            @Override
                            public void accept(List<Word> words) throws Exception {
                                if (words.size() == 0) {
                                    getWordsFromServer();
                                } else {
                                    datas.put("字母", DummyContent.getWords());
                                    mAdapter.setData(datas);
//                                  getWordsFromLocal();
                                }
                            }
                        }
                );
    }

    private void getWordsFromServer() {
        Service service = Client.retrofit.create(Service.class);
        Call<ResponseBody> call = service.getAllWords();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    JSONObject object = new JSONObject(result);
                    int code = object.getInt("code");
                    if (code == 200) {
                        Array2Words(object.getJSONArray("datas"));
//                        mAdapter.notifyNewData(datas);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                datas.put("字母", DummyContent.getWords());
                mAdapter.setData(datas);
            }
        });
    }


    private void Array2Words(JSONArray array) throws JSONException {
        if (array.length() == 0) {return ;}
        List<Word> words = new ArrayList<>();
        String curCategory = array.getJSONObject(0).getString("category");
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            String category = object.getString("category");
            if (!curCategory.equals(category)) {
                datas.put(category, words);
                words = new ArrayList<>();
                curCategory = category;
            }
            Word word = new Word(category,
                    object.getString("name"),
                    object.getString("description"),
                    object.getString("pictureURL"));
            words.add(word);
            WordRepository.getWordRepository().insert(word);
        }
        datas.put(curCategory, words);
    }

    private void getWordsFromLocal() {

        if (words.size() == 0) {
            datas.put("字母", DummyContent.getWords());
            mAdapter.setData(datas);
            return;
        }

        String curCategory = words.get(0).getCategory();
        List<Word> subWords = new ArrayList<>();
        for (Word word:words) {
            if (!word.getCategory().equals(curCategory)) {
                datas.put(curCategory, subWords);
                subWords = new ArrayList<>();
                curCategory = word.getCategory();
            }
            subWords.add(word);
        }
        datas.put(curCategory, subWords);
        mAdapter.setData(datas);
    }

    private class MyHandler extends Handler {

        private WeakReference<Activity> activity;

        public MyHandler(Activity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    // 需要从数据库提取数据
//                    getWordsFromServer();
                    getWordsFromLocal();
                    break;
                case 1:
                    // 从本地数据库提取数据
                    getWordsFromLocal();
                    break;
                default:
                    break;
            }

        }
    }


    public static List<Word> getWords() {
        return words;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        words = null;
    }

}
