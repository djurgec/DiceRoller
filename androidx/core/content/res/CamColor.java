package androidx.core.content.res;

import androidx.core.graphics.ColorUtils;

class CamColor {
  private static final float CHROMA_SEARCH_ENDPOINT = 0.4F;
  
  private static final float DE_MAX = 1.0F;
  
  private static final float DL_MAX = 0.2F;
  
  private static final float LIGHTNESS_SEARCH_ENDPOINT = 0.01F;
  
  private final float mAstar;
  
  private final float mBstar;
  
  private final float mChroma;
  
  private final float mHue;
  
  private final float mJ;
  
  private final float mJstar;
  
  private final float mM;
  
  private final float mQ;
  
  private final float mS;
  
  CamColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9) {
    this.mHue = paramFloat1;
    this.mChroma = paramFloat2;
    this.mJ = paramFloat3;
    this.mQ = paramFloat4;
    this.mM = paramFloat5;
    this.mS = paramFloat6;
    this.mJstar = paramFloat7;
    this.mAstar = paramFloat8;
    this.mBstar = paramFloat9;
  }
  
  private static CamColor findCamByJ(float paramFloat1, float paramFloat2, float paramFloat3) {
    CamColor camColor2;
    float f2 = 0.0F;
    float f1 = 100.0F;
    float f4 = 1000.0F;
    float f3 = 1000.0F;
    CamColor camColor1 = null;
    while (true) {
      camColor2 = camColor1;
      if (Math.abs(f2 - f1) > 0.01F) {
        float f7 = f2 + (f1 - f2) / 2.0F;
        int i = fromJch(f7, paramFloat2, paramFloat1).viewedInSrgb();
        float f9 = CamUtils.lStarFromInt(i);
        float f8 = Math.abs(paramFloat3 - f9);
        float f6 = f4;
        float f5 = f3;
        camColor2 = camColor1;
        if (f8 < 0.2F) {
          CamColor camColor = fromColor(i);
          float f = camColor.distance(fromJch(camColor.getJ(), camColor.getChroma(), paramFloat1));
          f6 = f4;
          f5 = f3;
          camColor2 = camColor1;
          if (f <= 1.0F) {
            f6 = f8;
            f5 = f;
            camColor2 = camColor;
          } 
        } 
        if (f6 == 0.0F && f5 == 0.0F)
          break; 
        if (f9 < paramFloat3) {
          f2 = f7;
        } else {
          f1 = f7;
        } 
        f4 = f6;
        f3 = f5;
        camColor1 = camColor2;
        continue;
      } 
      break;
    } 
    return camColor2;
  }
  
  static CamColor fromColor(int paramInt) {
    return fromColorInViewingConditions(paramInt, ViewingConditions.DEFAULT);
  }
  
  static CamColor fromColorInViewingConditions(int paramInt, ViewingConditions paramViewingConditions) {
    float[] arrayOfFloat = CamUtils.xyzFromInt(paramInt);
    float[][] arrayOfFloat1 = CamUtils.XYZ_TO_CAM16RGB;
    float f13 = arrayOfFloat[0];
    float f16 = arrayOfFloat1[0][0];
    float f17 = arrayOfFloat[1];
    float f15 = arrayOfFloat1[0][1];
    float f14 = arrayOfFloat[2];
    float f18 = arrayOfFloat1[0][2];
    float f9 = arrayOfFloat[0];
    float f11 = arrayOfFloat1[1][0];
    float f12 = arrayOfFloat[1];
    float f8 = arrayOfFloat1[1][1];
    float f10 = arrayOfFloat[2];
    float f7 = arrayOfFloat1[1][2];
    float f3 = arrayOfFloat[0];
    float f1 = arrayOfFloat1[2][0];
    float f4 = arrayOfFloat[1];
    float f6 = arrayOfFloat1[2][1];
    float f5 = arrayOfFloat[2];
    float f2 = arrayOfFloat1[2][2];
    f13 = paramViewingConditions.getRgbD()[0] * (f13 * f16 + f17 * f15 + f14 * f18);
    f7 = paramViewingConditions.getRgbD()[1] * (f9 * f11 + f12 * f8 + f10 * f7);
    f3 = paramViewingConditions.getRgbD()[2] * (f3 * f1 + f4 * f6 + f5 * f2);
    f1 = (float)Math.pow((paramViewingConditions.getFl() * Math.abs(f13)) / 100.0D, 0.42D);
    f4 = (float)Math.pow((paramViewingConditions.getFl() * Math.abs(f7)) / 100.0D, 0.42D);
    f2 = (float)Math.pow((paramViewingConditions.getFl() * Math.abs(f3)) / 100.0D, 0.42D);
    f1 = Math.signum(f13) * 400.0F * f1 / (f1 + 27.13F);
    f6 = Math.signum(f7) * 400.0F * f4 / (f4 + 27.13F);
    f2 = Math.signum(f3) * 400.0F * f2 / (27.13F + f2);
    f4 = (float)(f1 * 11.0D + f6 * -12.0D + f2) / 11.0F;
    f5 = (float)((f1 + f6) - f2 * 2.0D) / 9.0F;
    f3 = (f1 * 20.0F + f6 * 20.0F + 21.0F * f2) / 20.0F;
    f2 = (40.0F * f1 + f6 * 20.0F + f2) / 20.0F;
    f1 = (float)Math.atan2(f5, f4) * 180.0F / 3.1415927F;
    if (f1 < 0.0F) {
      f1 += 360.0F;
    } else if (f1 >= 360.0F) {
      f1 -= 360.0F;
    } 
    f6 = f1 * 3.1415927F / 180.0F;
    f11 = (float)Math.pow((paramViewingConditions.getNbb() * f2 / paramViewingConditions.getAw()), (paramViewingConditions.getC() * paramViewingConditions.getZ())) * 100.0F;
    f8 = 4.0F / paramViewingConditions.getC();
    f9 = (float)Math.sqrt((f11 / 100.0F));
    f10 = paramViewingConditions.getAw();
    f7 = paramViewingConditions.getFlRoot();
    if (f1 < 20.14D) {
      f2 = f1 + 360.0F;
    } else {
      f2 = f1;
    } 
    f2 = (float)(Math.cos(f2 * Math.PI / 180.0D + 2.0D) + 3.8D);
    f13 = paramViewingConditions.getNc();
    f12 = paramViewingConditions.getNcb();
    f2 = (float)Math.sqrt((f4 * f4 + f5 * f5)) * 3846.1538F * f2 * 0.25F * f13 * f12 / (0.305F + f3);
    f4 = (float)Math.pow(1.64D - Math.pow(0.29D, paramViewingConditions.getN()), 0.73D) * (float)Math.pow(f2, 0.9D);
    f3 = (float)Math.sqrt(f11 / 100.0D) * f4;
    f2 = paramViewingConditions.getFlRoot() * f3;
    f12 = (float)Math.sqrt((paramViewingConditions.getC() * f4 / (paramViewingConditions.getAw() + 4.0F)));
    f4 = 1.7F * f11 / (0.007F * f11 + 1.0F);
    f5 = (float)Math.log((0.0228F * f2 + 1.0F)) * 43.85965F;
    return new CamColor(f1, f3, f11, f8 * f9 * (f10 + 4.0F) * f7, f2, f12 * 50.0F, f4, (float)Math.cos(f6) * f5, (float)Math.sin(f6) * f5);
  }
  
  private static CamColor fromJch(float paramFloat1, float paramFloat2, float paramFloat3) {
    return fromJchInFrame(paramFloat1, paramFloat2, paramFloat3, ViewingConditions.DEFAULT);
  }
  
  private static CamColor fromJchInFrame(float paramFloat1, float paramFloat2, float paramFloat3, ViewingConditions paramViewingConditions) {
    float f3 = 4.0F / paramViewingConditions.getC();
    float f1 = (float)Math.sqrt(paramFloat1 / 100.0D);
    float f4 = paramViewingConditions.getAw();
    float f5 = paramViewingConditions.getFlRoot();
    float f2 = paramFloat2 * paramViewingConditions.getFlRoot();
    float f6 = paramFloat2 / (float)Math.sqrt(paramFloat1 / 100.0D);
    float f8 = (float)Math.sqrt((paramViewingConditions.getC() * f6 / (paramViewingConditions.getAw() + 4.0F)));
    float f7 = 3.1415927F * paramFloat3 / 180.0F;
    float f9 = 1.7F * paramFloat1 / (0.007F * paramFloat1 + 1.0F);
    f6 = (float)Math.log(f2 * 0.0228D + 1.0D) * 43.85965F;
    return new CamColor(paramFloat3, paramFloat2, paramFloat1, f3 * f1 * (f4 + 4.0F) * f5, f2, f8 * 50.0F, f9, f6 * (float)Math.cos(f7), f6 * (float)Math.sin(f7));
  }
  
  static int toColor(float paramFloat1, float paramFloat2, float paramFloat3) {
    return toColor(paramFloat1, paramFloat2, paramFloat3, ViewingConditions.DEFAULT);
  }
  
  static int toColor(float paramFloat1, float paramFloat2, float paramFloat3, ViewingConditions paramViewingConditions) {
    if (paramFloat2 < 1.0D || Math.round(paramFloat3) <= 0.0D || Math.round(paramFloat3) >= 100.0D)
      return CamUtils.intFromLStar(paramFloat3); 
    float f1 = 0.0F;
    if (paramFloat1 >= 0.0F)
      f1 = Math.min(360.0F, paramFloat1); 
    float f2 = paramFloat2;
    paramFloat1 = paramFloat2;
    paramFloat2 = 0.0F;
    boolean bool = true;
    CamColor camColor = null;
    while (Math.abs(paramFloat2 - f2) >= 0.4F) {
      CamColor camColor1 = findCamByJ(f1, paramFloat1, paramFloat3);
      if (bool) {
        if (camColor1 != null)
          return camColor1.viewed(paramViewingConditions); 
        bool = false;
        paramFloat1 = paramFloat2 + (f2 - paramFloat2) / 2.0F;
        continue;
      } 
      if (camColor1 == null) {
        f2 = paramFloat1;
      } else {
        camColor = camColor1;
        paramFloat2 = paramFloat1;
      } 
      paramFloat1 = paramFloat2 + (f2 - paramFloat2) / 2.0F;
    } 
    return (camColor == null) ? CamUtils.intFromLStar(paramFloat3) : camColor.viewed(paramViewingConditions);
  }
  
  float distance(CamColor paramCamColor) {
    float f2 = getJStar() - paramCamColor.getJStar();
    float f1 = getAStar() - paramCamColor.getAStar();
    float f3 = getBStar() - paramCamColor.getBStar();
    return (float)(Math.pow(Math.sqrt((f2 * f2 + f1 * f1 + f3 * f3)), 0.63D) * 1.41D);
  }
  
  float getAStar() {
    return this.mAstar;
  }
  
  float getBStar() {
    return this.mBstar;
  }
  
  float getChroma() {
    return this.mChroma;
  }
  
  float getHue() {
    return this.mHue;
  }
  
  float getJ() {
    return this.mJ;
  }
  
  float getJStar() {
    return this.mJstar;
  }
  
  float getM() {
    return this.mM;
  }
  
  float getQ() {
    return this.mQ;
  }
  
  float getS() {
    return this.mS;
  }
  
  int viewed(ViewingConditions paramViewingConditions) {
    if (getChroma() == 0.0D || getJ() == 0.0D) {
      float f13 = 0.0F;
      float f17 = (float)Math.pow(f13 / Math.pow(1.64D - Math.pow(0.29D, paramViewingConditions.getN()), 0.73D), 1.1111111111111112D);
      float f19 = getHue() * 3.1415927F / 180.0F;
      float f16 = (float)(Math.cos(f19 + 2.0D) + 3.8D);
      float f18 = paramViewingConditions.getAw();
      f13 = (float)Math.pow(getJ() / 100.0D, 1.0D / paramViewingConditions.getC() / paramViewingConditions.getZ());
      float f15 = paramViewingConditions.getNc();
      float f14 = paramViewingConditions.getNcb();
      f13 = f18 * f13 / paramViewingConditions.getNbb();
      f18 = (float)Math.sin(f19);
      f19 = (float)Math.cos(f19);
      f14 = (0.305F + f13) * 23.0F * f17 / (23.0F * 3846.1538F * f16 * 0.25F * f15 * f14 + 11.0F * f17 * f19 + 108.0F * f17 * f18);
      f16 = f14 * f19;
      f17 = f14 * f18;
      f14 = (f13 * 460.0F + 451.0F * f16 + 288.0F * f17) / 1403.0F;
      f15 = (f13 * 460.0F - 891.0F * f16 - 261.0F * f17) / 1403.0F;
      f18 = (460.0F * f13 - 220.0F * f16 - 6300.0F * f17) / 1403.0F;
      f16 = (float)Math.max(0.0D, Math.abs(f14) * 27.13D / (400.0D - Math.abs(f14)));
      f13 = Math.signum(f14);
      f14 = 100.0F / paramViewingConditions.getFl();
      f16 = (float)Math.pow(f16, 2.380952380952381D);
      f19 = (float)Math.max(0.0D, Math.abs(f15) * 27.13D / (400.0D - Math.abs(f15)));
      f15 = Math.signum(f15);
      f17 = 100.0F / paramViewingConditions.getFl();
      float f20 = (float)Math.pow(f19, 2.380952380952381D);
      float f21 = (float)Math.max(0.0D, Math.abs(f18) * 27.13D / (400.0D - Math.abs(f18)));
      f18 = Math.signum(f18);
      f19 = 100.0F / paramViewingConditions.getFl();
      f21 = (float)Math.pow(f21, 2.380952380952381D);
      f13 = f13 * f14 * f16 / paramViewingConditions.getRgbD()[0];
      f14 = f15 * f17 * f20 / paramViewingConditions.getRgbD()[1];
      f21 = f18 * f19 * f21 / paramViewingConditions.getRgbD()[2];
      arrayOfFloat = CamUtils.CAM16RGB_TO_XYZ;
      float f23 = arrayOfFloat[0][0];
      float f24 = arrayOfFloat[0][1];
      f16 = arrayOfFloat[0][2];
      f19 = arrayOfFloat[1][0];
      f18 = arrayOfFloat[1][1];
      f15 = arrayOfFloat[1][2];
      f17 = arrayOfFloat[2][0];
      float f22 = arrayOfFloat[2][1];
      f20 = arrayOfFloat[2][2];
      return ColorUtils.XYZToColor((f23 * f13 + f24 * f14 + f16 * f21), (f19 * f13 + f18 * f14 + f15 * f21), (f17 * f13 + f22 * f14 + f20 * f21));
    } 
    float f1 = getChroma() / (float)Math.sqrt(getJ() / 100.0D);
    float f5 = (float)Math.pow(f1 / Math.pow(1.64D - Math.pow(0.29D, arrayOfFloat.getN()), 0.73D), 1.1111111111111112D);
    float f7 = getHue() * 3.1415927F / 180.0F;
    float f4 = (float)(Math.cos(f7 + 2.0D) + 3.8D);
    float f6 = arrayOfFloat.getAw();
    f1 = (float)Math.pow(getJ() / 100.0D, 1.0D / arrayOfFloat.getC() / arrayOfFloat.getZ());
    float f3 = arrayOfFloat.getNc();
    float f2 = arrayOfFloat.getNcb();
    f1 = f6 * f1 / arrayOfFloat.getNbb();
    f6 = (float)Math.sin(f7);
    f7 = (float)Math.cos(f7);
    f2 = (0.305F + f1) * 23.0F * f5 / (23.0F * 3846.1538F * f4 * 0.25F * f3 * f2 + 11.0F * f5 * f7 + 108.0F * f5 * f6);
    f4 = f2 * f7;
    f5 = f2 * f6;
    f2 = (f1 * 460.0F + 451.0F * f4 + 288.0F * f5) / 1403.0F;
    f3 = (f1 * 460.0F - 891.0F * f4 - 261.0F * f5) / 1403.0F;
    f6 = (460.0F * f1 - 220.0F * f4 - 6300.0F * f5) / 1403.0F;
    f4 = (float)Math.max(0.0D, Math.abs(f2) * 27.13D / (400.0D - Math.abs(f2)));
    f1 = Math.signum(f2);
    f2 = 100.0F / arrayOfFloat.getFl();
    f4 = (float)Math.pow(f4, 2.380952380952381D);
    f7 = (float)Math.max(0.0D, Math.abs(f3) * 27.13D / (400.0D - Math.abs(f3)));
    f3 = Math.signum(f3);
    f5 = 100.0F / arrayOfFloat.getFl();
    float f8 = (float)Math.pow(f7, 2.380952380952381D);
    float f9 = (float)Math.max(0.0D, Math.abs(f6) * 27.13D / (400.0D - Math.abs(f6)));
    f6 = Math.signum(f6);
    f7 = 100.0F / arrayOfFloat.getFl();
    f9 = (float)Math.pow(f9, 2.380952380952381D);
    f1 = f1 * f2 * f4 / arrayOfFloat.getRgbD()[0];
    f2 = f3 * f5 * f8 / arrayOfFloat.getRgbD()[1];
    f9 = f6 * f7 * f9 / arrayOfFloat.getRgbD()[2];
    float[][] arrayOfFloat = CamUtils.CAM16RGB_TO_XYZ;
    float f11 = arrayOfFloat[0][0];
    float f12 = arrayOfFloat[0][1];
    f4 = arrayOfFloat[0][2];
    f7 = arrayOfFloat[1][0];
    f6 = arrayOfFloat[1][1];
    f3 = arrayOfFloat[1][2];
    f5 = arrayOfFloat[2][0];
    float f10 = arrayOfFloat[2][1];
    f8 = arrayOfFloat[2][2];
    return ColorUtils.XYZToColor((f11 * f1 + f12 * f2 + f4 * f9), (f7 * f1 + f6 * f2 + f3 * f9), (f5 * f1 + f10 * f2 + f8 * f9));
  }
  
  int viewedInSrgb() {
    return viewed(ViewingConditions.DEFAULT);
  }
}


/* Location:              C:\Users\dario\OneDrive\Desktop\dex-tools-v2.4\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\content\res\CamColor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */