package CWA.MatrixClassification;

public interface WeightClassificationMatrix
{
   public void addClassification(WeightClassification classification);
   
   public void displayMatrixTable();

   String getMatrixValue(int weight, short age);
   
   public AgeClass[] getAgeClasses();
   
   public void initializeMatrix();
   

}
