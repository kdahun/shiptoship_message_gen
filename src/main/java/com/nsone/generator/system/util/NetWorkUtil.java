package com.nsone.generator.system.util;

import java.net.Inet4Address;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NetWorkUtil {
	//
	public static NetworkInterface findNetworkInterface() {
        NetworkInterface networkInterface = null;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                System.out.println("Find Network Interface: " + ni.getName());
                if (isValidNetworkInterface(ni)) {
                    log.info("Selected Network Interface: " + ni.getName());
                    networkInterface = ni;
                    break;
                }
            }
        } catch (SocketException e) {
        	log.info(e.getMessage());
            return networkInterface;
        }

        if (networkInterface == null) {
        	log.info("No suitable network interface found.");
        }
        return networkInterface;
    }

    private static boolean isValidNetworkInterface(NetworkInterface ni) throws SocketException {
    	//
    	// Get the OS name
        String osName = System.getProperty("os.name").toLowerCase();
        
        boolean isUp = ni.isUp();
        boolean isLoopback = ni.isLoopback();
        boolean isVirtual = ni.isVirtual();
        boolean supportsMulticast = ni.supportsMulticast();
        boolean hasInet4Address = hasValidInet4Address(ni);
        
        if (osName.contains("win")) {
            // Windows-specific logic (if any)
        	log.info("실행환경이 윈도우다.");
        	if(ni.getName().contains("eth")) {
        		return isUp && !isLoopback && !isVirtual && hasInet4Address && supportsMulticast;
        	}else {
        		return false;
        	}
        } else if (osName.contains("mac")) {
            // MacOS-specific logic (if any)
        	log.info("실행환경이 맥OS다.");
        	if(ni.getName().contains("en")) {
        		return isUp && !isLoopback && !isVirtual && hasInet4Address && supportsMulticast;
        	}else {
        		return false;
        	}
        } else if (osName.contains("nux") || osName.contains("nix")) {
            // Linux-specific logic (if any)
        	log.info("실행환경이 리눅스다.");
        	if(ni.getName().contains("enp")) {
        		return isUp && !isLoopback && !isVirtual && hasInet4Address && supportsMulticast;
        	}else {
        		return false;
        	}
        } else {
        	log.info("실행환경이 무슨 환경이냐?");
        	return false;
        }
    }
	
	private static boolean hasValidInet4Address(NetworkInterface ni) {
        List<InterfaceAddress> addresses = ni.getInterfaceAddresses();
        for (InterfaceAddress addr : addresses) {
            if (addr.getAddress() instanceof Inet4Address) {
                return true;
            }
        }
        return false;
    }
}
