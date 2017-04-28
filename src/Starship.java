import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Starship extends SpaceObject {

	/*
	final int HEAD_SIZE = 10; //20
	final int BODY_SIZE = 15; //30
	*/
	public static final double THRUST_DEFAULT = .5; //1
	public static final double MAX_SPEED_DEFAULT = 8;
	public static final double DECEL_DEFAULT = .2;
	public static final double ROTATION_MAX_DEFAULT = 15;
	public static final double ROTATION_ACCEL_DEFAULT = .6;
	public static final double ROTATION_DECEL_DEFAULT = .4;
	
	private double thrust, max_speed, decel, rotation_max, rotation_accel, rotation_decel;

	private double structure = 100;
	private ArrayList<String> print = new ArrayList<String>();

	private ArrayList<Weapon> weapons = new ArrayList<Weapon>();

	enum Sovereign {
		PLAYER, ENEMY
	}
	Sovereign alignment;
	
	public Starship() {
		setBody(new Body_Starship(this));
		updateBody();
		updateSize();
		setManeuverStats(
				THRUST_DEFAULT,
				MAX_SPEED_DEFAULT,
				DECEL_DEFAULT
				);
		setRotationStats(
				ROTATION_MAX_DEFAULT,
				ROTATION_ACCEL_DEFAULT,
				ROTATION_DECEL_DEFAULT
				);
	}
	public double getMaxSpeed() {
		return max_speed;
	}

	public void draw(Graphics g) {
		g.setColor(Color.YELLOW);
		
		updateBody();
		drawBody(g);
	}
	public void update() {
		if(getActive()) {
			updateActive();
		}
	}
	public void updateActive() {
		double speed_r = Math.abs(vel_r);
		if (speed_r > 0)
			if(speed_r > rotation_max) {
				vel_r = (vel_r > 0 ? 1 : -1) * rotation_max;
			}
			else {
				double vel_r_original = vel_r;
				vel_r -= (vel_r > 0 ? 1 : -1)*rotation_decel;
				//Check if vel_r changed positive to negative or vice versa. If it did, then set it to zero
				if(vel_r / vel_r_original < 0) {
					vel_r = 0;
				}
			}
		if (Math.sqrt(Math.pow(vel_x, 2) + Math.pow(vel_y, 2)) > max_speed) {
			int velAngle = (int) arctanDegrees(vel_y, vel_x);
			vel_x = max_speed * cosDegrees(velAngle);
			vel_y = max_speed * sinDegrees(velAngle);
		}
		updatePosition();
	}
	
	public final void onAttacked(SpaceObject attacker)
	{
		
	}

	public final void thrust() {
		//Add rectangular exhaust effects
		GamePanel.getWorld().createSpaceObject(new Projectile_StarshipExhaust());
		accelerate(pos_r, thrust);
	}
	public final void turnCCW() {
		rotateLeft(rotation_accel);
	}
	public final void turnCW() {
		rotateRight(rotation_accel);
	}
	public final void strafeLeft() {
		accelerate(pos_r, thrust);
	}
	public final void strafeRight() {
		accelerate(pos_r, thrust);
	}
	public final void brake() {
		decelerate(decel);
	}

	public final void damage(double damage) {
		structure = structure - damage;
		if(structure < 0)
		{
			destroy();
		}
	}
	
	public final ArrayList<Weapon> getWeapon()
	{
		return weapons;
	}
	public final Weapon getWeaponPrimary()
	{
		return weapons.size() > 0 ? weapons.get(0) : null;
	}
	public boolean hasWeapon() {
		return weapons.size() > 0;
	}
	public final void setFiring(boolean state) {
		for (Weapon weapon : weapons) {
			weapon.setFiring(state);
		}
	}

	public void installWeapon(Weapon item) {
		item.setOwner(this);
		weapons.add(item);
		print("Installed Weapon");
	}
	/*
	public final void destroy()
	{
		for(Weapon w: weapons)
		{
			GamePanel.world.removeWeapon(w);
		}
		super.destroy();
	}
	*/
	
	public final double getVelTowards(SpaceObject object)
	{
		double angle_towards_object = getAngleTowards(object);
		return object.getVelAtAngle(angle_towards_object) - getVelAtAngle(angle_towards_object);
	}
	
	public final Point2D.Double getFuturePosWithDeceleration() {
		double x_decel_time = Math.abs(vel_x/decel);
		double y_decel_time = Math.abs(vel_y/decel);
		return new Point2D.Double(
				pos_x +
				vel_x * x_decel_time +
				((vel_x > 0) ? -1 : 1) * (1/2) * decel * Math.pow(x_decel_time, 2),
				pos_y +
				vel_y * y_decel_time+
				((vel_y > 0) ? -1 : 1) * (1/2) * decel * Math.pow(y_decel_time, 2)
				);
	}
	public final double getFutureAngleWithDeceleration() {
		double r_decel_time = Math.abs(vel_r/rotation_decel);
		//double angle_to_target_future = angle_to_target + target.getVelR() * r_decel_time;
		//Let's relearn AP Physics I!
		double pos_r_future =
				pos_r
				+ vel_r * r_decel_time
				+ ((vel_r > 0) ? -1 : 1) * (1/2) * rotation_decel * Math.pow(r_decel_time, 2)
				;	//Make sure that the deceleration value has the opposite sign of the rotation speed
		return pos_r_future;
	}
	public final double calcFutureFacingDifference(double angle_target)
	{
		double pos_r_future = getFutureAngleWithDeceleration();
		return calcAngleDiff(pos_r_future, angle_target);
	}
	public final double calcFacingDifference(double angle_target)
	{
		return calcAngleDiff(pos_r, angle_target);
	}
	public static final double calcAngleDiff(double angle1, double angle2) {
		double angleDiffCCW = modRangeDegrees(angle1 - angle2);
		double angleDiffCW = modRangeDegrees(angle2 - angle1);
		return min(angleDiffCCW, angleDiffCW);
	}
	public final void turnDirection(Behavior.RotatingState direction)
	{
		switch(direction)
		{
		case	CCW:	turnCCW();			break;
		case	CW:	turnCW();			break;
		}
	}
	public void setAlignment(Sovereign a) {
		alignment = a;
	}
	public Sovereign getAlignment() {
		return alignment;
	}
	public boolean targetIsEnemy(Starship s) {
		return !alignment.equals(s.getAlignment());
	}
	
	public final Starship getClosestEnemyStarship() {
		double distance = Integer.MAX_VALUE;
		Starship result = null;
		for(Starship o : GamePanel.getWorld().getStarships()) {
			double d = getDistanceBetween(o);
			if(!o.equals(this) && targetIsEnemy(o) && d < distance) {
				result = o;
				distance = d;
			}
		}
		return result;
	}
	public void setStructure(int structure) {
		this.structure = structure;
	}
	public void setManeuverStats(double thrust, double max_speed, double decel) {
		setThrust(thrust);
		setMax_speed(max_speed);
		setDecel(decel);
	}
	public void setRotationStats(double rotation_max, double rotation_accel, double rotation_decel) {
		setRotation_max(rotation_max);
		setRotation_accel(rotation_accel);
		setRotation_decel(rotation_decel);
	}
	public double getThrust() {
		return thrust;
	}

	public void setThrust(double thrust) {
		this.thrust = thrust;
	}

	public double getMax_speed() {
		return max_speed;
	}

	public void setMax_speed(double max_speed) {
		this.max_speed = max_speed;
	}

	public double getDecel() {
		return decel;
	}

	public void setDecel(double decel) {
		this.decel = decel;
	}

	public double getRotation_max() {
		return rotation_max;
	}

	public void setRotation_max(double rotation_max) {
		this.rotation_max = rotation_max;
	}

	public double getRotation_accel() {
		return rotation_accel;
	}

	public void setRotation_accel(double rotation_accel) {
		this.rotation_accel = rotation_accel;
	}

	public double getRotation_decel() {
		return rotation_decel;
	}

	public void setRotation_decel(double rotation_decel) {
		this.rotation_decel = rotation_decel;
	}
}
