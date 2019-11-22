package com.ellen.supermessagelibrary;

/**
 * 全局消息拦截器
 */
public interface AllMessageInterceptor {

    /**
     * 当有有效的消息发送时触发(消息发送了并触发了消息事件)
     * @param superMessage
     */
    void haveEffectiveMessage(SuperMessage superMessage);

    /**
     * 当有无效的消息发送时触发(消息发送了没有触发了消息事件)
     * @param superMessage
     */
    void haveNoEffectiveMessage(SuperMessage superMessage);

    /**
     * 当有消息发送时就触发(此方法先触发)
     * @param superMessage
     */
    void haveMessage(SuperMessage superMessage);

}
