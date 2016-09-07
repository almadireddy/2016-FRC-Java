package org.usfirst.frc.team4192;

import com.ni.vision.NIVision;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
  private RobotDrive myRobot;
  private Joystick stick;
  private int autoLoopCounter;
  private double speedSetting;
  private Victor    zero       = new Victor(0);
  private Victor    one        = new Victor(1);
  private Victor    two        = new Victor(2);
  private Victor    three      = new Victor(3);
  private TalonSRX  flywheel   = new TalonSRX(4);
  private Victor    lift       = new Victor(5);
  private VictorSP  intakeArm  = new VictorSP(6);
  private VictorSP  intake     = new VictorSP(7);
  private Servo     cam1       = new Servo(8);
  
  private int session;
  private NIVision.Image frame;
  
  public void robotInit() {
    flywheel.setInverted(true);
    myRobot = new RobotDrive(zero, one, two, three);
    stick = new Joystick(0);
    frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
    
    session = NIVision.IMAQdxOpenCamera("cam1",
        NIVision.IMAQdxCameraControlMode.CameraControlModeController);
    NIVision.IMAQdxConfigureGrab(session);
  }
  
  private void flywheelControl() {
    if(stick.getRawButton(1))
      flywheel.set(speedSetting);
    if(stick.getRawButton(2))
      flywheel.set(0);
  }
  private void flywheelSpeed() {
    if (stick.getRawButton(7))
      speedSetting = 0.5;
    if (stick.getRawButton(8))
      speedSetting = 0.75;
    if (stick.getRawButton(9))
      speedSetting = 0.85;
    if (stick.getRawButton(10))
      speedSetting = 0.9;
  }
  
  private void intakeControl() {
    if(stick.getRawButton(3))
      intake.set(1.0);
    else if(stick.getRawButton(5))
      intake.set(-1.0);
    else
      intake.set(0.0);
    
    if(stick.getRawButton(6))
      intakeArm.set(0.6);
    else if(stick.getRawButton(4))
      intakeArm.set(-0.6);
    else if (stick.getRawButton(12))
      intakeArm.set(0.1);
    else
      intakeArm.set(0.0);
  }
  
  private void cameraControl() {
    NIVision.IMAQdxGrab(session, frame, 1);
    CameraServer.getInstance().setImage(frame);
    
    if(SmartDashboard.getNumber("Camera Servo") == 0.0)
      cam1.set(stick.getThrottle() / 2);
    else
      cam1.set(SmartDashboard.getNumber("Camera Servo"));
  }
  
  private void driveControl() {
    myRobot.arcadeDrive(-stick.getY(), -stick.getX(), true);
  }
  
  public void autonomousInit() {
    autoLoopCounter = 0;
  }
  
  public void autonomousPeriodic() {
    autoLoopCounter++;
    double time = 3.5;
    double speed = 0.4;
  
    int auton = 0;
    switch (auton) {
      case 0: // Rock wall, rampart, rough terrain
        time = 4;
        speed = 0.7;
        break;
      case 1: // moat
        time = 5;
        speed = 0.7;
        break;
      case 2: //lowbar
        time=2;
        speed=0.4;
        break;
    }
    
    if(autoLoopCounter < 50 * time)
      myRobot.drive(speed * Math.sqrt((autoLoopCounter / 250)) + .6, 0.0);
    else
      myRobot.drive(0.0, 0.0);
    
    if(autoLoopCounter > 50 * time && autoLoopCounter < 50 * (time + 4))
      intake.set(0);
    else
      intake.set(0);
  }
  
  public void teleopInit() {
    NIVision.IMAQdxStartAcquisition(session);
  }
  
  public void teleopPeriodic() {
    driveControl();
    intakeControl();
    cameraControl();
    flywheelControl();
    flywheelSpeed();
  }
  
  public void testPeriodic() {
    LiveWindow.run();
  }
}