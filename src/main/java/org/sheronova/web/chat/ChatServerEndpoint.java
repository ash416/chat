package org.chat.sheronova;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(
        value="/chat/{username}",
        decoders = DataDecoder.class,
        encoders = DataEncoder.class
)

public class ChatServerEndpoint {
    private final Logger log = Logger.getLogger(getClass().getName());

    private Session session;
    private static final Set<ChatServerEndpoint> chatEndpoints = new CopyOnWriteArraySet<ChatServerEndpoint>();
    private static HashMap<String,String> users = new HashMap<String, String>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {
        log.info(session.getId() + " connected!");

        this.session = session;
        chatEndpoints.add(this);
        users.put(session.getId(), username);

        Data message = new Data();
        String listUsers = "";
        for (String user: users.values()) {
            listUsers += "<li class='person'>" + user + "</li>";
        }
        message.setUsers(listUsers);
        message.setUsersCount(users.size());
        message.setSender(username);
        message.setContent("connected!");
        broadcast(message);
    }

    @OnMessage
    public void onMessage(Session session, Data message) throws IOException, EncodeException {
        log.info(message.toString());
        String listUsers = "";
        for (String user: users.values()) {
            listUsers += "<li class='person'>" + user + "</li>";
        }
        message.setUsers(listUsers);
        message.setUsersCount(users.size());
        message.setSender(users.get(session.getId()));
        sendMessage(message);
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        log.info(session.getId() + " disconnected!");

        chatEndpoints.remove(this);
        Data message = new Data();
        message.setSender(users.get(session.getId()));
        users.remove(session.getId());
        String listUsers = "";
        for (String user: users.values()) {
            listUsers += "<li class='person'>" + user + "</li>";
        }
        message.setUsers(listUsers);
        message.setUsersCount(users.size());
        message.setContent("disconnected!");
        broadcast(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.warning(throwable.toString());
    }

    private static void broadcast(Data message) throws IOException, EncodeException {
        for (ChatServerEndpoint endpoint : chatEndpoints) {
            synchronized(endpoint) {
                endpoint.session.getBasicRemote().sendObject(message);
            }
        }
    }

    private static void sendMessage(Data message) throws IOException, EncodeException {
        for (ChatServerEndpoint endpoint : chatEndpoints) {
            synchronized(endpoint) {
                if (message.getAddr().length() == 0 && !endpoint.session.getId().equals(getSessionId(message.getSender()))) {
                    endpoint.session.getBasicRemote().sendObject(message);
                }
                else if (endpoint.session.getId().equals(getSessionId(message.getAddr()))) {
                    endpoint.session.getBasicRemote().sendObject(message);
                }
            }
        }
    }

    private static String getSessionId(String to) {
        if (users.containsValue(to)) {
            for (String sessionId: users.keySet()) {
                if (users.get(sessionId).equals(to)) {
                    return sessionId;
                }
            }
        }
        return null;
    }
}
