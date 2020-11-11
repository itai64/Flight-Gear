package view;

public class Joystick {
	GuiController vc;
	double radLimit;

	public Joystick(GuiController vc) {
		this.vc = vc;
		radLimit = vc.outerCircle.getRadius();
	}

	/*
	 * This function validate the move of the joystick
	 */
	public void moveJoyStick() {
		vc.joyStick.setOnMouseDragged(e -> {
			double vectorLength = Math.sqrt(Math.pow(e.getX(), 2) + Math.pow(e.getY(), 2));
			if (vectorLength <= radLimit) {
				vc.joyStick.setCenterX(e.getX());
				vc.joyStick.setCenterY(e.getY());
				vc.valFromJoystick();
			} else {
				vc.joyStick.setCenterX(e.getX() * radLimit / vectorLength);
				vc.joyStick.setCenterY(e.getY() * radLimit / vectorLength);
				vc.valFromJoystick();
			}
		});
		vc.joyStick.setOnMouseReleased(e -> {
			vc.joyStick.setCenterX(0);
			vc.joyStick.setCenterY(0);
			vc.valFromJoystick();
		});
	}
}
