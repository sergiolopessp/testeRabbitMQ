package com.example.demoRabbitMQ.data;

import com.example.demoRabbitMQ.queue.Queue;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class FortuneCookie {

    private static final String QUEUE_NAME = "fortuneCookie";
    private static final String EXCHANGE_NAME = "myExchange";
    private static final String KEY_NAME = "key";
    private static final Logger LOGGER = LoggerFactory.getLogger(FortuneCookie.class);
    private Queue queue;
    String mensagem;

    final private List<String> frases = Arrays.asList(
            "Thousands of candles can be lighted from a single candle, and the life of the candle will not be shortened. Happiness never decreases by being shared. - Buddha",
            "There are more things to alarm us than to harm us, and we suffer more often in apprehension than reality. - Seneca",
            "Time you enjoy wasting is not wasted time - Time you enjoy wasting is not wasted time - Marthe Troly-Curtin",
            "When one door of happiness closes, another opens, but often we look so long at the closed door that we do not see the one that has been opened for us. - Helen Keller",
            "Life is not measured by the number of breaths we take, but by the moments that take our breath away. - Maya Angelou ",
            "The pleasure which we most rarely experience gives us greatest delight. - Epictetus",
            "Do not spoil what you have by desiring what you have not; remember that what you now have was once among the things you only hoped for. - Epicurus",
            "Just because it didn’t last forever, doesn’t mean it wasn’t worth your while. - Unknown"
    );

    public FortuneCookie() {
        queue = new Queue();
        queue.createExchangeQueue(QUEUE_NAME, EXCHANGE_NAME, "direct", KEY_NAME);
    }

    public boolean enviaFrase() {
        boolean sucesso = false;

        try {
            queue.sendMessage(EXCHANGE_NAME, KEY_NAME, sorteiaFrase());
            sucesso = true;
        } catch (Exception e) {
            LOGGER.info("Falha no envio da mensagem", e);
        }
        return sucesso;
    }


    public String recuperaMensagem() throws InterruptedException {

        queue.listenToQueue(QUEUE_NAME, encontraFrase);
        Thread.sleep(1000);
        return  mensagem;
    }

    DeliverCallback encontraFrase = (consumerTag, delivery) -> {
        mensagem = new String(delivery.getBody(), "UTF-8");
        System.out.println(mensagem);
    };


    private String sorteiaFrase() {
        int indice = ThreadLocalRandom.current().nextInt(frases.size());
        return frases.get(indice);
    }
}
