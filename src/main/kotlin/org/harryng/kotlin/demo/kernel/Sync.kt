package org.harryng.kotlin.demo.kernel

import java.util.concurrent.locks.AbstractQueuedSynchronizer

class Sync : AbstractQueuedSynchronizer(){
    companion object{
        const val ACQUIRED_MODE = 1
        const val RELEASED_MODE = 100
    }
    override fun tryAcquire(arg: Int): Boolean {
        return compareAndSetState(RELEASED_MODE, ACQUIRED_MODE)
    }

    override fun tryRelease(arg: Int): Boolean {
        state = RELEASED_MODE
        return true
    }
}