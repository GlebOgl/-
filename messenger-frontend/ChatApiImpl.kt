package ru.minegoat.blinked.modules.chat.network

import androidx.compose.runtime.internal.isLiveLiteralsEnabled
import io.github.centrifugal.centrifuge.Client
import io.github.centrifugal.centrifuge.ResultCallback
import io.github.centrifugal.centrifuge.Subscription
import io.github.centrifugal.centrifuge.SubscriptionEventListener
import io.reactivex.*

import ru.minegoat.blinked.data.network.chat.model.ChatLinkedObjResponse
import ru.minegoat.blinked.data.network.chat.model.ChatResponse
import ru.minegoat.blinked.modules.chat.network.responses.MessageResponse

class ChatApiImpl : ChatApi {
    private lateinit var client: Client

    fun setClient(newClient: Client){
        this.client = newClient
    }

    override fun getChat(chatId: String): Maybe<ChatResponse> {
        val sub : Subscription? = client.getSubscription(chatId)
        val linkedObj = ChatLinkedObjResponse("linkedObj","type")

        val resp =
            Maybe.create<ChatResponse> { emitter ->
                if(sub == null){
                    emitter.onError(Throwable("sub is null"))
                }
                emitter.onSuccess(ChatResponse("objID",sub!!.channel,"owner", listOf(),linkedObj))
            }
        return resp
    }

    override fun getAllMessages(chatId: String): Single<List<MessageResponse>> {
        TODO("Not yet implemented")
    }

    override fun sendMessage(msg: MessageResponse): Completable {


        return Completable.create {
            val sub = client.getSubscription(msg.chatId)
            val jsonMsg = "{\"msg\": \"${msg.text}\"}" //думаю нужно подключать какую либо библиотеку для генарции json
            sub.publish(jsonMsg.toByteArray(), { e, result ->  })
        }
    }

    override fun subscribeOnNewMessages(chatId: String): Flowable<MessageResponse> {
        return Flowable.create({ emitter ->
            val sub: Subscription? = client.newSubscription(chatId, object : SubscriptionEventListener(){})
            sub?.subscribe()
            emitter.onNext(MessageResponse("id","owner","ownerName",chatId,"subscribed", 0, null))
        },BackpressureStrategy.BUFFER)
    }

    override fun unsubscribeOnNewMessages(chatId: String): Completable {
        return Completable.create {
            client.removeSubscription(client.getSubscription(chatId))
        }
    }

    override fun readMessage(messageId: String, userId: String): Completable {
        TODO("Not yet implemented")
    }

}