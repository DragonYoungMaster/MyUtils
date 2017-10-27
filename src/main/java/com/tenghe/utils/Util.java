package com.tenghe.utils;

import org.apache.log4j.Logger;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * author: teng.he
 * time: 12:03 2017/9/19
 * desc:
 */
public final class Util {
  private static final Logger LOGGER = Logger.getLogger(Util.class);

  private Util() {
  }

  public static String getLocalIp() {
    String ip = null;
    try {
      Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
      while (interfaces.hasMoreElements()) {
        NetworkInterface netif = interfaces.nextElement();
        if (!netif.isUp() || netif.isLoopback() || netif.isVirtual()) {
          continue;
        }
        Enumeration<InetAddress> addresses = netif.getInetAddresses();
        while (addresses.hasMoreElements()) {
          InetAddress addr = addresses.nextElement();
          if (addr.isLoopbackAddress()){
            continue;
          }
          if (addr instanceof Inet4Address) {
            ip = addr.getHostAddress();
          }
        }
      }
    } catch (SocketException ex) {
      LOGGER.error(ex.getMessage(), ex);
    }
    return ip;
  }

}
