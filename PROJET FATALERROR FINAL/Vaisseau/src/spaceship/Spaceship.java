/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



package spaceship;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;


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

public class Spaceship extends Canvas implements Runnable {
    
     public static final int WIDTH = 330;
    public static final int HEIGHT = 250;
    public static final int SCALE = 2;
    public final String TITLE = "Spaceship Game";
    public static int[][] copiePlateau;
    
    private static  boolean running = false;
    private static Thread thread;
    
    private static BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private static BufferedImage spriteSheet = null;
    private static BufferedImage background = null;
    
    private static BufferedImage player;
    private static BufferedImage IA;
    private static BufferedImage tir;
    private static BufferedImage tirRapide;
    
    public static void init(){
        BufferedImageLoader loader = new BufferedImageLoader();
        try{
            
            spriteSheet = loader.loadImage("/sprite_sheet.png");
            background = loader.loadImage("/background.png");
            
        }catch(IOException e){
            e.printStackTrace();
        }
        
        SpriteSheet ss = new SpriteSheet(spriteSheet);
        player = ss.grabImage(1, 1, 32, 32);
        IA = ss.grabImage(3, 1, 32, 32);
        tir = ss.grabImage(4, 1, 32, 32);
        tirRapide = ss.grabImage(2, 1, 32, 32);
    }
    
    private synchronized void start(){
        if(running)
            return;
        
        running = true;
        thread = new Thread(this);
        thread.start();
    }
    private synchronized void stop(){
        if(!running)
            return;
        
        running = false;
        try{
          thread.join();  
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        System.exit(1);
    }
    
    public void run(){
        init();
        long lastTime = System.nanoTime();
        final double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        int updates = 0;
        int frames = 0;
        long timer = System.currentTimeMillis();
        
        while(running){
            // boucle jeu
            long now = System.nanoTime();
            delta += (now - lastTime / ns);
            lastTime = now;
            if(delta >= 1){
                tick();
                updates++;
                delta--;
            }
            
            render();
            frames++;
            
            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                updates = 0;
                frames = 0;
            }
            
        }
        stop();
    }
    
    private void tick(){
        
    }
    
    public void render(){
        
        BufferStrategy bs = this.getBufferStrategy();
        
        if (bs == null ){
            
            createBufferStrategy(3); 
            return; 
            
        }
        Graphics g = bs.getDrawGraphics();
        ///////////////////////////////////////        
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
         g.drawImage(background, 0, 0,  this);
        int posX = 0;
        int posY = 0;
         for(int i = 0; i < TAILLEY; i++)
        {
            for(int j = 0; j < TAILLEX; j++)
            {
                if (i == 0){posX = 102;}
                if (i == 1){posX = 177;}
                if (i == 2){posX = 252;}
                if (i == 3){posX = 325;}
                if (i == 4){posX = 400;}
                if (i == 5){posX = 473;}
                if (i == 6){posX = 545;}

                if (j == 6){posY = 420;}
                if (j == 5){posY = 365;}
                if (j == 4){posY = 305;}
                if (j == 3){posY = 252;}
                if (j == 2){posY = 190;}
                if (j == 1){posY = 132;}
                if (j == 0){posY = 76;}
                
              if(copiePlateau[i][j] == 1){
                  g.drawImage(player, posX,posY ,  this);
              }  
              if(copiePlateau[i][j] == 2){
                  g.drawImage(IA, posX,posY ,  this);
              }  
              if(copiePlateau[i][j] == 5){
                  g.drawImage(tir, posX,posY ,  this);
              }  
              if(copiePlateau[i][j] == 6){
                  g.drawImage(tirRapide, posX,posY ,  this);
              }  
                
            }
        }
        
        
        
        
        
       /* g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        
        g.drawImage(background, 0, 0,  this);
        g.drawImage(IA, 325, 76, this);
        g.drawImage(player, 325,420 ,  this);
        g.drawImage(tir, 325, 365 ,  this);*/
        
        /* if (posX == 1){p.setX(102);}
        if (posX == 2){p.setX(177);}
        if (posX == 3){p.setX(252);}
        if (posX == 4){p.setX(325);}
        if (posX == 5){p.setX(400);}
        if (posX == 6){p.setX(473);}
        if (posX == 7){p.setX(545);}
        
        if (posY == 7){p.setY(420);}
        if (posY == 6){p.setY(365);}
        if (posY == 5){p.setY(305);}
        if (posY == 4){p.setY(252);}
        if (posY == 3){p.setY(190);}
        if (posY == 2){p.setY(132);}
        if (posY == 1){p.setY(76);} */
        
        /////////////////////////////////////// 
        
        g.dispose();
        bs.show();
        
    }
    
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
        
