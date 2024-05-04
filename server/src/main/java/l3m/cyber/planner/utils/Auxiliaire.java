package l3m.cyber.planner.utils;

import java.util.ArrayList;

public class Auxiliaire {
    public static ArrayList<Integer> integerList(int n) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(i);
        }
        return list;
    }

    public static <T> boolean estCarree(T[][] matrice) {
        for (T[] ligne : matrice) {
            if (ligne == null || ligne.length != matrice.length) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean estCarreeSym(T[][] matrice) {
        if (!estCarree(matrice)) {
            return false;
        }
        for (int i = 0; i < matrice.length; i++) {
            for (int j = 0; j < i; j++) {
                if (!matrice[i][j].equals(matrice[j][i])) {
                    return false;
                }
            }
        }
        return true;
    }
}
