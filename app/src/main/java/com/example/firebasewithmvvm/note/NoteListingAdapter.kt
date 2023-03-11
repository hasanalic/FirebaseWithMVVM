package com.example.firebasewithmvvm.note

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasewithmvvm.databinding.ItemNoteLayoutBinding
import com.example.firebasewithmvvm.model.Note

class NoteListingAdapter(
    val onItemClicked: (Int, Note) -> Unit,
    val onEditClicked: (Int, Note) -> Unit,
    val onDeleteClicked: (Int, Note) -> Unit
): RecyclerView.Adapter<NoteListingAdapter.MyViewHolder>() {

    private var list: MutableList<Note> = arrayListOf()

    inner class MyViewHolder(val binding: ItemNoteLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Note) {
            binding.noteIdValue.text = item.id
            binding.msg.text = item.text
            binding.edit.setOnClickListener { onEditClicked.invoke(bindingAdapterPosition,item) }
            binding.delete.setOnClickListener { onDeleteClicked.invoke(bindingAdapterPosition,item) }
            binding.itemLayout.setOnClickListener { onItemClicked.invoke(bindingAdapterPosition,item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = ItemNoteLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    fun removeItem(position: Int) {
        list.removeAt(position)
        notifyDataSetChanged()
    }

    fun updateList(list: MutableList<Note>) {
        this.list = list
        notifyDataSetChanged()
    }
}