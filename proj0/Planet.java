public class Planet {
	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;
	public Planet(double xP, double yP, double xV, double yV, double m, String img) {
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}

	public Planet(Planet p) {
		xxPos = p.xxPos;
		yyPos = p.yyPos;
		xxVel = p.xxVel;
		yyVel = p.yyVel;
		mass = p.mass;
		imgFileName = p.imgFileName;
	}

	public double calcDistance(Planet p1) {
		double dist = Math.pow((this.xxPos - p1.xxPos),2) + Math.pow((this.yyPos - p1.yyPos),2);
		return (Math.sqrt(dist));
	}
	

	public double calcForceExertedBy(Planet p) {
		double g = 0.0000000000667;
		if (!this.equals(p)) {
			double force = g * this.mass * p.mass / Math.pow(this.calcDistance(p),2);
			return (force);
		} else {
			return 0;
		}
	}

	public double calcForceExertedByX(Planet p) {
		double forceX = this.calcForceExertedBy(p) * Math.abs(this.xxPos - p.xxPos) / this.calcDistance(p);
		if (p.xxPos < this.xxPos) {
			forceX = -forceX;
		}
		return (forceX);
	}

	public double calcForceExertedByY(Planet p) {
		double forceY = this.calcForceExertedBy(p) * Math.abs(this.yyPos - p.yyPos) / this.calcDistance(p);
		if (p.yyPos < this.yyPos) {
			forceY = -forceY;
		}
		return (forceY);
	}

	public double calcNetForceExertedByX(Planet[] planets) {
		double netForceX = 0;
		for (Planet p : planets) {
			if (!this.equals(p)) {
				netForceX += this.calcForceExertedByX(p);
			}
		}
		return (netForceX);
	}

	public double calcNetForceExertedByY(Planet[] planets) {
		double netForceY = 0;
		for (Planet p : planets) {
			if (!this.equals(p)) {
				netForceY += this.calcForceExertedByY(p);
			}
		}
		return (netForceY);
	}
	
	public void update(double dt, double fX, double fY) {
		double aX = fX/this.mass;
		double aY = fY/this.mass;
		this.xxVel += dt * aX;
		this.yyVel += dt * aY;
		this.xxPos += dt * this.xxVel;
		this.yyPos += dt * this.yyVel;
	}

	public void draw() {
		StdDraw.picture(this.xxPos, this.yyPos, "images/" + this.imgFileName);
	}

}