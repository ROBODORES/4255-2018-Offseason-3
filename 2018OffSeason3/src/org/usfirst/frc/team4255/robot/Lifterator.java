package org.usfirst.frc.team4255.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Spark;

public class Lifterator {
	public static TalonSRX lift;
	public static Spark leftIntake;
	public static Spark rightIntake;
	public static Encoder liftEncoder;
	public static DigitalInput topLimit;
	public static DigitalInput bottomLimit;
	public static int open, closed;
	private int caseStep;
	private boolean properlyset;
	
	public Lifterator(TalonSRX lift, Encoder liftEncoder, Spark leftIntake, Spark rightIntake, 
			DigitalInput topLimit, DigitalInput bottomLimit) {
		this.lift = lift;
		this.leftIntake = leftIntake;
		this.rightIntake = rightIntake;
		this.liftEncoder = liftEncoder;
		this.topLimit = topLimit;
		this.bottomLimit = bottomLimit;
		lift.setInverted(true);
		caseStep = 0;
		properlyset = false;
		open = -1900; 
		closed = 400;
		//liftEncoder.setDistancePerPulse(???);
		//liftEncoder.setDistancePerPulse(???);
	}
	
	public void reset() {
		caseStep = 0;
	}
	
	public boolean isSet() {
		return properlyset;
	}
	
	public void printLift() {
		System.out.println(liftEncoder.getRaw());
		//print();
	}
	
	public int getLift() {
		return liftEncoder.getRaw();
	}
	
	public void setLift(double liftSpeed, double leftSpeed, double rightSpeed) {
		if (properlyset) {
			int liftHeight = liftEncoder.getRaw();
			double topSpeed = 1.0, bottomSpeed = -1.0;
			if (topLimit.get()) {
				topSpeed = 0.0;
			} else if (liftHeight <= -10200) {
				topSpeed = 0.5;
			} else {
				topSpeed = 1.0;
			}
			
			if (bottomLimit.get()) {
				bottomSpeed = 0.0;
			} else if (liftHeight >= -600) {
				bottomSpeed = -0.2;
			} else {
				bottomSpeed = -1.0;
			}
			
			liftSpeed = etc.constrain(liftSpeed, bottomSpeed, topSpeed);
			lift.set(ControlMode.PercentOutput, liftSpeed);
			leftIntake.set(leftSpeed);
			rightIntake.set(rightSpeed);
		} else {
			lift.set(ControlMode.PercentOutput, 0.0);
			leftIntake.set(0.0);
			rightIntake.set(0.0);
		}
	}
	
	public void print() {
		System.out.println("bottom: "+bottomLimit.get()+" top: "+topLimit.get());
	}
	
	public boolean encoderInit() {
		switch (caseStep) {
		case 0:
			if (bottomLimit.get()) {
				lift.set(ControlMode.PercentOutput, 1.0);
			} else {
				resetLiftEncoder();
				caseStep++;
			}
			break;
		case 1:
			lift.set(ControlMode.PercentOutput, 0.0);
			leftIntake.set(0.0);
			rightIntake.set(0.0);
			break;
		}
		if (caseStep >= 1) {
			properlyset = true;
			return true;
		}
		return false;
	}
	
	public void resetLiftEncoder() {liftEncoder.reset();}
}
