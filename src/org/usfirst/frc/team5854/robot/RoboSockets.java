package org.usfirst.frc.team5854.robot;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class RoboSockets
{
  public static Double recieve(int host)
    throws IOException
  {
    DatagramSocket socket = new DatagramSocket(host);
    byte[] b = new byte[1024];
    DatagramPacket packet = new DatagramPacket(b, b.length);
    socket.receive(packet);
    String str = new String(packet.getData());
    Double angle = Double.valueOf(Double.parseDouble(str));
    socket.close();
    return angle;
  }
  
  public static double getValue()
  {
    try
    {
      return recieve(5800).doubleValue();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return 1001001.0D;
  }
}
