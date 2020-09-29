package com.bfd.tools;

import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.utils.ZkUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.security.JaasUtils;
import scala.collection.JavaConversions;

import java.util.List;
import java.util.Properties;

/**
 * @author everywherewego
 * @date 2020-09-29 16:33
 */

public class Kafkautils {

    private static Producer<String, String> producer;
    private static ZkUtils zkUtils;

    static {
        Properties props = new Properties();
        props.put("bootstrap.servers", "172.24.5.242:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<String, String>(props);

        zkUtils = ZkUtils.apply("172.24.5.242:2181", 30000, 30000, JaasUtils.isZkSecurityEnabled());
    }

    public static void sendMessage(String message) {
        producer.send(new ProducerRecord<String, String>("test", message));
        producer.flush();
    }

    public static void createTopic(String topic) {
        if (!showTopics().contains(topic)) {
            AdminUtils.createTopic(zkUtils, topic, 1, 1, new Properties(), RackAwareMode.Enforced$.MODULE$);
        }
    }

    public static List<String> showTopics() {
        List<String> topics = JavaConversions.seqAsJavaList(zkUtils.getAllTopics());
        System.out.println(topics);
        return topics;
    }

    public static void main(String[] args) {
//        sendMessage("asdfasdf");

//        ZkUtils zkUtils = ZkUtils.apply("172.24.5.242:2181", 30000, 30000, JaasUtils.isZkSecurityEnabled());
        // 创建一个单分区单副本名为t1的topic
//        AdminUtils.createTopic(zkUtils, "t1", 1, 1, new Properties(), RackAwareMode.Enforced$.MODULE$);
//        zkUtils.close();


    }


}
