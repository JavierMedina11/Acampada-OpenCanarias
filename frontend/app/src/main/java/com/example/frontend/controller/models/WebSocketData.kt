package com.example.frontend.controller.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class WebSocketData (val data: String?)