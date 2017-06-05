package kiwi.search;

public class SearchStats {
  public long startTime = System.currentTimeMillis(); 
  public boolean completed;
  public int nNodes = 0;
  public int nFails = 0;
  public int nSolutions = 0;
}
