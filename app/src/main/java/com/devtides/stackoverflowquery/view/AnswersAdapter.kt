package com.devtides.stackoverflowquery.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devtides.stackoverflowquery.databinding.AnswerLayoutBinding
import com.devtides.stackoverflowquery.model.Answer

class AnswersAdapter(private val answers: ArrayList<Answer>) :
    RecyclerView.Adapter<AnswersAdapter.AdapterViewHolder>() {

    fun addAnswers(newAnswers: List<Answer>) {
        val currentLength = answers.size
        answers.addAll(newAnswers)
        notifyItemInserted(currentLength)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        return AdapterViewHolder(
            AnswerLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        holder.bind(answers[position])
    }

    override fun getItemCount() = answers.size

    class AdapterViewHolder(binding: AnswerLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        private val title = binding.itemAnswer

        fun bind(answer: Answer) {
            title.text = answer.toString()
        }

    }
}