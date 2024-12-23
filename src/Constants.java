public class Constants{
    public static final Function function = (x, a) -> {return x * Math.exp(a * (1 - x));};
//    public static final Function function = (x, a) -> {return x * a * (1 - x);};
    public static final int FRAME_WIDTH = 1000;
    public static final int FRAME_HEIGHT = 800;
    public static final int ZOOM_INTERVAL = 10;
    public static final int FUNCTION_MOVE_INTERVAL = 20;
    public static final int DEFAULT_ZOOM_MULTIPLIER = 50;
    public static final double DOT_DENSITY = 0.01;
    public static final double DOMAIN_MIN = -50;
    public static final double DOMAIN_MAX = 100;
    public static final int MAX_JUMP = 50;
    public static final double UNIT_LINE_SIZE = 0.05;
    public static final int ORBIT_STEP_COUNT = 500;
    public static final int ARange = 15;
    public static final int X_DENSITY = 500;
    public static final int Y_DENSITY = 100;
}
