package com.shouxin.shouxin.DataModel;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;

public class Message implements Parcelable {

    private String username;
    private String content;
    private String time;


    public Message(String username, String content, String time) {
        this.username = username;
        this.content = content;
        this.time = time;
    }

    protected Message(Parcel in) {
        username = in.readString();
        content = in.readString();
        time = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(username);
        dest.writeString(content);
        dest.writeString(time);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Message) {
            Message message = (Message) obj;
            return username.equals(((Message) obj).getUsername()) &&
                    content.equals(((Message) obj).getContent()) &&
                    time.equals(((Message) obj).getTime());
        }
        return false;
    }

}
