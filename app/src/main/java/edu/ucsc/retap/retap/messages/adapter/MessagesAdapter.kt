package edu.ucsc.retap.retap.messages.adapter

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import edu.ucsc.retap.retap.messages.model.Message
import edu.ucsc.retap.retap.messages.view.MessageItemViewHolder
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class MessagesAdapter(
        private val layoutInflater: LayoutInflater,
        @LayoutRes private val messageLayoutId: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val itemClickPublishSubject = PublishSubject.create<Int>()
    private val itemsInPlaintextMode = HashSet<Message>()

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
        val messagesViewHolder = viewHolder as MessageItemViewHolder
        messagesViewHolder.setSenderText(item.displayName ?: item.sender)
        messagesViewHolder.setSelected(selectedItemIndex == position)

        if (itemsInPlaintextMode.contains(item)) {
            messagesViewHolder.showAsPlaintext(item.contents)
        } else {
            messagesViewHolder.showAsMorseCode(item.contents)
        }

        messagesViewHolder.itemView.setOnClickListener {
            itemClickPublishSubject.onNext(position)
        }
        messagesViewHolder.itemView.setOnLongClickListener {
            if (itemsInPlaintextMode.contains(item)) {
                messagesViewHolder.showAsMorseCode(item.contents)
                itemsInPlaintextMode.remove(item)
            } else {
                messagesViewHolder.showAsPlaintext(item.contents)
                itemsInPlaintextMode.add(item)
            }
            true
        }
    }

    fun observeItemClick(): Observable<Int> = itemClickPublishSubject
}
