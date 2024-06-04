package com.demo.oneclickdrivetask.task

import android.app.AlertDialog
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val applicationContext = application.applicationContext
    companion object{
        const val TAG = "TaskViewModel"
    }

    var boxOneData = MutableLiveData<String>()
    var boxTwoData = MutableLiveData<String>()
    var boxThreeData = MutableLiveData<String>()

    var resultMessage = MutableLiveData<String>()




    fun calculate(){
        if (validateInputBox(boxOneData.value) == null
            && validateInputBox(boxTwoData.value) == null
            && validateInputBox(boxThreeData.value) == null){
            try {

                val inputDataSetOne = boxOneData.value.extractNumbersAsSet()
                val inputDataSetTwo = boxTwoData.value.extractNumbersAsSet()
                val inputDataSetThree = boxThreeData.value.extractNumbersAsSet()

                val intersectionSet = inputDataSetOne.intersect(inputDataSetTwo, inputDataSetThree)
                val unionSet = inputDataSetOne.union(inputDataSetTwo, inputDataSetThree)
                val highestNumber = unionSet.highestNumber()
                resultMessage.value = buildResultMessage(intersectionSet,unionSet,highestNumber)

            }catch (e: Exception){
                Log.e(TAG, "calculate: ${e.message}")
            }
        }else{
          Toast.makeText(applicationContext, "Invalid inputs", Toast.LENGTH_SHORT).show()
        }


    }

    fun validateInputBox(it: String?):String?{
        return when {
            it.isNullOrEmpty() -> null
            it.trim().last() == ',' -> "It requires a number after"
            !Regex("^(\\d+(?:,\\s*\\d+)*)?\$").matches(it.trim()) -> "Not valid input."
            else -> null
        }
    }

    private fun String?.extractNumbersAsSet():Set<Int> {
        if (this.isNullOrEmpty()) {
            return emptySet()
        }
        return this.splitToSequence(",")
            .map{it.trim()}
            .map { it.toInt() }
            .toSet()
    }




    private fun buildResultMessage(intersectionResult:Set<Int>, unionResult:Set<Int>, highestNumber:Int):String{
        return "Intersection: $intersectionResult\n" +
                "Union: $unionResult\n" +
                "Highest number: $highestNumber"
    }

    private fun Set<Int>.intersect(setA:Set<Int>,setB:Set<Int>):Set<Int>{
        return this.intersect(setA).intersect(setB)
    }
    private fun Set<Int>.union(setA:Set<Int>,setB:Set<Int>):Set<Int>{
        return this.union(setA).union(setB)
    }
    private fun Set<Int>.highestNumber():Int{
        return this.maxOrNull()?: Int.MIN_VALUE
    }
}


