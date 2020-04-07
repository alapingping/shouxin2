package com.shouxin.shouxin.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.shouxin.shouxin.API.Client;
import com.shouxin.shouxin.API.Service;
import com.shouxin.shouxin.DataModel.Message;
import com.shouxin.shouxin.R;
import com.shouxin.shouxin.Utils.SPHelper;
import com.shouxin.shouxin.databinding.ActivityEditMessageBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMessageActivity extends AppCompatActivity {

    private ActivityEditMessageBinding editMessageBinding;
    private Button mPublishButton;
    private EditText mEditText;
    private final int PUBLISH_SUCCESS = 1;
    private final int PUBLISH_FAIL = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editMessageBinding = ActivityEditMessageBinding.inflate(LayoutInflater.from(this));
        mPublishButton = editMessageBinding.publishButton;
        mPublishButton.setOnClickListener(v -> {
            sendMessage();
        });

        mEditText = editMessageBinding.contentInput;
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    mPublishButton.setBackgroundColor(getColor(R.color.colorMint));
                    mPublishButton.setClickable(true);
                } else {
                    mPublishButton.setBackgroundColor(getColor(R.color.colorHalfTransparentBlack));
                    mPublishButton.setClickable(false);
                }
            }
        });

        setContentView(editMessageBinding.getRoot());
    }

    private void sendMessage() {
        String username = SPHelper.getUsername(this);
        String content = mEditText.getText().toString();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String time = timestamp.toString();
        Message message = new Message(username, content, time);
        Service service = Client.retrofit.create(Service.class);
        Gson gson = new Gson();
        String obj = gson.toJson(message);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Call<ResponseBody> call = service.publishMessage(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    JSONObject object = new JSONObject(result);
                    Intent intent = new Intent();
                    if (object.getInt("code") == 200) {
                        intent.putExtra("newMessage", message);
                        setResult(PUBLISH_SUCCESS, intent);
                    } else {
                        intent.putExtra("newMessage", message);
                        setResult(PUBLISH_FAIL, intent);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

}
