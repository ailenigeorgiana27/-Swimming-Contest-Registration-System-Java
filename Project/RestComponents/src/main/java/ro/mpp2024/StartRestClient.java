package ro.mpp2024;

import ro.mpp2024.domain.Proba;

public class StartRestClient {
    private final static ProbaClient probaClient = new ProbaClient();

    public static void main(String[] args) {
        try {
            show(() -> {
                Iterable<Proba> res = probaClient.getAll();
                System.out.println("Toate probele:");
                for (Proba p : res) {
                    System.out.println(p.getId() + ": " + p.getStil() + " - " + p.getDistanta());
                }

                show(() -> {
                    Proba proba = probaClient.getById(1L);
                    System.out.println("Proba găsită: " + proba.getId() + ": " + proba.getStil() + " - " + proba.getDistanta());
                });
            });

            Proba newProba = new Proba(null, 100, "liber");
            show(() -> {
                Proba added = probaClient.create(newProba);
                System.out.println("Proba adăugată: " + added.getId());
            });

            Proba updated = new Proba(3L, 200, "spate");
            show(() -> System.out.println("Proba actualizată: " + probaClient.update(3L, updated)));

            show(() -> {
                System.out.println("După actualizare:");
                Iterable<Proba> res = probaClient.getAll();
                for (Proba p : res) {
                    System.out.println(p.getId() + ": " + p.getStil() + " - " + p.getDistanta());
                }
            });

            show(() -> {
                System.out.println("Șterg proba cu id=5");
                probaClient.delete(5L);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void show(Runnable task) {
        try {
            task.run();
        } catch (Exception e) {
            System.out.println("Service exception: " + e.getMessage());
        }
    }
}
