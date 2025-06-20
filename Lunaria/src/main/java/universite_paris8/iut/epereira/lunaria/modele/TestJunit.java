package universite_paris8.iut.epereira.lunaria.modele;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import universite_paris8.iut.epereira.lunaria.modele.Craft.CraftHacheBois;
import universite_paris8.iut.epereira.lunaria.modele.Craft.CraftHachePierre;
import universite_paris8.iut.epereira.lunaria.modele.Craft.CraftPiocheBois;
import universite_paris8.iut.epereira.lunaria.modele.Craft.CraftPiochePierre;
import universite_paris8.iut.epereira.lunaria.modele.items.Consommables.Pierre;
import universite_paris8.iut.epereira.lunaria.modele.items.Consommables.Planche;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class TestJunit {

    Environement env;
    CraftPiocheBois craftPiocheBois;
    CraftHacheBois craftHacheBois;
    CraftPiochePierre craftPiochePierre;
    CraftHachePierre craftHachePierre;

    @BeforeEach
    public void setUp() {
        env = new Environement(10,10);
        env.getHero().getInv().ajouterItem(new Planche(),10);
        env.getHero().getInv().ajouterItem(new Pierre(),10);
        craftPiocheBois = new CraftPiocheBois(env);
        craftHacheBois = new CraftHacheBois(env);
        craftPiochePierre = new CraftPiochePierre(env);
        craftHachePierre = new CraftHachePierre(env);
    }

    @Test
    public final void CraftInitInventaireTest(){

        assertTrue(env.getHero().getInv().getListeditem().get(2).getId() == 3 );
        assertTrue(env.getHero().getInv().getQuantite()[0].getValue() == 49);
        assertTrue(39 == env.getHero().getInv().getQuantite()[1].getValue());

    }

    @Test
    public final void CraftCraftableTest(){
        assertFalse(craftHacheBois.craftable());
        assertFalse(craftHachePierre.craftable());
        assertFalse(craftPiocheBois.craftable());
        assertFalse(craftPiochePierre.craftable());

        env.getHero().getInv().ajouterItem(new Planche(), 30);
        env.getHero().getInv().ajouterItem(new Pierre(), 30);

        assertTrue(craftHacheBois.craftable());
        assertTrue(craftHachePierre.craftable());
        assertTrue(craftPiocheBois.craftable());
        assertTrue(craftPiochePierre.craftable());
    }

    @Test //test si le craft prend bien la quantite qu'il faut
    public final void CraftCraftingTest(){
        env.getHero().getInv().ajouterItem(new Planche(), 54);
        env.getHero().getInv().ajouterItem(new Pierre(), 54);


        Item item = craftHacheBois.crafting();

        assertTrue(item.getId() == craftHacheBois.getResultat().getId());

        //verifie que 30 planche on étét pris et que les 64 pierre n'ont oas été toucher
        assertTrue(env.getHero().getInv().getQuantite()[0].getValue() == 34);
        assertTrue(env.getHero().getInv().getQuantite()[1].getValue() == 64);

        //remet les planche a 64
        env.getHero().getInv().getQuantite()[0].setValue(64);

        Item item1 = craftHachePierre.crafting();

        assertTrue(item1.getId() == craftHachePierre.getResultat().getId());

        //verifie que les quantite prise sont les bonnes
        assertTrue(env.getHero().getInv().getQuantite()[0].getValue() == 54);
        assertTrue(env.getHero().getInv().getQuantite()[1].getValue() == 34);


    }

    @Test //test si le craft supprime bien l'item si la quantite qu'il faut est égal a la quantite qu'on as
    public final void craftCraftingTest2(){

        //initialise les planche au bon nombre qu'il faut
        env.getHero().getInv().getQuantite()[0].setValue(30);

        //on verifie que toute les planche sont partie et que l'inventaire est redevenue vide de planche
        craftHacheBois.crafting();
        assertTrue(env.getHero().getInv().getQuantite()[0].getValue() == 0);
        assertTrue(env.getHero().getInv().getListeditem().get(0) == null);

        //renitialise pour faire le teste avec une hache en pierre
        env.getHero().getInv().getListeditem().set(0,new Planche());
        env.getHero().getInv().getQuantite()[0].setValue(10);

        env.getHero().getInv().getQuantite()[1].set(30);

        craftHachePierre.crafting();

        //on verifie que chaque materiaux sont partie
        assertTrue(env.getHero().getInv().getListeditem().get(0) == null );
        assertTrue(env.getHero().getInv().getQuantite()[0].getValue() == 0);

        assertTrue(env.getHero().getInv().getListeditem().get(1) == null);
        assertTrue(env.getHero().getInv().getQuantite()[1].getValue() == 0);

    }


    @Test //test le craft peut prendre tout les items qu'il faut, meme si ils sont éparpiller dans l'inventaire
    public final void craftCrafatingTest3(){

        env.getHero().getInv().getListeditem().set(5,new Planche());
        env.getHero().getInv().getQuantite()[5].setValue(10);

        env.getHero().getInv().getListeditem().set(7, new Planche());
        env.getHero().getInv().getQuantite()[7].setValue(20);


        craftHacheBois.crafting();

        assertTrue(env.getHero().getInv().getListeditem().get(0) == null);
        assertTrue(env.getHero().getInv().getListeditem().get(5) == null);
        assertTrue(env.getHero().getInv().getListeditem().get(7).getId() == craftHacheBois.getResultat().getId());

        assertTrue(env.getHero().getInv().getQuantite()[0].getValue() == 0);
        assertTrue(env.getHero().getInv().getQuantite()[5].getValue() == 0);
        assertTrue(env.getHero().getInv().getQuantite()[7].getValue() == 10);

    }

}
