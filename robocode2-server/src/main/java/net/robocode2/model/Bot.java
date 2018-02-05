package net.robocode2.model;

import lombok.Builder;
import lombok.Builder.Default;
import net.robocode2.util.MathUtil;
import lombok.Value;

/**
 * Mutable bot instance.
 * 
 * @author Flemming N. Larsen
 */
@Value
@Builder(toBuilder=true)
public class Bot implements IBot {

	/** Bot id */
	int id;

	/** Energy level */
	@Default double energy = 100;

	/** Position on the arena */
	Point position;

	/** Driving direction in degrees */
	double direction;

	/** Gun direction in degrees */
	double gunDirection;

	/** Radar direction in degrees */
	double radarDirection;

	/** Radar spread angle in degrees */
	double radarSpreadAngle;

	/** Speed */
	double speed;

	/** Gun heat */
	double gunHeat;

	/** Score record */
	Score score;


	public static class BotBuilder {

		public int getId() {
			return id;
		}
		
		public Point getPosition() {
			return position;
		}
		
		public double getEnergy() {
			return energy;
		}

		public double getSpeed() {
			return speed;
		}

		public boolean isAlive() {
			return energy >= 0;
		}
		
		public boolean isDead() {
			return energy < 0;
		}

		public boolean isDisabled() {
			return isAlive() && MathUtil.nearlyEqual(getEnergy(), 0);
		}

		public double getDirection() {
			return direction;
		}

		public double getGunDirection() {
			return gunDirection;
		}

		public double getRadarDirection() {
			return radarDirection;
		}

		public double getRadarSpreadAngle() {
			return radarSpreadAngle;
		}
		
		public double getGunHeat() {
			return gunHeat;
		}
		
		/**
		 * Adds damage to the bot.
		 * 
		 * @param damage
		 *            is the damage done to this bot
		 * @return true if the robot got killed due to the damage, false otherwise.
		 */
		public boolean addDamage(double damage) {
			boolean aliveBefore = isAlive();
			energy -= damage;
			return isDead() && aliveBefore;
		}
	
		/**
		 * Change the energy level.
		 * 
		 * @param deltaEnergy
		 *            is the delta energy to add to the current energy level, which can be both positive and negative.
		 */
		public void changeEnergy(double deltaEnergy) {
			energy += deltaEnergy;
		}
	
		/**
		 * Move bot to new position of the bot based on the current position, the driving direction and speed.
		 */
		public void moveToNewPosition() {
			position = calcNewPosition(direction, speed);
		}
	
		/**
		 * Moves bot backwards a specific amount of distance due to bouncing.
		 * 
		 * @param distance
		 *            is the distance to bounce back
		 */
		public void bounceBack(double distance) {
			position = calcNewPosition(direction, (speed > 0 ? -distance : distance));
		}
	
		/**
		 * Calculates new position of the bot based on the current position, the driving direction and speed.
		 * 
		 * @param direction
		 *            is the new driving direction
		 * @param distance
		 *            is the distance to move
		 * @return the calculated new position of the bot
		 */
		private Point calcNewPosition(double direction, double distance) {
			double angle = Math.toRadians(direction);
			double x = position.x + Math.cos(angle) * distance;
			double y = position.y + Math.sin(angle) * distance;
			return new Point(x, y);
		}
	}
}