@file:Suppress(
    "unused",
    "SpellCheckingInspection",
    "UnusedReceiverParameter",
)

package backend


import backend.Log.log
import com.fasterxml.jackson.annotation.JsonProperty
import java.lang.Integer.MAX_VALUE
import java.util.*

object Dijkstra {
    fun calculateShortestPathFromSource(graph: Graph_, source: Node): Graph_ {
        source.distance = 0

        val settledNodes = mutableSetOf<Node>()
        val unsettledNodes = mutableSetOf(source)
        while (unsettledNodes.isNotEmpty()) {
            val currentNode = getLowestDistanceNode(unsettledNodes)!!
            unsettledNodes.remove(currentNode)
//            for (adjacencyPair in currentNode.getAdjacentNodes().entries) {
//                log.info("adjacencyPair: $adjacencyPair")
//                val adjacentNode = adjacencyPair.key
//                val edgeWeigh = adjacencyPair.value
//                if (!settledNodes.contains(adjacentNode)) {
//                    CalculateMinimumDistance(adjacentNode, edgeWeigh, currentNode)
////                    unsettledNodes.add(adjacentNode)
//                }
//            }
            settledNodes.add(currentNode)
        }
        log.info(settledNodes)
        return graph
    }

     fun CalculateMinimumDistance(
        evaluationNode: Node,
        edgeWeigh: Int,
        sourceNode: Node
    ) {
        val sourceDistance = sourceNode.distance
        if (sourceDistance + edgeWeigh < evaluationNode.distance) {
            evaluationNode.distance = sourceDistance + edgeWeigh
//            val shortestPath: MutableList<Node> = sourceNode.getShortestPath()
//            shortestPath.add(sourceNode)
//            evaluationNode.setShortestPath(shortestPath)
        }
    }

    private fun getLowestDistanceNode(unsettledNodes: MutableSet<Node>): Node? {
        var lowestDistanceNode: Node? = null
        var lowestDistance = MAX_VALUE
        for (node in unsettledNodes) {
            val nodeDistance = node.distance
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance
                lowestDistanceNode = node
            }
        }
        return lowestDistanceNode
    }

}

/*public class Dijkstra {

    public static Graph_ calculateShortestPathFromSource(Graph_ graph, Node source) {

        source.setDistance(0);

        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();
        unsettledNodes.add(source);

        while (unsettledNodes.size() != 0) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Entry<Node, Integer> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacencyPair.getKey();
                Integer edgeWeigh = adjacencyPair.getValue();

                if (!settledNodes.contains(adjacentNode)) {
                    CalculateMinimumDistance(adjacentNode, edgeWeigh, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        return graph;
    }

    private static void CalculateMinimumDistance(Node evaluationNode, Integer edgeWeigh, Node sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

    private static Node getLowestDistanceNode(Set<Node> unsettledNodes) {
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Node node : unsettledNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }
}
 */
private fun getLowestDistanceNode(unsettledNodes: Set<Node>): Node {
    var lowestDistance = MAX_VALUE
    lateinit var lowestDistanceNode: Node
    unsettledNodes.forEach { node: Node ->
        val nodeDistance = node.distance
        if (nodeDistance < lowestDistance) {
            lowestDistance = nodeDistance
            lowestDistanceNode = node
        }
    }
    return lowestDistanceNode
}

/*=================================================================================*/
fun List<Route>.shortestPath(source: String): Graph_ = roadmap
    .graph
    .apply {
        val settledNodes: MutableSet<Node> = HashSet<Node>()
        val unsettledNodes: MutableSet<Node> = HashSet<Node>()
        unsettledNodes.add(getNodes().first { it.name == source }.apply { distance = 0 })
        while (unsettledNodes.isNotEmpty()) {
            val currentNode: Node = getLowestDistanceNode(unsettledNodes)
            unsettledNodes.remove(currentNode)
            currentNode.getAdjacentNodes().entries.forEach { adjacencyPair ->
                log.info(adjacencyPair)
                val adjacentNode: Node = adjacencyPair.key
                val edgeWeigh: Int = adjacencyPair.value
                if (!settledNodes.contains(adjacentNode)) {
//                calculateMinimumDistance(adjacentNode, edgeWeigh, currentNode)
                    unsettledNodes.add(adjacentNode)
                }
            }
            settledNodes.add(currentNode)
        }
    }


