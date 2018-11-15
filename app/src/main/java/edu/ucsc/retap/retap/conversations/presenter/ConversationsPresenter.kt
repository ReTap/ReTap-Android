package edu.ucsc.retap.retap.messages.presenter

import edu.ucsc.retap.retap.conversations.adapter.ConversationsAdapter
import edu.ucsc.retap.retap.conversations.model.Conversation
import edu.ucsc.retap.retap.conversations.view.ConversationsViewModule
import edu.ucsc.retap.retap.messages.interactor.MessagesInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ConversationsPresenter(
        private val conversationsViewModule: ConversationsViewModule,
        private val conversationsAdapter: ConversationsAdapter,
        private val messagesInteractor: MessagesInteractor) {

    private val compositeDisposable = CompositeDisposable()

    fun loadMessages() {
        conversationsViewModule.showLoading()
        compositeDisposable.add(
            messagesInteractor.getSMSMessages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    val items = it
                            .map { it.sender }
                            .distinct()
                            .map { Conversation(it) }

                    conversationsAdapter.items = items
                    conversationsViewModule.hideLoading()
                }
                .subscribe()
        )

        compositeDisposable.add(
                conversationsAdapter.observeItemClick()
                        .doOnNext {
                           setItemIndex(it)
                        }
                        .subscribe()
        )
    }

    private fun setItemIndex(index: Int) {
        conversationsAdapter.selectedItemIndex = index
    }

    fun cleanUp() {
        compositeDisposable.clear()
    }
}