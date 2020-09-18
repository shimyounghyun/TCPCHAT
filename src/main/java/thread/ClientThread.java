package thread;

import function.Chat;
import function.SendHash;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;

public class ClientThread extends Thread{
    public AcceptThread accept;
    public Socket socket;
    public String name;
    public int roomNum;
    public int userIdx;

    public InputStream in;
    public OutputStream out;
    public ObjectInputStream oin;
    public ObjectOutputStream oout;

    HashMap<String,Object> response;

    public ClientThread(AcceptThread accept, Socket socket) throws Exception{
        this.accept = accept;
        this.socket = socket;

        in = socket.getInputStream();
        out = socket.getOutputStream();
        oin = new ObjectInputStream(in);
        oout = new ObjectOutputStream(out);
    }

    @Override
    public void run() {
        try {
            while(true) {
                HashMap<String , Object> request = (HashMap<String , Object>)oin.readObject();
                if(request.get("protocol") == null) {
                    break;
                }
                this.roomNum = (int)request.get("room_idx");
                this.userIdx = (int)request.get("user_idx");

                int protocol = (int)request.get("protocol");
                System.out.println("클라이언트 요청  - protocol:" + protocol);
                switch(protocol) {
                    case 1201:
                        response = new Chat(this).chatProc(request);
                        synchronized(accept.clients){
                            for(int i =0;i<accept.clients.size();i++) {
                                if (accept.clients.get(i).roomNum == this.roomNum
                                        && accept.clients.get(i).userIdx != this.userIdx)
                                new SendHash(accept.clients.get(i),response).send();
                            }
                        }
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println(socket.getRemoteSocketAddress()+"클라이언트 종료");
            try {
                in.close();
                out.close();
                oin.close();
                oout.close();
                socket.close();
            } catch (Exception e1) {}
            synchronized(accept.clients) {
                accept.clients.remove(this);
            }
        }
    }
}
