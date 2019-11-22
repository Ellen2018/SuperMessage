package com.ellen.supermessagelibrary;

import android.support.v4.app.FragmentActivity;

/**
 * 消息事件触发回调接口
 */
public interface BaseEvent {

    /**
     * 处理消息时回调
     * @param message
     */
    void handleMessage(SuperMessage message);

    /**
     * 注册消息事件成功时回调
     * @param superMessage
     */
    void registerEventSuccess(SuperMessage superMessage);
    /**
     * 注册消息事件失败时回调
     * @param superMessage
     */
    void registerEventFailure(SuperMessage superMessage);
    /**
     * 注销消息事件成功时回调
     * @param superMessage
     */
    void unRegisterEventSuccess(SuperMessage superMessage);
    /**
     * 注销消息事件失败时回调
     * @param superMessage
     */
    void unRegisterEventFailure(SuperMessage superMessage);
    /**
     * 与事件绑定的Activity
     */
    FragmentActivity bindActivity();
}
