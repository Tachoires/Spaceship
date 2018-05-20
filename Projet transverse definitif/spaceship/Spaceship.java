/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



package spaceship;
import java.util.*;


/*plateau:
0: case libre
1: Joueur 1
2: Joueur 2
3: Obstacles
11: missiles 1 joueur 1
12: missiles 2 joueur 1
21: missiles 1 joueur 2
22: missiles 2 joueur 2


*/
public class Spaceship {

    public static final int TAILLEX = 7;
    public static final int TAILLEY = 7;
    private static boolean MOUVEMENT = false; 
    private static boolean TIR = false; 
    private static boolean ENDGAME = false;
    private static boolean simulationIA = false;
    private static int compteurAggro = 0, compteurPassif = 0, compteurLache = 0;
    /**
     * 
     */
    
    
    public static void printPlateau(int [][] plateau)
    {
        for(int i = 0; i < TAILLEY; i++)
        {
            for(int j = 0; j < TAILLEX; j++)
            {
                
                System.out.print("  " + plateau[j][i]);
            }
            System.out.println();
        }
    }
    
    public static int[][] initPlateau(int[][] plateau)
    {
        for(int i = 0; i < TAILLEX; i++)
        {
            for(int j = 0; j < TAILLEY; j++)
            {
                plateau[i][j] = 0;
            }
        }
        return plateau;
    }
    
    public static void afficherCommandes(int joueur)
    {
        System.out.printf("C'est au tour du joueur %d: \nListes des commandes pour jouer:", joueur);
                 System.out.printf("Mouvements: \n\t d pour vous déplacer à droite\n\t"
                         + " q pour vous déplacer à gauche\n\t z pour vous déplacer en haut\n\t s pour vous déplacer en bas\n");
                 System.out.printf("Attaques: \n\t 1 pour missile d'une case par tour\n\t"
                         + " 2 pour missile de deux cases par tour: \n\t");
                 System.out.printf(" p pour passer son tour: ");
    }
    
    public static void afficherCommandesMouvement(int joueur)
    {
        System.out.printf(" \nListes des commandes pour jouer:", joueur);
                 System.out.printf("Mouvements: \n\t d pour vous déplacer à droite\n\t"
                         + " q pour vous déplacer à gauche\n\t z pour vous déplacer en haut\n\t s pour vous déplacer en bas: \n\t");
                 System.out.printf(" p pour passer son tour: ");
    }
    
    public static void afficherCommandesTir(int joueur)
    {
        System.out.printf("\nListes des commandes pour jouer:", joueur);
                  System.out.printf("Attaques: \n\t 1 pour missile d'une case par tour\n\t"
                         + " 2 pour missile de deux cases par tour: \n\t");
                 System.out.printf(" p pour passer son tour: ");
    }
    
    public static int[][] action(char input, int[][] plateau, Ship joueur)
    {
        Scanner k = new Scanner(System.in);
        //if(joueur.getJoueur() == 2) System.out.println("ICI");
        //if(joueur.getJoueur() == 2) System.out.println("input = " + input + " | MOUVEMENT = " + MOUVEMENT);
        if (input == 'z' || input == 'd' || input == 's' || input == 'q' && MOUVEMENT == false) // mouvement
        {
            //if(joueur.getJoueur() == 2) System.out.println("HERE");
            if(joueur.getJoueur() == 1)MOUVEMENT = true;
            int x = joueur.getX();
            int y = joueur.getY();
            //if(joueur.getJoueur() == 2) System.out.println("x = " + x);
            //if(joueur.getJoueur() == 2) System.out.println("y = " + y);
            boolean actionValide = joueur.move(input, plateau); //boolean ne servent à rien
            if(!actionValide)
            {
                win(joueur.getJoueur());
            }
            plateau[x][y] = 0;
            plateau[joueur.getX()][joueur.getY()] = joueur.getJoueur();
        }
        else if (input == '1' || input == '2' && TIR == false) // tir
        {
            if(joueur.getJoueur() == 1)TIR = true;
            int x = joueur.getX();
            int y = joueur.getY();
            int tirX;
            int tirY;
            if(joueur.getJoueur()  == 1)
            {
                tirX = x;
                tirY = y - 1;
            }
            else
            {
                tirX = x;
                tirY = y + 1;
            }
            if( plateau[tirX][tirY] == 3)
            {
                 plateau[tirX][tirY] = 0;
            }
            else if (plateau[tirX][tirY] == 1 || plateau[tirX][tirY] == 2)
            {

                win(joueur.getJoueur());
            }
            else
            {
                boolean fausseAction = joueur.tir(x, y,  input, joueur.getJoueur(), plateau);
                plateau[tirX][tirY] = Integer.parseInt(input + "") + 4;
            }
        }
        return plateau;
    }
    
