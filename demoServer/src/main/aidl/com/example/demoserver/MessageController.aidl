// MessageController.aidl
package com.example.demoserver;

import com.example.demoserver.SnapMessage;
import java.util.List;
interface MessageController {

    List<SnapMessage> getMessages();
    void sendMessage(inout SnapMessage message);
}