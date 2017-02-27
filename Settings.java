package com.prosus.androidgames.viral;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.prosus.androidgames.framework.FileIO;

public class Settings {
  public static boolean soundEnabled = true;
  public static int highscore;

  public static float neutralYAccel = Constants.DEFAULT_Y_ACCEL;
  public static float neutralZAccel = Constants.DEFAULT_Z_ACCEL;
  public static boolean neutralPositiveY = Constants.DEFAULT_POSITIVE_Y;

  public static void load(FileIO files) {
    BufferedReader in = null;
    try {
      in = new BufferedReader(new InputStreamReader(files.readFile("viralSettings.txt")));
      soundEnabled = Boolean.parseBoolean(in.readLine());
      highscore = decode(in.readLine());
      neutralYAccel = Float.parseFloat(in.readLine());
      neutralZAccel = Float.parseFloat(in.readLine());

    } catch (IOException e) {
      // It's OK, we have defaults.
    } catch (NumberFormatException e) {
      // It's OK, defaults save our day.
    } catch (NullPointerException e) {
      // It's OK, defaults lol.
    } finally {
      try {
        if (in != null) {
          in.close();
        }
      } catch (IOException e) {}
    }
  }

  public static void save(FileIO files) {
    BufferedWriter out = null;
    try {
      out = new BufferedWriter(new OutputStreamWriter(files.writeFile("viralSettings.txt")));
      out.write(Boolean.toString(soundEnabled));

      out.newLine();
      out.write(encode(highscore));

      out.newLine();
      out.write(Float.toString(neutralYAccel));

      out.newLine();
      out.write(Float.toString(neutralZAccel));

      out.newLine();
      out.write(Boolean.toString(neutralPositiveY));
    } catch (IOException e) {} finally {
      try {
        if (out != null) {
          out.close();
        }
      } catch (IOException e) {}
    }
  }

  private static int decode(String s) {
    int fib1 = 1;
    int fib2 = 1;
    int temp = fib2;

    int score = 0;
    int mult = 1;

    while (s.length() > fib1) {
      s = s.substring(fib1);
      score += mult * (s.charAt(0) - 35);
      s = s.substring(1);
      mult *= 10;

      temp = fib2;
      fib2 += fib1;
      fib1 = temp;
    }

    return score;
  }

  private static String encode(int score) {
    int fib1 = 1;
    int fib2 = 1;
    int temp = fib2;

    int upper = 126;
    int lower = 32;
    char curr = ' ';

    String encoded = "";
    while (score != 0) {
      for (int i = 0; i < fib1; i++) {
        curr = (char) ((int) (Math.random() * (upper - lower)) + lower);
        encoded += curr;
      }
      // Digits start at '#'.
      encoded += (char) ((score % 10) + 35);
      score /= 10;

      temp = fib2;
      fib2 += fib1;
      fib1 = temp;
    }

    return encoded;
  }
}