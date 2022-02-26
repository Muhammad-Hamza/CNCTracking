package com.example.cnctracking_2.util

class NumberUtility
{
    companion object{
        fun roundOff(x: Float, position: Int): Float
        {
            var a = x
            val temp = Math.pow(10.0, position.toDouble())
            a *= temp.toFloat()
            a = Math.round(a).toFloat()
            return a / temp.toFloat()
        }
    }

}