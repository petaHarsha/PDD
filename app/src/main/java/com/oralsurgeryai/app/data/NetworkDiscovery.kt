package com.oralsurgeryai.app.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

object NetworkDiscovery {
    private const val TAG = "OSAI-Discovery"
    private const val DISCOVERY_PORT = 8001
    private const val DISCOVERY_MSG = "DISCOVER_OSAI_SERVER"
    private const val TIMEOUT_MS = 2000

    /**
     * Broadcasts a UDP message to find the laptop's IP address.
     * Includes 3 retry attempts for hotspot stability.
     */
    suspend fun discoverServerIp(): String? = withContext(Dispatchers.IO) {
        // QUICK CHECK: If on Emulator, bias towards 10.0.2.2
        if (android.os.Build.PRODUCT.contains("sdk") || android.os.Build.MODEL.contains("Emulator")) {
            Log.d(TAG, "Emulator detected, using 10.0.2.2 as fallback")
        }

        var socket: DatagramSocket? = null
        try {
            socket = DatagramSocket()
            socket.broadcast = true
            socket.soTimeout = TIMEOUT_MS

            val sendData = DISCOVERY_MSG.toByteArray()
            val sendPacket = DatagramPacket(
                sendData, 
                sendData.size, 
                InetAddress.getByName("255.255.255.255"), 
                DISCOVERY_PORT
            )

            // Retry loop for Hotspot latency
            for (attempt in 1..3) {
                try {
                    Log.d(TAG, "Broadcasting discovery signal (Attempt $attempt)...")
                    socket.send(sendPacket)

                    val receiveData = ByteArray(1024)
                    val receivePacket = DatagramPacket(receiveData, receiveData.size)
                    
                    socket.receive(receivePacket)
                    
                    val message = String(receivePacket.data, 0, receivePacket.length)
                    Log.d(TAG, "Received response: $message from ${receivePacket.address.hostAddress}")

                    if (message.startsWith("OSAI_SERVER_IP:")) {
                        val ip = message.substringAfter("OSAI_SERVER_IP:")
                        return@withContext if (ip == "127.0.0.1") "10.0.2.2" else ip
                    }
                } catch (timeout: java.net.SocketTimeoutException) {
                    Log.w(TAG, "Attempt $attempt timed out.")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Discovery failed: ${e.message}")
        } finally {
            socket?.close()
        }
        return@withContext null
    }
}
