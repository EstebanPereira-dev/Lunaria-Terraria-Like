package universite_paris8.iut.epereira.lunaria.modele;

public class InventaireJoueur {
    private Item[] listeditem;
    private int[] quantite; // Quantité pour chaque slot
    private int taille;

    public InventaireJoueur() {
        taille = 9;
        listeditem = new Item[taille];
        quantite = new int[taille];
    }

    public int trouverPremiereCaseVide() {
        for (int i = 0; i < listeditem.length; i++) {
            if (listeditem[i] == null)
                return i;
        }
        return -1; // inventaire plein
    }

    // Trouve la première case contenant un item stackable du même type
    public int trouverCaseItem(Item item) {
        //-1 si l'item est pas stackable
        if (item == null || !item.isEstStackable()) return -1;

        for (int i = 0; i < listeditem.length; i++) {
            if (listeditem[i] != null && listeditem[i].getId()==(item.getId()) &&
                    quantite[i] < item.getStackMax()) {
                System.out.println("i");
                return i;
            }
        }
        System.out.println("-1");
        //-1 si l'item n'est pas dans l'inventaire
        return -1;
    }

    // Ajoute un item en gérant le stacking automatiquement
    //pour le moment, ne gère pas le surplus
    public void ajouterItem(Item item, int quantite) {
        if (item != null && quantite > 0) {
            // Si l'item est stackable, essayer de le combiner avec des items existants
            if (item.isEstStackable()) {
                int caseItem = trouverCaseItem(item);
                if (caseItem != -1) { //Si l'item est présent dans l'inv
                    System.out.println("entré ici");
                    this.quantite[caseItem] += quantite;
                } else {
                    int caseLibre = trouverPremiereCaseVide();
                    System.out.println("ne pas entrer ici svp");
                    if (caseLibre != -1) {
                        listeditem[caseLibre] = item;
                        this.quantite[caseLibre] = quantite;
                    }
                }
            } else { // si c'est pas stackable
                int caseLibre = trouverPremiereCaseVide();
                if (caseLibre != -1) {
                    listeditem[caseLibre] = item;
                    this.quantite[caseLibre] = 1;
                }
            }
        }
    }

    //enlève une quantité d'items à l'emplacement demandé
    public void retirerItem(int pos, int quantite) {
        if (pos >= 0 && pos < taille && listeditem[pos] != null && quantite > 0) {
            this.quantite[pos] -= quantite;

            // Si la quantité atteint 0, déséquiper l'item avant de le supprimer
            if (this.quantite[pos] <= 0) {
                if (listeditem[pos].estEquipe()) {
                    desequiperItem(pos);
                }
                supprimmerItem(pos);
            }
        }
    }


    //enlève tout l'item à l'emplacement
    public void supprimmerItem(int pos) {
        if (pos >= 0 && pos < taille) {
            listeditem[pos] = null;
            quantite[pos] = 0;
        }
    }

    public Item[] getListeditem() {
        return listeditem;
    }
    public int[] getQuantite() {
        return quantite;
    }

    public int getTaille() {
        return taille;
    }

    public int getItemEquipe() {
        for (int i = 0; i < listeditem.length; i++) {
            if (this.listeditem[i] != null && this.listeditem[i].estEquipe()) {
                return i;
            }
        }
        return -1; // Aucun item équipé
    }

    public void equiperItem(int pos) {
        if (pos >= 0 && pos < taille && listeditem[pos] != null) {
            // Déséquiper tous les items actuellement équipés
            for (int i = 0; i < listeditem.length; i++) {
                if (listeditem[i] != null && listeditem[i].estEquipe()) {
                    desequiperItem(i);
                }
            }
            // Équiper le nouvel item
            listeditem[pos].setEquipe(true);
        }
    }


    public void desequiperItem(int pos) {
        if (pos >= 0 && pos < taille && listeditem[pos] != null) {
            listeditem[pos].setEquipe(false);
        }
    }

    // Compte le nombre total d'un item spécifique dans l'inventaire
    public int compterItem(String nomItem) {
        int total = 0;
        for (int i = 0; i < listeditem.length; i++) {
            if (listeditem[i] != null && listeditem[i].getNom().equals(nomItem)) {
                total += quantite[i];
            }
        }
        return total;
    }
    public void afficherInventaire() {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║                 INVENTAIRE                   ║");
        System.out.println("╠══════════════════════════════════════════════╣");

        boolean inventaireVide = true;

        for (int i = 0; i < listeditem.length; i++) {
            if (listeditem[i] != null) {
                inventaireVide = false;

                // Numéro du slot
                System.out.printf("║ [%d] ", i + 1);

                // Nom de l'item
                String nomItem = listeditem[i].getNom();

                // Quantité
                String quantiteStr = (quantite[i] > 1) ? " x" + quantite[i] : "";

                // Status équipé
                String equipeStr = listeditem[i].estEquipe() ? " [ÉQUIPÉ]" : "";

                // Formatage pour aligner le texte
                String ligne = nomItem + quantiteStr + equipeStr;
                System.out.printf("%-35s ║%n", ligne);
            }
        }

        if (inventaireVide) {
            System.out.println("║              Inventaire vide                 ║");
        }

        // Affichage des statistiques
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.println("╚══════════════════════════════════════════════╝");
    }
}