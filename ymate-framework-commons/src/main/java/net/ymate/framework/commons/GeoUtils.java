/*
 * Copyright 2007-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ymate.framework.commons;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author 刘镇 (suninformation@163.com) on 16/12/12 下午3:27
 * @version 1.0
 */
public class GeoUtils {

    /**
     * 地球半径
     */
    private static final double EARTH_RADIUS = 6378.137;

    /**
     * @param d 值
     * @return 弧度单位
     */
    private static double __rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * @param p1 坐标点1
     * @param p2 坐标点2
     * @return 计算两点间的距离(米)
     */
    public static double distance(Point p1, Point p2) {
        return p1.distance(p2);
    }

    /**
     * @param point    坐标点
     * @param distance 距离(米)
     * @return 返回从坐标点到直定距离的矩形范围
     */
    public static Bounds rectangle(Point point, long distance) {
        if (point == null || distance <= 0) {
            return new Bounds();
        }
        float _delta = 111000;
        if (point.getLatitude() != 0 && point.getLongitude() != 0) {
            double lng1 = point.getLongitude() - distance / Math.abs(Math.cos(Math.toRadians(point.getLatitude())) * _delta);
            double lng2 = point.getLongitude() + distance / Math.abs(Math.cos(Math.toRadians(point.getLatitude())) * _delta);
            double lat1 = point.getLatitude() - (distance / _delta);
            double lat2 = point.getLatitude() + (distance / _delta);
            return new Bounds(new Point(lng1, lat1), new Point(lng2, lat2));
        } else {
            double lng1 = point.getLongitude() - distance / _delta;
            double lng2 = point.getLongitude() + distance / _delta;
            double lat1 = point.getLatitude() - (distance / _delta);
            double lat2 = point.getLatitude() + (distance / _delta);
            return new Bounds(new Point(lng1, lat1), new Point(lng2, lat2));
        }
    }

    /**
     * 判断点是否在多边形区域内
     *
     * @param polygon 多边形区域
     * @param point   待判断点
     * @return true - 多边形包含这个点, false - 多边形未包含这个点。
     */
    public static boolean contains(Polygon polygon, Point point) {
        return contains(polygon, point, false);
    }

    public static boolean contains(Polygon polygon, Point point, boolean on) {
        if (on) {
            // 判断是否在多边形区域边界上
            return polygon.on(point);
        }
        // 判断点是否在多边形区域内
        return polygon.in(point);
    }

    /**
     * 判断点是否在圆形范围内
     *
     * @param circle 圆形区域
     * @param point  待判断点
     * @return -1 - 点在圆外, 0 - 点在圆上, 1 - 点在圆内
     */
    public static int contains(Circle circle, Point point) {
        return circle.contains(point);
    }

    /**
     * 地理坐标点
     */
    public static class Point implements Serializable {

        /**
         * 经度, X
         */
        private double longitude;

        /**
         * 纬度, Y
         */
        private double latitude;

        /**
         * 构造器
         *
         * @param longitude 经度
         * @param latitude  纬度
         */
        public Point(double longitude, double latitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public Point2D.Double toPoint2D() {
            return new Point2D.Double(longitude, latitude);
        }

        /**
         * @param point 坐标点
         * @return 计算两点间的距离(米)
         */
        public double distance(Point point) {
            double _lat1 = __rad(latitude);
            double _lat2 = __rad(latitude);
            return Math.round(2 * Math.asin(Math.sqrt(Math.pow(Math.sin((_lat1 - _lat2) / 2), 2) + Math.cos(_lat1) * Math.cos(_lat2) * Math.pow(Math.sin((__rad(point.getLongitude()) - __rad(point.getLongitude())) / 2), 2))) * EARTH_RADIUS * 10000) / 10;
        }

        /**
         * @return 验证是否为合法有效的经纬度
         */
        public boolean isValidCoordinate() {
            // 经度: 180° >= x >= 0°
            if (0.0 > longitude || 180.0 < longitude) {
                return false;
            }
            // 纬度: 90° >= y >= 0°
            return !(0.0 > latitude) && !(90.0 < latitude);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Point point = (Point) o;
            return new EqualsBuilder()
                    .append(longitude, point.longitude)
                    .append(latitude, point.latitude)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(longitude)
                    .append(latitude)
                    .toHashCode();
        }

        @Override
        public String toString() {
            return "Point{longitude=" + longitude + ", latitude=" + latitude + '}';
        }
    }

