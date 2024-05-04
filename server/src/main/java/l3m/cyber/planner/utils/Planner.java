package l3m.cyber.planner.utils;

import l3m.cyber.planner.requests.PlannerParameter;
import l3m.cyber.planner.responses.PlannerResult;
import java.util.ArrayList;
import java.util.List;

public class Planner {
    private Double[][] distances;
    private int k;
    private int debut;
    private Partition p;
    private ArrayList<ArrayList<Integer>> tournees;
    private ArrayList<Double> longTournees;

    // Constructeur utilisant PlannerParameter pour initialiser les valeurs
    public Planner(PlannerParameter param) {
        this.distances = param.matrix();
        this.k = param.k();
        this.debut = param.start();
        this.p = new PartitionKCentre(distances.length, k);
        this.tournees = new ArrayList<>();
        this.longTournees = new ArrayList<>();
    }

    // Constructeur direct avec paramètres
    public Planner(Double[][] distances, int k, int debut) {
        this.distances = distances;
        this.k = k;
        this.debut = debut;
        this.p = new PartitionKCentre(distances.length, k);
        this.tournees = new ArrayList<>();
        this.longTournees = new ArrayList<>();
    }

    // Partitionne les livraisons entre les livreurs
    public void divise() {
        p.partitionne(distances);

    }

    // Calcule les tournées en utilisant le TSP pour chaque sous-matrice
    public void calculeTournees() {
        for (List<Integer> subset : p.getParties()) {
            ArrayList<Integer> tournee = calculeUneTournee(new ArrayList<>(subset));
            tournees.add(tournee);

        }
        calculeLongTournees();
    }

    // Calcule une tournée individuelle à partir d'un sous-ensemble de sommets
    private ArrayList<Integer> calculeUneTournee(ArrayList<Integer> subset) {
        Graphe graphe = new Graphe(getSousMatrice(subset), subset);
        return graphe.tsp(debut);
    }

    // Extrait la sous-matrice pour les sommets spécifiés
    private Double[][] getSousMatrice(List<Integer> selec) {
        Double[][] sousMatrice = new Double[selec.size()][selec.size()];
        for (int i = 0; i < selec.size(); i++) {
            for (int j = 0; j < selec.size(); j++) {
                sousMatrice[i][j] = distances[selec.get(i)][selec.get(j)];
            }
        }
        return sousMatrice;
    }

    // Calcule la longueur de chaque tournée
    public void calculeLongTournees() {
        longTournees.clear();
        for (ArrayList<Integer> tour : tournees) {
            double length = 0.0;
            for (int i = 0; i < tour.size() - 1; i++) {
                length += distances[tour.get(i)][tour.get(i + 1)];
            }
            if (!tour.isEmpty()) {
                length += distances[tour.get(tour.size() - 1)][tour.get(0)];
            }
            longTournees.add(length);
        }
    }

    // Retourne le résultat final sous forme d'un PlannerResult
    public PlannerResult result() {
        return new PlannerResult(tournees, longTournees);
    }

    @Override
    public String toString() {
        return "Planner{" +
                "distances=" + java.util.Arrays.deepToString(distances) +
                ", k=" + k +
                ", debut=" + debut +
                ", partition=" + p +
                ", tournees=" + tournees +
                ", longTournees=" + longTournees +
                '}';
    }
}
