package thread;

import app.ChatServer;
import db.ConnectionDB;

import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class AcceptThread extends Thread{
    ChatServer main;
    private final String DB_HOST = "3.133.92.18";
    private final String DB_NAME = "promise";

    public ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    public ConnectionDB db = new ConnectionDB(DB_HOST, DB_NAME);
    public Connection con = db.getCon();
    public String insertSql = "insert into message(room_idx, user_idx, content) values(?, ?, ?)";
    public PreparedStatement pstmt;

    public AcceptThread(ChatServer main) {
        pstmt = db.getPSTMT(insertSql);
        this.main = main;
    }

    @Override
    public void run() {
        System.out.println("::: 채팅서버 시작 - 접속 대기 :::");
        while(true) {
            String ip="";
            try {
                Socket socket = main.server.accept();
                ip = socket.getRemoteSocketAddress().toString();
                System.out.println("[ 클라이언트 접속 ] - "+socket.getRemoteSocketAddress());
                ClientThread ct = new ClientThread(AcceptThread.this,socket);
                clients.add(ct);
                ct.start();
            } catch (Exception e) {
                System.out.println("[ "+ip+" ] 접속 오류!! ");
            }
        }
    }
}
