package com.devtides.stackoverflowquery.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devtides.stackoverflowquery.databinding.QuestionLayoutBinding
import com.devtides.stackoverflowquery.model.Question
import com.devtides.stackoverflowquery.model.convertDate
import com.devtides.stackoverflowquery.model.convertTitle

class QuestionsAdapter(val questions: ArrayList<Question>, private val listener: QuestionClickListener) :
    RecyclerView.Adapter<QuestionsAdapter.AdapterViewHolder>() {

    fun addQuestions(newQuestions: List<Question>) {
        val currentLength = questions.size
        questions.addAll(newQuestions)
        notifyItemInserted(currentLength)
    }

    fun clearQuestions() {
        questions.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AdapterViewHolder(
            QuestionLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            listener
        )


    override fun getItemCount() = questions.size

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        holder.bind(questions[position])
    }

    class AdapterViewHolder(binding: QuestionLayoutBinding, private val listener: QuestionClickListener) :
        RecyclerView.ViewHolder(binding.root) {

        val layout = binding.itemLayout
        val title = binding.itemTitle
        val score = binding.itemScore
        val date = binding.itemDate

        fun bind(question: Question) {
            title.text = convertTitle(question.title)
            score.text = question.score
            date.text = convertDate(question.date)
            layout.setOnClickListener { listener.onQuestionClicked(question) }
        }


    }

}