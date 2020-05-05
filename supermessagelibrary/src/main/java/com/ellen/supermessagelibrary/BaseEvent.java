package com.ellen.supermessagelibrary;

import android.support.v4.app.FragmentActivity;

/**
 * 消息事件触发回调接口
 */
public abstract class BaseEvent {

    private boolean isRemove = false;

    boolean isRemove() {
        return isRemove;
    }

    void setRemove(boolean remove) {
        isRemove = remove;
    }

    /**
     * 处理消息时回调
     * @param message
     */
    public abstract void handleMessage(SuperMessage message);

    /**
     * 注册消息事件成功时回调
     * @param superMessage
     */
    public abstract void registerEventSuccess(SuperMessage superMessage);
    /**
     * 注册消息事件失败时回调
     * @param superMessage
     */
    public abstract void registerEventFailure(SuperMessage superMessage);
    /**
     * 注销消息事件成功时回调
     * @param superMessage
     */
    public abstract void unRegisterEventSuccess(SuperMessage superMessage);
    /**
     * 注销消息事件失败时回调
     * @param superMessage
     */
    public abstract void unRegisterEventFailure(SuperMessage superMessage);
    /**
     * 与事件绑定的Activity
     */
    public abstract FragmentActivity bindActivity();
}
