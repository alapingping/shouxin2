package com.shouxin.shouxin.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shouxin.shouxin.DataModel.Message;
import com.shouxin.shouxin.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.MyViewHolder> {


    private ArrayList<Message> mdatas;
    private Context context;

    public CommunityAdapter(ArrayList<Message> datas, Context context){
        //初始化数据内容
        this.mdatas = datas;
        this.context = context;
    }

     static class MyViewHolder extends RecyclerView.ViewHolder{
         final TextView text_username;
         final TextView text_content;
         final TextView text_time;
         final GridLayout picture_list;
         final int viewWidth;
         MyViewHolder(View itemView) {
            super(itemView);
            text_username = itemView.findViewById(R.id.username);
            text_content = itemView.findViewById(R.id.text_community_content);
            text_time = itemView.findViewById(R.id.text_time);
            picture_list = itemView.findViewById(R.id.community_picture_list);
            viewWidth = itemView.getWidth();
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_community_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Message message = mdatas.get(position);
        holder.text_username.setText(message.getUsername());
        holder.text_content.setText(message.getContent());
        String time = changeTimeFormat(message.getTime());
        holder.text_time.setText(time);
    }

    @Override
    public int getItemCount() {
        return mdatas.size();
    }

    private String changeTimeFormat(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String res = null;
        try {
            Long sendTime = sdf.parse(time).getTime();
            Long curTime = System.currentTimeMillis();
            Long diff = curTime - sendTime;
            int seconds = (int) (diff / 1000);
            int minutes = seconds / 60;
            if (minutes < 60) {
                res = minutes + "分钟前";
            } else if (minutes >= 60 && minutes < 1440) {
                int hours = minutes / 60;
                res = hours + "小时前";
            } else if (minutes >= 1440) {
                int days = minutes / 1400;
                res = days + "天前";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }
}