/*=================================================================================*/
private fun calculateMinimumDistance(evaluationNode: Node, edgeWeigh: Int, sourceNode: Node) {
    with(sourceNode.distance) {
        if (this + edgeWeigh < evaluationNode.distance)
            LinkedList(sourceNode.getShortestPath()).apply {
                add(sourceNode)
                evaluationNode.apply {
                    distance = this@with + edgeWeigh
                }.setShortestPath(this)
            }
    }
}


/*=================================================================================*/
data class Graph_(private var nodes: MutableSet<Node> = HashSet()) {
    fun addNode(node: Node) = nodes.add(node)
    fun getNodes(): Set<Node> = nodes
    fun setNodes(nodes: MutableSet<Node>) {
        this.nodes = nodes
    }
}
/*=================================================================================*/

data class Node(
    val name: String,
    var distance: Int = MAX_VALUE,
    private var shortestPath: MutableList<Node> = mutableListOf(),
    private var adjacentNodes: MutableMap<Node, Int> = mutableMapOf()
) {
    fun addDestination(destination: Node, distance: Int) {
        adjacentNodes[destination] = distance
    }

    fun getAdjacentNodes(): MutableMap<Node, Int> = adjacentNodes

    fun setAdjacentNodes(adjacentNodes: MutableMap<Node, Int>) {
        this.adjacentNodes = adjacentNodes
    }

    fun getShortestPath(): MutableList<Node> = shortestPath

    fun setShortestPath(shortestPath: MutableList<Node>) {
        this.shortestPath = shortestPath
    }
}

/*=================================================================================*/
val List<Route>.destinations: Set<String>
    get() = mutableListOf<String>().apply {
        addAll(this@destinations.map { it.origin })
        addAll(this@destinations.map { it.destination })
    }.toSet()


/*=================================================================================*/
val List<Route>.roadmap: MutableMap<String, MutableMap<String, Int>>
    get() = mutableMapOf<String, MutableMap<String, Int>>().apply {
        destinations.forEach { destination -> set(destination, mutableMapOf()) }
        forEach { route: Route ->
            if (!this[route.origin]!!.containsKey(route.destination))
                this[route.origin]!![route.destination] = route.travelTime
            if (!this[route.destination]!!.containsKey(route.origin))
                this[route.destination]!![route.origin] = route.travelTime
        }
    }
/*=================================================================================*/

val MutableMap<String, MutableMap<String, Int>>.graph: Graph_
    get() = Graph_().apply {
        with(keys) {
            forEach { destination -> addNode(Node(name = destination)) }
            forEach { destination ->
                this@graph[destination]?.forEach { node ->
                    getNodes()
                        .first { it.name == destination }
                        .addDestination(Node(node.key), node.value)
                }
            }
        }
    }


/*=================================================================================*/
data class Route(
    val origin: String,
    val destination: String,
    @JsonProperty("travel_time")
    val travelTime: Int
)

/*=================================================================================*/
data class ComputerConfig(
    val autonomy: Int,
    val departure: String,
    val arrival: String,
    @JsonProperty("routes_db")
    val routesDb: String
)

/*=================================================================================*/
data class Answer(val odds: Double)

/*=================================================================================*/
data class Empire(
    val countdown: Int,
    @JsonProperty("bounty_hunters")
    val bountyHunters: List<BountyHunter>
)

/*=================================================================================*/
data class BountyHunter(
    val planet: String,
    val day: Int
)
/*=================================================================================*/
