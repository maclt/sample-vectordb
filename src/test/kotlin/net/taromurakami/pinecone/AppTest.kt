package net.taromurakami.pinecone

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

            val expectedLines =
                listOf(
                    "Hello from Kotlin + Maven!",
                )

            // Check that each expected line is in the output
            expectedLines.forEach { line ->
                assertTrue(output.contains(line), "Output should contain: $line")
            }
        } finally {
            // Restore standard output
            System.setOut(standardOut)
        }
    }
}
