package com.zhangjun.quyi.currency_test.utils;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.util.Arrays;

// m1 wss://api.livechatinc.com/v3.4/customer/rtm/ws?organization_id=ea65a442-7b2d-4cc2-a9e6-c30eac41d81f
public class SocketIoClientUtil {

    public static void main(String[] args) throws Exception {
        String url ="wss://api.livechatinc.com/v3.4/customer/rtm/ws?organization_id=ea65a442-7b2d-4cc2-a9e6-c30eac41d81f";
        try{
            IO.Options options = new IO.Options();
            options.transports = new String[]{"websocket","xhr-polling","jsonp-polling"};
            options.reconnectionAttempts = 2;
            options.reconnectionDelay = 5000;//失败重连的时间间隔
            options.timeout = 5000;//连接超时时间(ms)
            //par1 是任意参数
            Socket socket = IO.socket(url, options);

            // 当通信连接成功后
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                public void call(Object... args) {
                    socket.send("hello");
                    System.out.println(args);
                }
            });

            // 当通信连接失败后
            socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    Arrays.stream(objects).forEach(item-> System.out.println("连接失败！！！" + item));
                }
            });

//            //自定义事件
//            socket.on("borcast", new Emitter.Listener() {
//                public void call(Object... objects) {
//                    System.out.println("receive borcast data:" + objects[0].toString());
//                }
//            });

            socket.on("connected", new Emitter.Listener() {
                public void call(Object... objects) {
                    System.out.println("receive connected data:" + objects[0].toString());
                }
            });



            socket.connect();
            String data = "{\"request_id\":\"1cle94ajgea\",\"action\":\"login\",\"payload\":{\"token\":\"Bearer dal:-AgRc6I2T0CxIQC7Cym2lA\",\"customer\":{\"session_fields\":[{\"user_id\":\"648ad79cdf976502bc665312\"}]},\"customer_side_storage\":{\"announcements_displayed\":\"\",\"greetings_accepted_count\":\"0\",\"greetings_shown_count\":\"22\",\"last_visit_timestamp\":\"1686720856630473\",\"name\":\"\",\"page_views_count\":\"2580\",\"visits_count\":\"176\"},\"is_mobile\":false,\"application\":{\"name\":\"chat_widget\",\"version\":\"efd5c0aa96d9af4d356f9987f939bffd92d1e3db\",\"channel_type\":\"code\"},\"customer_page\":{\"url\":\"https://mexlucky.office.coinmoney.xyz/diceGame\",\"title\":\"MexLucky | Casino en línea, juegos de cifrado hash, casino justo rastreable\"},\"referrer\":\"https://mexlucky.office.coinmoney.xyz/\",\"pushes\":{\"3.4\":[\"chat_deactivated\",\"chat_properties_deleted\",\"chat_properties_updated\",\"chat_transferred\",\"customer_page_updated\",\"customer_side_storage_updated\",\"customer_updated\",\"event_properties_deleted\",\"event_properties_updated\",\"event_updated\",\"events_marked_as_seen\",\"greeting_accepted\",\"greeting_canceled\",\"incoming_chat\",\"incoming_event\",\"incoming_greeting\",\"incoming_multicast\",\"incoming_rich_message_postback\",\"incoming_typing_indicator\",\"queue_position_updated\",\"thread_properties_deleted\",\"thread_properties_updated\",\"user_added_to_chat\",\"user_removed_from_chat\"]}},\"version\":\"3.4\"}";
            //循环发送数据
            while (true){
                socket.emit("client_info",data);
                Thread.sleep(2000);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
