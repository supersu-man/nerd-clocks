package com.supersuman.nerdclocks

import org.junit.Test

import org.junit.Assert.*
import java.util.*
import kotlin.collections.HashSet
import kotlin.math.min


class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        println(rgb())
    }

    fun rgb(): Map<String, MutableList<Int>> {
        var hour = Calendar.getInstance().get(Calendar.HOUR)
        if (hour==0) hour=12
        val minute = Calendar.getInstance().get(Calendar.MINUTE)/5
        val map : Map<String, MutableList<Int>> = mapOf("r" to mutableListOf(),"g" to mutableListOf(),"b" to mutableListOf())
        val h = filterPartitions(hour).random()
        val m = filterPartitions(minute).random()
        val hm = HashSet(h.plus(m))
        if (h.count { it==1 }==2 && m.count { it==1 }==2){
            map["b"]?.add(1)
        }else if (h.count { it==1 }==2){
            map["r"]?.add(1)
        }else if (m.count { it==1 }==2){
            map["g"]?.add(1)
        }
        for (i in hm){
            if (i==0) continue
            if (i in m && i in h ){
                map["b"]?.add(i)
            }else if (i in h && !m.contains(i)){
                map["r"]?.add(i)
            }else if (i in m && !h.contains(i)){
                map["g"]?.add(i)
            }
        }
        return map
    }

    fun filterPartitions(num : Int): MutableList<MutableList<Int>> {
        val arr = partition(num)
        val v = mutableListOf<MutableList<Int>>()
        for (i in arr){
            if (i.count { it==1 }<=2 && i.count { it==2 }<=1 &&
                i.count { it==3 }<=1 && i.count { it==5 }<=1 &&
                !i.contains(4) && !i.contains(6) && !i.contains(7) &&
                !i.contains(8) && !i.contains(9) && !i.contains(10) &&
                !i.contains(11) && !i.contains(12))
                {
                v.add(i)
            }
        }
        return v
    }

    fun partition(num: Int): MutableSet<MutableList<Int>> {
        val partitions = mutableSetOf<MutableList<Int>>()
        partitions.add(mutableListOf(num))
        for(i in 1 until num){
            for (j in partition(num-i)){
                j.add(i)
                j.sort()
                partitions.add(j)
            }
        }
        return partitions
    }
}