package com.example.kisobran

interface PermissionTracker {
    suspend fun provjeriDozvole(): Boolean
}