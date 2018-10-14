package CWA.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import CWA.Pairing;
import CWA.PairingResult;
import CWA.MatrixClassification.CWAAgeWeightClassificationMatrix;
import CWA.Player.Player;
import CWA.Player.PlayerAgeWeightComparator;
import CWA.Team.Team;

public class PairingModel
{
   static final int ageThresholdDays = 365;
   static final int weightThresholdPercent = 5;
   
   public static PairingResult pairTeams(Team team1, Team team2)
   {
      PairingResult pairingResult = new PairingResult();
      HashMap<String, ArrayList<Player>> playersByClassMapTeam1 = PlayerClassificationModel.getTeamPlayerClassifications(team1);
      HashMap<String, ArrayList<Player>> playersByClassMapTeam2 = PlayerClassificationModel.getTeamPlayerClassifications(team2);
      CWAAgeWeightClassificationMatrix matrix = new CWAAgeWeightClassificationMatrix();
      matrix.initializeMatrix();
      
      
      ArrayList<Pairing> pairings = new ArrayList<>();
      ArrayList<Player> noMatches1Lst = new ArrayList<>();
      ArrayList<Player> noMatches2Lst = new ArrayList<>();
      
      HashMap<String,Set<Player>> playersWithMatchesTeam1 = new HashMap<>();
      HashMap<String,Set<Player>> playersWithMatchesTeam2 = new HashMap<>();
      
      //perform first round of pairing.  Perform matchups by player class
      for (String currClass : CWAAgeWeightClassificationMatrix.getMatrixClasses())
      {
         //find players in the current class
         ArrayList<Player> playersInClassTeam1 = playersByClassMapTeam1.get(currClass);
         ArrayList<Player> playersInClassTeam2 = playersByClassMapTeam2.get(currClass);
         if (playersInClassTeam1 != null && playersInClassTeam2 != null)
         {
            //records exist in both lists, we can pair
            //TODO: only pair if match is within age and weight thresholds
            if (playersInClassTeam1.size() > 0 && playersInClassTeam2.size() > 0 )
            {
               //wonderful the same number exists in both list.  Pairing is easy.
               if (playersInClassTeam1.size() == playersInClassTeam2.size())
               {
                  while( playersInClassTeam1.size() > 0)
                  {
                     Pairing pair = new Pairing(playersInClassTeam1.get(0), playersInClassTeam2.get(0), currClass, currClass);
                     pairings.add(pair);
                     
                     //Move these wrestlers into the map which contains those with matches.  Remove the players from the map of
                     //players which need still need matches
                     addToHasMatchesMap(playersWithMatchesTeam1,playersInClassTeam1.get(0),currClass);
                     addToHasMatchesMap(playersWithMatchesTeam2,playersInClassTeam2.get(0),currClass);
                     playersInClassTeam1.remove(0);
                     if (playersInClassTeam1.size() == 0)
                        playersByClassMapTeam1.remove(currClass);
                     
                     playersInClassTeam2.remove(0);
                     //playersInClassTeam1.remove(0);
                     
                     if (playersInClassTeam2.size() == 0)
                        playersByClassMapTeam2.remove(currClass);
                  }
               }
               //different number in both lists.  Someone may get an extra match.
               //depending on which team has more players, order of evaluation differs
               
               //team1 has more players, so find matches for all of them.
               else if (playersInClassTeam1.size() > playersInClassTeam2.size())
               {
                  int team2Index = 0;
                  
                  while( playersInClassTeam1.size() > 0)
                  {
                     //loop back around and give people more matches on team2 because they don't have enough in the class.
                     if (playersInClassTeam2.size() <= team2Index +1)
                     {
                        team2Index = 0;
                     }
                     else
                     {
                        team2Index++;
                     }
                     //TODO: maybe look at the closeness of fit to pick the best class match.
                     Pairing pair = new Pairing(playersInClassTeam1.get(0), playersInClassTeam2.get(team2Index), currClass, currClass);
                     pairings.add(pair);
                     
                     //Move these wrestlers into the map which contains those with matches.
                     addToHasMatchesMap(playersWithMatchesTeam1,playersInClassTeam1.get(0),currClass);
                     addToHasMatchesMap(playersWithMatchesTeam2,playersInClassTeam2.get(team2Index),currClass);
                     playersInClassTeam1.remove(0);
                     
                     if (playersInClassTeam1.size() == 0)
                        playersByClassMapTeam1.remove(currClass);
                  }
                  //remove all players on team 2 because we know they all got matches
                  for (Player playerToRemove: playersInClassTeam2.toArray(new Player[0]))
                  {
                     playersInClassTeam2.remove(playerToRemove);   
                  }
                  playersByClassMapTeam2.remove(currClass);
               }
               
               //team2 has more players, so find matches for all of them.
               else if (playersInClassTeam2.size() > playersInClassTeam1.size())
               {
                  int team1Index = 0;
                  while (playersInClassTeam2.size() > 0)
                  {
                     if (playersInClassTeam1.size() <= team1Index +1)
                     {                        
                        team1Index = 0;
                     }
                     else
                     {
                        team1Index++;
                     }
                     
                     //Pairing pair = new Pairing(playersInClassTeam1.get(0), playersInClassTeam2.get(team1Index), currClass, currClass);
                     Pairing pair = new Pairing(playersInClassTeam1.get(team1Index), playersInClassTeam2.get(0), currClass, currClass);
                     pairings.add(pair);
                     
                     //Move these wrestlers into the map which contains those with matches.
                     addToHasMatchesMap(playersWithMatchesTeam2,playersInClassTeam2.get(0),currClass);
                     addToHasMatchesMap(playersWithMatchesTeam1,playersInClassTeam1.get(team1Index),currClass);
                     playersInClassTeam2.remove(0);
                     if (playersInClassTeam2.size() == 0)
                        playersByClassMapTeam2.remove(currClass);

                  }
                  //remove all players on team 1 because we know they all got matches
                  for (Player playerToRemove: playersInClassTeam1.toArray(new Player[0]))
                  {
                     playersInClassTeam1.remove(playerToRemove);   
                  }
                  playersByClassMapTeam1.remove(currClass);
               }               
            }
         }
      }
      
      //Find the best matches for the remaining players on team1 without matches
      //Look for the closest match in the 1 adjacent upper and lower classes using age and weight as
      //additional criteria.
      for (String classKey: playersByClassMapTeam1.keySet() )
      {
         System.out.println("Curr key: " + classKey);
         ArrayList<Player> playersWithoutMatches = playersByClassMapTeam1.get(classKey);
         int numPlayersToFindMatchesFor = playersWithoutMatches.size();
         
         for (int i = 0; i < numPlayersToFindMatchesFor; i++)
         {
            ArrayList<Player> potentialMatches = new ArrayList<>();
            Player player = playersWithoutMatches.get(0);
            int indexOfPlayer1Class = matrix.getIndexOfClass(classKey);
            //search for a match in the class below.
            ArrayList<Player> lowerClassPlayers = new ArrayList<>();
            for (int ctr = 1; ctr <=1; ctr++)
            {
               ArrayList<Player> lowerPlayers = playersByClassMapTeam2.get(matrix.getClassStrByIndex(indexOfPlayer1Class - ctr));
               Set<Player> lowerPlayersWithMatches = playersWithMatchesTeam2.get(matrix.getClassStrByIndex(indexOfPlayer1Class - ctr));
               if(lowerPlayers != null)
                  lowerClassPlayers.addAll(lowerPlayers);
               if (lowerPlayersWithMatches != null)
                  lowerClassPlayers.addAll(lowerPlayersWithMatches);
            }
            
            if (lowerClassPlayers.size() > 0)
            {
               //System.out.println("There are players in the lower class");
               for (Player lowerPlayer: lowerClassPlayers)
               {
                  potentialMatches.add(lowerPlayer);
               }
            }
            else
            {
               System.out.println("No players in 1 adjacent lower classes");
            }
            
            //search for a match in the class above
            ArrayList<Player> higherClassPlayers = new ArrayList<>();
            for (int ctr = 1; ctr <=1; ctr++)
            {
               try
               {
                  ArrayList<Player> higherPlayers = playersByClassMapTeam2.get(matrix.getClassStrByIndex(indexOfPlayer1Class + ctr));
                  if(higherPlayers != null)
                     higherClassPlayers.addAll(higherPlayers);
                  
                  Set<Player> higherPlayersWithMatches = playersWithMatchesTeam2.get(matrix.getClassStrByIndex(indexOfPlayer1Class + ctr));
                  if(higherPlayersWithMatches != null)
                     higherClassPlayers.addAll(higherPlayersWithMatches);
               }
               catch (ArrayIndexOutOfBoundsException e)
               {
                  System.out.println("Out of bounds fat boy!");
               }

            }            
            if (higherClassPlayers.size() > 0)
            {
               //System.out.println("There are players in the higher class");
               for (Player higherPlayer: higherClassPlayers)
               {
                  potentialMatches.add(higherPlayer);
               }
            }
            else
            {
               System.out.println("no players in 1 adjacent higher class.");
            }
            System.out.println("-----------------------------------------");
            System.out.println("Finding match for the following player...");
            System.out.println(player.getFirstName() + " " + player.getLastName() + " Age: " + player.getAgeYears() + " Wt: " +
               player.getWeightInLbs() + " Class: " + matrix.getMatrixValue(player.getWeightInLbs(), player.getAgeYears()));
            
            if (matrix.getMatrixValue(player.getWeightInLbs(), player.getAgeYears()).compareToIgnoreCase("No class match") == 0)
               continue;
            //sort the list of potential matches to determine the best fit.
            System.out.println("Potential matches in order of best to worst fit...");
            
            potentialMatches.sort(new PlayerAgeWeightComparator());
            for (int j=0; j< potentialMatches.size(); j++)
            {
               Player potentialMatch = potentialMatches.get(j);
               System.out.println("\t " + potentialMatch.getFirstName() + " " + potentialMatch.getLastName() +
                     " Age: " + potentialMatch.getAgeYears() + " Wt: " + potentialMatch.getWeightInLbs() +
                     " Class: " + matrix.getMatrixValue(potentialMatch.getWeightInLbs(), potentialMatch.getAgeYears()));               
            }
            //choose the first element.  It's the best match
            if (potentialMatches.size() > 0)
            {
               Player bestFit = potentialMatches.get(0);
               String bestFitClass = matrix.getMatrixValue(bestFit.getWeightInLbs(), bestFit.getAgeYears());
               
               Pairing pair = new Pairing(player,bestFit, classKey, bestFitClass );
               pairings.add(pair);
               
               //Move these wrestlers into the map which contains those with matches.
               addToHasMatchesMap(playersWithMatchesTeam1,player,classKey);
               addToHasMatchesMap(playersWithMatchesTeam2,bestFit,bestFitClass);
               playersByClassMapTeam1.get(classKey).remove(player);
               //playersByClassMapTeam2.get(bestFitClass).remove(bestFit);
               
               /*if (playersByClassMapTeam1.get(classKey).size() == 0)
                  playersByClassMapTeam1.remove(classKey);
               if (playersByClassMapTeam2.get(classKey).size() == 0)
                  playersByClassMapTeam2.remove(classKey);*/
            }
         }
      }
      
      pairings.addAll(findMatchInAdjacentClass(playersByClassMapTeam2, playersByClassMapTeam1, playersWithMatchesTeam2, playersWithMatchesTeam1, false));
      
      System.out.println("players on team2 without matches");
      //Find the best matches for the remaining players on team2 without matches
      for (String classKey: playersByClassMapTeam2.keySet() )
      {
         //System.out.println("Curr key: " + classKey);
         ArrayList<Player> playersWithoutMatches = playersByClassMapTeam2.get(classKey);
         
         for (int i = 0; i < playersWithoutMatches.size(); i++)
         {
            Player player = playersWithoutMatches.get(i);
            System.out.println(player.getFirstName() + " " + player.getLastName() + " Age: " + player.getAgeYears() + " Wt: " + player.getWeightInLbs());
            noMatches2Lst.add(player);
         }
      }
      pairingResult.setNomatches2(noMatches2Lst.toArray(new Player[0]));
      
      System.out.println("players on team1 without matches");
      for (String classKey: playersByClassMapTeam1.keySet() )
      {
         //System.out.println("Curr key: " + classKey);
         ArrayList<Player> playersWithoutMatches = playersByClassMapTeam1.get(classKey);
         
         for (int i = 0; i < playersWithoutMatches.size(); i++)
         {
            Player player = playersWithoutMatches.get(i);
            System.out.println(player.getFirstName() + " " + player.getLastName() + " Age: " + player.getAgeYears() + " Wt: " + player.getWeightInLbs());
            noMatches1Lst.add(player);
         }
      }
      pairingResult.setNoMatches1(noMatches1Lst.toArray(new Player[0]));
      pairingResult.setPairings(pairings.toArray(new Pairing[0]));

      return pairingResult;
   }
   
