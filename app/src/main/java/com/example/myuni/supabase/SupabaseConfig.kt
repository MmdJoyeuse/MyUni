package com.example.myuni.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseConfig {
    const val SUPABASE_URL = "https://kbngbbqzvvnobpsgwcng.supabase.co"
    const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtibmdiYnF6dnZub2Jwc2d3Y25nIiwicm9sZSI6ImFub24iLCJpYXQiOjE3OTAzOTYwMTEsImV4cCI6MjEwNTk3MjAxMX0.6t1qH4Dl1PIve5xj7EksrNkUo3yZG0BUWvfe8COnxzM"

    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_ANON_KEY
        ) {
            install(Postgrest)
        }
    }
}
