@file:Suppress(
    "NonAsciiCharacters",
    "unused",
    "ClassName",
)

package backend

import backend.Data.expectedGraph
import backend.Data.routes
import backend.Log.log
import kotlin.test.Test
import kotlin.test.assertEquals

internal class `Domain tests` {
    @Test
    fun `list of destinations from routes`() {
        mutableListOf<String>().apply {
            addAll(routes.map { it.origin })
            addAll(routes.map { it.destination })
        }.toSet().run {
            assertEquals(setOf("Tatooine", "Dagobah", "Hoth", "Endor"), this)
            assertEquals(setOf("Tatooine", "Dagobah", "Hoth", "Endor"), routes.destinations)
        }
    }


    @Test
    fun `roadmap function return the destinations map for a list of routes`() = with(routes) {
        expectedGraph.run {
            assertEquals(this, mutableMapOf<String, MutableMap<String, Int>>().apply {
                destinations.forEach { destination -> set(destination, mutableMapOf()) }
                forEach { route: Route ->
                    if (!this[route.origin]!!.containsKey(route.destination))
                        this[route.origin]!![route.destination] = route.travelTime
                    if (!this[route.destination]!!.containsKey(route.origin))
                        this[route.destination]!![route.origin] = route.travelTime
                }
            })
            assertEquals(this, routes.roadmap)
            assertEquals(toString(), routes.roadmap.toString())
        }
    }


    @Test
    fun `graph function`() {
        val roadmap = routes.roadmap
        // construct graph
        val graph = Graph_().apply {
            roadmap.keys.forEach { destination ->
                addNode(Node(name = destination))
            }
            roadmap.keys.forEach { destination ->
                roadmap[destination]!!.map { node ->
                    getNodes()
                        .first { it.name == destination }
                        .addDestination(Node(node.key), node.value)
                }
            }

        }
        assertEquals(graph.toString(), routes.roadmap.graph.toString())
    }

    @Test
    fun `CalculateMinimumDistance function`(){
        val nodeA = Node("A")
        val nodeB = Node("B")
        val nodeC = Node("C")
        val nodeD = Node("D")
        val nodeE = Node("E")
        val nodeF = Node("F")

        nodeA.addDestination(nodeB, 10)
        nodeA.addDestination(nodeC, 15)

        nodeB.addDestination(nodeD, 12)
        nodeB.addDestination(nodeF, 15)

        nodeC.addDestination(nodeE, 10)

        nodeD.addDestination(nodeE, 2)
        nodeD.addDestination(nodeF, 1)

        nodeF.addDestination(nodeE, 5)

        log.info("nodeB.distance avant: ${nodeB.distance}")
        Dijkstra.CalculateMinimumDistance(nodeB,25,nodeA)
        log.info("nodeB.distance apres: ${nodeB.distance}")
        log.info("nodeB.getShortestPath(): ${nodeB.getShortestPath()}")
    }

    @Test
    fun `Dijkstra test`() {

        val nodeA = Node("A")
        val nodeB = Node("B")
        val nodeC = Node("C")
        val nodeD = Node("D")
        val nodeE = Node("E")
        val nodeF = Node("F")

        nodeA.addDestination(nodeB, 10)
        nodeA.addDestination(nodeC, 15)

        nodeB.addDestination(nodeD, 12)
        nodeB.addDestination(nodeF, 15)

        nodeC.addDestination(nodeE, 10)

        nodeD.addDestination(nodeE, 2)
        nodeD.addDestination(nodeF, 1)

        nodeF.addDestination(nodeE, 5)

        var graph = Graph_()

        graph.addNode(nodeA)
        graph.addNode(nodeB)
        graph.addNode(nodeC)
        graph.addNode(nodeD)
        graph.addNode(nodeE)
        graph.addNode(nodeF)

//        graph = Dijkstra.calculateShortestPathFromSource(graph, nodeA)
        log.info("graph: $graph")
//
//        val shortestPathForNodeB = listOf(nodeA)
//        val shortestPathForNodeC = listOf(nodeA)
//        val shortestPathForNodeD = listOf(nodeA, nodeB)
//        val shortestPathForNodeE = listOf(nodeA, nodeB, nodeD)
//        val shortestPathForNodeF = listOf(nodeA, nodeB, nodeD)
//
//        graph.getNodes().forEach { node ->
//            when (node.name) {
//                "B" -> assertEquals(node.getShortestPath(), shortestPathForNodeB)
//                "C" -> assertEquals(node.getShortestPath(), shortestPathForNodeC)
//                "D" -> assertEquals(node.getShortestPath(), shortestPathForNodeD)
//                "E" -> assertEquals(node.getShortestPath(), shortestPathForNodeE)
//                "F" -> assertEquals(node.getShortestPath(), shortestPathForNodeF)
//            }
//        }
    }


    @Test
    @kotlin.test.Ignore
    fun `shortestPath function`() {

        val graph = routes.shortestPath("Tatooine")
            .apply { log.info("graph: $this") }
    }
}


