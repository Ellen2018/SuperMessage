package com.ellen.supermessage;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ellen.supermessagelibrary.AllMessageInterceptor;
import com.ellen.supermessagelibrary.BaseEvent;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;

public class MainActivity extends AppCompatActivity {

    private String messageId = "test";
    private BaseEvent baseEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerMessageEvent();
        sendMessagePuTong();
        sendMessageMainThread();
        unRegisterMessageEvent();
        //添加全局消息拦截器
        MessageManager.getInstance().addMessageInterceptor(new AllMessageInterceptor() {
            @Override
            public void haveEffectiveMessage(SuperMessage superMessage) {
                //当发送的消息为有效的(触发了至少一个消息事件)消息回调这里
                Log.e("Ellen2018","有位置发送了有效消息:"+superMessage.getMessageId());
            }

            @Override
            public void haveNoEffectiveMessage(SuperMessage superMessage) {
                //当发送的消息为无效的(没有触发任意一个消息事件)消息回调这里
                Log.e("Ellen2018","有位置发送了无效消息:"+superMessage.getMessageId());
            }

            @Override
            public void haveMessage(SuperMessage superMessage) {
               //每发送一个消息都会触发这里(除了null的空消息)
                Log.e("Ellen2018","有位置发送了消息:"+superMessage.getMessageId());
            }
        });
    }

    private void unRegisterMessageEvent() {
        findViewById(R.id.bt_un_register_message_event).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //手动注销消息事件
                MessageManager.getInstance().unRegisterMessageEvent(messageId,baseEvent);
            }
        });
    }

    private void sendMessageMainThread() {
        findViewById(R.id.bt_send_message_mian_thread).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuperMessage superMessage = new SuperMessage(messageId);
                superMessage.arg1 = 1;
                superMessage.arg2 = 2;
                superMessage.what = 3;
                superMessage.object = "hi,吃了没";
                //发送的消息将会切到主线程再进行发送
                MessageManager.getInstance().sendMainThreadMessage(superMessage);
            }
        });
    }

    private void sendMessagePuTong() {
        findViewById(R.id.bt_send_message_putong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuperMessage superMessage = new SuperMessage(messageId);
                superMessage.arg1 = 1;
                superMessage.arg2 = 2;
                superMessage.what = 3;
                superMessage.object = "hi,吃了没";
                MessageManager.getInstance().sendMessage(superMessage);
            }
        });
    }

    private void registerMessageEvent(){
        findViewById(R.id.bt_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Ellen2018","执行没?");
                baseEvent = new BaseEvent() {
                    @Override
                    public void handleMessage(SuperMessage message) {
                        //收到消息
                        Log.e("Ellen2018",message.what+"");
                        Log.e("Ellen2018",message.arg1+"");
                        Log.e("Ellen2018",message.arg2+"");
                        Log.e("Ellen2018",message.object+"");
                    }

                    @Override
                    public void registerEventSuccess(SuperMessage superMessage) {
                        //消息事件注册成功回调这里
                        Log.e("Ellen2018",superMessage.getMessageId()+":消息事件注册成功!");
                    }

                    @Override
                    public void registerEventFailure(SuperMessage superMessage) {
                        //消息事件注册失败回调这里
                        Log.e("Ellen2018",superMessage.getMessageId()+":消息事件注册失败!");
                    }

                    @Override
                    public void unRegisterEventSuccess(SuperMessage superMessage) {
                        //消息事件注销成功回调这里
                        Log.e("Ellen2018",superMessage.getMessageId()+":消息事件注销成功!");
                    }

                    @Override
                    public void unRegisterEventFailure(SuperMessage superMessage) {
                        //消息事件注销失败回调这里
                        Log.e("Ellen2018",superMessage.getMessageId()+":消息事件注销失败!");
                    }

                    @Override
                    public FragmentActivity bindActivity() {
                        //此方法的作用就是让消息事件跟随Activity注销而注销(自动注销)
                        //如果返回当前Activity的实例，则会自动注销
                        //如果返回null,则需要手动注销
                        return MainActivity.this;
                    }
                };
                //注册消息事件
                MessageManager.getInstance().registerMessageEvent(messageId,baseEvent);
            }
        });
    }
}
