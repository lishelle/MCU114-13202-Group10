package com.example.homework5

import android.os.Handler
import android.os.HandlerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WorkViewModel : ViewModel() {
    private val handlerThread = HandlerThread("VM-Work").apply { start() }
    private val worker = Handler(handlerThread.looper)

    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> = _progress

    private val _status = MutableLiveData("Idle")
    val status: LiveData<String> = _status

    @Volatile private var running = false

    fun start() {
        if (running) return
        running = true
        _status.postValue("準備中")
        _progress.postValue(0)

        worker.post {
            try {
                Thread.sleep(2000) // 模擬準備時間
                _status.postValue("工作中")

                for (i in 1..100) {
                    if (!running) break
                    Thread.sleep(100)
                    _progress.postValue(i)
                }

                _status.postValue(if (running) "背景工作結束" else "背景工作中斷")
                running = false

            } catch (_: InterruptedException) {
                _status.postValue("背景工作中斷")
                running = false
            }
        }
    }

    fun cancel() {
        running = false
        _status.postValue("背景工作中斷")
    }

    override fun onCleared() {
        running = false
        handlerThread.quitSafely()
        super.onCleared()
    }
}