   private static void addToHasMatchesMap(HashMap <String,Set<Player>> hasMatchesMap, Player player, String currClass)
   {
      Set<Player> classAry = hasMatchesMap.get(currClass);
      if (classAry == null)
      {
         classAry = new HashSet<Player>();
         hasMatchesMap.put(currClass, classAry);
      }
      classAry.add(player);
   }
   
   private static ArrayList<Pairing> findMatchInAdjacentClass(HashMap<String, ArrayList<Player>> playersByClassMapTeam1, 
         HashMap<String, ArrayList<Player>> playersByClassMapTeam2, 
         HashMap<String,Set<Player>> playersWithMatchesTeam1,
         HashMap<String,Set<Player>> playersWithMatchesTeam2, boolean pairOnLeft)
   {
      ArrayList<Pairing> pairings = new ArrayList<>();
      CWAAgeWeightClassificationMatrix matrix = new CWAAgeWeightClassificationMatrix();
      matrix.initializeMatrix();
      
      for (String classKey: playersByClassMapTeam1.keySet() )
      {
         System.out.println("Curr key: " + classKey);
         ArrayList<Player> playersWithoutMatches = playersByClassMapTeam1.get(classKey);
         int numPlayersToFindMatchesFor = playersWithoutMatches.size();
         
         for (int i = 0; i < numPlayersToFindMatchesFor; i++)
         {
            ArrayList<Player> potentialMatches = new ArrayList<>();
            Player player = playersWithoutMatches.get(0);
            int indexOfPlayer1Class = matrix.getIndexOfClass(classKey);
            //search for a match in the class below.
            ArrayList<Player> lowerClassPlayers = new ArrayList<>();
            for (int ctr = 1; ctr <=1; ctr++)
            {
               ArrayList<Player> lowerPlayers = playersByClassMapTeam2.get(matrix.getClassStrByIndex(indexOfPlayer1Class - ctr));
               Set<Player> lowerPlayersWithMatches = playersWithMatchesTeam2.get(matrix.getClassStrByIndex(indexOfPlayer1Class - ctr));
               if(lowerPlayers != null)
                  lowerClassPlayers.addAll(lowerPlayers);
               if (lowerPlayersWithMatches != null)
                  lowerClassPlayers.addAll(lowerPlayersWithMatches);
            }
            
            if (lowerClassPlayers.size() > 0)
            {
               //System.out.println("There are players in the lower class");
               for (Player lowerPlayer: lowerClassPlayers)
               {
                  potentialMatches.add(lowerPlayer);
               }
            }
            else
            {
               System.out.println("No players in 1 adjacent lower classes");
            }
            
            //search for a match in the class above
            ArrayList<Player> higherClassPlayers = new ArrayList<>();
            for (int ctr = 1; ctr <=1; ctr++)
            {
               try
               {
                  ArrayList<Player> higherPlayers = playersByClassMapTeam2.get(matrix.getClassStrByIndex(indexOfPlayer1Class + ctr));
                  if(higherPlayers != null)
                     higherClassPlayers.addAll(higherPlayers);
                  
                  Set<Player> higherPlayersWithMatches = playersWithMatchesTeam2.get(matrix.getClassStrByIndex(indexOfPlayer1Class + ctr));
                  if(higherPlayersWithMatches != null)
                     higherClassPlayers.addAll(higherPlayersWithMatches);
               }
               catch (ArrayIndexOutOfBoundsException e)
               {
                  System.out.println("Out of bounds fat boy!");
               }

            }            
            if (higherClassPlayers.size() > 0)
            {
               //System.out.println("There are players in the higher class");
               for (Player higherPlayer: higherClassPlayers)
               {
                  potentialMatches.add(higherPlayer);
               }
            }
            else
            {
               System.out.println("no players in 1 adjacent higher class.");
            }
            System.out.println("-----------------------------------------");
            System.out.println("Finding match for the following player...");
            System.out.println(player.getFirstName() + " " + player.getLastName() + " Age: " + player.getAgeYears() + " Wt: " +
               player.getWeightInLbs() + " Class: " + matrix.getMatrixValue(player.getWeightInLbs(), player.getAgeYears()));
            if (matrix.getMatrixValue(player.getWeightInLbs(), player.getAgeYears()).compareToIgnoreCase("No class match") == 0)
               continue;
            
            //sort the list of potential matches to determine the best fit.
            System.out.println("Potential matches in order of best to worst fit...");
            
            potentialMatches.sort(new PlayerAgeWeightComparator());
            for (int j=0; j< potentialMatches.size(); j++)
            {
               Player potentialMatch = potentialMatches.get(j);
               System.out.println("\t " + potentialMatch.getFirstName() + " " + potentialMatch.getLastName() +
                     " Age: " + potentialMatch.getAgeYears() + " Wt: " + potentialMatch.getWeightInLbs() +
                     " Class: " + matrix.getMatrixValue(potentialMatch.getWeightInLbs(), potentialMatch.getAgeYears()));               
            }
            //choose the first element.  It's the best match
            if (potentialMatches.size() > 0)
            {
               Player bestFit = potentialMatches.get(0);
               String bestFitClass = matrix.getMatrixValue(bestFit.getWeightInLbs(), bestFit.getAgeYears());
               Pairing pair = null;
               
               if (pairOnLeft)
                  pair = new Pairing(player,bestFit, classKey, bestFitClass );
               else
                  pair = new Pairing(bestFit, player, bestFitClass, classKey);
               pairings.add(pair);
               
               //Move these wrestlers into the map which contains those with matches.
               addToHasMatchesMap(playersWithMatchesTeam1,player,classKey);
               addToHasMatchesMap(playersWithMatchesTeam2,bestFit,bestFitClass);
               playersByClassMapTeam1.get(classKey).remove(player);
               //playersByClassMapTeam2.get(bestFitClass).remove(bestFit);
               
               /*if (playersByClassMapTeam1.get(classKey).size() == 0)
                  playersByClassMapTeam1.remove(classKey);
               if (playersByClassMapTeam2.get(classKey).size() == 0)
                  playersByClassMapTeam2.remove(classKey);*/
            }
         }
      }      
      return pairings;
   }
}
