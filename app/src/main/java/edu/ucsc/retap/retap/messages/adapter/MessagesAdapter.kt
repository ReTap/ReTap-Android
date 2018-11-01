package edu.ucsc.retap.retap.messages.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import edu.ucsc.retap.retap.R
import edu.ucsc.retap.retap.messages.model.Message
import edu.ucsc.retap.retap.messages.view.MessageItemViewHolder
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class MessagesAdapter(
        private val layoutInflater: LayoutInflater
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
        val itemView = layoutInflater.inflate(R.layout.item_message, parent, false)
        return MessageItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        val messageItemViewHolder = viewHolder as MessageItemViewHolder
        messageItemViewHolder.setSenderText(item.sender)
        messageItemViewHolder.setContentText(item.contents)
        messageItemViewHolder.setSelected(selectedItemIndex == position)
        messageItemViewHolder.itemView.setOnClickListener {
            itemClickPublishSubject.onNext(position)
        }
    }

    fun observeItemClick(): Observable<Int> = itemClickPublishSubject
}
