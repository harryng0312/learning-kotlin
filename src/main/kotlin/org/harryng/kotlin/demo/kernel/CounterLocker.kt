package org.harryng.kotlin.demo.kernel

class CounterLocker {
    private val sync: Sync = Sync()

    fun lock(){
        sync.acquire(Sync.ACQUIRED_MODE)
    }
    fun unlock(){
        sync.release(Sync.RELEASED_MODE)
    }
}
