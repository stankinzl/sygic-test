package com.kinzlstanislav.sigyctest.core.extensions

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Exception.isConnectionError() = this is ConnectException ||
            this is UnknownHostException || this is SocketTimeoutException