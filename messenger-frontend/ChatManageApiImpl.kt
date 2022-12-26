package ru.minegoat.blinked.data.network.chat

import io.github.centrifugal.centrifuge.*
import io.reactivex.Completable
import ru.minegoat.blinked.data.network.chat.model.ChatResponse

class ChatManageApiImpl : ChatManageApi {

    private lateinit var client: Client
    private val subs: MutableList <Subscription> = mutableListOf()

    fun connect(endpoint: String, opts: Options, listener: EventListener){
        client = Client(endpoint, opts, listener)
        client.connect()
    }

    override fun createChat(chatNetwork: ChatResponse): Completable {
        val listener: SubscriptionEventListener = object : SubscriptionEventListener(){} //либо получаем из вне

        return Completable.create {
            val sub: Subscription? = client.newSubscription(chatNetwork.name, listener)
            sub?.subscribe()
        }

    }

    override fun addToChat(chatId: String, userId: String): Completable {
        TODO("Not yet implemented")
    }

    override fun removeFromChat(chatId: String, userId: String): Completable {
        TODO("Not yet implemented")
    }
}