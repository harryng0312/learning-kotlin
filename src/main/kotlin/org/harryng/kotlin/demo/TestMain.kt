package org.harryng.kotlin.demo

import java.math.BigInteger

open class TestClass(var testField: String){
//    private lateinit var testField: String
    private fun testPrivFunc(){}
    protected fun testProtFunc(){}
    public fun testPubFunc(){}

    fun testForLoop() : Int {
        for(i in 6 downTo 1 step 1){
            print(i);
        }
        println("")
        return 0;
    }
    fun testEquals(){
        val authors = setOf("Shakespeare", "Hemingway", "Twain")
        val writers = setOf("Shakespeare", "Hemingway", "Twain")

        println(authors == writers)   // 1
        println(authors === writers)  // 2
    }
}

class TestSubClass(var testSubField: Int) : TestClass("dsafas") {
    fun testSubFunc(){
        testSubField = 1;
        println("from super<TestClass>:$testField");
    }
}
fun operation(): (Int) -> Int {                                     // 1
    return ::square
}

fun square(x: Int) = x * x                                          // 2

fun main(args: Array<String>){
    println("===Kotlin")
    var bigInt: BigInteger = BigInteger.ONE
    println("bigint:$bigInt")
    var tm:TestClass = TestClass("")
    tm.testForLoop()
    tm.testEquals();

    var subClass: TestSubClass = TestSubClass(0);
    subClass.testSubFunc()

//    val func = operation()                                          // 3
//    println("${func.javaClass} $func(2)")

    var x: UInt = 3u
    println("${x.javaClass}")

    val numbers = listOf(1, -2, 3, -4, 5, -6)
    val afterMapNumbers = numbers.map ret@{ x ->
        var result: Int = x * 2
        return@ret result
    }
    println("map:$afterMapNumbers")
}