       // Spaceship s = new Spaceship();
        //s.render(plateau);
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
        System.out.printf("C'est au tour du joueur %d: \n\nListes des commandes pour jouer: \n\n", joueur);
                 System.out.printf("Mouvements: \n\t d pour vous déplacer à droite\n\t"
                         + " q pour vous déplacer à gauche\n\t z pour vous déplacer en haut\n\t s pour vous déplacer en bas\n");
                 System.out.printf("Attaques: \n\t 1 pour missile d'une case par tour\n\t"
                         + " 2 pour missile de deux cases par tour: \n\t");
                 System.out.printf(" p pour passer son tour.\n");
                 System.out.printf(" Votre choix : ");
    }
    
    public static void afficherCommandesMouvement(int joueur)
    {
        System.out.printf(" \nListes des commandes pour jouer:", joueur);
                 System.out.printf("Mouvements: \n\t d pour vous déplacer à droite\n\t"
                         + " q pour vous déplacer à gauche\n\t z pour vous déplacer en haut\n\t s pour vous déplacer en bas: \n\t");
                 System.out.printf(" p pour passer son tour.\n");
                 System.out.printf(" Votre choix : ");
    }
    
    public static void afficherCommandesTir(int joueur)
    {
        System.out.printf("\nListes des commandes pour jouer: \n\n", joueur);
                  System.out.printf("Attaques: \n\t 1 pour missile d'une case par tour\n\t"
                         + " 2 pour missile de deux cases par tour: \n\t");
                 System.out.printf(" p pour passer son tour.\n");
                 System.out.printf(" Votre choix : ");
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
            boolean actionValide = joueur.move(input, plateau); //boolean ne servent � rien
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
        else if((joueur.getX() == 0 && input == 'q') || (joueur.getX() == TAILLEX - 1 && input == 'd')) // d�placement horizontal
        {
            return false;
        }
        else if((joueur.getY() == 0 && input == 'z') || (joueur.getY() == TAILLEY - 1 && input == 's')) // d�placement vertical
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
        else if((joueur.getX() == 0 && input == 'q') || (joueur.getX() == TAILLEX - 1 && input == 'd')) // d�placement horizontal
        {
            return false;
        }
        else if((joueur.getY() == 0 && input == 'z') || (joueur.getY() == TAILLEY - 1 && input == 's')) // d�placement vertical
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
            System.out.println("Vous avez eu un jeu tr�s polyvalent lors de cette partie, vous avez �quilibr� entre la d�fense et l'attaque.");
        }
        else if(compteurPassif > compteurAggro && compteurPassif > compteurLache)
        {
            System.out.println("Vous avez jou� tr�s passif. \nVous pourriez gagner plus de parties en essayant davantage d'�liminer l'adversaire au lieu d'essayer de survivre.");
        }
        else if(compteurAggro > compteurPassif && compteurAggro > compteurLache)
        {
            System.out.println("Vous avez jou� tr�s aggressif, vous pourriez gagner plus souvent en essayant de planifier davantage vos actions.");
        }
        else if(compteurLache > compteurAggro && compteurLache > compteurPassif)
        {
            System.out.println("Vous semblez avoir peur de mourir, il faut essayer d'avoir davantage confiance en vous et de coincer votre adversaire.");
        }
        else
        {
            System.out.printf("Votre fa�on de jouer est indescriptible.");
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
    public static void win(int joueur) //boolean ia sert � savoir si c'est une simulationo u pas 
    {
        if(!simulationIA)
        {
            System.out.printf("Bravo le joueur " + joueur + " a gagn� !\n");
            ENDGAME = true;
        }

    }
            
     public static void  setCopiePlateau(int[][] plateau)
     {
          for(int i = 0; i < TAILLEY; i++)
        {
            for(int j = 0; j < TAILLEX; j++)
            {
                
                copiePlateau[i][j] = plateau[i][j];
            }
        }
         
     }

    
    
    public static void main(String[] args) throws InterruptedException 
    {
        copiePlateau = new int[TAILLEX][TAILLEY];
        Spaceship game = new Spaceship();
        
        game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        
        JFrame frame = new JFrame(game.TITLE);
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
         int plateau[][] = new int[TAILLEX][TAILLEY];
        setCopiePlateau(plateau);
        
        game.start();
        
        Scanner k = new Scanner(System.in);

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
           
            
            System.out.println("\nTour numéro " + turn+ "\n");
            //printPlateau(plateau);
            setCopiePlateau(plateau);
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
                        System.out.printf("Veuillez entrer une commande correcte, merci de votre compréhension, Pa pa pala...");//g�n�rique de la SNCF
                        input = k.next().charAt(0);
                    }
                    evaluationJoueur(input);
                    
                    plateau = action(input, plateau, joueur1);
                }
                
                //printPlateau(plateau);
                setCopiePlateau(plateau);
                if (TIR && !endTurn) // On doit bouger
                {
                    afficherCommandesMouvement(1);
                    input = k.next().charAt(0);
                    while(!conditionActionMouvement(input, plateau, joueur1))
                    {
                        System.out.printf("Veuillez entrer une commande correcte, merci de votre compréhension, Pa pa pala...");//g�n�rique de la SNCF
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
                        System.out.printf("Veuillez entrer une commande correcte, merci de votre compréhension, Pa pa pala...");//g�n�rique de la SNCF
                        input = k.next().charAt(0);
                    }
                    evaluationJoueur(input);
                    plateau = action(input, plateau, joueur1);
                }
            }
            else  //joueur 2
            {
                TimeUnit.SECONDS.sleep(1);
                
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
                        System.out.printf("Veuillez entrer une commande correcte, merci de votre compréhension.");//g�n�rique de la SNCF
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
                //printPlateau(plateau);
                setCopiePlateau(plateau);
                if (TIR && !endTurn) // On doit mouvementer
                {
                    
                    //afficherCommandesMouvement(2);
                    //input = k.next().charAt(0);input = k.next().charAt(0);
                    
                    input = toPlay.charAt(1);
                    while(!conditionActionMouvement(input, plateau, joueur2))
                    {
                        System.out.printf("Veuillez entrer une commande correcte, merci de votre compréhension.");//g�n�rique de la SNCF
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
                        System.out.printf("Veuillez entrer une commande correcte, merci de votre compréhension.");//g�n�rique de la SNCF
                        System.out.println(input + " n'est pas une commande correcte");
                        input = k.next().charAt(0);
                    }
                    plateau = action(input, plateau, joueur2);
                }
                
            }
           /* if(turn % 7 == 0) //Cr�ation d'obstacles
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
            }*/
            
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
