package edu.ucsc.retap.retap.conversations.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import edu.ucsc.retap.retap.R
import edu.ucsc.retap.retap.conversations.model.Conversation
import edu.ucsc.retap.retap.conversations.view.ConversationItemViewHolder
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ConversationsAdapter(
        private val layoutInflater: LayoutInflater
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val itemClickPublishSubject = PublishSubject.create<Int>()

    var items: List<Conversation> = ArrayList()
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
        val itemView = layoutInflater.inflate(R.layout.item_conversation, parent, false)
        return ConversationItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        val conversationViewHolder = viewHolder as ConversationItemViewHolder
        conversationViewHolder.setSenderText(item.phoneNumber)
        conversationViewHolder.itemView.setOnClickListener {
            itemClickPublishSubject.onNext(position)
        }
    }

    fun observeItemClick(): Observable<Int> = itemClickPublishSubject
}
