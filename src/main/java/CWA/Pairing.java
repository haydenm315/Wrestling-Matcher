package CWA;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import CWA.Player.Player;

public class Pairing implements Serializable
{  
   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   public Pairing(Player player1, Player player2, String player1Class, String player2Class)
   {
      m_player1 = player1;
      m_player2 = player2;
      m_player1Class = player1Class;
      m_player2Class = player2Class;
      m_weightDifference = Math.abs(m_player1.getWeightInLbs() - m_player2.getWeightInLbs());
      setClassDifference(player1Class, player2Class);
      setAgeDifferenceDays();
   }
   
   private Player m_player1;
   private Player m_player2;
   
   private String m_player1Class;
   
   private String m_player2Class;
   
   private int m_weightDifference;
   
   private int m_ageDifferenceDays;
   
   private int m_classDifference;
   
   public int getClassDifference()
   {
      return m_classDifference;
   }
   
   public String getPlayer1Class()
   {
      return m_player1Class;
   }
   
   public String getPlayer2Class()
   {
      return m_player2Class;
   }
   
   public int getAgeDifferenceDays()
   {
      return m_ageDifferenceDays;
   }
   
   public int getWeightDifference()
   {
      return m_weightDifference;
   }
   
   public Player getPlayer1()
   {
      return m_player1;
   }
   
   public Player getPlayer2()
   {
      return m_player2;
   }
   
   public static int getClassDifference(String player1Class, String player2Class)
   {
      int player1ClassInt = 0;
      int player2ClassInt = 0;
      if (player1Class.compareToIgnoreCase("A") == 0)
         player1ClassInt = -4;
      else if (player1Class.compareToIgnoreCase("B") == 0)
         player1ClassInt = -3;
      else if (player1Class.compareToIgnoreCase("C") == 0)
         player1ClassInt = -2;
      else if (player1Class.compareToIgnoreCase("D") == 0)
         player1ClassInt = -1;
      else player1ClassInt = Integer.parseInt(player1Class);
      
      if (player2Class.compareToIgnoreCase("A") == 0)
         player2ClassInt = -4;
      else if (player2Class.compareToIgnoreCase("B") == 0)
         player2ClassInt = -3;
      else if (player2Class.compareToIgnoreCase("C") == 0)
         player2ClassInt = -2;
      else if (player2Class.compareToIgnoreCase("D") == 0)
         player2ClassInt = -1;
      else player2ClassInt = Integer.parseInt(player2Class);
      
      return Math.abs(player1ClassInt - player2ClassInt);
   }
   
   private void setClassDifference(String player1Class, String player2Class)
   {
      int player1ClassInt = 0;
      int player2ClassInt = 0;
      if (player1Class.compareToIgnoreCase("A") == 0)
         player1ClassInt = -4;
      else if (player1Class.compareToIgnoreCase("B") == 0)
         player1ClassInt = -3;
      else if (player1Class.compareToIgnoreCase("C") == 0)
         player1ClassInt = -2;
      else if (player1Class.compareToIgnoreCase("D") == 0)
         player1ClassInt = -1;
      else player1ClassInt = Integer.parseInt(player1Class);
      
      if (player2Class.compareToIgnoreCase("A") == 0)
         player2ClassInt = -4;
      else if (player2Class.compareToIgnoreCase("B") == 0)
         player2ClassInt = -3;
      else if (player2Class.compareToIgnoreCase("C") == 0)
         player2ClassInt = -2;
      else if (player2Class.compareToIgnoreCase("D") == 0)
         player2ClassInt = -1;
      else player2ClassInt = Integer.parseInt(player2Class);
      
      m_classDifference = Math.abs(player1ClassInt - player2ClassInt);
      
   }
   
   private void setAgeDifferenceDays()
   {
      LocalDate player1DOB = Instant.ofEpochMilli(m_player1.getBirthDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
      LocalDate player2DOB = Instant.ofEpochMilli(m_player2.getBirthDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
      m_ageDifferenceDays = Math.abs((int) ChronoUnit.DAYS.between(player1DOB, player2DOB));
   }
}
