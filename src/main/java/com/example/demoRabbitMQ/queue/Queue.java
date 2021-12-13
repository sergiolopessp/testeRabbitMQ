package com.example.demoRabbitMQ.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class Queue {
    private static final String HOST = "localhost";
    private static final Logger LOGGER = LoggerFactory.getLogger(Queue.class);
    private Channel channel;

    public Queue() {
        ConnectionFactory cf = new ConnectionFactory();
        cf.setHost(HOST);
        try {
            Connection connection = cf.newConnection();
            channel = connection.createChannel();

        } catch (Exception e) {
            LOGGER.error("Erro ao criar a conexão com o RabbitMQ:", e);
        }
    }

    public void sendMessage(String exchange, String key, String message) {
        try {
            channel.basicPublish(exchange, key, null, message.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            LOGGER.error("Falha ao enviar a mensagem:", e.getMessage());
        }
    }

    public void listenToQueue(String queueName, DeliverCallback dlr) {
        try {
            channel.basicConsume(queueName, true, dlr, consumerTag -> {});
        } catch (Exception e) {
            LOGGER.error("Falha ao ouvir a lista", e);
        }
    }

    public void createExchangeQueue(String queueName, String exchangeName, String exchangeType, String key) {
        try {
            channel.queueDeclare(queueName, false, false, false, null);
            channel.exchangeDeclare(exchangeName, exchangeType);
            channel.queueBind(queueName, exchangeName, key);

        } catch (Exception e) {
            LOGGER.error("Erro ao criar a fila de troca: ", e);
        }
    }
}
