import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

public class Weapon {

	SpaceObject owner;

	final int SIZE = 5; //10

	private boolean firing = false;

	private double pos_angle = 0;
	private double pos_radius = 0;

	private double fire_angle = 0;

	protected double aim_x;

	protected double aim_y;
	
	private double pos_x;
	private double pos_y;

	private int fire_cooldown_max = 10;
	private int fire_cooldown_time = fire_cooldown_max;
	private int projectile_speed = 10;
	private int projectile_damage = 5;
	private int projectile_lifetime = 60;

	private Color color = Color.RED;
	public Weapon()
	{
		
	}
	public Weapon(double angle, double radius, double fire_angle, int cooldown, int speed, int damage, int lifetime, Color color) {
		pos_angle = angle;
		pos_radius = radius;
		this.fire_angle = fire_angle;
		fire_cooldown_max = cooldown;
		fire_cooldown_time = fire_cooldown_max;
		projectile_speed = speed;
		projectile_damage = damage;
		projectile_lifetime = lifetime;
		this.color = color;
	}

	public void update() {
		updateWeapon();
	}
	public void updateWeapon() {
		//System.out.println("---> Weapon Update");
		fire_cooldown_time++;
		double angle = pos_angle + owner.getPosR();
		pos_x = owner.getPosX() + pos_radius * cosDegrees(angle);
		pos_y = owner.getPosY() + pos_radius * sinDegrees(angle);

		fire_angle = angle;
		//System.out.println("<--- Weapon Update");
	}
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		int[] bodyX = new int[4];
		int[] bodyY = new int[4];

		int bodyFrontX = (int) (pos_x + SIZE * cosDegrees(fire_angle));
		int bodyFrontY = (int) (GameWindow.HEIGHT - (pos_y + SIZE * sinDegrees(fire_angle)));

		bodyX[0] = bodyFrontX;
		bodyY[0] = bodyFrontY;

		bodyX[1] = (int) (pos_x + SIZE * cosDegrees(fire_angle - 120));
		bodyY[1] = (int) (GameWindow.HEIGHT - (pos_y + SIZE * sinDegrees(fire_angle - 120)));

		bodyX[2] = (int) (pos_x + SIZE * cosDegrees(fire_angle + 120));
		bodyY[2] = (int) (GameWindow.HEIGHT - (pos_y + SIZE * sinDegrees(fire_angle + 120)));

		bodyX[3] = bodyFrontX;
		bodyY[3] = bodyFrontY;
		g.drawPolygon(new Polygon(bodyX, bodyY, 4));
	}

	public final Projectile getShotType() {
		return new Projectile(getPosX(), getPosY(), getFireAngle(), getProjectileDamage(), getProjectileLifetime(), color);
	}

	public final Projectile getShot() {
		Projectile shot = getShotType();
		shot.setVelPolar(getFireAngle(), getProjectileSpeed());
		shot.incVelRectangular(owner.getVelX(), owner.getVelY());
		shot.setOwner(owner);
		return shot;
	}

	public final void setFiring(boolean state) {
		firing = state;
	}

	public final boolean getFiring() {
		return firing;
	}

	public final int getFireCooldownLeft() {
		return fire_cooldown_time;
	}

	public final void setFireCooldownLeft(int time) {
		fire_cooldown_time = time;
	}

	public final int getFireCooldownMax() {
		return fire_cooldown_max;
	}

	public double getFireAngle() {
		return fire_angle;
	}

	public final int getProjectileSpeed() {
		return projectile_speed;
	}
	
	public final void setProjectileSpeed(int speed) {
		projectile_speed = speed;
	}

	public final int getProjectileDamage() {
		return projectile_damage;
	}

	public final void setProjectileDamage(int damage) {
		projectile_damage = damage;
	}

	public final int getProjectileLifetime() {
		return projectile_lifetime;
	}
	
	public final void setProjectileLifetime(int lifetime) {
		projectile_lifetime = lifetime;
	}
	
	public final int getProjectileRange() {
		return projectile_speed * projectile_lifetime;
	}

	public final SpaceObject getOwner() {
		return owner;
	}
	
	public final void setAimPos(double x, double y) {
		aim_x = x;
		aim_y = y;
	}

	public final void setOwner(Starship owner_new) {
		owner = owner_new;
	}

	public final void setPosAngle(double angle) {
		pos_angle = angle;
	}

	public final void setPosRadius(double radius) {
		pos_radius = radius;
	}

	public final void setFireCooldownTime(int time) {
		fire_cooldown_max = time;
	}

	public final int getFireCooldownTime() {
		return fire_cooldown_max;
	}

	public final double getPosX() {
		return pos_x;
	}

	public final double getPosY() {
		return pos_y;
	}

	public final double cosDegrees(double angle) {
		return Math.cos(Math.toRadians(angle));
	}

	public final double sinDegrees(double angle) {
		return Math.sin(Math.toRadians(angle));
	}
	
	public final boolean exists(Object o)
	{
		return o != null;
	}
}