package com.devtides.stackoverflowquery.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devtides.stackoverflowquery.databinding.ActivityDetailBinding
import com.devtides.stackoverflowquery.model.Question
import com.devtides.stackoverflowquery.model.convertDate
import com.devtides.stackoverflowquery.model.convertTitle
import com.devtides.stackoverflowquery.viewmodel.DetailViewModel

class DetailActivity : AppCompatActivity() {


    companion object {
        const val PARAM_QUESTION = "param_question"
        fun getIntent(context: Context, question: Question) =
            Intent(context, DetailActivity::class.java).putExtra(PARAM_QUESTION, question)

    }

    lateinit var binding: ActivityDetailBinding
    var question: Question? = null

    private val viewModel:DetailViewModel by viewModels()
    private val answersAdapter = AnswersAdapter(arrayListOf())
    private val lm = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        question = intent.extras?.getParcelable(PARAM_QUESTION)
        if (question == null) {
            finish()
        }

        populateUI()

        binding.answersList.apply {
            layoutManager = lm
            adapter = answersAdapter
            addOnScrollListener(object:RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if(dy > 0){
                        val childCount = answersAdapter.itemCount
                        val lastPosition = lm.findLastCompletelyVisibleItemPosition()
                        if(childCount -1 == lastPosition && binding.loadingView.visibility == View.GONE){
                            binding.loadingView.visibility = View.VISIBLE
                            viewModel.getNextPage(question!!.questionId)
                        }
                    }
                }
            })
        }

        observeViewModel()
        viewModel.getNextPage(question!!.questionId)

    }

    private fun observeViewModel() {
        viewModel.answersResponse.observe(this){answers ->
            answers?.let {
                binding.answersList.visibility = View.VISIBLE
                answersAdapter.addAnswers(it)

            }

        }
        viewModel.error.observe(this){errorMessage->
            binding.listError.visibility = if(errorMessage == null) View.GONE else View.VISIBLE
            binding.listError.text = errorMessage

        }
        viewModel.loading.observe(this){isLoading ->
            isLoading?.let {
                binding.loadingView.visibility = if(it) View.VISIBLE else View.GONE
                if(it){
                    binding.listError.visibility = View.GONE
                    binding.answersList.visibility = View.GONE

                }
            }
        }

    }

    private fun populateUI() {
        binding.questionScore.text = question!!.score
        binding.questionDate.text = convertDate(question!!.date)
        binding.questionTitle.text = convertTitle(question!!.title)
    }
}