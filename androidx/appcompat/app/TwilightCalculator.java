package androidx.appcompat.app;

class TwilightCalculator {
  private static final float ALTIDUTE_CORRECTION_CIVIL_TWILIGHT = -0.10471976F;
  
  private static final float C1 = 0.0334196F;
  
  private static final float C2 = 3.49066E-4F;
  
  private static final float C3 = 5.236E-6F;
  
  public static final int DAY = 0;
  
  private static final float DEGREES_TO_RADIANS = 0.017453292F;
  
  private static final float J0 = 9.0E-4F;
  
  public static final int NIGHT = 1;
  
  private static final float OBLIQUITY = 0.4092797F;
  
  private static final long UTC_2000 = 946728000000L;
  
  private static TwilightCalculator sInstance;
  
  public int state;
  
  public long sunrise;
  
  public long sunset;
  
  static TwilightCalculator getInstance() {
    if (sInstance == null)
      sInstance = new TwilightCalculator(); 
    return sInstance;
  }
  
  public void calculateTwilight(long paramLong, double paramDouble1, double paramDouble2) {
    float f2 = (float)(paramLong - 946728000000L) / 8.64E7F;
    float f1 = 0.01720197F * f2 + 6.24006F;
    double d = 1.796593063D + f1 + Math.sin(f1) * 0.03341960161924362D + Math.sin((2.0F * f1)) * 3.4906598739326E-4D + Math.sin((3.0F * f1)) * 5.236000106378924E-6D + Math.PI;
    paramDouble2 = -paramDouble2 / 360.0D;
    paramDouble2 = (9.0E-4F + (float)Math.round((f2 - 9.0E-4F) - paramDouble2)) + paramDouble2 + Math.sin(f1) * 0.0053D + Math.sin(2.0D * d) * -0.0069D;
    d = Math.asin(Math.sin(d) * Math.sin(0.4092797040939331D));
    paramDouble1 = 0.01745329238474369D * paramDouble1;
    paramDouble1 = (Math.sin(-0.10471975803375244D) - Math.sin(paramDouble1) * Math.sin(d)) / Math.cos(paramDouble1) * Math.cos(d);
    if (paramDouble1 >= 1.0D) {
      this.state = 1;
      this.sunset = -1L;
      this.sunrise = -1L;
      return;
    } 
    if (paramDouble1 <= -1.0D) {
      this.state = 0;
      this.sunset = -1L;
      this.sunrise = -1L;
      return;
    } 
    f1 = (float)(Math.acos(paramDouble1) / 6.283185307179586D);
    this.sunset = Math.round((f1 + paramDouble2) * 8.64E7D) + 946728000000L;
    long l = Math.round((paramDouble2 - f1) * 8.64E7D) + 946728000000L;
    this.sunrise = l;
    if (l < paramLong && this.sunset > paramLong) {
      this.state = 0;
    } else {
      this.state = 1;
    } 
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\appcompat\app\TwilightCalculator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */