package spaceship;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

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
                
                
                char[] déplacementsPossibles = new char[4];// TOUS LES DEPLACEMENTS POSSIBLES

		déplacementsPossibles[0] = 'z';
		déplacementsPossibles[1] = 'q';
		déplacementsPossibles[2] = 's';
		déplacementsPossibles[3] = 'd';

		char[] tirsPossibles = new char[3];// TOUS LES TIRS POSSIBLES

		tirsPossibles[0] = '1';
		tirsPossibles[1] = '2';
		tirsPossibles[2] = 'p';

		// NB : string = tableau de char

		coupsPossibles = new String[23]; // Tableau regroupant TOUS les coups
		int compteur = 0;

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 2; j++) {
				coupsPossibles[compteur] = "" + déplacementsPossibles[i] + tirsPossibles[j];
				compteur++;

				coupsPossibles[compteur] = "" + tirsPossibles[j] + déplacementsPossibles[i];
				compteur++;
			}
		}
		coupsPossibles[16] = "1p";
		coupsPossibles[17] = "2p";
		coupsPossibles[18] = "zp";
		coupsPossibles[19] = "qp";
		coupsPossibles[20] = "sp";
		coupsPossibles[21] = "dp";
		coupsPossibles[22] = "p";
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
	 public String coupJoue(int[][] plateau, Ship joueur1, Ship joueur2) {
	 setPlateau(plateau);
	 /*coupIA[0] = 'q';
	 coupIA[1] = '1';*/
         return minMax(plateau, joueur1, joueur2);
	 }
         
         public int evalPlateau(int plateau[][])
         {
             return 0;
         }
         
	public String minMax(int[][] plateau, Ship joueur1, Ship joueur2) {
		int max = -10000;
		int tmp = 0, maxi, maxj = 0;
                int valeur = 0;
		
                for(int i = 0; i < coupsPossibles.length; i++)
                {
                    valeur = max_(plateau, coupsPossibles[i], 0, joueur1, joueur2);
                    if(valeur > max)
                    {
                        max = valeur;
                        tmp = i;
                    }   
                }
		return coupsPossibles[tmp];
	}
        
        public int max_(int[][] plateau, String coupAJoue, int profondeur, Ship joueur1, Ship joueur2)
        {
            //étape 0: on vérifie qu'on est avant la profondeur demandé, sinon on return evalPlateau
            
            if(profondeur >= profondeurMax)
            {
                return evalPlateau(plateau);
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
                Spaceship.action(coupAJoue.charAt(0), copiePlateau, joueur2);
                
                //étape 3: On vérifie les conditions de victoire
                
                // a faire
                
                //étape 4: On vérifie que la deuxième action est possible
                
                if(Spaceship.conditionAction(coupAJoue.charAt(1), copiePlateau, joueur2))
                {
                    
                    //étape 5: On simule la deuxième action sur un nouveau plateau
                    
                    Spaceship.action(coupAJoue.charAt(1), copiePlateau, joueur2);
                    int max = 0;
                    for(int i = 0; i < coupsPossibles.length; i++)
                    {
                        max = Math.max(max, min_(copiePlateau, coupsPossibles[i], profondeur + 1, joueur1, joueur2));
                    }
                    return max;
                }
            }
            return 0;
        }
        
        
        public int min_(int[][] plateau, String coupAJoue, int profondeur, Ship joueur1, Ship joueur2)
        {
            //étape 0: on vérifie qu'on est avant la profondeur demandé, sinon on return evalPlateau
            
            if(profondeur >= profondeurMax)
            {
                return evalPlateau(plateau);
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
                Spaceship.action(coupAJoue.charAt(0), copiePlateau, joueur1);
                
                //étape 3: On vérifie les conditions de victoire
                
                // a faire
                
                //étape 4: On vérifie que la deuxième action est possible
                
                if(Spaceship.conditionAction(coupAJoue.charAt(1), copiePlateau, joueur1))
                {
                    //étape 5: On simule la deuxième action sur un nouveau plateau
                    
                    Spaceship.action(coupAJoue.charAt(1), copiePlateau, joueur2);
                    int min = 0;
                    for(int i = 0; i < coupsPossibles.length; i++)
                    {
                        min = Math.min(min, max_(copiePlateau, coupsPossibles[i], profondeur + 1, joueur1, joueur2));
                    }
                    return min;
                }
            }
            return 0;
        }
}