    /**
     * 地理坐标矩形区域
     */
    public static class Bounds implements Serializable {

        /**
         * 左下(西南)角坐标点
         */
        private Point southWest;

        /**
         * 右上(东北)角坐标点
         */
        private Point northEast;

        /**
         * 空矩形
         */
        public Bounds() {
        }

        /**
         * 取两个矩形区域的并集
         *
         * @param first 第一个矩形区域
         * @param other 另一个矩形区域
         */
        public Bounds(Bounds first, Bounds other) {
            if (first == null || first.isEmpty() || other == null || other.isEmpty()) {
                throw new NullArgumentException("bounds");
            }
            this.southWest = new Point(Math.min(first.southWest.getLongitude(), other.southWest.getLongitude()), Math.min(first.southWest.getLatitude(), other.southWest.getLatitude()));
            //
            this.northEast = new Point(Math.max(first.northEast.getLongitude(), other.northEast.getLongitude()), Math.max(first.northEast.getLatitude(), other.northEast.getLatitude()));
        }

        public Bounds(Point southWest, Point northEast) {
            this.southWest = southWest;
            this.northEast = northEast;
        }

        public Point getSouthWest() {
            return southWest;
        }

        public void setSouthWest(Point southWest) {
            this.southWest = southWest;
        }

        public Point getNorthEast() {
            return northEast;
        }

        public void setNorthEast(Point northEast) {
            this.northEast = northEast;
        }

        /**
         * @return 返回矩形的中心点
         */
        public Point getCenter() {
            return new Point((southWest.getLongitude() + northEast.getLongitude()) / 2, (southWest.getLatitude() + northEast.getLatitude()) / 2);
        }

        /**
         * @return 矩形区域是否为空
         */
        public boolean isEmpty() {
            return southWest == null || northEast == null;
        }

        /**
         * @param point 地理坐标点
         * @return 地理坐标点是否位于此矩形内
         */
        public boolean contains(Point point) {
            return !isEmpty() && (point.getLongitude() >= southWest.getLongitude() && point.getLongitude() <= northEast.getLongitude()) && (point.getLatitude() >= southWest.getLatitude() && point.getLatitude() <= northEast.getLatitude());
        }

        /**
         * @param bounds 矩形区域
         * @return 矩形区域是否完全包含于此矩形区域中
         */
        public boolean contains(Bounds bounds) {
            return contains(bounds.getSouthWest()) && contains(bounds.getNorthEast());
        }

        /**
         * @param bounds 矩形区域
         * @return 计算与另一矩形的交集区域
         */
        public Bounds intersects(Bounds bounds) {
            if (bounds != null && !bounds.isEmpty() && !isEmpty()) {
                Bounds _merged = new Bounds(this, bounds);
                //
                double _x1 = this.southWest.getLongitude() == _merged.southWest.getLongitude() ? bounds.southWest.getLongitude() : this.southWest.getLongitude();
                double _y1 = this.southWest.getLatitude() == _merged.southWest.getLatitude() ? bounds.southWest.getLatitude() : this.southWest.getLatitude();
                //
                double _x2 = this.northEast.getLongitude() == _merged.northEast.getLongitude() ? bounds.northEast.getLongitude() : this.northEast.getLongitude();
                double _y2 = this.northEast.getLatitude() == _merged.northEast.getLatitude() ? bounds.northEast.getLatitude() : this.northEast.getLatitude();
                //
                if (_x1 < _x2 && _y1 < _y2) {
                    return new Bounds(new Point(_x1, _y1), new Point(_x2, _y2));
                }
            }
            return new Bounds();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Bounds bounds = (Bounds) o;
            return new EqualsBuilder()
                    .append(southWest, bounds.southWest)
                    .append(northEast, bounds.northEast)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(southWest)
                    .append(northEast)
                    .toHashCode();
        }

