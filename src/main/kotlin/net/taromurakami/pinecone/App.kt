package net.taromurakami.pinecone

import io.pinecone.clients.Pinecone
import org.openapitools.db_control.client.model.DeletionProtection
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class PineconeApplication

fun main(args: Array<String>) {
    val logger: Logger = LoggerFactory.getLogger("main")

    logger.info("main function started.")

    val pc: Pinecone =
        System.getenv("PINECONE_API_KEY").let { it: String? ->
            if (it == null) {
                throw Exception("Error: Environment variable PINECONE_API_KEY is not defined")
            }

            Pinecone.Builder(it).build()
        }

    val indexName: String = "quickstart"

    try {
        pc.describeIndex(indexName)
        logger.info("index $indexName already exists.")
    } catch (e: Exception) {
        logger.info("index $indexName doesn't exists.")
        pc.createServerlessIndex(
            indexName,
            "cosine",
            3,
            "aws",
            "us-east-1",
            DeletionProtection.DISABLED,
            null,
        )
        logger.info("index $indexName has created.")
    }

    runApplication<PineconeApplication>(*args)
}
