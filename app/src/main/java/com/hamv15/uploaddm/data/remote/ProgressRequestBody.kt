package com.hamv15.uploaddm.data.remote

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.Buffer
import okio.BufferedSink
import okio.ForwardingSink
import okio.Sink
import okio.buffer


class ProgressRequestBody(
    private val requestBody: RequestBody,
    private val progressListener: (Int) -> Unit
) : RequestBody() {

    override fun contentType(): MediaType? = requestBody.contentType()

    override fun contentLength(): Long = requestBody.contentLength()

    override fun writeTo(sink: BufferedSink) {
        val countingSink = CountingSink(sink)
        val bufferedSink = countingSink.buffer()
        requestBody.writeTo(bufferedSink)
        bufferedSink.flush()
    }

    private inner class CountingSink(delegate: Sink) : ForwardingSink(delegate) {
        private var bytesWritten = 0L

        override fun write(source: Buffer, byteCount: Long) {
            super.write(source, byteCount)
            bytesWritten += byteCount
            val progress = ((bytesWritten.toFloat() / contentLength()) * 100).toInt()
            progressListener(progress)
        }
    }
}