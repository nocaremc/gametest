// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Vector2d.java

package spacegame.Util;

// Referenced classes of package org.lwjgl.util.vector:
//            Vector, ReadableVector2d, WritableVector2d

public class Vector2d {
	public double x;
	public double y;

	public Vector2d() {
	}

	public Vector2d(double x, double y) {
		set(x, y);
	}

	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double lengthSquared() {
		return x * x + y * y;
	}

	public double length() {
		return Math.sqrt(lengthSquared());
	}

	public Vector2d translate(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
	}

	public Vector2d negate() {
		x = -x;
		y = -y;
		return this;
	}

	public Vector2d negate(Vector2d dest) {
		if (dest == null)
			dest = new Vector2d();
		dest.x = -x;
		dest.y = -y;
		return dest;
	}

	public Vector2d normalise(Vector2d dest) {
		double l = length();
		if (dest == null)
			dest = new Vector2d(x / l, y / l);
		else
			dest.set(x / l, y / l);
		return dest;
	}

	public static double dot(Vector2d left, Vector2d right) {
		return left.x * right.x + left.y * right.y;
	}

	public static double angle(Vector2d a, Vector2d b) {
		double dls = dot(a, b) / (a.length() * b.length());
		if (dls < -1F)
			dls = -1F;
		else if (dls > 1.0F)
			dls = 1.0F;
		return (double) Math.acos(dls);
	}

	public static Vector2d add(Vector2d left, Vector2d right, Vector2d dest) {
		if (dest == null) {
			return new Vector2d(left.x + right.x, left.y + right.y);
		} else {
			dest.set(left.x + right.x, left.y + right.y);
			return dest;
		}
	}

	public static Vector2d sub(Vector2d left, Vector2d right, Vector2d dest) {
		if (dest == null) {
			return new Vector2d(left.x - right.x, left.y - right.y);
		} else {
			dest.set(left.x - right.x, left.y - right.y);
			return dest;
		}
	}

	public Vector2d scale(double scale) {
		x *= scale;
		y *= scale;
		return this;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer(64);
		sb.append("Vector2d[");
		sb.append(x);
		sb.append(", ");
		sb.append(y);
		sb.append(']');
		return sb.toString();
	}

	public final double getX() {
		return x;
	}

	public final double getY() {
		return y;
	}

	public final void setX(double x) {
		this.x = x;
	}

	public final void setY(double y) {
		this.y = y;
	}
}