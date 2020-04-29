package com.shouxin.shouxin.dummy;

import com.shouxin.shouxin.DataModel.Message;

import java.util.ArrayList;

public class DummyMessage {

    private static ArrayList<Message> messages;

    static {
        messages = new ArrayList<>();
        messages.add(new Message("路人甲", "昨天是个好日子", "2020-04-29 10:56"));
        messages.add(new Message("路人甲", "昨天是个好日子", "2020-04-29 10:56"));

    }

    public static ArrayList<Message> getMessages() {
        return messages;
    }
}
