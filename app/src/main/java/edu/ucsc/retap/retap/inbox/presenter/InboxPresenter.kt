package edu.ucsc.retap.retap.inbox.presenter

import edu.ucsc.retap.retap.messages.adapter.MessagesAdapter
import edu.ucsc.retap.retap.messages.interactor.MessagesInteractor
import edu.ucsc.retap.retap.messages.model.Message
import edu.ucsc.retap.retap.messages.view.MessagesViewModule
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class InboxPresenter(
        private val messageViewModule: MessagesViewModule,
        private val messagesAdapter: MessagesAdapter,
        private val messagesInteractor: MessagesInteractor) {

    private val compositeDisposable = CompositeDisposable()

    fun loadMessages() {
        messageViewModule.showLoading()
        compositeDisposable.add(
                messagesInteractor.getSMSMessages()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSuccess {
                            val items = it
                                    .distinctBy {
                                        it.sender
                                    }

                            messagesAdapter.items = items
                            messageViewModule.hideLoading()
                        }
                        .subscribe()
        )

        compositeDisposable.add(
                messagesAdapter.observeItemClick()
                        .doOnNext {
                            setItemIndex(it)
                        }
                        .subscribe()
        )
    }

    private fun setItemIndex(index: Int) {
        messagesAdapter.selectedItemIndex = index
    }

    fun cleanUp() {
        compositeDisposable.clear()
    }

    fun onVolumeUp() {
        val newIndex = maxOf(-1, messagesAdapter.selectedItemIndex - 1)
        setItemIndex(newIndex)
    }

    fun onVolumeDown() {
        val newIndex = minOf(messagesAdapter.items.size - 1, messagesAdapter.selectedItemIndex + 1)
        setItemIndex(newIndex)
    }

    fun currentSelectedItem(): Message? {
        val selectedIndex = messagesAdapter.selectedItemIndex
        if (selectedIndex < 0 || selectedIndex >= messagesAdapter.itemCount) {
            return null
        }
        return messagesAdapter.items[selectedIndex]
    }
}
