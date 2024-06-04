package com.demo.oneclickdrivetask.task

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.demo.oneclickdrivetask.R
import com.demo.oneclickdrivetask.databinding.ActivityTaskBinding
import kotlin.math.log

class TaskActivity : AppCompatActivity() {

    companion object{
        const val TAG = "TaskActivity"
    }

    private lateinit var binding: ActivityTaskBinding
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //binding the layout with viewModel and activity
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task)
        taskViewModel = ViewModelProvider(this, defaultViewModelProviderFactory)[TaskViewModel::class.java]
        binding.taskViewModel = taskViewModel
        binding.lifecycleOwner = this


        // it is only to validate the inputs while inputting
        //It can be removed if not needed
        createInputFieldObservers()

        //It is to show the result on an alertDialog
        taskViewModel.resultMessage.observe(this){
            showResultAlert(it)
        }
    }

    private fun createInputFieldObservers(){

        taskViewModel.boxOneData.observe(this){
            binding.inputBoxLayout1.error = taskViewModel.validateInputBox(it)
        }
        taskViewModel.boxTwoData.observe(this){
            binding.inputBoxLayout2.error = taskViewModel.validateInputBox(it)
        }
        taskViewModel.boxThreeData.observe(this){
            binding.inputBoxLayout3.error = taskViewModel.validateInputBox(it)
        }
    }

    private fun showResultAlert(message: String) {
        val dialogBuilder = AlertDialog.Builder(this)
            .setTitle("Result")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        dialogBuilder.show()
    }

}