        @Override
        public String toString() {
            return "Bounds{southWest=" + southWest + ", northEast=" + northEast + '}';
        }
    }

    /**
     * 地理坐标圆形
     */
    public static class Circle implements Serializable {

        /**
         * 圆心
         */
        private Point center;

        /**
         * 半径
         */
        private double r;

        /**
         * 构造器
         *
         * @param center 圆心
         * @param r      半径
         */
        public Circle(Point center, double r) {
            this.center = center;
            this.r = r;
        }

        public Point getCenter() {
            return center;
        }

        public void setCenter(Point center) {
            this.center = center;
        }

        public double getR() {
            return r;
        }

        public void setR(double r) {
            this.r = r;
        }

        /**
         * 判断点是否在圆形范围内
         *
         * @param point 点
         * @return -1 - 点在圆外, 0 - 点在圆上, 1 - 点在圆内
         */
        public int contains(Point point) {
            double value = Math.hypot((point.longitude - center.longitude), (point.latitude - center.latitude));
            if (value > r) {
                // 点在圆外
                return -1;
            } else if (value < r) {
                // 点在圆内
                return 1;
            }
            // 点在圆上
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            Circle circle = (Circle) o;

            return new EqualsBuilder()
                    .append(r, circle.r)
                    .append(center, circle.center)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(center)
                    .append(r)
                    .toHashCode();
        }

        @Override
        public String toString() {
            return "Circle{" + "center=" + center + ", r=" + r + '}';
        }
    }

    /**
     * 地理坐标多边形区域
     */
    public static class Polygon implements Serializable {

        /**
         * 多边形坐标点列表
         */
        private List<Point> points = new ArrayList<Point>();

        public Polygon() {
        }

        public Polygon(Point[] points) {
            if (ArrayUtils.isNotEmpty(points)) {
                this.points.addAll(Arrays.asList(points));
            }
        }

        public Polygon(Collection<Point> points) {
            if (points != null && !points.isEmpty()) {
                this.points.addAll(points);
            }
        }

        public boolean isEmpty() {
            return this.points.isEmpty();
        }

        public Polygon add(Point point) {
            if (point != null) {
                this.points.add(point);
            }
            return this;
        }

        public Polygon add(double longitude, double latitude) {
            this.points.add(new Point(longitude, latitude));
            return this;
        }

        public List<Point> getPoints() {
            return points;
        }

        public boolean in(Point point) {
            int nCross = 0;
            for (int i = 0; i < points.size(); i++) {
                Point p1 = points.get(i);
                Point p2 = points.get((i + 1) % points.size());
                if (p1.latitude == p2.latitude) {
                    continue;
                }
                if (point.latitude < Math.min(p1.latitude, p2.latitude)) {
                    continue;
                }
                if (point.latitude >= Math.max(p1.latitude, p2.latitude)) {
                    continue;
                }
                double x = (point.latitude - p1.latitude) * (p2.longitude - p1.longitude) / (p2.latitude - p1.latitude) + p1.longitude;
                if (x > point.longitude) {
                    nCross++;
                }
            }
            return (nCross % 2 == 1);
        }

        public boolean on(Point point) {
            for (int i = 0; i < points.size(); i++) {
                Point p1 = points.get(i);
                Point p2 = points.get((i + 1) % points.size());
                if (point.latitude < Math.min(p1.latitude, p2.latitude)) {
                    continue;
                }
                if (point.latitude > Math.max(p1.latitude, p2.latitude)) {
                    continue;
                }
                if (p1.latitude == p2.latitude) {
                    double minX = Math.min(p1.longitude, p2.longitude);
                    double maxX = Math.max(p1.longitude, p2.longitude);
                    if ((point.latitude == p1.latitude) && (point.longitude >= minX && point.longitude <= maxX)) {
                        return true;
                    }
                } else {
                    double x = (point.latitude - p1.latitude) * (p2.longitude - p1.longitude) / (p2.latitude - p1.latitude) + p1.longitude;
                    if (x == point.longitude) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
