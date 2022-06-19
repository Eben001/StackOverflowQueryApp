package com.devtides.stackoverflowquery.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devtides.stackoverflowquery.databinding.ActivityMainBinding
import com.devtides.stackoverflowquery.model.Question
import com.devtides.stackoverflowquery.viewmodel.QuestionsViewModel


class MainActivity : AppCompatActivity(), QuestionClickListener {

    private val questionsAdapter = QuestionsAdapter(arrayListOf(), this)
    private val viewModel: QuestionsViewModel by viewModels()
    private val lm = LinearLayoutManager(this)


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.questionsList.apply {
            layoutManager = lm
            adapter = questionsAdapter
            addOnScrollListener(object :RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0){
                        val childCount = questionsAdapter.itemCount
                        val lastPosition = lm.findLastCompletelyVisibleItemPosition()
                        if (childCount - 1 == lastPosition && binding.loadingView.visibility == View.GONE){
                            binding.loadingView.visibility = View.VISIBLE
                            viewModel.getNextpage()
                        }
                    }
                }
            })
        }

        observeViewModel()

        viewModel.getNextpage()

        binding.swipeLayout.setOnRefreshListener {
            questionsAdapter.clearQuestions()
            viewModel.getFirstPage()
            binding.loadingView.visibility = View.VISIBLE
            binding.questionsList.visibility = View.GONE
        }
    }


    private fun observeViewModel() {
        viewModel.questionsResponse.observe(this) { items ->
            items?.let {
                binding.questionsList.visibility = View.VISIBLE
                binding.swipeLayout.isRefreshing = false
                questionsAdapter.addQuestions(it)
            }
        }

        viewModel.error.observe(this) { errorMsg ->
            binding.listError.visibility = if (errorMsg == null) View.GONE else View.VISIBLE
            binding.listError.text = "Error\n$errorMsg"
        }

        viewModel.loading.observe(this) { isLoading ->
            isLoading?.let {
                binding.loadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    binding.listError.visibility = View.GONE
                    binding.questionsList.visibility = View.GONE
                }
            }
        }
    }

    override fun onQuestionClicked(question: Question) {
        startActivity(DetailActivity.getIntent(this, question))
    }
}
