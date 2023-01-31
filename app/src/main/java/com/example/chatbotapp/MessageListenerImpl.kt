package com.example.chatbotapp

import android.util.Log
import androidx.core.app.NotificationCompat
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smackx.chatstates.ChatState
import org.jivesoftware.smackx.chatstates.ChatStateListener


class MessageListenerImpl : MessageListener, ChatStateListener {

    override fun processMessage(message: Message?) {
        Log.d("chatstate process", "$message")
    }

    override fun stateChanged(chat: Chat?, state: ChatState?, message: Message?) {
        if (ChatState.composing == state) {
            Log.d("chatstate", " is typing..")
        } else if (ChatState.gone == state) {
            Log.d("chatstate", "left the conversation.")
        } else {
            Log.d("chatstate", "oke")
        }
    }
}