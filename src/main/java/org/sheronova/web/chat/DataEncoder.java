package org.sheronova.web.chat;


import com.google.gson.Gson;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.util.logging.Logger;

public class DataEncoder implements Encoder.Text<Data> {
    private final Logger log = Logger.getLogger(getClass().getName());

    @Override
    public void init(EndpointConfig endpointConfig) { }

    @Override
    public void destroy() { }

    @Override
    public String encode(Data message) {
        log.info("converting message to json format");

        Gson gson = new Gson();
        return gson.toJson(message);
    }




}