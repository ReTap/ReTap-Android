package edu.ucsc.retap.retap.messages.adapter

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import edu.ucsc.retap.retap.conversations.interactor.ContactsHelper
import edu.ucsc.retap.retap.messages.model.Message
import edu.ucsc.retap.retap.messages.view.MessageItemViewHolder
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class MessagesAdapter(
        private val layoutInflater: LayoutInflater,
        @LayoutRes private val messageLayoutId: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val itemClickPublishSubject = PublishSubject.create<Int>()

    var items: List<Message> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var selectedItemIndex = -1
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val itemView = layoutInflater.inflate(messageLayoutId, parent, false)
        return MessageItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        val contact = ContactsHelper.retrieveContactPhoto(viewHolder.itemView.context,
                item.sender)
        val messagesViewHolder = viewHolder as MessageItemViewHolder
        messagesViewHolder.setSenderText(
                if (contact.first == "") item.sender else contact.first)
        messagesViewHolder.setSelected(selectedItemIndex == position)
        messagesViewHolder.setProfileImage(contact.second)
        messagesViewHolder.setContentText(item.contents)
        messagesViewHolder.itemView.setOnClickListener {
            itemClickPublishSubject.onNext(position)
        }
    }

    fun observeItemClick(): Observable<Int> = itemClickPublishSubject
}
