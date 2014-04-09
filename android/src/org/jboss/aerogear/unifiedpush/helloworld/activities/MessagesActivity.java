package org.jboss.aerogear.unifiedpush.helloworld.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.jboss.aerogear.android.unifiedpush.MessageHandler;
import org.jboss.aerogear.android.unifiedpush.Registrations;
import org.jboss.aerogear.unifiedpush.helloworld.HelloWorldApplication;
import org.jboss.aerogear.unifiedpush.helloworld.R;
import org.jboss.aerogear.unifiedpush.helloworld.handler.NotificationBarMessageHandler;

import java.util.ArrayList;
import java.util.List;

public class MessagesActivity extends Activity implements MessageHandler{

    private HelloWorldApplication application;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages);

        application = (HelloWorldApplication) getApplication();

        listView = (ListView) findViewById(R.id.messages);

        if(getIntent().getExtras() != null) {
            addNewMessage(getIntent().getExtras());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Registrations.registerMainThreadHandler(this);
        Registrations.unregisterBackgroundThreadHandler(NotificationBarMessageHandler.instance);

        displayMessages();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Registrations.unregisterMainThreadHandler(this);
        Registrations.registerBackgroundThreadHandler(NotificationBarMessageHandler.instance);
    }

    @Override
    public void onMessage(Context context, Bundle message) {
        addNewMessage(message);
    }

    @Override
    public void onDeleteMessage(Context context, Bundle message) {
    }

    @Override
    public void onError() {
    }

    private void addNewMessage(Bundle message) {
        application.addMessage(message.getString("alert"));
        displayMessages();
    }

    private void displayMessages() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.message_item, application.getMessages());
        listView.setAdapter(adapter);
    }

}
