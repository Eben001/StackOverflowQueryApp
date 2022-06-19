package com.devtides.stackoverflowquery.view

import com.devtides.stackoverflowquery.model.Question

interface QuestionClickListener {
    fun onQuestionClicked(question:Question)
}

