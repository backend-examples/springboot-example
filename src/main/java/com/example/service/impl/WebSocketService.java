package com.example.service.impl;

import com.example.util.WebSocketClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

// 前端使用websocket连接时必须拼上{userName}
// 不拼的话会走正常接口请求
@ServerEndpoint(value = "/websocket/{userId}")
@Component
public class WebSocketService {

    private static final Logger log = LoggerFactory.getLogger(WebSocketService.class);

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
    private static ConcurrentHashMap<Integer, WebSocketClient> webSocketMap = new ConcurrentHashMap<>();

    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;

    /**接收userName*/
    private int userId;

    /**
     * 连接建立成功调用的方法
     * @param session
     * @param userId
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") int userId) {
        if(!webSocketMap.containsKey(userId)) {
            addOnlineCount(); // 在线数 +1
        }

        this.session = session;
        this.userId= userId;
        WebSocketClient client = new WebSocketClient();
        client.setSession(session);
        client.setUri(session.getRequestURI().toString());
        webSocketMap.put(userId, client);

        log.info("----------------------------------------------------------------------------");
        log.info("用户连接:"+userId+",当前在线人数为:" + getOnlineCount());
        try {
            sendMessage("来自后台的反馈：连接成功");
        } catch (IOException e) {
            log.error("用户:"+userId+",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);

            if(webSocketMap.size()>0) {
                //从set中删除
                subOnlineCount();
            }
        }

        log.info("----------------------------------------------------------------------------");
        log.info(userId+"用户退出,当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到用户消息:"+userId+",报文:"+message);
        //可以群发消息
        //消息保存到数据库、redis
        if(StringUtils.isNotBlank(message)){

        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:"+this.userId+",原因:"+error.getMessage());
        error.printStackTrace();
    }

    /**
     * 连接服务器成功后主动推送
     */
    public void sendMessage(String message) throws IOException {
        synchronized (session){
            this.session.getBasicRemote().sendText(message);
        }
    }

    /**
     * 向指定客户端发送消息
     * @param userName
     * @param message
     */
    public static void sendMessage(String userName,String message){
        try {
            WebSocketClient webSocketClient = webSocketMap.get(userName);
            if(webSocketClient!=null){
                webSocketClient.getSession().getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketService.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketService.onlineCount--;
    }

    public static void setOnlineCount(int onlineCount) {
        WebSocketService.onlineCount = onlineCount;
    }


    public static ConcurrentHashMap<Integer, WebSocketClient> getWebSocketMap() {
        return webSocketMap;
    }

    public static void setWebSocketMap(ConcurrentHashMap<Integer, WebSocketClient> webSocketMap) {
        WebSocketService.webSocketMap = webSocketMap;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public int getUserName() {
        return userId;
    }

    public void setUserName(int userName) {
        this.userId = userName;
    }

}