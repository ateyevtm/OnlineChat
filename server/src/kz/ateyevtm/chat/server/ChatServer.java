package kz.ateyevtm.chat.server;

import kz.ateyevtm.network.TCPConnection;
import kz.ateyevtm.network.TCPConnectionListner;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements TCPConnectionListner {
    public static void main(String[] args){
        new ChatServer();
    }
    private  final ArrayList <TCPConnection> connections = new ArrayList<>();
    private ChatServer(){
        System.out.println("Server running...");
        try (ServerSocket serverSocket = new ServerSocket(8119)){
            while (true){
                try{
                    new TCPConnection(this,serverSocket.accept());
                } catch (IOException e){
                    System.out.println("TCPConn exc: " + e);
                }
            }
        }
        catch (IOException e){
            throw  new RuntimeException(e);
        }
    }

    @Override
    public synchronized void  onConnnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        System.out.println("Client connection: " + tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        sendToAllConnections(value);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        System.out.println("Client disc: " + tcpConnection);
        connections.remove(tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnecttion exeption: " + e);
    }
    private void sendToAllConnections(String value){
        System.out.println(value);
        final int cnt = connections.size();
        for (int i = 0; i < cnt; i++) {
            connections.get(i).sentString(value);
        }
    }
}
