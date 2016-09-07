package org.usfirst.frc.team4192;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.interfaces.Gyro;

//
// Created by Al on 6/17/2016.
//

class GyroPID implements PIDOutput {
  private double value;
  private SpeedController leftFront;
  private SpeedController leftBack;
  private SpeedController rightFront;
  private SpeedController rightBack;
  private Gyro gyro;

  //// // // // // //
  // Wheel Mapping //
  //   on robot    //
  //               //
  //   [0]  [1]    //
  //               //
  //   [3]  [2]    //
  //               //
  // // // // // ////

  public GyroPID(SpeedController leftFront, SpeedController leftBack,
                 SpeedController rightFront, SpeedController rightBack, Gyro gyro) {
    this.leftFront = leftFront;
    this.leftBack = leftBack;
    this.rightFront = rightFront;
    this.rightBack = rightBack;

    this.gyro = gyro;

    value = 0.0;
  }

  public void pidWrite(double output) {
    value = output;
  }

  public double get() {
    return value;
  }

  public boolean turn(double degree) {
    double target = gyro.getAngle() + degree;

    while (!atDegree(target)) {
      leftFront.set(value);
      leftBack.set(value);
      rightFront.set(value);
      rightBack.set(value);
    }

    return true;
  }

  private boolean atDegree(double target) {
    return gyro.getAngle() == target;
  }
}
