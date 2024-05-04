package l3m.cyber.planner;

import l3m.cyber.planner.utils.Graphe;
import l3m.cyber.planner.utils.Auxiliaire;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class PlannerApplicationTests {

    @Test
    void testGrapheCreationAndTSP() {
        Double[][] distances = {
                { 0.0, 1.0, 3.0, 4.0 },
                { 1.0, 0.0, 2.0, 3.0 },
                { 3.0, 2.0, 0.0, 5.0 },
                { 4.0, 3.0, 5.0, 0.0 }
        };
        Graphe graphe = new Graphe(distances, Auxiliaire.integerList(4));
        ArrayList<Integer> tour = graphe.tsp(0);

        assertNotNull(tour, "TSP should not return null");
        assertEquals(0, tour.get(0), "TSP should start at the specified start node");
        assertTrue(tour.size() > 1, "TSP should return a valid tour with more than one node");

        // Print the tour for visual verification
        System.out.println("TSP Tour: " + tour);
    }

    @Test
    void testKruskalAlgorithm() {
        Double[][] distances = {
                { 0.0, 1.0, 3.0, 4.0 },
                { 1.0, 0.0, 2.0, 3.0 },
                { 3.0, 2.0, 0.0, 5.0 },
                { 4.0, 3.0, 5.0, 0.0 }
        };
        Graphe graphe = new Graphe(distances, Auxiliaire.integerList(4));
        Graphe mst = graphe.kruskalInverse();

        assertNotNull(mst, "Kruskal should return a valid MST");
        assertTrue(mst.estConnexe(), "The MST should be a connected graph");

        // Display the edges of MST
        System.out.println("MST Edges:");
        mst.listeAretes().forEach(e -> System.out.println(e.getC1() + " - " + e.getC2() + " : " + e.getC3()));
    }
}
