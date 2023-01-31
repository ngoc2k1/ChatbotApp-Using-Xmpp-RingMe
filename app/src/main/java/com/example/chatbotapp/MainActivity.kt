package com.example.chatbotapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.chatbotapp.databinding.ActivityMainBinding
import org.jivesoftware.smack.AbstractXMPPConnection
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.chat.ChatManagerListener
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jxmpp.jid.EntityBareJid
import org.jxmpp.jid.impl.JidCreate
import java.util.*
import javax.net.ssl.HostnameVerifier


//Xmpp: 192.168.1.88:5222
//0tbx8pkcaz@vnpost, 8d79bcbd5b893571c47c04596ebf3a5443ebdd2e
//3yaam21qoi@vnpost, 64d15e00b35881b9a26c460c1ab813761ec31e4a

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var messList: ArrayList<MessagesData> = ArrayList()

    private lateinit var messagesAdapter: MessagesAdapter
    private lateinit var chatManager: ChatManager
    private lateinit var mConnection: AbstractXMPPConnection

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        messagesAdapter = MessagesAdapter(this@MainActivity, messList)
        binding.apply {
            rvMainListMess.adapter = messagesAdapter
            btMainConnectUser.setOnClickListener {
                edtMainInputMessage.visibility = View.VISIBLE
                btMainSendMess.visibility = View.VISIBLE
                configXmppMineAndConnectUserSend()
            }
        }
//        chatManager.liste
//            .addChatListener(object : ChatManagerListener {
//                fun chatCreated(
//                    arg0: Chat, arg1: Boolean
//                ) {
//                    Log.d("typing", "chat created: $arg1")
//                    arg0.addMessageListener(object : MessageListener {
//                        override fun processMessage(message: Message?) {
//                            Log.d("typing", " is typing......")
//                        }
//                    })
//                }
//            })
        binding.btMainSendMess.setOnClickListener {
            val messageUser = binding.edtMainInputMessage.text.toString()
            if (messageUser.isNotEmpty()) {
                val jid: EntityBareJid =
                    JidCreate.entityBareFrom(binding.edtMainInputUsernameSend.text.toString())
                val chat: Chat = chatManager.chatWith(jid)
                chat.send(messageUser)
                val data =
                    MessagesData(0, "me", messageUser)
                messList.add(data)
                messagesAdapter.notifyDataSetChanged()
            }
            binding.edtMainInputMessage.text.clear()
        }
        //người gửi đang nhắn thì người nhận luôn hiện isTyping. Khi người gửi đang nhắn rồi k nhắn nữa hoặc ấn Enter thì isTyping ẩn
//        binding.edtMainInputMessage.addTextChangedListener(object : TextWatcher {
//            var timer: Timer? = Timer()
//            override fun afterTextChanged(s: Editable?) {}
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                timer?.cancel()
//                timer = Timer()
//                timer?.schedule(
//                    object : TimerTask() {
//                        override fun run() {
//
//                        }
//                    },
//                    1000
//                )
//            }
//        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun configXmppMineAndConnectUserSend() {
        val verifier = HostnameVerifier { _, _ -> true }
        val config: XMPPTCPConnectionConfiguration =
            XMPPTCPConnectionConfiguration.builder()
                .setXmppAddressAndPassword(
                    binding.edtMainInputUsernameMine.text.toString(),
                    binding.edtMainInputPasswordMine.text.toString()
                ).setHost("192.168.1.88")
                .setPort(5222)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setHostnameVerifier(verifier)
                .build()
        mConnection = XMPPTCPConnection(config)

        try {
            mConnection.connect()
            mConnection.login()
            if (mConnection.isAuthenticated && mConnection.isConnected) {
                chatManager = ChatManager.getInstanceFor(mConnection)//may kia
                chatManager.addIncomingListener { jid, message, chat ->
                    Log.d("ngoc", "onCreate: ${message.body}")
                    Log.d("ngoc", "onCreate: $jid")//nguoi gui den cho mk

                    if (!jid.contains(
                            binding.edtMainInputUsernameMine.text.toString(),
                            ignoreCase = true
                        )
                    ) {
                        val data = MessagesData(1, "you", message.body.toString())
                        messList.add(data)
                    }
                    runOnUiThread {
                        kotlin.run {
                            messagesAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        } catch (_: Exception) {
        }
    }
}
