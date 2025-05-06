package com.example.cleanaf

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.cleanaf.ui.theme.CleanAFTheme
import dagger.hilt.android.AndroidEntryPoint

//@AndroidEntryPoint
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        try {
//            setContent {
//                CleanAFTheme {
//                    Surface(color = MaterialTheme.colorScheme.background) {
//                        Greeting("CleanAF")
//                    }
//                }
//            }
//        } catch (e: Exception) {
//            Log.e("APP_CRASH", "App crashed", e)
//            throw e
//        }
//    }
//}
//
//@Composable
//fun Greeting(name: String) {
//    Text(text = "Hello, $name!")
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    CleanAFTheme {
//        Greeting("CleanAF")
//    }
//}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CleanAFTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("CleanAF - UI Only")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello, $name!")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CleanAFTheme {
        Greeting("CleanAF - Preview")
    }
}