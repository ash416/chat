package org.sheronova.web.chat;

import com.google.gson.Gson;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.util.logging.Logger;

public class DataDecoder implements Decoder.Text<Data> {
    private final Logger log = Logger.getLogger(getClass().getName());

    @Override
    public void init(EndpointConfig endpointConfig) { }

    @Override
    public boolean willDecode(String s) { return (s != null); }

    @Override
    public void destroy() { }

    @Override
    public Data decode(String s) {
        log.info("converting message from json format. Message: " + s);

        Gson gson = new Gson();
        return gson.fromJson(s, Data.class);
    }




}
