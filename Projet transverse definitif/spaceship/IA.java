package spaceship;

import java.util.ArrayList;
import java.util.List;

public class IA {

	// d pour vous dÃ©placer Ã  droite
	// q pour vous dÃ©placer Ã  gauche
	// z pour vous dÃ©placer en haut
	// s pour vous dÃ©placer en bas
	//
	// 1 pour missile d'une case par tour
	// 2 pour missile de deux cases par tour
	// p pour passer son tour

	private int plateau[][]; // RACINE DE L'ARBRE
	private char[] coupIA = new char[3]; // COUP DE L'IA : SERA ISSU D'UNE FCT
	// ATTENTION : RETOURNER COUP DEPLACEMENT ET COUP TIR, CONSTITUENT A EUX 2 LE
	// COUP IA
	// 3EME CASE TABLEAU = VALEUR DE L'ACTION
	private List arbre;
        private int profondeurMax = 2;
	public static final int joueur = 2;
        public static String[] coupsPossibles;
        
	public IA(int[][] plateau) {
		this.plateau = plateau; // PLACEMENT RACINE
                
                
                char[] deplacementsPossibles = new char[4];// TOUS LES DEPLACEMENTS POSSIBLES

		deplacementsPossibles[0] = 'z';
		deplacementsPossibles[1] = 'q';
		deplacementsPossibles[2] = 's';
		deplacementsPossibles[3] = 'd';

		char[] tirsPossibles = new char[3];// TOUS LES TIRS POSSIBLES

		tirsPossibles[0] = '1';
		tirsPossibles[1] = '2';
		tirsPossibles[2] = 'p';

		// NB : string = tableau de char

		coupsPossibles = new String[23]; // Tableau regroupant TOUS les coups
		int compteur = 0;

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 2; j++) {
				coupsPossibles[compteur] = "" + deplacementsPossibles[i] + tirsPossibles[j];
				compteur++;

				coupsPossibles[compteur] = "" + tirsPossibles[j] + deplacementsPossibles[i];
				compteur++;
			}
		}
		coupsPossibles[16] = "1p";
		coupsPossibles[17] = "2p";
		coupsPossibles[18] = "zp";
		coupsPossibles[19] = "qp";
		coupsPossibles[20] = "sp";
		coupsPossibles[21] = "dp";
		coupsPossibles[22] = "pp";
	}

	@Override
	public String toString() {
		return "Tamer";
	}

	public int[][] getPlateau() {
		return plateau;
	}

	public void setPlateau(int[][] plateau) {
		this.plateau = plateau;
	}
	// TEST DE COUP UNIQUE
	 public String coupJoue(int[][] plateau, final Ship joueur1, final Ship joueur2) 
         {
            //setPlateau(plateau);
            return minMax(plateau, joueur1, joueur2);
	 }
         
         public double distance (double x1, double y1, double x2, double y2)
         {
             //System.out.println( Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1)));
             return Math.sqrt(Math.abs((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1)));
         }         
         
         public double evalPlateau(int plateau[][], final Ship joueur1, final Ship joueur2)
         {
            double score = 0;
            ArrayList<Missile> missiles = joueur2.getMissile();
            for(Missile missile : missiles)
            {
                double distance1 = distance(missile.getX(), missile.getY(), joueur1.getX(), joueur1.getY());
                
                if (distance1 != 0)
                {
                    
                    score += 1/distance1;
                    
                }
                else score = 10000;
            }
             
            missiles = joueur1.getMissile();
            for(Missile missile : missiles)
            {
                double distance1 = distance(missile.getX(), missile.getY(), joueur2.getX(), joueur2.getY());
                if (distance1 != 0) score -= 1/distance1;
                else score = -10000;
            }
             /*for(int i = 0; i < plateau.length; i++)
             {
                 for(int j = 0; j < plateau[0].length; j++)
                 {
                     
                 }
             }*/
             double nombreAleatoire = 0 + (double)(Math.random() * ((1 - 0) + 1));
             score += nombreAleatoire;
             //System.out.println("Score = " + score);
             return score;
         }
         
	public String minMax(int[][] plateau, final Ship joueur1, final Ship joueur2) {
		double max = -10000;
		int tmp = 0;
                double valeur = 0;
		
                for(int i = 0; i < coupsPossibles.length; i++)
                {
                    valeur = max_(plateau, coupsPossibles[i], 0, new Ship(joueur1), new Ship(joueur2), -699999, +699999);
                    //System.out.println("valeur = : " + valeur);
                    if(valeur >= max)
                    {
                        System.out.println("Coup possible: " + coupsPossibles[i]);
                        max = valeur;
                        tmp = i;
                    }   
                }
                System.out.println("numéro : " + tmp);
                System.out.println("Coup à jouer:" + coupsPossibles[tmp]);
		return coupsPossibles[tmp];
	}
        
        public double max_(int[][] plateau, String coupAJoue, int profondeur, Ship joueur1, Ship joueur2, double alpha, double beta)
        {
            //étape 0: on vérifie qu'on est avant la profondeur demandé, sinon on return evalPlateau
            
            if(profondeur >= profondeurMax)
            {
                return evalPlateau(plateau, joueur1, joueur2);
            }
            
            //étape 1: on vérifie que la première action est possible
            
            if(Spaceship.conditionAction(coupAJoue.charAt(0), plateau, joueur2))
            {
                //étape 2: on simule l'action sur un nouveau plateau
                int[][] copiePlateau = new int[plateau.length][plateau[0].length];
                for(int i = 0; i < plateau.length; i++)
                {
                    for(int j = 0; j < plateau[0].length; j++)
                    {
                        copiePlateau[i][j] = plateau[i][j];
                    }
                    
                }
                copiePlateau =  Spaceship.action(coupAJoue.charAt(0), copiePlateau, joueur2);
                
                //étape 3: On vérifie les conditions de victoire
                
                // a faire
                
                //étape 4: On vérifie que la deuxième action est possible
                
                if(Spaceship.conditionAction(coupAJoue.charAt(1), copiePlateau, joueur2))
                {
                    
                    //étape 5: On simule la deuxième action sur un nouveau plateau
                    boolean aGagneJ1 = false;
                    boolean aGagneJ2 = false;
                    copiePlateau =  Spaceship.action(coupAJoue.charAt(1), copiePlateau, joueur2);
                    aGagneJ1 = Spaceship.mouvementMissile(joueur1, copiePlateau);
                    aGagneJ2 = Spaceship.mouvementMissile(joueur2, copiePlateau);
                    if(aGagneJ1) return -5000;
                    if(aGagneJ2) return 5000;
                    
                    
                    double resultat = alpha;
                    //double max = 0;
                    double temp = 0;
                    for(int i = 0; i < coupsPossibles.length; i++)
                    {
                        temp = min_(copiePlateau, coupsPossibles[i], profondeur + 1, new Ship(joueur1), new Ship(joueur2), resultat, beta);
                        //System.out.println("_min = " + temp + "max = " + max);
                        resultat = Math.max(resultat, temp);
                        if(resultat >= beta) return resultat;
                    }
                    //System.out.println("Max = " + max);
                    return resultat;
                }
            }
            System.out.println("IMPOSSIBLE pour J2: couptenté = " + coupAJoue.charAt(0) + coupAJoue.charAt(1));
            return -10000;
        }
        
        
        public double min_(int[][] plateau, String coupAJoue, int profondeur, Ship joueur1, Ship joueur2, double alpha, double beta)
        {
            //étape 0: on vérifie qu'on est avant la profondeur demandé, sinon on return evalPlateau
            
            if(profondeur >= profondeurMax)
            {
                return evalPlateau(plateau, joueur1, joueur2);
            }
            
            //étape 1: on vérifie que la première action est possible
            
            if(Spaceship.conditionAction(coupAJoue.charAt(0), plateau, joueur1))
            {
                //étape 2: on simule l'action sur un nouveau plateau
                int[][] copiePlateau = new int[plateau.length][plateau[0].length];
                for(int i = 0; i < plateau.length; i++)
                {
                    for(int j = 0; j < plateau[0].length; j++)
                    {
                        copiePlateau[i][j] = plateau[i][j];
                    }
                    
                }
                copiePlateau =  Spaceship.action(coupAJoue.charAt(0), copiePlateau, joueur1);
                
                //étape 3: On vérifie les conditions de victoire
                
                // a faire
                
                //étape 4: On vérifie que la deuxième action est possible
                
                if(Spaceship.conditionAction(coupAJoue.charAt(1), copiePlateau, joueur1))
                {
                    //System.out.println("Joueur1: "+ joueur1.getX() + "/" + joueur1.getY());
                    //System.out.println("coupA joue: " + coupAJoue.charAt(1));
                    
                    //étape 5: On simule la deuxième action sur un nouveau plateau
                    boolean aGagneJ1 = false;
                    boolean aGagneJ2 = false;
                    copiePlateau =  Spaceship.action(coupAJoue.charAt(1), copiePlateau, joueur1);
                    aGagneJ1 = Spaceship.mouvementMissile(joueur1, copiePlateau);
                    aGagneJ2 = Spaceship.mouvementMissile(joueur2, copiePlateau);
                    if(aGagneJ1) return -5000;
                    if(aGagneJ2) return 5000;
                    
                    
                    double resultat = beta;
                    //double min = 50000;
                    double temp = 0;
                    for(int i = 0; i < coupsPossibles.length; i++)
                    {
                        temp = max_(copiePlateau, coupsPossibles[i], profondeur + 1, new Ship(joueur1), new Ship(joueur2), alpha, resultat);
                        //System.out.println("_max = " + temp + "min = " + min);
                        resultat = Math.min(resultat, temp);
                        if(alpha >= resultat) return resultat;
                    }
                    return resultat;
                }
            }
            System.out.println("IMPOSSIBLE pour J1: couptenté = " + coupAJoue.charAt(0) + coupAJoue.charAt(1));
            return -10000;
        }
}