    public static boolean conditionAction(char input, int[][] plateau, Ship joueur)  // renvoie false si c'est pas bon
    {
        if(joueur.getX() == TAILLEX || joueur.getX() == -1 || joueur.getY() == TAILLEY || joueur.getY() == -1) return false;
        //System.out.println("input = " + input + " | joueur.getX() = " + joueur.getX() + " | joueur.getY() = " + joueur.getX());
        /*return ((input == 'q' || input == 's' || input == 'z' || input == 'd' ||input == '1' || input == '2' ||input == 'p') 
              
             && (joueur.getX() != 0 || input != 'q') && (joueur.getX() != TAILLEX - 1 || input != 'd')
             && (joueur.getY() != 0 || input != 'z') && (joueur.getY() != TAILLEY - 1 || input != 's')
             && (joueur.getJoueur() != 1 || (input != '1' && input != '2') || joueur.getY() != 0)
             && (joueur.getJoueur() != 2 || (input != '1' && input != '2') || joueur.getY() != TAILLEY - 1)*/
        
             /*&& (input != 'd' || plateau[joueur.getX() + 1][joueur.getY()] != 3)
             && (input != 'q' || plateau[joueur.getX() - 1][joueur.getY()] != 3)
             && (input != 's' || plateau[joueur.getX()][joueur.getY() + 1] != 3)
             && (input != 'z' || plateau[joueur.getX() + 1][joueur.getY() - 1] != 3)*/
        if(input != 'q' && input != 'z' && input != 'd' && input != 's' && input != '1' && input != '2' && input != 'p') //input correct
        {
            return false;
        }
        else if((joueur.getX() == 0 && input == 'q') || (joueur.getX() == TAILLEX - 1 && input == 'd')) // déplacement horizontal
        {
            return false;
        }
        else if((joueur.getY() == 0 && input == 'z') || (joueur.getY() == TAILLEY - 1 && input == 's')) // déplacement vertical
        {
            return false;
        }
        else if((joueur.getJoueur() == 1 && (input == '1' || input == '2') && joueur.getY() == 0)) // tir possible J1
        {
            return false;
        }
        else if((joueur.getJoueur() == 2 && (input == '1' || input == '2') && joueur.getY() == TAILLEY - 1)) // tir possible J2
        {
            return false;
        }
        else if((input == 'd' && plateau[joueur.getX() + 1][joueur.getY()] == 3)) //Obstacle a droite
        {
            return false;
        }
        else if((input == 'q' && plateau[joueur.getX() - 1][joueur.getY()] == 3))//Obstacle a gauche
        {
            return false;
        }
        else if((input == 's' && plateau[joueur.getX()][joueur.getY() + 1] == 3))//Obstacle en bas
        {
            return false;
        }
        else if((input == 'z' && plateau[joueur.getX()][joueur.getY() - 1] == 3))//Obstacle en haut
        {
            return false;
        }
        else
            return true;
        
        
       /* boolean numero1 = (input == 'q' || input == 's' || input == 'z' || input == 'd' ||input == '1' || input == '2' ||input == 'p') && 
                (joueur.getX() != 0 || input != 'q') && (joueur.getX() != TAILLEX - 1 || input != 'd')
             && (joueur.getY() != 0 || input != 'z') && (joueur.getY() != TAILLEY - 1 || input != 's')
             && (joueur.getJoueur() != 1 || (input != '1' && input != '2') || joueur.getY() != 0)
             && (joueur.getJoueur() != 2 || (input != '1' && input != '2') || joueur.getY() != TAILLEY - 1);
        if(!numero1) return numero1;
        else 
        {
            if((joueur.getX() != 0 && input != 'q') && (joueur.getX() == 0 || input != 'd'))
            {
                return (
                (input != 'd' || plateau[joueur.getX() + 1][joueur.getY()] != 3)
                && (joueur.getX() != 0 || input != 'q' || plateau[joueur.getX() - 1][joueur.getY()] != 3)
                && (input != 's' || plateau[joueur.getX()][joueur.getY() + 1] != 3)
                && (input != 'z' || plateau[joueur.getX() + 1][joueur.getY() - 1] != 3));
            }
            else
            {
                return numero1;
            }*/
            
        
            
        
        /*return ((joueur.getX() == 0 && input == 'q') || (joueur.getX() == TAILLEX - 1 && input == 'd')
             || (joueur.getY() == 0 && input == 'z') || (joueur.getY() == TAILLEY - 1 && input == 's')
             || (joueur.getJoueur() == 1 && (input == '1' || input == '2') && joueur.getY() == 0)
             || (joueur.getJoueur() == 2 && (input == '1' || input == '2') && joueur.getY() == TAILLEY - 1));*/  //manque  obstacles + collision
    }
    
