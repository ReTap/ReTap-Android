package edu.ucsc.retap.retap.conversations

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import edu.ucsc.retap.retap.R
import edu.ucsc.retap.retap.messages.interactor.MessagesInteractor
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.view.KeyEvent
import edu.ucsc.retap.retap.common.BaseActivity
import edu.ucsc.retap.retap.conversations.view.ConversationsViewModule
import edu.ucsc.retap.retap.messages.MessagesActivity
import edu.ucsc.retap.retap.messages.adapter.MessagesAdapter
import edu.ucsc.retap.retap.messages.model.Message
import edu.ucsc.retap.retap.messages.presenter.ConversationsPresenter
import io.reactivex.disposables.CompositeDisposable

class ConversationsActivity : BaseActivity() {
    private companion object {
        private const val SMS_PERMISSION_CODE = 1
        private const val AUTOMATICALLY_SELECT_DELAY_MS = 2000L
    }

    private lateinit var presenter: ConversationsPresenter
    private val compositeDisposable = CompositeDisposable()
    private val handler: Handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<DrawerLayout>(R.id.drawer_layout).setBackgroundResource(android.R.color.white)
        val recyclerView = findViewById<RecyclerView>(R.id.messages_view)
        val adapter = MessagesAdapter(LayoutInflater.from(this), R.layout.item_conversation)
        compositeDisposable
                .add(
                        adapter.observeItemClick()
                                .doOnNext {
                                    navigateToConversation(adapter.items[it])
                                }
                                .subscribe()
                )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        presenter = ConversationsPresenter(ConversationsViewModule(findViewById(R.id.root)),
                adapter, MessagesInteractor(this))
    }

    override fun layoutId(): Int = R.layout.activity_messages

    override fun onStart() {
        super.onStart()
        if (!isSMSPermissionGranted()) {
            requestReadSMSPermission()
        } else {
            presenter.loadMessages()
        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        presenter.cleanUp()
        // Super call always goes last.
        super.onDestroy()
    }

    private fun isSMSPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) ==
                        PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) ==
                        PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            SMS_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter.loadMessages()
                }
                return
            }
        }
    }

    private fun requestReadSMSPermission() {
        ActivityCompat.requestPermissions(this,
                arrayOf(
                        Manifest.permission.READ_SMS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_CONTACTS
                ), SMS_PERMISSION_CODE)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                presenter.onVolumeDown()
                automaticallySelectItem()
                return true
            }
            KeyEvent.KEYCODE_VOLUME_UP -> {
                presenter.onVolumeUp()
                automaticallySelectItem()
                return true
            }
        }

        return super.onKeyDown(keyCode, event)
    }

    private fun automaticallySelectItem() {
        handler.removeCallbacksAndMessages(null)
        val message = presenter.currentSelectedItem() ?: return
        handler.postDelayed(
                {
                    navigateToConversation(message)
                },
                AUTOMATICALLY_SELECT_DELAY_MS
        )
    }

    private fun navigateToConversation(item: Message) {
        val intent = Intent(this, MessagesActivity::class.java)
        val title = item.displayName ?: item.sender
        intent.putExtra(MessagesActivity.EXTRA_PHONE_NUMBER,
                item.sender)
        intent.putExtra(MessagesActivity.EXTRA_TITLE, title)
        startActivity(intent)
    }
}
