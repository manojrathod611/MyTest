package com.example.mytest.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mytest.databinding.ItemTrendingBinding
import com.example.mytest.models.TrendingModel

class TrendingAdapter(
    private val interaction: Interaction? = null,
    private val sList: MutableMap<Int, Boolean>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TrendingModel.Item>() {

        override fun areItemsTheSame(oldItem: TrendingModel.Item, newItem: TrendingModel.Item): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: TrendingModel.Item, newItem: TrendingModel.Item): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            ItemTrendingBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            interaction,
            sList
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MyViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<TrendingModel.Item?>?) {
        differ.submitList(list)
    }

    class MyViewHolder constructor(
        private val viewBinding: ItemTrendingBinding,
        private val interaction: Interaction?,
        private val sList: MutableMap<Int, Boolean>
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(item: TrendingModel.Item) = with(viewBinding) {
            lblFullName.text = item.fullName
            Glide.with(itemView.context).load(item.owner?.avatarUrl)
                .into(imgAvatar)

            if(sList.contains(item.id))
                itemLayout.setCardBackgroundColor(Color.CYAN)
            else
                itemLayout.setCardBackgroundColor(Color.WHITE)

            itemView.setOnClickListener {
                if(sList.get(item.id) == true){
                    itemLayout.setCardBackgroundColor(Color.WHITE)
                    item.isSelected = false
                } else {
                    itemLayout.setCardBackgroundColor(Color.CYAN)
                    item.isSelected = true
                }

                interaction?.onItemSelected(adapterPosition, item)
            }

        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: TrendingModel.Item?)
    }

    private var list = listOf<TrendingModel.Item>()

    fun setData(list: List<TrendingModel.Item?>?){
        this.list = list as List<TrendingModel.Item>
        submitList(list)
    }

    override fun getFilter(): Filter = customFilter

    private val customFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = mutableListOf<TrendingModel.Item>()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(list)
            } else {
                val filterPattern = constraint.toString().lowercase().trim()

                for (item in list) {
                    if (item.fullName?.lowercase()?.contains(filterPattern)!!) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
            submitList(filterResults?.values as List<TrendingModel.Item>?)
        }
    }

}