    public static boolean conditionActionMouvement(char input, int[][] plateau, Ship joueur)  // renvoie false si c'est pas bon
    {
        /*return ((input == 'q' || input == 's' || input == 'z' || input == 'd' || input == 'p') 
             && (joueur.getX() != 0 || input != 'q') && (joueur.getX() != TAILLEX - 1 || input != 'd')
             && (joueur.getY() != 0 || input != 'z') && (joueur.getY() != TAILLEY - 1 || input != 's')
             && (input != 'd' || plateau[joueur.getX() + 1][joueur.getY()] != 3)
             && (input != 'q' || plateau[joueur.getX() - 1][joueur.getY()] != 3)
             && (input != 's' || plateau[joueur.getX()][joueur.getY() + 1] != 3)
             && (input != 'z' || plateau[joueur.getX() + 1][joueur.getY() - 1] != 3));*/
        
        if(input != 'q' && input != 'z' && input != 'd' && input != 's' && input != 'p') //input correct
        {
            return false;
        }
        else if((joueur.getX() == 0 && input == 'q') || (joueur.getX() == TAILLEX - 1 && input == 'd')) // déplacement horizontal
        {
            return false;
        }
        else if((joueur.getY() == 0 && input == 'z') || (joueur.getY() == TAILLEY - 1 && input == 's')) // déplacement vertical
        {
            return false;
        }
        else if((input == 'd' && plateau[joueur.getX() + 1][joueur.getY()] == 3)) //Obstacle a droite
        {
            return false;
        }
        else if((input == 'q' && plateau[joueur.getX() - 1][joueur.getY()] == 3))//Obstacle a gauche
        {
            return false;
        }
        else if((input == 's' && plateau[joueur.getX()][joueur.getY() + 1] == 3))//Obstacle en bas
        {
            return false;
        }
        else if((input == 'z' && plateau[joueur.getX()][joueur.getY() - 1] == 3))//Obstacle en haut
        {
            return false;
        }
        else
            return true;
    }
    
    public static boolean conditionActionTir(char input, int[][] plateau, Ship joueur)  // renvoie false si c'est pas bon
    {
        /*return ((input == '1' || input == '2' ||input == 'p')
             && (joueur.getJoueur() != 1 || (input != '1' && input != '2') || joueur.getY() != 0)
             && (joueur.getJoueur() != 2 || (input != '1' && input != '2') || joueur.getY() != TAILLEY - 1));*/
        
         if(input != '1' && input != '2' && input != 'p') //input correct
        {
            return false;
        }
        else if((joueur.getJoueur() == 1 && (input == '1' || input == '2') && joueur.getY() == 0)) // tir possible J1
        {
            return false;
        }
        else if((joueur.getJoueur() == 2 && (input == '1' || input == '2') && joueur.getY() == TAILLEY - 1)) // tir possible J2
        {
            return false;
        }
        else return true;
    }
    
    public static void evaluationJoueur(char input)
    {
        if(input == 'z')
        {
            compteurAggro+= 5;
        }
        else if(input == 's')
        {
            compteurLache++;
        }
        else if(input == 'q' || input == 's')
        {
            compteurPassif++;
        }
        else if(input == '1')
        {
            compteurLache++;
        }
        else if(input == '2')
        {
            compteurAggro++;
        }
        else
        {
            compteurPassif += 4;
        }
    }
    
    public static void affichageEval()
    {
        if((compteurAggro * 1.5 >= compteurPassif * 0.5) && (compteurLache * 1.5 >= compteurPassif * 0.5))
        {
            System.out.println("Vous avez eu un jeu très polyvalent lors de cette partie, vous avez équilibré entre la défense et l'attaque.");
        }
        else if(compteurPassif > compteurAggro && compteurPassif > compteurLache)
        {
            System.out.println("Vous avez joué très passif. \nVous pourriez gagner plus de parties en essayant davantage d'éliminer l'adversaire au lieu d'essayer de survivre.");
        }
        else if(compteurAggro > compteurPassif && compteurAggro > compteurLache)
        {
            System.out.println("Vous avez joué très aggressif, vous pourriez gagner plus souvent en essayant de planifier davantage vos actions.");
        }
        else if(compteurLache > compteurAggro && compteurLache > compteurPassif)
        {
            System.out.println("Vous semblez avoir peur de mourir, il faut essayer d'avoir davantage confiance en vous et de coincer votre adversaire.");
        }
        else
        {
            System.out.printf("Votre façon de jouer est indescriptible.");
        }
    }
    
