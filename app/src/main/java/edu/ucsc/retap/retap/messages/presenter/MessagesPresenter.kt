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

                    // Read first message.
                    vibrationInteractor.vibrate(it[0])
                    messagesAdapter.selectedItemIndex = 0
                }
                .subscribe()
        )
    }

    fun cleanUp() {
        compositeDisposable.clear()
    }
}