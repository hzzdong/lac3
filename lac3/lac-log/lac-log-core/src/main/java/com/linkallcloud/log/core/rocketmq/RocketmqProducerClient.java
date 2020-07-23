package com.linkallcloud.log.core.rocketmq;

import java.io.UnsupportedEncodingException;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import com.linkallcloud.log.core.constant.LogMessageConstant;

public class RocketmqProducerClient {

    private static RocketmqProducerClient instance;
    private DefaultMQProducer producer = null;

    public static RocketmqProducerClient init(DefaultMQProducer producer) {
        if (instance == null) {
            synchronized (RocketmqProducerClient.class) {
                if (instance == null) {
                    instance = new RocketmqProducerClient(producer);
                }
            }
        }
        return instance;
    }

    public static RocketmqProducerClient getInstance() {
        return instance;
    }

    private RocketmqProducerClient(DefaultMQProducer producer) {
        this.producer = producer;
    }

    public void sendMsg(String logMessage) throws UnsupportedEncodingException, MQClientException, RemotingException,
            MQBrokerException, InterruptedException {
        Message msg = new Message(LogMessageConstant.LOG_TOPIC, logMessage.getBytes(RemotingHelper.DEFAULT_CHARSET));
        producer.send(msg);
    }
}
