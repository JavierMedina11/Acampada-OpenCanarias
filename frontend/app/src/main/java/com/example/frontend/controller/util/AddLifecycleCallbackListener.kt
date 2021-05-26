package com.example.frontend.controller.util

import com.example.frontend.controller.models.Persona

interface AddLifecycleCallbackListener {
    fun addLifeCycleCallBack(persona: Persona?)
}