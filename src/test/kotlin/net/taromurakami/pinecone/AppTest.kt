package net.taromurakami.pinecone

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class AppTest {
    @Test
    fun testApp() {
        assertTrue(true)
    }

    @Test
    fun testMainFunction() {
        // Redirect standard output to capture printed content
        val standardOut = System.out
        val outputCaptor = ByteArrayOutputStream()
        System.setOut(PrintStream(outputCaptor))

        try {
            // Call the main function from App.kt
            net.taromurakami.pinecone.main()

            // Check that the expected output was printed
            val output = outputCaptor.toString().trim()
            assertEquals("Hello from Kotlin + Maven!", output)
        } finally {
            // Restore standard output
            System.setOut(standardOut)
        }
    }
}
