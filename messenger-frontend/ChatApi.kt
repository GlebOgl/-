package ru.minegoat.blinked.modules.chat.network

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import ru.minegoat.blinked.data.network.chat.model.ChatResponse
import ru.minegoat.blinked.modules.chat.network.responses.MessageResponse

interface ChatApi {
    fun getChat(chatId: String): Maybe<ChatResponse>
    fun getAllMessages(chatId: String): Single<List<MessageResponse>>
    fun sendMessage(msg: MessageResponse): Completable
    fun subscribeOnNewMessages(chatId: String): Flowable<MessageResponse>
    fun unsubscribeOnNewMessages(chatId: String): Completable
    fun readMessage(messageId: String, userId: String): Completable
}