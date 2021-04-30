package com.asynctaskcoffee.audiorecorder.worker

import android.media.MediaRecorder
import android.os.Environment
import java.io.IOException
import java.util.*

class Recorder(audioRecordListener: AudioRecordListener?) {

    private var recorder: MediaRecorder? = null
    private var audioRecordListener: AudioRecordListener? = null

    private var outputFile: String? = null

    private var isRecording = false

    fun setOutputFile(fileName: String?) {
        this.outputFile = fileName
    }

    fun startRecord() {
        recorder = MediaRecorder()
        recorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        recorder!!.setOutputFile(outputFile)
        recorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        try {
            recorder!!.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
            reflectError(e.toString())
            return
        }
        recorder!!.start()
        isRecording = true
    }

    fun reset() {
        if (recorder != null) {
            recorder!!.release()
            recorder = null
            isRecording = false
        }
    }

    fun stopRecording() {
        try {
            Thread.sleep(150)
            recorder!!.stop()
            recorder!!.release()
            recorder = null
            reflectRecord(outputFile)
        } catch (e: Exception) {
            e.printStackTrace()
            reflectError(e.toString())
        }
    }

    private fun reflectError(error: String?) {
        audioRecordListener?.onRecordFailed(error)
        isRecording = false
    }

    private fun reflectRecord(uri: String?) {
        audioRecordListener?.onAudioReady(uri)
        isRecording = false
    }

    init {
        this.audioRecordListener = audioRecordListener
    }
}