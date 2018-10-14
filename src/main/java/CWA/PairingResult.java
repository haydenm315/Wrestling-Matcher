package CWA;

import java.io.Serializable;

import CWA.Player.Player;

public class PairingResult implements Serializable
{
   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   private Pairing[] pairings;
   
   private Player[] noMatches1;
   
   private Player[] noMatches2;
   
   public Pairing[] getPairings()
   {
      return pairings;
   }
   
   public Player[] getNoMatches1()
   {
      return noMatches1;
   }
   
   public Player[] getNoMatches2()
   {
      return noMatches2;
   }
   
   public void setPairings(Pairing[] pairings)
   {
      this.pairings = pairings;
   }
   
   public void setNoMatches1(Player[] players)
   {
      this.noMatches1 = players;
   }
   
   public void setNomatches2(Player[] players)
   {
      this.noMatches2 = players;
   }

}