    public static boolean deplacementMissile(int[][] plateau, Missile missile, int joueur, Ship vaisseau) // return false si c'est pas bon
    {
        int x = missile.getX();
        int y = missile.getY();
        if(joueur == 1)
        {
            if(y == 0)
            {
                plateau[missile.getX()][missile.getY()] = 0;
                vaisseau.delMissile(missile);
                
                return false;
            }
            else if(plateau[x][y - 1] == 2)
            {
 
                win(1);
                return true;
            }
             else if(plateau[x][y - 1] == 3)
            {
                plateau[missile.getX()][missile.getY()] = 0;
                vaisseau.delMissile(missile);
                plateau[x][y - 1] = 0;
                return false;
            }
            else
            {
                missile.setUp();
                plateau[x][y] = 0;
                plateau[missile.getX()][missile.getY()] = missile.getType();
                return false;
            }
        }
        if(joueur == 2)
        {
            if(y == TAILLEY - 1)
            {
                plateau[missile.getX()][missile.getY()] = 0;
                vaisseau.delMissile(missile);
                
                return false;
            }
            else if(plateau[x][y + 1] == 1)
            {
                win(2);
                return true;
            }
            else if(plateau[x][y + 1] == 3)
            {
                plateau[missile.getX()][missile.getY()] = 0;
                vaisseau.delMissile(missile);
                plateau[x][y + 1] = 0;
                return false;
            }
            else
            {
                missile.setDown();
                plateau[x][y] = 0;
                plateau[missile.getX()][missile.getY()] = missile.getType();
                return false;
            }
        }
        else
            return false;
    }
    public static void win(int joueur) //boolean ia sert à savoir si c'est une simulationo u pas 
    {
        if(!simulationIA)
        {
            System.out.printf("Bravo le joueur " + joueur + " a gagné !\n");
            ENDGAME = true;
        }

    }
            
    
    
    
    public static void main(String[] args) 
    {
        
        Scanner k = new Scanner(System.in);
        int plateau[][] = new int[TAILLEX][TAILLEY];
        plateau = initPlateau(plateau);
        Ship joueur2 = new Ship(TAILLEX/2, 0, 2); // (joueur, x, y)
        plateau[joueur2.getX()][joueur2.getY()] = 2;
        Ship joueur1 = new Ship(TAILLEY/2, TAILLEY - 1, 1);
        plateau[joueur1.getX()][joueur1.getY()] = 1;
        char input;
        IA iaJoueur2 = new IA(plateau);
        int turn = 1;
        
        while(!ENDGAME)
        {
            
            int nombreAleatoireTour = 0 + (int)(Math.random() * ((3 - 0) + 1));
            if(nombreAleatoireTour == 0)
            {
                int nombreAleatoireX = 0 + (int)(Math.random() * ((TAILLEX - 1 - 0) + 1));
                int nombreAleatoireY = 0 + (int)(Math.random() * ((TAILLEY - 1 - 0) + 1));
                if(plateau[nombreAleatoireX][nombreAleatoireY] == 0)
                {
                    
                    plateau[nombreAleatoireX][nombreAleatoireY] = 3;
                }
            }
           
            
            System.out.println("Tour numéro " + turn);
            printPlateau(plateau);
            if (turn %2 == 1) //joueur 1
            {
                boolean endTurn = false;
                afficherCommandes(1); // Le joueur peut bouger ou tirer
                input = k.next().charAt(0);
                if(input == 'p') 
                {
                    endTurn = true;
                }
                else
                {
                    while(!conditionAction(input, plateau, joueur1))
                    {
                        System.out.printf("Veuillez entrer une commande correcte, merci de votre compréhension, Pa pa pala...");//générique de la SNCF
                        input = k.next().charAt(0);
                    }
                    evaluationJoueur(input);
                    
                    plateau = action(input, plateau, joueur1);
                }
                
                printPlateau(plateau);
                if (TIR && !endTurn) // On doit bouger
                {
                    afficherCommandesMouvement(1);
                    input = k.next().charAt(0);
                    while(!conditionActionMouvement(input, plateau, joueur1))
                    {
                        System.out.printf("Veuillez entrer une commande correcte, merci de votre compréhension, Pa pa pala...");//générique de la SNCF
                        input = k.next().charAt(0);
                    }
                    evaluationJoueur(input);
                    plateau = action(input, plateau, joueur1);
                }
                else if(!endTurn) // On doit tirer
                {
                    afficherCommandesTir(1);
                    input = k.next().charAt(0);
                    while(!conditionActionTir(input, plateau, joueur1))
                    {
                        System.out.printf("Veuillez entrer une commande correcte, merci de votre compréhension, Pa pa pala...");//générique de la SNCF
                        input = k.next().charAt(0);
                    }
                    evaluationJoueur(input);
                    plateau = action(input, plateau, joueur1);
                }
            }
            else  //joueur 2
            {
                
                boolean endTurn = false;
                simulationIA = true;
                String toPlay = iaJoueur2.coupJoue(plateau, joueur1, joueur2);
                simulationIA = false;
                MOUVEMENT = false;
                TIR = false;
                input = toPlay.charAt(0);
                
                if(input == 'p') 
                {
                    endTurn = true;
                }
                else
                {
                    while(!conditionAction(input, plateau, joueur2))
                    {
                        System.out.printf("Veuillez entrer une commande correcte, merci de votre compréhension, Pa pa pala...");//générique de la SNCF
                        System.out.println(input + " n'est pas une commande correcte");
                        input = k.next().charAt(0);
                    }

                    plateau = action(input, plateau, joueur2);
                }
                if(input == '1' || input == '2') 
                {
                    TIR = true;
                    MOUVEMENT = false;
                }
                else
                {
                    TIR = false;
                    MOUVEMENT = true;
                }
                //System.out.println("TIR = " + TIR + " MVT = " + MOUVEMENT);
                printPlateau(plateau);
                if (TIR && !endTurn) // On doit mouvementer
                {
                    
                    //afficherCommandesMouvement(2);
                    //input = k.next().charAt(0);input = k.next().charAt(0);
                    
                    input = toPlay.charAt(1);
                    while(!conditionActionMouvement(input, plateau, joueur2))
                    {
                        System.out.printf("Veuillez entrer une commande correcte, merci de votre compréhension, Pa pa pala...");//générique de la SNCF
                        System.out.println(input + " n'est pas une commande correcte");
                        input = k.next().charAt(0);
                    }
                     
                    plateau = action(input, plateau, joueur2);
                }
                else if(!endTurn)// On doit tirer
                {
                    //afficherCommandesTir(2);
                    //input = iaJoueur2.coupJoue(plateau, joueur1, joueur2).charAt(1);
                    input = toPlay.charAt(1);
                    while(!conditionActionTir(input, plateau, joueur2))
                    {
                        System.out.printf("Veuillez entrer une commande correcte, merci de votre compréhension, Pa pa pala...");//générique de la SNCF
                        System.out.println(input + " n'est pas une commande correcte");
                        input = k.next().charAt(0);
                    }
                    plateau = action(input, plateau, joueur2);
                }
                
            }
            if(turn % 7 == 0) //Création d'obstacles
            {
                int nombreAleatoireX = 0 + (int)(Math.random() * ((TAILLEX - 0)));
                int nombreAleatoireY = 0 + (int)(Math.random() * ((TAILLEY - 0)));
                if(plateau[nombreAleatoireX][nombreAleatoireY] == 0)
                {
                    plateau[nombreAleatoireX][nombreAleatoireY] = 3;
                }
                else
                {
                    while(plateau[nombreAleatoireX][nombreAleatoireY] != 0)
                    {
                        nombreAleatoireX = 0 + (int)(Math.random() * ((TAILLEX - 0)));
                        nombreAleatoireY = 0 + (int)(Math.random() * ((TAILLEY - 0)));
                    }
                    plateau[nombreAleatoireX][nombreAleatoireY] = 3;
                }
            }
            
            mouvementMissile(joueur1, plateau);
            
            
            mouvementMissile(joueur2, plateau); 

            turn++;
            MOUVEMENT = false; 
            TIR = false; 
        }
        affichageEval();
        
    }

    public static boolean mouvementMissile(Ship joueur, int[][] plateau) {
        //faire bouger les missiles:
        boolean ennemiDetruit = false;
        ArrayList<Missile> missiles = joueur.getMissile();
        for(Missile missile : missiles)
        {
            if(missile.getType() == 6)
            {
                ennemiDetruit = ennemiDetruit || deplacementMissile(plateau, missile, joueur.getJoueur(), joueur);
                ennemiDetruit = ennemiDetruit || deplacementMissile(plateau, missile, joueur.getJoueur(), joueur);
            }
            else
            {
                ennemiDetruit = ennemiDetruit || deplacementMissile(plateau, missile, joueur.getJoueur(), joueur);
            }
            
            
        }
        return ennemiDetruit;
    }
    
}
