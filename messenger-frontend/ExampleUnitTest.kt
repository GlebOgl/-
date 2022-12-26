package ru.minegoat.blinked

import android.annotation.SuppressLint
import android.util.Log
import io.github.centrifugal.centrifuge.*
import org.junit.Test

import org.junit.Assert.*
import ru.minegoat.blinked.data.network.chat.ChatManageApiImpl
import ru.minegoat.blinked.data.network.chat.model.ChatLinkedObjResponse
import ru.minegoat.blinked.data.network.chat.model.ChatResponse
import ru.minegoat.blinked.modules.chat.network.ChatApiImpl
import ru.minegoat.blinked.modules.chat.network.responses.MessageResponse
import java.nio.charset.StandardCharsets

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    val endpoint = "http://test.minegoat.ru:8000/connection/websocket?cf_protocol_version=v2"
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    @Test
    fun createChat(){
        val opts = Options()
        opts.token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyMSIsImV4cCI6MTY1OTQ0MTY4MiwiaWF0IjoxNjU4ODM2ODgyfQ.tyd2_TVq29WZuhh5OBni6dq7Lqry2s8z2PHZavplr7A";
        opts.tokenGetter = object : ConnectionTokenGetter() {
            @Override
            override fun getConnectionToken(event: ConnectionTokenEvent, cb: TokenCallback) {
                // At this place you must request the token from the backend in real app.
                cb.Done(null, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyMSIsImV4cCI6MTY2MDA3MDYzMiwiaWF0IjoxNjU5NDY1ODMyfQ.EWBmBsvbUsOublFJeG0fAMQz_RnX3ZQwd5E00ldyyh0");
            }
        };
        var error = false
        var errorMessage = ""
        val chatLinkedObjResponse = ChatLinkedObjResponse("linkedObj","type")
        val chatResponse = ChatResponse("","chat", "id", listOf(),chatLinkedObjResponse, listOf(),null)
        val listener: EventListener = object : EventListener() {
            @SuppressLint("SetTextI18n")
            override fun onError(client: Client, event: ErrorEvent) {
                errorMessage = event.error.toString()
                error = true
            }
        }
        val chatManage = ChatManageApiImpl()
        chatManage.connect(endpoint,opts,listener)
        chatManage.createChat(chatResponse).subscribe()
        Thread.sleep(10000)
        assertEquals("", errorMessage)
        assertEquals(error, false)
    }

    @Test
    fun sendMessage(){
        val opts = Options()
        opts.token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyMSIsImV4cCI6MTY1OTQ0MTY4MiwiaWF0IjoxNjU4ODM2ODgyfQ.tyd2_TVq29WZuhh5OBni6dq7Lqry2s8z2PHZavplr7A";
        opts.tokenGetter = object : ConnectionTokenGetter() {
            @Override
            override fun getConnectionToken(event: ConnectionTokenEvent, cb: TokenCallback) {
                // At this place you must request the token from the backend in real app.
                cb.Done(null, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyMSIsImV4cCI6MTY2MDA3MDYzMiwiaWF0IjoxNjU5NDY1ODMyfQ.EWBmBsvbUsOublFJeG0fAMQz_RnX3ZQwd5E00ldyyh0");
            }
        };
        var error = false
        var errorMessage = ""
        val chatLinkedObjResponse = ChatLinkedObjResponse("linkedObj","type")
        val chatResponse = ChatResponse("","chat", "id", listOf(),chatLinkedObjResponse, listOf(),null)
        val listener: EventListener = object : EventListener() {
            @SuppressLint("SetTextI18n")
            override fun onError(client: Client, event: ErrorEvent) {
                errorMessage =  event.error.toString()
                error = true
            }
        }
        val chatManage = ChatManageApiImpl()
        chatManage.connect(endpoint,opts,listener)
        chatManage.createChat(chatResponse).subscribe()
        val chatApi = ChatApiImpl()
        chatApi.setClient(Client(endpoint, opts, listener))
        val messageResponse = MessageResponse("objId","ownerID","ownerName","chat", "text",0)
        chatApi.sendMessage(messageResponse)
        Thread.sleep(1000)
        assertEquals("", errorMessage)
        assertEquals(error, false)
    }

    @Test
    fun getChat(){
        val opts = Options()
        opts.token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyMSIsImV4cCI6MTY1OTQ0MTY4MiwiaWF0IjoxNjU4ODM2ODgyfQ.tyd2_TVq29WZuhh5OBni6dq7Lqry2s8z2PHZavplr7A";
        opts.tokenGetter = object : ConnectionTokenGetter() {
            @Override
            override fun getConnectionToken(event: ConnectionTokenEvent, cb: TokenCallback) {
                // At this place you must request the token from the backend in real app.
                cb.Done(null, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyMSIsImV4cCI6MTY2MDA3MDYzMiwiaWF0IjoxNjU5NDY1ODMyfQ.EWBmBsvbUsOublFJeG0fAMQz_RnX3ZQwd5E00ldyyh0");
            }
        };
        var error = false
        var errorMessage = ""
        val chatLinkedObjResponse = ChatLinkedObjResponse("linkedObj","type")
        val chatResponse = ChatResponse("","chat", "id", listOf(),chatLinkedObjResponse, listOf(),null)
        val listener: EventListener = object : EventListener() {
            @SuppressLint("SetTextI18n")
            override fun onError(client: Client, event: ErrorEvent) {
                errorMessage = event.error.toString()
                error = true
            }
        }
        val chatManage = ChatManageApiImpl()
        chatManage.connect(endpoint,opts,listener)
        chatManage.createChat(chatResponse).subscribe()
        val chatApi = ChatApiImpl()
        chatApi.setClient(Client(endpoint, opts, listener))
        chatApi.getChat("chat")
        Thread.sleep(1000)
        assertEquals("", errorMessage)
        assertEquals(error, false)
    }
}