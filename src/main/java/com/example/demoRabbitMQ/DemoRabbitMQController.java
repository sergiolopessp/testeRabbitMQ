package com.example.demoRabbitMQ;

import com.example.demoRabbitMQ.data.FortuneCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoRabbitMQController {

    @Autowired
    private FortuneCookie fortuneCookie;

    @GetMapping("/envia")
    public String enviaMensagem() {
        if (fortuneCookie.enviaFrase()) {
            return "mensagem enviada com sucesso";
        } else {
            return "Falha no envio da frase";
        }
    }

    @GetMapping("/recuperaMensagem")
    public String recuperaMensagem() throws InterruptedException {
       return fortuneCookie.recuperaMensagem();
    }
}
