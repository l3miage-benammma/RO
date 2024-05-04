package l3m.cyber.planner.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Graphe implements Cloneable {
    private int nbSommets;
    private int[][] adj;
    private Double[][] poidsA;
    private ArrayList<Integer> nomSommets;

    // Constructors
    public Graphe(int n) {
        this.nbSommets = n;
        this.adj = new int[nbSommets][nbSommets];
        this.poidsA = new Double[nbSommets][nbSommets];
        this.nomSommets = Auxiliaire.integerList(nbSommets);
    }

    public Graphe(Double[][] poidsA, ArrayList<Integer> nomSommets) {
        this.nbSommets = nomSommets.size();
        this.poidsA = poidsA;
        this.nomSommets = nomSommets;
        this.adj = new int[nbSommets][nbSommets];
        for (int i = 0; i < nbSommets; i++) {
            for (int j = 0; j < nbSommets; j++) {
                if (poidsA[i][j] != null && poidsA[i][j] > 0) {
                    adj[i][j] = 1;
                }
            }
        }
    }

    public Graphe(int[][] adj, ArrayList<Integer> nomSommets) {
        this.nbSommets = nomSommets.size();
        this.adj = adj;
        this.poidsA = new Double[nbSommets][nbSommets];
        this.nomSommets = new ArrayList<>(nomSommets);
        pondereAretes();
    }

    public void pondereAretes() {
        for (int i = 0; i < nbSommets; i++) {
            for (int j = 0; j < nbSommets; j++) {
                if (adj[i][j] == 1 && poidsA[i][j] == null) {
                    poidsA[i][j] = 1.0;
                }
            }
        }
    }

    public void ajouterArete(int i, int j) {
        adj[i][j] = 1;
        adj[j][i] = 1; // Graphe non orienté
    }

    public void ajouterArete(int i, int j, double poids) {
        adj[i][j] = 1;
        adj[j][i] = 1;
        poidsA[i][j] = poids;
        poidsA[j][i] = poids;
    }

    public void retirerArete(int i, int j) {
        adj[i][j] = 0;
        adj[j][i] = 0;
        poidsA[i][j] = null;
        poidsA[j][i] = null;
    }

    public void ajusterPoids(int i, int j, double poids) {
        if (adj[i][j] == 1) {
            poidsA[i][j] = poids;
            poidsA[j][i] = poids;
        }
    }

    public boolean voisins(int i, int j) {
        return adj[i][j] == 1;
    }

    public boolean estConnexe() {
        ArrayList<Integer> resultatParcours = parcoursProfondeur(0);
        return resultatParcours.size() == nbSommets; // si la longueur de la liste est egale au nombre de sommets alors
                                                     // true.
    }

    public ArrayList<Integer> parcoursProfondeur(int debut) {
        ArrayList<Integer> visited = new ArrayList<>(); // Créez une liste pour stocker les sommets visités
        Stack<Integer> stack = new Stack<>();
        stack.push(debut);

        while (!stack.isEmpty()) {
            int sommet = stack.pop();
            if (!visited.contains(sommet)) { // Si ce sommet n'a pas encore été visité
                visited.add(sommet); // Marquez-le comme visité
                for (int i = nbSommets - 1; i >= 0; i--) { // Parcourez les voisins de ce sommet dans l'ordre inverse
                    if (adj[sommet][i] == 1 && !visited.contains(i)) { // Si le voisin est connecté et non visité
                        stack.push(i);
                    }
                }
            }
        }
        return visited; // Retournez la liste des sommets visités
    }

    public ArrayList<Integer> parcoursLargeur(int debut) {
        boolean[] visite = new boolean[nbSommets];
        ArrayList<Integer> parcours = new ArrayList<>();
        ArrayList<Integer> queue = new ArrayList<>();
        queue.add(debut);
        visite[debut] = true;

        while (!queue.isEmpty()) {
            int sommet = queue.remove(0);
            parcours.add(sommet);
            for (int i = 0; i < nbSommets; i++) {
                if (adj[sommet][i] == 1 && !visite[i]) {
                    visite[i] = true;
                    queue.add(i);
                }
            }
        }
        return parcours;
    }

    public List<Triplet> listeAretes() {
        List<Triplet> aretes = new ArrayList<>();
        for (int i = 0; i < nbSommets; i++) {
            for (int j = 0; j < nbSommets; j++) {
                if (adj[i][j] == 1) {
                    aretes.add(new Triplet(i, j, poidsA[i][j]));
                }
            }
        }
        return aretes;
    }

    public List<Triplet> aretesTriees(boolean croissant) {
        List<Triplet> aretes = listeAretes();
        if (croissant) {
            Collections.sort(aretes);
        } else {
            Collections.sort(aretes, Collections.reverseOrder());
        }
        return aretes;
    }

    public Graphe kruskalInverse() {
        Graphe T = new Graphe(nbSommets); // Création d'un nouveau graphe vide.

        // Ajouter toutes les arêtes du graphe actuel à T
        for (int i = 0; i < nbSommets; i++) {
            for (int j = 0; j < nbSommets; j++) {
                if (adj[i][j] == 1) { // S'assurer que l'arête existe dans le graphe original.
                    T.ajouterArete(i, j, poidsA[i][j]); // Utiliser ajouterArete pour copier l'arête.
                }
            }
        }

        List<Triplet> aretes = T.aretesTriees(false); // Obtenir les arêtes triées par poids décroissant.
        for (Triplet arete : aretes) {
            T.retirerArete(arete.getC1(), arete.getC2()); // Retirer l'arête.
            if (!T.estConnexe()) { // Vérifier si le graphe reste connexe.
                T.ajouterArete(arete.getC1(), arete.getC2(), arete.getC3()); // Remettre l'arête si nécessaire.
            }
        }
        return T;
    }

    public ArrayList<Integer> tsp(int debut) {
        Graphe mst = Kruskal();
        return mst.parcoursProfondeur(debut);
    }

    public Graphe Kruskal() {
        UnionFind uf = new UnionFind(nbSommets);
        Graphe arbreCouvrant = new Graphe(nbSommets);
        List<Triplet> aretes = aretesTriees(true);

        for (Triplet arete : aretes) {
            if (uf.find(arete.getC1()) != uf.find(arete.getC2())) {
                arbreCouvrant.ajouterArete(arete.getC1(), arete.getC2(), arete.getC3());
                uf.union(arete.getC1(), arete.getC2());
            }
        }
        return arbreCouvrant;
    }

    @Override
    protected Object clone() {
        try {
            Graphe clone = (Graphe) super.clone();
            clone.adj = new int[nbSommets][];
            clone.poidsA = new Double[nbSommets][];
            for (int i = 0; i < nbSommets; i++) {
                clone.adj[i] = adj[i].clone();
                clone.poidsA[i] = poidsA[i].clone();
            }
            clone.nomSommets = new ArrayList<>(nomSommets);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Should not happen
        }
    }

    @Override
    public String toString() {
        return "Graphe{" +
                "nbSommets=" + nbSommets +
                ", adj=" + Arrays.deepToString(adj) +
                ", poidsA=" + Arrays.deepToString(poidsA) +
                ", nomSommets=" + nomSommets +
                '}';
    }

    public int getNbSommets() {
        return nbSommets;
    }

    public int[][] getAdj() {
        return adj;
    }

    public Double[][] getPoidsA() {
        return poidsA;
    }

    public ArrayList<Integer> getNomSommets() {
        return nomSommets;
    }

    public void setNomSommets(ArrayList<Integer> nomSommets) {
        this.nomSommets = nomSommets;
    }

    public void setPoidsA(Double[][] poidsA) {
        this.poidsA = poidsA;
    }

    public void setAdj(int[][] adj) {
        this.adj = adj;
    }

    public void setNbSommets(int nbSommets) {
        this.nbSommets = nbSommets;
    }

}
