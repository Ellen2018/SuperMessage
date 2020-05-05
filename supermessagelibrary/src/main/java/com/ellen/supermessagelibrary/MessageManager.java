package com.ellen.supermessagelibrary;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;

import com.ellen.supermessagelibrary.ActivityLifeListener.ActivityLifeListener;
import com.ellen.supermessagelibrary.ActivityLifeListener.ActivityLifeListenerManager;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class MessageManager {

    private Map<SuperMessage, List<BaseEvent>> messageMaps;
    private Map<String, SuperMessage> stickMessageMap;
    private List<AllMessageInterceptor> allMessageInterceptorList;

    private static volatile MessageManager messageManager;

    private MessageManager() {
    }

    public static MessageManager getInstance() {
        if (messageManager == null) {
            synchronized (MessageManager.class) {
                if (messageManager == null) {
                    messageManager = new MessageManager();
                }
            }
        }
        return messageManager;
    }

    public void addMessageInterceptor(final AllMessageInterceptor allMessageInterceptor){
        if(allMessageInterceptor == null)return;
        if(allMessageInterceptorList == null){
            allMessageInterceptorList = new Vector<>();
        }
        allMessageInterceptorList.add(allMessageInterceptor);
        FragmentActivity activity = allMessageInterceptor.bindActivity();
        if(activity != null){
            new ActivityLifeListenerManager().startActivityLifeListener(activity, new ActivityLifeListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onStop() {

                }

                @Override
                public void onDestory() {
                   removeMessageInterceptor(allMessageInterceptor);
                }
            });
        }
    }

    public void removeMessageInterceptor(AllMessageInterceptor allMessageInterceptor){
        if(allMessageInterceptor == null)return;
        if(allMessageInterceptorList != null){
            allMessageInterceptorList.remove(allMessageInterceptor);
        }
    }

    /**
     * 注册消息事件
     * @param messageId
     * @param baseEvent
     */
    public void registerMessageEvent(String messageId,BaseEvent baseEvent){
        SuperMessage superMessage = new SuperMessage(messageId);
        registerMessageEvent(superMessage,baseEvent);
    }

    /**
     * 注册消息事件
     * @param message
     * @param baseEvent
     */
    private void registerMessageEvent(final SuperMessage message, final BaseEvent baseEvent) {
        if (message == null) return;
        if(baseEvent == null){
            baseEvent.registerEventFailure(message);
            return;
        }
        if (messageMaps == null) {
            messageMaps = new Hashtable<>();
        }
        if (message.getMessageId() == null) {
            throw new MessageIdNullException("消息id为null异常");
        }
        List<BaseEvent> baseEventList = messageMaps.get(message);
        if (baseEventList == null) {
            baseEventList = new Vector<>();
            baseEventList.add(baseEvent);
            messageMaps.put(message, baseEventList);
        } else {
            baseEventList.add(baseEvent);
        }
        baseEvent.registerEventSuccess(message);
        //检测粘性事件
        if (stickMessageMap != null) {
            boolean isContains = stickMessageMap.containsKey(message.getMessageId());
            if (isContains) {
                baseEvent.handleMessage(stickMessageMap.get(message.getMessageId()));
            }
        }
        FragmentActivity activity = baseEvent.bindActivity();
        //生命周期监听
        if(activity != null){
           new ActivityLifeListenerManager().startActivityLifeListener(activity, new ActivityLifeListener() {
               @Override
               public void onStart() {

               }

               @Override
               public void onStop() {

               }

               @Override
               public void onDestory() {
                  unRegisterMessageEvent(message,baseEvent);
               }
           });
        }
    }

    /**
     * 注销消息事件
     * @param message
     * @param baseEvent
     */
    private void unRegisterMessageEvent(SuperMessage message,BaseEvent baseEvent){
        if(message == null)return;
        if(messageMaps == null)return;
        if(baseEvent == null) {
            baseEvent.registerEventFailure(message);
            return;
        }
        if (message.getMessageId() == null) {
            throw new MessageIdNullException("消息id为null异常");
        }
        List<BaseEvent> baseEventList = messageMaps.get(message);
        if (baseEventList != null) {
            boolean isRemove;
            //用迭代器删除防止java.util.ConcurrentModificationException异常
            Iterator<BaseEvent> it_b = baseEventList.iterator();
            while(it_b.hasNext()){
                BaseEvent be1 = it_b.next();
                if (be1 == baseEvent) {
                    it_b.remove();
                }
            }
            synchronized (MessageManager.class) {
                isRemove = baseEventList.remove(baseEvent);
            }
            if(isRemove) {
                baseEvent.unRegisterEventSuccess(message);
            }else {
                baseEvent.unRegisterEventFailure(message);
            }
        }else {
            baseEvent.unRegisterEventFailure(message);
        }
    }

    /**
     * 注销消息事件
     * @param messageId
     * @param baseEvent
     */
    public void unRegisterMessageEvent(String messageId,BaseEvent baseEvent){
        SuperMessage superMessage = new SuperMessage(messageId);
        unRegisterMessageEvent(superMessage,baseEvent);
    }

    public void sendMessage(SuperMessage message) {
        if (message == null) { return;}
        if (messageMaps == null) return;
        if (message.getMessageId() == null) {
            //抛出异常
            throw new MessageIdNullException("消息id为null异常");
        } else {
            List<BaseEvent> baseEventList = messageMaps.get(message);
            if(allMessageInterceptorList != null){
                for(AllMessageInterceptor allMessageInterceptor:allMessageInterceptorList){
                    allMessageInterceptor.haveMessage(message);
                }
            }
            if (baseEventList != null && baseEventList.size() > 0) {
                if(baseEventList.size() > 0){
                    if(allMessageInterceptorList != null){
                        for(AllMessageInterceptor allMessageInterceptor:allMessageInterceptorList){
                            allMessageInterceptor.haveEffectiveMessage(message);
                        }
                    }
                }
                //迭代器访问防止：java.util.ConcurrentModificationException异常
                Iterator<BaseEvent> it_b = baseEventList.iterator();
                while (it_b.hasNext()){
                    BaseEvent baseEvent = it_b.next();
                    baseEvent.handleMessage(message);
                }
            }else {
                if(allMessageInterceptorList != null){
                    for(AllMessageInterceptor allMessageInterceptor:allMessageInterceptorList){
                        allMessageInterceptor.haveNoEffectiveMessage(message);
                    }
                }
            }
        }
    }

    public void sendEmptyMessage(String messageId){
        SuperMessage superMessage = new SuperMessage(messageId);
        sendMessage(superMessage);
    }

    /**
     * 发送粘性消息(重复发送消息内容被最新的覆盖)
     *
     * @param message
     */
    public void sendStickMessage(SuperMessage message) {
        if (message == null) { return;}
        if (message.getMessageId() == null) {
            //抛出异常
            throw new MessageIdNullException("消息id为null异常");
        }
        if (stickMessageMap == null) {
            stickMessageMap = new Hashtable<>();
        }
        stickMessageMap.put(message.getMessageId(), message);
        sendMessage(message);
    }

    public void sendEmptyStickMessage(String messageId){
        SuperMessage superMessage = new SuperMessage(messageId);
        sendStickMessage(superMessage);
    }

    public void sendMainThreadMessage(final SuperMessage message) {
        if (isMainThread()) {
            sendMessage(message);
        } else {
            //需要切换线程
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    sendMessage(message);
                }
            });
        }
    }

    public void sendEmptyMainThreadMessage(String messageId){
        SuperMessage superMessage = new SuperMessage(messageId);
        sendMainThreadMessage(superMessage);
    }

    public void sendStickMainThreadMessage(final SuperMessage message) {
        if (isMainThread()) {
            sendStickMessage(message);
        } else {
            //需要切换线程
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    sendStickMessage(message);
                }
            });
        }
    }

    public void sendStickMainThreadMessage(String messageId){
        SuperMessage superMessage = new SuperMessage(messageId);
        sendStickMainThreadMessage(superMessage);
    }

    /**
     * 判断是否为主线程
     *
     * @return
     */
    private boolean isMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

}
