package com.ellen.supermessagelibrary;

import android.support.v4.app.FragmentActivity;

public abstract class MessageEventTrigger implements BaseEvent {

    @Override
    public void registerEventSuccess(SuperMessage superMessage) {

    }

    @Override
    public void registerEventFailure(SuperMessage superMessage) {

    }

    @Override
    public void unRegisterEventSuccess(SuperMessage superMessage) {

    }

    @Override
    public void unRegisterEventFailure(SuperMessage superMessage) {

    }

    @Override
    public FragmentActivity bindActivity() {
        return null;
    }
}
