// MessageController.aidl
package com.example.demoserver;

// Declare any non-default types here with import statements
import com.example.demoserver.SnapMessage;
import java.util.List;
interface MessageController {

    List<SnapMessage> getMessages();
    void sendMessage(inout SnapMessage message);
}