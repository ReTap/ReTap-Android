package edu.ucsc.retap.retap.messages.presenter

import edu.ucsc.retap.retap.messages.adapter.MessagesAdapter
import edu.ucsc.retap.retap.messages.interactor.MessagesInteractor
import edu.ucsc.retap.retap.messages.view.MessagesViewModule
import edu.ucsc.retap.retap.vibration.VibrationInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MessagesPresenter(
        private val messagesViewModule: MessagesViewModule,
        private val messagesAdapter: MessagesAdapter,
        private val messagesInteractor: MessagesInteractor,
        private val vibrationInteractor: VibrationInteractor) {

    private val compositeDisposable = CompositeDisposable()

    fun loadMessages() {
        messagesViewModule.showLoading()
        compositeDisposable.add(
            messagesInteractor.getSMSMessages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    messagesAdapter.items = it
                    messagesViewModule.hideLoading()
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

    fun onVolumeUp() {
        val newIndex = maxOf(-1, messagesAdapter.selectedItemIndex - 1)
        setItemIndex(newIndex)
    }

    fun onVolumeDown() {
        val newIndex = minOf(messagesAdapter.items.size - 1, messagesAdapter.selectedItemIndex + 1)
        setItemIndex(newIndex)
    }

    fun pauseVibration() {
        setItemIndex(-1)
        vibrationInteractor.stop()
    }

    private fun setItemIndex(index: Int) {
        if (index < 0) {
            vibrationInteractor.stop()
        } else {
            vibrationInteractor.vibrate(messagesAdapter.items[index])
        }
        messagesAdapter.selectedItemIndex = index
    }

    fun cleanUp() {
        compositeDisposable.clear()
    }
}