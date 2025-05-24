package net.taromurakami.pinecone.presentation

import net.taromurakami.pinecone.domain.SearchResult
import net.taromurakami.pinecone.service.MainService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/vectors")
class MainController(
    private val service: MainService,
) {
    val logger: Logger = LoggerFactory.getLogger(MainController::class.java)

    @PostMapping
    fun insert(
        @RequestBody vector: List<Float>,
    ): ResponseEntity<CreatedResponse> {
        logger.info("POST /vector. Received vector: $vector")
        val res = service.insert(vector)
        return ResponseEntity
            .status(201)
            .body(CreatedResponse(res.id, res.data))
    }

    @GetMapping("search")
    fun search(
        @RequestBody vector: List<Float>,
        @RequestParam(required = false) limit: Int?,
    ): ResponseEntity<SearchResponse> {
        logger.info("GET /vector/search. Received vector: $vector, limit: $limit")
        val searchResults = service.search(vector, limit)
        val res = SearchResponse(searchResults)
        return ResponseEntity.ok(res)
    }

    @PostMapping("random")
    fun insertRandom(): ResponseEntity<CreatedResponse> {
        logger.info("POST /vector/random.")
        val res = service.insertRamdom()
        return ResponseEntity
            .status(201)
            .body(CreatedResponse(res.id, res.data))
    }

    // Data class to wrap the search results
    data class SearchResponse(
        val results: List<SearchResult>,
        val size: Int = results.size,
    )

    data class CreatedResponse(
        val id: String,
        val vector: List<Float>,
    )
}
