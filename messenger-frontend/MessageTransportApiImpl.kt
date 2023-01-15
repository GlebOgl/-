package ru.minegoat.blinked.modules.chat.network

import com.google.gson.Gson
import io.github.centrifugal.centrifuge.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import io.reactivex.Flowable
import ru.minegoat.blinked.base.di.modules.MessageTransportModule
import ru.minegoat.blinked.modules.chat.network.responses.MessageResponse
import java.nio.charset.StandardCharsets

class MessageTransportApiImpl(private val client: Client) : MessageTransportApi {

    override fun sendMessage(msg: MessageResponse): Completable {
        return Completable.create {
            val sub = client.getSubscription(msg.chatId)
            try {
                sub.publish(Gson().toJson(msg).toByteArray(), { e, result ->  })
                it.onComplete()
            }
            catch (e: Throwable){
                it.onError(e)
            }
        }
    }

    override fun subscribeOnNewMessages(chatId: String): Flowable<MessageResponse> {
        val listener: EventListener = object : EventListener() {
            override fun onConnected(client: Client, event: ConnectedEvent) {
                System.out.printf("connected with client id %s%n", event.client)
            }

            override fun onConnecting(client: Client, event: ConnectingEvent) {
                System.out.printf("connecting: %s%n", event.reason)
            }

            override fun onDisconnected(client: Client, event: DisconnectedEvent) {
                System.out.printf("disconnected %d %s%n", event.code, event.reason)
            }

            override fun onError(client: Client, event: ErrorEvent) {
                System.out.printf("connection error: %s%n", event.error.toString())
            }

            override fun onMessage(client: Client, event: MessageEvent) {
                val data = String(event.data, StandardCharsets.UTF_8)
                println("message received: $data")
            }

            override fun onSubscribed(client: Client, event: ServerSubscribedEvent) {
                println("server side subscribed: " + event.channel + ", recovered " + event.recovered)
            }

            override fun onSubscribing(client: Client, event: ServerSubscribingEvent) {
                println("server side subscribing: " + event.channel)
            }

            override fun onUnsubscribed(client: Client, event: ServerUnsubscribedEvent) {
                println("server side unsubscribed: " + event.channel)
            }

            override fun onPublication(client: Client, event: ServerPublicationEvent) {
                val data = String(event.data, StandardCharsets.UTF_8)
                println("server side publication: " + event.channel + ": " + data)
            }

            override fun onJoin(client: Client, event: ServerJoinEvent) {
                println("server side join: " + event.channel + " from client " + event.info.client)
            }

            override fun onLeave(client: Client, event: ServerLeaveEvent) {
                println("server side leave: " + event.channel + " from client " + event.info.client)
            }
        }
        val opts = Options()
        opts.token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIzMjIifQ.T37dMzcyGODhrPN4TVwyYXnZ-OvPFy9CovGrh6V4Gvc"
/*        client = Client(
            "ws://test.minegoat.ru:8000/connection/websocket?cf_protocol_version=v2",
            opts,
            listener
        )*/
        client.connect()
        return Flowable.create({ emitter ->
            val lel : Subscription
            val sub: Subscription? = client.newSubscription(chatId, object : SubscriptionEventListener(){
                override fun onPublication(sub: Subscription, event: PublicationEvent) {
                    val data = String(event.data, StandardCharsets.UTF_8)
                    println("message from " + sub.channel + " " + data)
                    val gson = Gson()
                    val messageResponse = Gson().fromJson(data, MessageResponse::class.java)
                    emitter.onNext(messageResponse)
                }

            })
            val f = Subscription::class.java.getDeclaredField("token")
            f.isAccessible = true
            f.set(sub,"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIzMjIiLCJjaGFubmVsIjoicXd3cXdxd3dxdyJ9.rvhZwmrEYY8v05paBQs6ZvPtn8QvqA3nXmr2z-rW60w")
            sub?.subscribe()
        }, BackpressureStrategy.BUFFER)
    }

    override fun unsubscribeOnNewMessages(chatId: String): Completable {
        TODO("Not yet implemented")
    }
}
