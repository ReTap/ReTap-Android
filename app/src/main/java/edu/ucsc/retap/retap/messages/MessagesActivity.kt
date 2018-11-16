package edu.ucsc.retap.retap.messages

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import edu.ucsc.retap.retap.R
import edu.ucsc.retap.retap.messages.interactor.MessagesInteractor
import edu.ucsc.retap.retap.messages.presenter.MessagesPresenter
import edu.ucsc.retap.retap.messages.view.MessagesViewModule
import android.os.Vibrator
import android.view.KeyEvent
import android.view.Menu
import edu.ucsc.retap.retap.common.BaseActivity
import edu.ucsc.retap.retap.messages.adapter.MessagesAdapter
import edu.ucsc.retap.retap.vibration.VibrationInteractor

class MessagesActivity : BaseActivity() {
    companion object {
        const val EXTRA_PHONE_NUMBER = "e_phone_number"
        const val EXTRA_TITLE = "e_title"
    }
    private lateinit var presenter: MessagesPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val recyclerView = findViewById<RecyclerView>(R.id.messages_view)
        val adapter = MessagesAdapter(LayoutInflater.from(this), R.layout.item_message)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        supportActionBar?.title = intent.getStringExtra(EXTRA_TITLE) ?: ""

        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val messagesInteractor = MessagesInteractor(this)
        messagesInteractor.filterByPhone = intent.getStringExtra(EXTRA_PHONE_NUMBER)
        presenter = MessagesPresenter(MessagesViewModule(findViewById(R.id.root)), adapter,
            messagesInteractor, VibrationInteractor(vibrator))
    }

    override fun layoutId(): Int = R.layout.activity_messages

    override fun onStart() {
        super.onStart()
        presenter.loadMessages()
    }

    override fun onDestroy() {
        presenter.cleanUp()
        // Super call always goes last.
        super.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                presenter.onVolumeDown()
                return true
            }
            KeyEvent.KEYCODE_VOLUME_UP -> {
                presenter.onVolumeUp()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_messages, menu)

        val pauseAction = menu?.findItem(R.id.action_pause)
        pauseAction?.setOnMenuItemClickListener {
            presenter.pauseVibration()
            return@setOnMenuItemClickListener true
        }

        return super.onCreateOptionsMenu(menu)
    }
}
