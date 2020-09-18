package function;

import thread.ClientThread;

import java.util.HashMap;

public class Chat {
    ClientThread client;
    public Chat(ClientThread client) {
        this.client = client;
    }

    public HashMap<String,Object> chatProc(HashMap<String, Object> request) {

        String msg = "[ "+ client.name + " ] " + (String)request.get("content");
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("protocol", 1201);
        map.put("content", msg);

        boolean isSuccess = false;
        try {
            client.accept.pstmt.setString(1, (String)request.get("room_idx"));
            client.accept.pstmt.setString(2, (String)request.get("user_idx"));
            client.accept.pstmt.setString(3, (String)request.get("content"));
            client.accept.pstmt.executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
            isSuccess = false;
        }
        return map;

    }
}
